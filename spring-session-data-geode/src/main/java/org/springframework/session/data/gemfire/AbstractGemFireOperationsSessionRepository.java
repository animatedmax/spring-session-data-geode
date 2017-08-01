/*
 * Copyright 2014-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.session.data.gemfire;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;

import org.apache.geode.DataSerializable;
import org.apache.geode.DataSerializer;
import org.apache.geode.Delta;
import org.apache.geode.Instantiator;
import org.apache.geode.InvalidDeltaException;
import org.apache.geode.cache.EntryEvent;
import org.apache.geode.cache.Operation;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.util.CacheListenerAdapter;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.gemfire.GemfireAccessor;
import org.springframework.data.gemfire.GemfireOperations;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.session.data.gemfire.config.annotation.web.http.GemFireHttpSessionConfiguration;
import org.springframework.session.data.gemfire.support.GemFireUtils;
import org.springframework.session.events.SessionCreatedEvent;
import org.springframework.session.events.SessionDeletedEvent;
import org.springframework.session.events.SessionDestroyedEvent;
import org.springframework.session.events.SessionExpiredEvent;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * {@link AbstractGemFireOperationsSessionRepository} is an abstract base class encapsulating functionality
 * common to all implementations that support {@link SessionRepository} operations backed by Apache Geode.
 *
 * @author John Blum
 * @see org.apache.geode.DataSerializable
 * @see org.apache.geode.DataSerializer
 * @see org.apache.geode.Delta
 * @see org.apache.geode.Instantiator
 * @see org.apache.geode.cache.Region
 * @see org.apache.geode.cache.util.CacheListenerAdapter
 * @see org.springframework.beans.factory.InitializingBean
 * @see org.springframework.context.ApplicationEventPublisher
 * @see org.springframework.context.ApplicationEventPublisherAware
 * @see org.springframework.data.gemfire.GemfireOperations
 * @see org.springframework.expression.Expression
 * @see org.springframework.session.FindByIndexNameSessionRepository
 * @see org.springframework.session.Session
 * @see org.springframework.session.SessionRepository
 * @see org.springframework.session.data.gemfire.config.annotation.web.http.GemFireHttpSessionConfiguration
 * @see org.springframework.session.data.gemfire.config.annotation.web.http.EnableGemFireHttpSession
 * @since 1.1.0
 */
public abstract class AbstractGemFireOperationsSessionRepository extends CacheListenerAdapter<Object, Session>
		implements ApplicationEventPublisherAware, FindByIndexNameSessionRepository<Session>, InitializingBean {

	private ApplicationEventPublisher applicationEventPublisher = new ApplicationEventPublisher() {

		public void publishEvent(ApplicationEvent event) {
		}

		public void publishEvent(Object event) {
		}
	};

	private Duration maxInactiveInterval =
		Duration.ofSeconds(GemFireHttpSessionConfiguration.DEFAULT_MAX_INACTIVE_INTERVAL_IN_SECONDS);

	private final GemfireOperations template;

	private final Log logger = newLogger();

	private final Set<Integer> cachedSessionIds = new ConcurrentSkipListSet<>();

	private String fullyQualifiedRegionName;

	/**
	 * Constructs an instance of {@link AbstractGemFireOperationsSessionRepository}
	 * with a required {@link GemfireOperations} instance used to perform GemFire data access operations
	 * and interactions supporting the SessionRepository operations.
	 *
	 * @param template {@link GemfireOperations} instance used to interact with GemFire; must not be {@literal null}.
	 * @throws IllegalArgumentException if {@link GemfireOperations} is {@literal null}.
	 * @see org.springframework.data.gemfire.GemfireOperations
	 */
	public AbstractGemFireOperationsSessionRepository(GemfireOperations template) {

		Assert.notNull(template, "GemfireOperations must not be null");

		this.template = template;
	}

	/**
	 * Constructs a new instance of {@link Log} using Apache Commons {@link LogFactory}.
	 *
	 * Used in testing to override the {@link Log} implementation with a mock.
	 *
	 * @return an instance of {@link Log} constructed from Apache commons-logging {@link LogFactory}.
	 * @see org.apache.commons.logging.LogFactory#getLog(Class)
	 * @see org.apache.commons.logging.Log
	 */
	Log newLogger() {
		return LogFactory.getLog(getClass());
	}

	/**
	 * Sets the ApplicationEventPublisher used to publish Session events corresponding to
	 * GemFire cache events.
	 *
	 * @param applicationEventPublisher the Spring ApplicationEventPublisher used to
	 * publish Session-based events; must not be {@literal null}.
	 * @throws IllegalArgumentException if {@link ApplicationEventPublisher} is {@literal null}.
	 * @see org.springframework.context.ApplicationEventPublisher
	 */
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {

		Assert.notNull(applicationEventPublisher, "ApplicationEventPublisher must not be null");

		this.applicationEventPublisher = applicationEventPublisher;
	}

	/**
	 * Returns a reference to the {@link ApplicationEventPublisher} used to publish {@link Session} events
	 * corresponding to GemFire/Geode cache events.
	 *
	 * @return the Spring {@link ApplicationEventPublisher} used to publish {@link Session} events.
	 * @see org.springframework.context.ApplicationEventPublisher
	 */
	protected ApplicationEventPublisher getApplicationEventPublisher() {
		return this.applicationEventPublisher;
	}

	/**
	 * Returns the fully-qualified name of the cache {@link Region} used to store and manage {@link Session} state.
	 *
	 * @return a {@link String} containing the fully qualified name of the cache {@link Region}
	 * used to store and manage {@link Session} data.
	 */
	protected String getFullyQualifiedRegionName() {
		return this.fullyQualifiedRegionName;
	}

	/**
	 * Return a reference to the {@link Log} used to log messages.
	 *
	 * @return a reference to the {@link Log} used to log messages.
	 * @see org.apache.commons.logging.Log
	 */
	protected Log getLogger() {
		return this.logger;
	}

	/**
	 * Sets the {@link Duration maximum interval} in which a {@link Session} can remain inactive
	 * before the {@link Session} is considered expired.
	 *
	 * @param maxInactiveInterval {@link Duration} specifying the maximum interval that a {@link Session}
	 * can remain inactive before the {@link Session} is considered expired.
	 * @see java.time.Duration
	 */
	public void setMaxInactiveInterval(Duration maxInactiveInterval) {
		this.maxInactiveInterval = maxInactiveInterval;
	}

	/**
	 * Returns the {@link Duration maximum interval} in which a {@link Session} can remain inactive
	 * before the {@link Session} is considered expired.
	 *
	 * @return a {@link Duration} specifying the maximum interval that a {@link Session} can remain inactive
	 * before the {@link Session} is considered expired.
	 * @see java.time.Duration
	 */
	public Duration getMaxInactiveInterval() {
		return this.maxInactiveInterval;
	}

	/**
	 * Sets the maximum interval in seconds in which a {@link Session} can remain inactive
	 * before the {@link Session} is considered expired.
	 *
	 * @param maxInactiveIntervalInSeconds an integer value specifying the maximum interval in seconds
	 * that a {@link Session} can remain inactive before the {@link Session }is considered expired.
	 * @see #setMaxInactiveInterval(Duration)
	 */
	public void setMaxInactiveIntervalInSeconds(int maxInactiveIntervalInSeconds) {
		setMaxInactiveInterval(Duration.ofSeconds(maxInactiveIntervalInSeconds));
	}

	/**
	 * Returns the maximum interval in seconds in which a {@link Session} can remain inactive
	 * before the {@link Session} is considered expired.
	 *
	 * @return an integer value specifying the maximum interval in seconds that a {@link Session} can remain inactive
	 * before the {@link Session} is considered expired.
	 * @see #getMaxInactiveInterval()
	 */
	public int getMaxInactiveIntervalInSeconds() {
		return Optional.ofNullable(getMaxInactiveInterval()).map(Duration::getSeconds)
			.map(Long::intValue).orElse(0);
	}

	/**
	 * Gets a reference to the {@link GemfireOperations template} used to perform data access operations
	 * and other interactions on the cache {@link Region} backing this {@link SessionRepository}.
	 *
	 * @return a reference to the {@link GemfireOperations template} used to interact with GemFire/Geode.
	 * @see org.springframework.data.gemfire.GemfireOperations
	 */
	public GemfireOperations getTemplate() {
		return this.template;
	}

	/**
	 * Callback method during Spring bean initialization that will capture the fully-qualified name
	 * of the cache {@link Region} used to manage {@link Session} state and register this {@link SessionRepository}
	 * as a GemFire/Geode {@link org.apache.geode.cache.CacheListener}.
	 *
	 * Additionally, this method registers GemFire/Geode {@link Instantiator Instantiators}
	 * for the {@link GemFireSession} and {@link GemFireSessionAttributes} types to optimize GemFire/Geode's
	 * instantiation logic on deserialization using the data serialization framework when accessing the stored
	 * {@link Session} state.
	 *
	 * @throws Exception if an error occurs during the initialization process.
	 */
	public void afterPropertiesSet() throws Exception {

		GemfireOperations template = getTemplate();

		Assert.isInstanceOf(GemfireAccessor.class, template);

		Region<Object, Session> region = ((GemfireAccessor) template).getRegion();

		this.fullyQualifiedRegionName = region.getFullPath();

		region.getAttributesMutator().addCacheListener(this);

		Instantiator.register(GemFireSessionInstantiator.create());
		Instantiator.register(GemFireSessionAttributesInstantiator.create());
	}

	/* (non-Javadoc) */
	boolean isCreate(EntryEvent<?, ?> event) {
		return (isCreate(event.getOperation()) && isNotUpdate(event) && isSessionOrNull(event.getNewValue()));
	}

	/* (non-Javadoc) */
	private boolean isCreate(Operation operation) {
		return (operation.isCreate() && !Operation.LOCAL_LOAD_CREATE.equals(operation));
	}

	/* (non-Javadoc) */
	private boolean isNotUpdate(EntryEvent event) {
		return (isNotProxyRegion() || !this.cachedSessionIds.contains(ObjectUtils.nullSafeHashCode(event.getKey())));
	}

	/* (non-Javadoc) */
	private boolean isNotProxyRegion() {
		return !isProxyRegion();
	}

	/* (non-Javadoc) */
	private boolean isProxyRegion() {
		return GemFireUtils.isProxy(((GemfireAccessor) getTemplate()).getRegion());
	}

	/**
	 * Used to determine whether the developer is storing (HTTP) Sessions with other, arbitrary application
	 * domain objects in the same GemFire cache {@link Region}; crazier things have happened!
	 *
	 * @param obj {@link Object} to evaluate.
	 * @return a boolean value indicating whether the {@link Object} from the entry event is indeed
	 * a {@link Session}.
	 * @see org.springframework.session.Session
	 */
	private boolean isSessionOrNull(Object obj) {
		return (obj instanceof Session || obj == null);
	}

	boolean forget(Object sessionId) {
		return this.cachedSessionIds.remove(ObjectUtils.nullSafeHashCode(sessionId));
	}

	@SuppressWarnings("all")
	boolean remember(Object sessionId) {
		return (isProxyRegion() && this.cachedSessionIds.add(ObjectUtils.nullSafeHashCode(sessionId)));
	}

	/* (non-Javadoc) */
	Session toSession(Object obj) {
		return (obj instanceof Session ? (Session) obj : null);
	}

	/**
	 * Callback method triggered when an entry is created in the GemFire cache {@link Region}.
	 *
	 * @param event {@link EntryEvent} containing the details of the cache {@link Region} operation.
	 * @see org.apache.geode.cache.EntryEvent
	 * @see #handleCreated(String, Session)
	 */
	@Override
	public void afterCreate(EntryEvent<Object, Session> event) {
		if (isCreate(event)) {
			handleCreated(event.getKey().toString(), toSession(event.getNewValue()));
		}
	}

	/**
	 * Callback method triggered when an entry is destroyed in the GemFire cache
	 * {@link Region}.
	 *
	 * @param event an EntryEvent containing the details of the cache operation.
	 * @see org.apache.geode.cache.EntryEvent
	 * @see #handleDestroyed(String, Session)
	 */
	@Override
	public void afterDestroy(EntryEvent<Object, Session> event) {
		handleDestroyed(event.getKey().toString(), toSession(event.getOldValue()));
	}

	/**
	 * Callback method triggered when an entry is invalidated in the GemFire cache
	 * {@link Region}.
	 *
	 * @param event an EntryEvent containing the details of the cache operation.
	 * @see org.apache.geode.cache.EntryEvent
	 * @see #handleExpired(String, Session)
	 */
	@Override
	public void afterInvalidate(EntryEvent<Object, Session> event) {
		handleExpired(event.getKey().toString(), toSession(event.getOldValue()));
	}

	/**
	 * Deletes the given {@link Session} from GemFire.
	 *
	 * @param session {@link Session} to delete.
	 * @return {@literal null}.
	 * @see org.springframework.session.Session#getId()
	 * @see #deleteById(String)
	 */
	protected Session delete(Session session) {
		deleteById(session.getId());
		return null;
	}

	/**
	 * Causes Session created events to be published to the Spring application context.
	 *
	 * @param sessionId a String indicating the ID of the Session.
	 * @param session a reference to the Session triggering the event.
	 * @see org.springframework.session.events.SessionCreatedEvent
	 * @see org.springframework.session.Session
	 * @see #newSessionCreatedEvent(Session, String)
	 * @see #publishEvent(ApplicationEvent)
	 */
	protected void handleCreated(String sessionId, Session session) {
		remember(sessionId);
		publishEvent(newSessionCreatedEvent(session, sessionId));
	}

	/**
	 * Causes Session deleted events to be published to the Spring application context.
	 *
	 * @param sessionId a String indicating the ID of the Session.
	 * @param session a reference to the Session triggering the event.
	 * @see org.springframework.session.events.SessionDeletedEvent
	 * @see org.springframework.session.Session
	 * @see #newSessionDeletedEvent(Session, String)
	 * @see #publishEvent(ApplicationEvent)
	 * @see #forget(Object)
	 */
	protected void handleDeleted(String sessionId, Session session) {
		forget(sessionId);
		publishEvent(newSessionDeletedEvent(session, sessionId));
	}

	/**
	 * Causes Session destroyed events to be published to the Spring application context.
	 *
	 * @param sessionId a String indicating the ID of the Session.
	 * @param session a reference to the Session triggering the event.
	 * @see org.springframework.session.events.SessionDestroyedEvent
	 * @see org.springframework.session.Session
	 * @see #newSessionDestroyedEvent(Session, String)
	 * @see #publishEvent(ApplicationEvent)
	 * @see #forget(Object)
	 */
	protected void handleDestroyed(String sessionId, Session session) {
		forget(sessionId);
		publishEvent(newSessionDestroyedEvent(session, sessionId));
	}

	/**
	 * Causes Session expired events to be published to the Spring application context.
	 *
	 * @param sessionId a String indicating the ID of the Session.
	 * @param session a reference to the Session triggering the event.
	 * @see org.springframework.session.events.SessionExpiredEvent
	 * @see org.springframework.session.Session
	 * @see #newSessionExpiredEvent(Session, String)
	 * @see #publishEvent(ApplicationEvent)
	 * @see #forget(Object)
	 */
	protected void handleExpired(String sessionId, Session session) {
		forget(sessionId);
		publishEvent(newSessionExpiredEvent(session, sessionId));
	}

	/* (non-Javadoc) */
	private SessionCreatedEvent newSessionCreatedEvent(Session session, String sessionId) {

		return (session != null ? new SessionCreatedEvent(this, session)
			: new SessionCreatedEvent(this, sessionId));
	}

	/* (non-Javadoc) */
	private SessionDeletedEvent newSessionDeletedEvent(Session session, String sessionId) {

		return (session != null ? new SessionDeletedEvent(this, session)
			: new SessionDeletedEvent(this, sessionId));
	}

	/* (non-Javadoc) */
	private SessionDestroyedEvent newSessionDestroyedEvent(Session session, String sessionId) {

		return (session != null ? new SessionDestroyedEvent(this, session)
			: new SessionDestroyedEvent(this, sessionId));
	}

	/* (non-Javadoc) */
	private SessionExpiredEvent newSessionExpiredEvent(Session session, String sessionId) {

		return (session != null ? new SessionExpiredEvent(this, session)
			: new SessionExpiredEvent(this, sessionId));
	}

	/**
	 * Publishes the specified ApplicationEvent to the Spring application context.
	 *
	 * @param event the ApplicationEvent to publish.
	 * @see org.springframework.context.ApplicationEventPublisher#publishEvent(ApplicationEvent)
	 * @see org.springframework.context.ApplicationEvent
	 */
	protected void publishEvent(ApplicationEvent event) {
		try {
			getApplicationEventPublisher().publishEvent(event);
		}
		catch (Throwable t) {
			getLogger().error(String.format("Error occurred publishing event [%s]", event), t);
		}
	}

	/**
	 * Updates the {@link Session#setLastAccessedTime(Instant)} property of the {@link Session}.
	 *
	 * @param <T> {@link Class} sub-type of the {@link Session}.
	 * @param session {@link Session} to touch.
	 * @return the {@link Session}.
	 * @see org.springframework.session.Session#setLastAccessedTime(Instant)
	 */
	protected <T extends Session> T touch(T session) {

		session.setLastAccessedTime(Instant.now());

		return session;
	}

	/**
	 * {@link GemFireSession} is a GemFire model for a Spring {@link Session} that stores and manages {@link Session}
	 * state in GemFire. This class implements GemFire's {@link DataSerializable} interface to better handle
	 * replication of {@link Session} state across the GemFire cluster.
	 *
	 * @see java.lang.Comparable
	 * @see org.apache.geode.DataSerializable
	 * @see org.apache.geode.Delta
	 * @see org.springframework.session.Session
	 */
	@SuppressWarnings("serial")
	public static class GemFireSession implements Comparable<Session>, DataSerializable, Delta, Session {

		protected static final boolean DEFAULT_ALLOW_JAVA_SERIALIZATION = true;

		private static final Duration DEFAULT_MAX_INACTIVE_INTERVAL = Duration.ZERO;

		protected static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

		private transient boolean delta = false;

		private Duration maxInactiveInterval = DEFAULT_MAX_INACTIVE_INTERVAL;

		private transient final GemFireSessionAttributes sessionAttributes = new GemFireSessionAttributes(this);

		private Instant creationTime;
		private Instant lastAccessedTime;

		private transient final SpelExpressionParser parser = new SpelExpressionParser();

		private String id;

		/* (non-Javadoc) */
		protected GemFireSession() {
			this(generateId());
		}

		/* (non-Javadoc) */
		protected GemFireSession(String id) {
			this.id = validateId(id);
			this.creationTime = Instant.now();
			this.lastAccessedTime = this.creationTime;
		}

		/* (non-Javadoc) */
		protected GemFireSession(Session session) {

			Assert.notNull(session, "The Session to copy cannot be null");

			this.id = session.getId();
			this.creationTime = session.getCreationTime();
			this.lastAccessedTime = session.getLastAccessedTime();
			this.maxInactiveInterval = session.getMaxInactiveInterval();
			this.sessionAttributes.from(session);
		}

		/* (non-Javadoc) */
		public static GemFireSession copy(Session session) {
			return new GemFireSession(session);
		}

		/* (non-Javadoc) */
		public static GemFireSession create(Duration maxInactiveInterval) {

			GemFireSession session = new GemFireSession();

			session.setMaxInactiveInterval(maxInactiveInterval);

			return session;
		}

		/* (non-Javadoc) */
		public static GemFireSession from(Session session) {
			return (session instanceof GemFireSession ? (GemFireSession) session : copy(session));
		}

		/**
		 * Randomly generates a unique identifier (ID) from {@link UUID} to be used as the {@link Session} ID.
		 *
		 * @return a new unique identifier (ID).
		 * @see java.util.UUID#randomUUID()
		 */
		private static String generateId() {
			return UUID.randomUUID().toString();
		}

		/* (non-Javadoc) */
		private static String validateId(String id) {

			return Optional.ofNullable(id).filter(StringUtils::hasText)
				.orElseThrow(() -> new IllegalArgumentException("ID is required"));
		}

		/* (non-Javadoc) */
		protected boolean allowJavaSerialization() {
			return DEFAULT_ALLOW_JAVA_SERIALIZATION;
		}

		@Override
		public synchronized String changeSessionId() {

			this.id = generateId();

			return getId();
		}

		/* (non-Javadoc) */
		public synchronized String getId() {
			return this.id;
		}

		/* (non-Javadoc) */
		public synchronized Instant getCreationTime() {
			return this.creationTime;
		}

		/* (non-Javadoc) */
		public void setAttribute(String attributeName, Object attributeValue) {
			this.sessionAttributes.setAttribute(attributeName, attributeValue);
		}

		/* (non-Javadoc) */
		public void removeAttribute(String attributeName) {
			this.sessionAttributes.removeAttribute(attributeName);
		}

		/* (non-Javadoc) */
		public <T> T getAttribute(String attributeName) {
			return this.sessionAttributes.getAttribute(attributeName);
		}

		/* (non-Javadoc) */
		public Set<String> getAttributeNames() {
			return this.sessionAttributes.getAttributeNames();
		}

		/* (non-Javadoc) */
		public GemFireSessionAttributes getAttributes() {
			return this.sessionAttributes;
		}

		/* (non-Javadoc) */
		public synchronized boolean isExpired() {

			Instant lastAccessedTime = getLastAccessedTime();

			Duration maxInactiveInterval = getMaxInactiveInterval();

			return (isExpirationEnabled(maxInactiveInterval)
				&& Instant.now().minus(maxInactiveInterval).isAfter(lastAccessedTime));
		}

		/* (non-Javadoc) */
		private boolean isExpirationDisabled(Duration duration) {
			return (duration == null || duration.isNegative() || duration.isZero());
		}

		/* (non-Javadoc) */
		private boolean isExpirationEnabled(Duration duration) {
			return !isExpirationDisabled(duration);
		}

		/* (non-Javadoc) */
		public synchronized void setLastAccessedTime(Instant lastAccessedTime) {
			this.delta |= !ObjectUtils.nullSafeEquals(this.lastAccessedTime, lastAccessedTime);
			this.lastAccessedTime = lastAccessedTime;
		}

		/* (non-Javadoc) */
		public synchronized Instant getLastAccessedTime() {
			return this.lastAccessedTime;
		}

		/* (non-Javadoc) */
		public synchronized void setMaxInactiveInterval(Duration maxInactiveIntervalInSeconds) {
			this.delta |= !ObjectUtils.nullSafeEquals(this.maxInactiveInterval, maxInactiveIntervalInSeconds);
			this.maxInactiveInterval = maxInactiveIntervalInSeconds;
		}

		/* (non-Javadoc) */
		public synchronized Duration getMaxInactiveInterval() {
			return Optional.ofNullable(this.maxInactiveInterval).orElse(DEFAULT_MAX_INACTIVE_INTERVAL);
		}

		/* (non-Javadoc) */
		public synchronized void setPrincipalName(String principalName) {
			setAttribute(PRINCIPAL_NAME_INDEX_NAME, principalName);
		}

		/* (non-Javadoc) */
		public synchronized String getPrincipalName() {

			String principalName = getAttribute(PRINCIPAL_NAME_INDEX_NAME);

			if (principalName == null) {

				Object authentication = getAttribute(SPRING_SECURITY_CONTEXT);

				if (authentication != null) {

					Expression expression = this.parser.parseExpression("authentication?.name");

					principalName = expression.getValue(authentication, String.class);
				}
			}

			return principalName;
		}

		/* (non-Javadoc) */
		public synchronized void toData(DataOutput out) throws IOException {

			out.writeUTF(getId());
			out.writeLong(getCreationTime().toEpochMilli());
			out.writeLong(getLastAccessedTime().toEpochMilli());
			out.writeLong(getMaxInactiveInterval().getSeconds());

			String principalName = getPrincipalName();

			int length = (StringUtils.hasText(principalName) ? principalName.length() : 0);

			out.writeInt(length);

			if (length > 0) {
				out.writeUTF(principalName);
			}

			writeObject(this.sessionAttributes, out);

			this.delta = false;
		}

		/* (non-Javadoc) */
		void writeObject(Object obj, DataOutput out) throws IOException {
			DataSerializer.writeObject(obj, out, allowJavaSerialization());
		}

		/* (non-Javadoc) */
		public synchronized void fromData(DataInput in) throws ClassNotFoundException, IOException {

			this.id = in.readUTF();
			this.creationTime = Instant.ofEpochMilli(in.readLong());
			setLastAccessedTime(Instant.ofEpochMilli(in.readLong()));
			setMaxInactiveInterval(Duration.ofSeconds(in.readLong()));

			int principalNameLength = in.readInt();

			if (principalNameLength > 0) {
				setPrincipalName(in.readUTF());
			}

			this.sessionAttributes.from(this.<GemFireSessionAttributes>readObject(in));

			this.delta = false;
		}

		/* (non-Javadoc) */
		<T> T readObject(DataInput in) throws ClassNotFoundException, IOException {
			return DataSerializer.readObject(in);
		}

		/* (non-Javadoc) */
		public synchronized boolean hasDelta() {
			return (this.delta || this.sessionAttributes.hasDelta());
		}

		/* (non-Javadoc) */
		public synchronized void toDelta(DataOutput out) throws IOException {
			out.writeLong(getLastAccessedTime().toEpochMilli());
			out.writeLong(getMaxInactiveInterval().getSeconds());
			getAttributes().toDelta(out);
			this.delta = false;
		}

		/* (non-Javadoc) */
		public synchronized void fromDelta(DataInput in) throws IOException {
			setLastAccessedTime(Instant.ofEpochMilli(in.readLong()));
			setMaxInactiveInterval(Duration.ofSeconds(in.readLong()));
			getAttributes().fromDelta(in);
			this.delta = false;
		}

		/* (non-Javadoc) */
		@SuppressWarnings("all")
		public int compareTo(Session session) {
			return getCreationTime().compareTo(session.getCreationTime());
		}

		/* (non-Javadoc) */
		@Override
		public boolean equals(final Object obj) {

			if (this == obj) {
				return true;
			}

			if (!(obj instanceof Session)) {
				return false;
			}

			Session that = (Session) obj;

			return this.getId().equals(that.getId());
		}

		/* (non-Javadoc) */
		@Override
		public int hashCode() {

			int hashValue = 17;

			hashValue = 37 * hashValue + getId().hashCode();

			return hashValue;
		}

		/* (non-Javadoc) */
		@Override
		public synchronized String toString() {

			return String.format("{ @type = %1$s, id = %2$s, creationTime = %3$s, lastAccessedTime = %4$s"
				+ ", maxInactiveInterval = %5$s, principalName = %6$s }",
				getClass().getName(), getId(), getCreationTime(), getLastAccessedTime(),
				getMaxInactiveInterval(), getPrincipalName());
		}
	}

	/**
	 * GemFireSessionInstantiator is a GemFire {@link Instantiator} use to instantiate instances
	 * of the {@link GemFireSession} object used in GemFire's data serialization framework when
	 * persisting Session state in GemFire.
	 */
	public static class GemFireSessionInstantiator extends Instantiator {

		public static GemFireSessionInstantiator create() {
			return new GemFireSessionInstantiator(GemFireSession.class, 800813552);
		}

		public GemFireSessionInstantiator(Class<? extends DataSerializable> type, int id) {
			super(type, id);
		}

		@Override
		public DataSerializable newInstance() {
			return new GemFireSession();
		}
	}

	/**
	 * The GemFireSessionAttributes class is a container for Session attributes implementing
	 * both the {@link DataSerializable} and {@link Delta} GemFire interfaces for efficient
	 * storage and distribution (replication) in GemFire. Additionally, GemFireSessionAttributes
	 * extends {@link AbstractMap} providing {@link Map}-like behavior since attributes of a Session
	 * are effectively a name to value mapping.
	 *
	 * @see java.util.AbstractMap
	 * @see org.apache.geode.DataSerializable
	 * @see org.apache.geode.DataSerializer
	 * @see org.apache.geode.Delta
	 */
	@SuppressWarnings("serial")
	public static class GemFireSessionAttributes extends AbstractMap<String, Object>
			implements DataSerializable, Delta {

		protected static final boolean DEFAULT_ALLOW_JAVA_SERIALIZATION = true;

		private transient final Map<String, Object> sessionAttributes = new HashMap<>();
		private transient final Map<String, Object> sessionAttributeDeltas = new HashMap<>();

		private transient final Object lock;

		/* (non-Javadoc) */
		protected GemFireSessionAttributes() {
			this.lock = this;
		}

		/* (non-Javadoc) */
		protected GemFireSessionAttributes(Object lock) {
			this.lock = (lock != null ? lock : this);
		}

		/* (non-Javadoc) */
		public static GemFireSessionAttributes create() {
			return new GemFireSessionAttributes();
		}

		/* (non-Javadoc) */
		public static GemFireSessionAttributes create(Object lock) {
			return new GemFireSessionAttributes(lock);
		}

		/* (non-Javadoc) */
		public void setAttribute(String attributeName, Object attributeValue) {

			synchronized (this.lock) {
				if (attributeValue != null) {
					if (!attributeValue.equals(this.sessionAttributes.put(attributeName, attributeValue))) {
						this.sessionAttributeDeltas.put(attributeName, attributeValue);
					}
				}
				else {
					removeAttribute(attributeName);
				}
			}
		}

		/* (non-Javadoc) */
		public void removeAttribute(String attributeName) {

			synchronized (this.lock) {
				if (this.sessionAttributes.remove(attributeName) != null) {
					this.sessionAttributeDeltas.put(attributeName, null);
				}
			}
		}

		/* (non-Javadoc) */
		@SuppressWarnings("unchecked")
		public <T> T getAttribute(String attributeName) {

			synchronized (this.lock) {
				return (T) this.sessionAttributes.get(attributeName);
			}
		}

		/* (non-Javadoc) */
		public Set<String> getAttributeNames() {

			synchronized (this.lock) {
				return Collections.unmodifiableSet(new HashSet<>(this.sessionAttributes.keySet()));
			}
		}

		/* (non-Javadoc) */
		protected boolean allowJavaSerialization() {
			return DEFAULT_ALLOW_JAVA_SERIALIZATION;
		}

		/* (non-Javadoc); NOTE: entrySet implementation is not Thread-safe. */
		@Override
		@SuppressWarnings("all")
		public Set<Entry<String, Object>> entrySet() {

			return new AbstractSet<Entry<String, Object>>() {

				@Override
				public Iterator<Entry<String, Object>> iterator() {
					return Collections.unmodifiableMap(GemFireSessionAttributes.this.sessionAttributes)
						.entrySet().iterator();
				}

				@Override
				public int size() {
					return GemFireSessionAttributes.this.sessionAttributes.size();
				}
			};
		}

		/* (non-Javadoc) */
		public void from(Session session) {

			synchronized (this.lock) {
				session.getAttributeNames().forEach(attributeName ->
					setAttribute(attributeName, session.getAttribute(attributeName)));
			}
		}

		/* (non-Javadoc) */
		public void from(GemFireSessionAttributes sessionAttributes) {

			synchronized (this.lock) {
				sessionAttributes.getAttributeNames().forEach(attributeName ->
					setAttribute(attributeName, sessionAttributes.getAttribute(attributeName)));
			}
		}

		/* (non-Javadoc) */
		public void toData(DataOutput out) throws IOException {

			synchronized (this.lock) {

				Set<String> attributeNames = getAttributeNames();

				out.writeInt(attributeNames.size());

				for (String attributeName : attributeNames) {
					out.writeUTF(attributeName);
					writeObject(getAttribute(attributeName), out);
				}
			}
		}

		/* (non-Javadoc) */
		void writeObject(Object obj, DataOutput out) throws IOException {
			DataSerializer.writeObject(obj, out, allowJavaSerialization());
		}

		/* (non-Javadoc) */
		public void fromData(DataInput in) throws IOException, ClassNotFoundException {

			synchronized (this.lock) {

				for (int count = in.readInt(); count > 0; count--) {
					setAttribute(in.readUTF(), readObject(in));
				}

				this.sessionAttributeDeltas.clear();
			}
		}

		/* (non-Javadoc) */
		<T> T readObject(DataInput in) throws ClassNotFoundException, IOException {
			return DataSerializer.readObject(in);
		}

		/* (non-Javadoc) */
		public boolean hasDelta() {

			synchronized (this.lock) {
				return !this.sessionAttributeDeltas.isEmpty();
			}
		}

		/* (non-Javadoc) */
		public void toDelta(DataOutput out) throws IOException {

			synchronized (this.lock) {

				out.writeInt(this.sessionAttributeDeltas.size());

				for (Map.Entry<String, Object> entry : this.sessionAttributeDeltas.entrySet()) {
					out.writeUTF(entry.getKey());
					writeObject(entry.getValue(), out);
				}

				this.sessionAttributeDeltas.clear();
			}
		}

		/* (non-Javadoc) */
		public void fromDelta(DataInput in) throws InvalidDeltaException, IOException {

			synchronized (this.lock) {
				try {
					int count = in.readInt();

					Map<String, Object> deltas = new HashMap<>(count);

					while (count-- > 0) {
						deltas.put(in.readUTF(), readObject(in));
					}

					deltas.forEach((key, value) -> {
						setAttribute(key, value);
						this.sessionAttributeDeltas.remove(key);
					});
				}
				catch (ClassNotFoundException e) {
					throw new InvalidDeltaException("class type in data not found", e);
				}
			}
		}

		@Override
		public String toString() {
			return this.sessionAttributes.toString();
		}
	}

	/**
	 * GemFireSessionAttributesInstantiator is a GemFire {@link Instantiator} use to instantiate instances
	 * of the {@link GemFireSessionAttributes} object used in GemFire's data serialization framework when
	 * persisting Session attributes state in GemFire.
	 */
	public static class GemFireSessionAttributesInstantiator extends Instantiator {

		public static GemFireSessionAttributesInstantiator create() {
			return new GemFireSessionAttributesInstantiator(GemFireSessionAttributes.class, 800828008);
		}

		public GemFireSessionAttributesInstantiator(Class<? extends DataSerializable> type, int id) {
			super(type, id);
		}

		@Override
		public DataSerializable newInstance() {
			return new GemFireSessionAttributes();
		}
	}
}
