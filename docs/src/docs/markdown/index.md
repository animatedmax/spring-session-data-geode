---
title: Spring Session
---

Spring Session provides an API and implementations for managing a user's
session information. It also provides transparent integration with:

- **HttpSession**: Enables the `HttpSession` to be clustered without being tied to an application container-specific solution.

- **REST API**: Allows the session ID to be provided in the protocol header to work with RESTful APIs.

- **WebSocket**: Provides the ability to keep the `HttpSession` alive when receiving WebSocket messages.

- **WebSession**: Allows replacing the Spring WebFlux's `WebSession` in an application container neutral way.

Spring Session replaces the `javax.servlet.http.HttpSession` in an application container neutral way by supplying a more common and robust session implementation backing the `HttpSession`.

## <a id="sample-applications-and-guides"></a>Sample Applications and Guides

### <a id="spring-boot"></a>Table 1. Sample Application using Spring Boot

<table>
  <thead>
    <tr class="header">
      <th>Source Code</th>
      <th>Description</th>
      <th>Guide</th>
    </tr>
  </thead>
  <tbody>
    <tr class="odd">
      <td><a href="https://github.com/spring-projects/spring-session-data-geode/tree/2.7.1/samples/boot/gemfire">HttpSession with Spring Boot and VMware GemFire</a></td>
      <td>Demonstrates how to use Spring Session to manage the <code>HttpSession</code> with VMware GemFire in a Spring Boot application using a Client-Server topology.</td>
      <td><a href="guides/boot-gemfire.html">HttpSession with Spring Boot and VMware GemFire Guide</a></td>
    </tr>
    <tr class="even">
      <td><a href="https://github.com/spring-projects/spring-session-data-geode/tree/2.7.1/samples/boot/gemfire-with-gfsh-servers">HttpSession with Spring Boot and VMware GemFire using gfsh</a></td>
      <td>Demonstrates how to use Spring Session to manage the <code>HttpSession</code> with VMware GemFire in a Spring Boot application using a Client-Server topology. Additionally configures and uses VMware GemFire's <em>DataSerialization</em> framework.</td>
      <td><a href="guides/boot-gemfire-with-gfsh-servers.html">HttpSession with Spring Boot and VMware GemFire using gfsh Guide</a></td>
    </tr>
    <tr class="odd">
      <td><a href="https://github.com/spring-projects/spring-session-data-geode/tree/2.7.1/samples/boot/gemfire-with-scoped-proxies">HttpSession with Spring Boot and VMware GemFire using Scoped Proxies</a></td>
      <td>Demonstrates how to use Spring Session to manage the <code>HttpSession</code> with VMware GemFire in a Spring Boot application using a Client-Server topology. The application also makes use of Spring Request and Session Scoped Proxy beans.</td>
      <td><a href="guides/boot-gemfire-with-scoped-proxies.html">HttpSession with Spring Boot and VMware GemFire using Scoped Proxies Guide</a></td>
    </tr>
  </tbody>
</table>

### <a id="java-based"></a>Table 2. Sample Applications Using Spring's Java-Based Configuration

<table>
  <thead>
    <tr class="header">
      <th>Source Code</th>
      <th>Description</th>
      <th>Guide</th>
    </tr>
  </thead>
  <tbody>
    <tr class="odd">
      <td><a href="https://github.com/spring-projects/spring-session-data-geode/tree/2.7.1/samples/javaconfig/gemfire-clientserver">HttpSession with VMware GemFire Client-Server</a></td>
      <td>Demonstrates how to use Spring Session to manage the <code>HttpSession</code> with VMware GemFire using a Client-Server topology.</td>
      <td><a href="guides/java-gemfire-clientserver.html">HttpSession with VMware GemFire Client-Server Guide</a></td>
    </tr>
    <tr class="even">
      <td><a href="https://github.com/spring-projects/spring-session-data-geode/tree/2.7.1/samples/javaconfig/gemfire-p2p">HttpSession with VMware GemFire P2P</a></td>
      <td>Demonstrates how to use Spring Session to manage the <code>HttpSession</code> with VMware GemFire using a P2P topology.</td>
      <td><a href="guides/java-gemfire-p2p.html">HttpSession with VMware GemFire P2P Guide</a></td>
    </tr>
  </tbody>
</table>

### <a id="xml-based"></a>Table 2. Sample Applications Using Spring's XML-Based Configuration

<table>
  <thead>
    <tr class="header">
      <th>Source Code</th>
      <th>Description</th>
      <th>Guide</th>
    </tr>
  </thead>
  <tbody>
    <tr class="odd">
      <td><a href="https://github.com/spring-projects/spring-session-data-geode/tree/2.7.1/samples/xml/gemfire-clientserver">HttpSession with VMware GemFire Client-Server</a></td>
      <td>Demonstrates how to use Spring Session to manage the <code>HttpSession</code> with VMware GemFire using a Client-Server topology.</td>
      <td><a href="guides/xml-gemfire-clientserver.html">HttpSession with VMware GemFire Client-Server Guide</a></td>
    </tr>
    <tr class="even">
      <td><a href="https://github.com/spring-projects/spring-session-data-geode/tree/2.7.1/samples/xml/gemfire-p2p">HttpSession with VMware GemFire P2P</a></td>
      <td>Demonstrates how to use Spring Session to manage the <code>HttpSession</code> with VMware GemFire using a P2P topology.</td>
      <td><a href="guides/xml-gemfire-p2p.html">HttpSession with VMware GemFire P2P Guide</a></td>
    </tr>
  </tbody>
</table>

## <a id="httpsession-integration"></a>HttpSession Integration

Spring Session provides transparent integration with
`javax.servlet.http.HttpSession`. This means that developers can replace
the `HttpSession` implementation with an implementation that is backed
by Spring Session.

Spring Session enables multiple different data store providers, like VMware GemFire, to be plugged in to manage the `HttpSession` state.

### <a id="httpsession-management"></a>HttpSession Management with VMware GemFire

When [VMware GemFire](https://docs.vmware.com/en/VMware-Tanzu-GemFire/index.html) is used with
Spring Session, a web application's `javax.servlet.http.HttpSession` can
be replaced with a **clustered** implementation managed by
VMware GemFire and accessed using Spring Session's API.

The two most common topologies for managing session state using VMware GemFire:

- [Client-Server](#clientserver)
- [Peer-To-Peer (P2P)](#p2p)

Additionally, VMware GemFire supports site-to-site replication using WAN topology.
The ability to configure and use VMware GemFire's WAN functionality is independent of Spring Session.

For more information about configuring VMware GemFire WAN functionality using Spring Data for
VMware GemFire, see [Configuring WAN Gateways](https://docs-staging.vmware.com/en/Spring-Data-for-VMware-GemFire/2.7/sdfg/GUID-reference-bootstrap.html)
in the Spring Data for VMware GemFire product documentation.

#### <a id="gemfire-client-server"></a>VMware GemFire Client-Server

The [Client-Server](https://docs.vmware.com/en/VMware-Tanzu-GemFire/9.15/tgf/GUID-topologies_and_comm-cs_configuration-chapter_overview.html)
topology is a common configuration choice among users
when using VMware GemFire as a provider in Spring Session
because a VMware GemFire server has significantly different and
unique JVM heap requirements than compared to the application. Using a
Client-Server topology enables an application to manage
application state independently from other application processes.

In a Client-Server topology, an application using Spring Session will
open one or more connections to a remote cluster of
VMware GemFire servers that will manage access to all
`HttpSession` state.

You can configure a Client-Server topology with either:

- [Java-based Configuration](#gemfire-clientserver-java)

- [XML-based Configuration](#gemfire-clientserver-xml)

## <a name="gemfire-client-server-java">VMware GemFire Client-Server Java-Based Configuration

This section describes how to configure VMware GemFire's
Client-Server topology to manage `HttpSession` state with Java-based
configuration.

<p class="note"><strong>Note</strong>: The <a href="#samples">HttpSession with
VMware GemFire Client-Server</a> provides a working sample
demonstrating how to integrate Spring Session with
VMware GemFire to manage <code>HttpSession</code> state using
Java configuration.</p>

### <a id="spring-java-configuration"></a>Spring Java Configuration

After adding the required dependencies and repository declarations, we
can create the Spring configuration. The Spring configuration is
responsible for creating a Servlet `Filter` that replaces the
`HttpSession` with an implementation backed by Spring Session and VMware GemFire.

#### <a id="java-client-configuration"></a>Client Configuration

Add the following Spring configuration:

```highlight
@ClientCacheApplication(name = "SpringSessionDataGeodeJavaConfigSampleClient", logLevel = "error",
  readTimeout = 15000, retryAttempts = 1, subscriptionEnabled = true) <!--SEE COMMENT 1-->
@EnableGemFireHttpSession(poolName = "DEFAULT") <!--SEE COMMENT 2-->
public class ClientConfig extends ClientServerIntegrationTestsSupport {

    @Bean
    ClientCacheConfigurer gemfireServerReadyConfigurer( <!--SEE COMMENT 3-->
        @Value("${spring.data.gemfire.cache.server.port:40404}") int cacheServerPort) {

        return (beanName, clientCacheFactoryBean) -> waitForServerToStart("localhost", cacheServerPort);
    }
}
```

Comments:

1. We declare our Web application to be an VMware GemFire cache client by annotating the `ClientConfig` class with `@ClientCacheApplication`. Additionally, we adjust a few "DEFAULT" `Pool` settings, for example `readTimeout`.

2. `@EnableGemFireHttpSession` creates a Spring bean named `springSessionRepositoryFilter` that implements `javax.servlet.Filter`. The filter replaces the `HttpSession` with an implementation provided by Spring Session and backed by VMware GemFire. Additionally, the configuration creates the necessary client-side `Region`  corresponding to the same server-side `Region` by name. By default, this is `ClusteredSpringSessions`, which is a `PROXY` `Region`. All session state is sent from the client to the server through `Region` data access operations. The client-side `Region` use the "DEFAULT" `Pool`.

3. We wait to ensure the VMware GemFire Server is up and running before we proceed. This is used for automated integration testing purposes.

<p class="note">Note</strong>: In typical VMware GemFire production deployments,
where the cluster includes potentially hundreds or thousands of servers
(a.k.a. data nodes), it is more common for clients to connect to one or
more VMware GemFire Locators running in the same cluster. A Locator passes
meta-data to clients about the servers available in the cluster, the
individual server load and which servers have the client's data of
interest, which is particularly important for direct, single-hop data
access and latency-sensitive applications. For more information, see <a
href="https://docs.vmware.com/en/VMware-Tanzu-GemFire/9.15/tgf/GUID-topologies_and_comm-cs_configuration-standard_client_server_deployment.html">Standard Client-Server Deployment</a> in the VMware GemFire product documentation.</p>

For more information about configuring Spring Data for VMware GemFire, refer to the <a
href="https://docs.spring.io/spring-data/geode/docs/current/reference/html">Spring Data for Apache Geode Reference Guide</a>.

##### <a id="enabling-gemfire-httpsession-management"></a>Enabling VMware Tanzu GemFire HttpSession Management

The `@EnableGemFireHttpSession` annotation enables developers to
configure certain aspects of both Spring Session and VMware GemFire
out-of-the-box using the following attributes:

- `clientRegionShortcut`: Specifies VMware GemFire [data management
policy](https://docs.vmware.com/en/VMware-Tanzu-GemFire/9.15/tgf/GUID-developing-region_options-region_types.html) on the client with the [ClientRegionShortcut](https://geode.apache.org/releases/latest/javadoc/org/apache/geode/cache/client/ClientRegionShortcut.html). Default: `PROXY`. This attribute is only used when configuring the client `Region`.

- `indexableSessionAttributes`: Identifies the Session attributes by name that should be indexed for querying purposes. Only Session attributes explicitly identified by name will be indexed.

- `maxInactiveIntervalInSeconds`: Controls *HttpSession* idle-timeout expiration. Defaults to 30 minutes.

- `poolName`: Name of the dedicated VMware GemFire `Pool` used to connect a client to the cluster of servers. This attribute is only used when the application is a cache client. Default: `gemfirePool`.

- `regionName`: Specifies the name of the VMware GemFire `Region` used to store and manage `HttpSession` state. Default: `ClusteredSpringSessions`.

- `serverRegionShortcut`: Specifies VMware GemFire [data management
policy](https://docs.vmware.com/en/VMware-Tanzu-GemFire/9.15/tgf/GUID-developing-region_options-region_types.html) on the server with the [RegionShortcut](https://geode.apache.org/releases/latest/javadoc/org/apache/geode/cache/RegionShortcut.html). Default: `PARTITION`. This attribute is only used when configuring server `Regions`, or when a P2P topology is employed.

<p class="note">Note</strong>: The VMware GemFire client <code>Region</code> name must match a
server <code>Region</code> by the same name if the client <code>Region</code> is a
<code>PROXY</code> or <code>CACHING_PROXY</code>. Client and server
<code>Region</code> names are not required to match if the client
<code>Region</code> used to store session state is 
<code>LOCAL</code>.
However, Session state will not be propagated to the server and you lose all the benefits of using
VMware GemFire to store and
manage distributed, replicated session state information on the servers
in a distributed, replicated manner.</p>

#### <a id="java-server-configuration"></a>Server Configuration

We create a VMware GemFire Server. The cache client communicates with the server and sends session state to the server to manage.

In this sample, we use the following Java configuration to configure and run a VMware GemFire Server:

```highlight
@CacheServerApplication(name = "SpringSessionDataGeodeJavaConfigSampleServer", logLevel = "error") <!--SEE COMMENT 1-->
@EnableGemFireHttpSession(maxInactiveIntervalInSeconds = 30) <!--SEE COMMENT 2-->
public class GemFireServer {

    @SuppressWarnings("resource")
    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(GemFireServer.class).registerShutdownHook();
    }
}
```

Comments

1. We use the `@CacheServerApplication` annotation to simplify the creation of a peer cache instance containing with a `CacheServer` for cache clients to connect.

2. (Optional) We use the `@EnableGemFireHttpSession` annotation to create the necessary server-side `Region` to store the `HttpSessions` state. By default, this is `ClusteredSpringSessions`. This step is optional because we could create the Session `Region` manually.

## <a id="gemfire-client-server-xml-configuration"></a>VMware GemFire Client-Server using XML Configuration

This section describes how to configure VMware GemFire's
Client-Server topology to manage `HttpSession` state with XML-based
configuration.

### <a id="spring-xml-configuration"></a>Spring XML Configuration

After adding the required dependencies and repository declarations, we
can create the Spring configuration. The Spring configuration is
responsible for creating a `Servlet` `Filter` that replaces the
`javax.servlet.http.HttpSession` with an implementation backed by Spring
Session and VMware GemFire.

#### <a id="xml-client-configuration"></a>Client Configuration

Add the following Spring configuration:

```highlight
    <context:annotation-config/>

    <context:property-placeholder/>

    <bean class="sample.client.ClientServerReadyBeanPostProcessor"/>

    <!--SEE COMMENT 1-->
    <util:properties id="gemfireProperties">
        <prop key="log-level">${spring.data.gemfire.cache.log-level:error}</prop>
    </util:properties>

    <!--SEE COMMENT 2-->
    <gfe:client-cache properties-ref="gemfireProperties" pool-name="gemfirePool"/>

    <!--SEE COMMENT 3-->
    <gfe:pool read-timeout="15000" retry-attempts="1" subscription-enabled="true">
        <gfe:server host="localhost" port="${spring.data.gemfire.cache.server.port:40404}"/>
    </gfe:pool>

    <!--SEE COMMENT 4-->
    <bean class="org.springframework.session.data.gemfire.config.annotation.web.http.GemFireHttpSessionConfiguration"
        p:poolName="DEFAULT"/>
```

Comments:

1. (Optional) Include a `Properties` bean to configure certain aspects of the VMware GemFire `ClientCache` using [Tanzu GemFire Properties](https://docs.vmware.com/en/VMware-Tanzu-GemFire/9.15/tgf/GUID-reference-topics-gemfire_properties.html). In this case, we are setting VMware GemFire's `log-level` using an application-specific System property, defaulting to `warning` if unspecified.

2. Create an instance of an VMware GemFire `ClientCache`. Initialize it with the `gemfireProperties`.

3. Configure a `Pool` of connections to communicate with the VMware GemFire Server in this  Client-Server topology. The `Pool` has been configured to connect directly to the server using the nested `gfe:server` element.

4. A `GemFireHttpSessionConfiguration` bean is registered to enable Spring Session functionality.

<p class="note">Note</strong>: In typical VMware GemFire production deployments,
where the cluster includes potentially hundreds or thousands of servers
(a.k.a. data nodes), it is more common for clients to connect to one or
more VMware GemFire Locators running in the same cluster. A Locator passes
meta-data to clients about the servers available in the cluster, the
individual server load and which servers have the client's data of
interest, which is particularly important for direct, single-hop data
access and latency-sensitive applications. For more information, see <a
href="https://docs.vmware.com/en/VMware-Tanzu-GemFire/9.15/tgf/GUID-topologies_and_comm-cs_configuration-standard_client_server_deployment.html">Standard Client-Server Deployment</a> in the VMware GemFire product documentation.</p>

For more information about configuring Spring Data for VMware GemFire, refer to the <a
href="https://docs.spring.io/spring-data/geode/docs/current/reference/html">Spring Data for Apache Geode Reference Guide</a>.

#### <a id="xml-server-configuration"></a>Server Configuration

We create a VMware GemFire Server. The cache client communicates with the server and sends session state to the server to manage.

In this sample, we use the following XML configuration to configure and run a VMware GemFire Server:

```highlight
<context:annotation-config/>

    <context:property-placeholder/>

    <!--SEE COMMENT 1-->
    <util:properties id="gemfireProperties">
        <prop key="name">SpringSessionDataGeodeSampleXmlServer</prop>
        <prop key="log-level">${spring.data.gemfire.cache.log-level:error}</prop>
    </util:properties>

    <!--SEE COMMENT 2-->
    <gfe:cache properties-ref="gemfireProperties"/>

    <!--SEE COMMENT 3-->
    <gfe:cache-server port="${spring.data.gemfire.cache.server.port:40404}"/>

    <!--SEE COMMENT 4-->
    <bean class="org.springframework.session.data.gemfire.config.annotation.web.http.GemFireHttpSessionConfiguration"
        p:maxInactiveIntervalInSeconds="30"/>
```

Comments:

1. (Optional) Include a `Properties` bean to configure certain aspects of the VMware GemFire `ClientCache` using [Tanzu GemFire Properties](https://docs.vmware.com/en/VMware-Tanzu-GemFire/9.15/tgf/GUID-reference-topics-gemfire_properties.html). In this case, we are setting VMware GemFire's `log-level` using an application-specific System property, defaulting to `warning` if unspecified.

2. Configure a VMware GemFire peer `Cache` instance. Initialize it with the VMware GemFire properties.

3. Define a `CacheServer` with sensible configuration for `bind-address` and `port` used by our cache client application to connect to the server and send session state.

4. Enable the same Spring Session functionality that was declared in the client XML configuration by registering an instance of `GemFireHttpSessionConfiguration`. Set the session expiration timeout to 30 seconds.

Bootstrap the VMware GemFire Server with the following:

```highlight
@Configuration <!--SEE COMMENT 1-->
@ImportResource("META-INF/spring/session-server.xml") <!--SEE COMMENT 1-->
public class GemFireServer {

    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(GemFireServer.class).registerShutdownHook();
    }
```

Comments:

1. The `@Configuration` annotation designates this Java class as a source of Spring configuration metadata.

2. The configuration primarily comes from the `META-INF/spring/session-server.xml` file.

### <a id="xml-servlet-container-initialization"></a>XML Servlet Container Initialization

[Spring XML Configuration](#spring-xml-configuration) created a Spring bean named
`springSessionRepositoryFilter` that implements the `javax.servlet.Filter` interface.
The `springSessionRepositoryFilter` bean is responsible for replacing the
`javax.servlet.http.HttpSession` with a custom implementation that is
provided by Spring Session and VMware GemFire.

The `Filter` requires that we instruct Spring to load the `session-client.xml` configuration file.
We do this with the following configuration in `src/main/webapp/WEB-INF/web.xml`:

```highlight
<context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>/WEB-INF/spring/session-client.xml</param-value>
</context-param>
<listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>
```

The [ContextLoaderListener](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/context/ContextLoaderListener.html)
reads the `contextConfigLocation` context parameter value and picks up the `session-client.xml`
configuration file.
We must also ensure that the Servlet container, Tomcat, uses the `springSessionRepositoryFilter` for every request.

The following in `src/main/webapp/WEB-INF/web.xml` performs this last step:

```highlight
<filter>
    <filter-name>springSessionRepositoryFilter</filter-name>
    <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
</filter>
<filter-mapping>
    <filter-name>springSessionRepositoryFilter</filter-name>
    <url-pattern>/*</url-pattern>
    <dispatcher>REQUEST</dispatcher>
    <dispatcher>ERROR</dispatcher>
</filter-mapping>
```

The [DelegatingFilterProxy](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/filter/DelegatingFilterProxy.html)
will look up a bean by the name of `springSessionRepositoryFilter` and cast it to a `Filter`.
For every HTTP request, the `DelegatingFilterProxy` is invoked, which delegates to the
`springSessionRepositoryFilter`.

## <a id="gemfire-peer-to-peer"></a>VMware GemFire Peer-To-Peer (P2P)

A less common approach is to configure your Spring Session application
as a peer member in the VMware GemFire cluster using the
[Peer-To-Peer (P2P)](https://docs.vmware.com/en/VMware-Tanzu-GemFire/9.15/tgf/GUID-topologies_and_comm-p2p_configuration-chapter_overview.html)
topology. In this configuration, the Spring Session application would be
an actual server or data node in the VMware GemFire cluster, and not just a cache client as before.

One advantage to this approach is the proximity of the application to
the application's state, and in particular the
`HttpSession` state. However, there are other effective means of
accomplishing similar data dependent computations, such as using
VMware GemFire's [Function
Execution](https://docs.vmware.com/en/VMware-Tanzu-GemFire/9.15/tgf/GUID-developing-function_exec-chapter_overview.html).
Any of VMware GemFire's other 
features can be used when VMware GemFire is serving as a provider in
Spring Session.

You can use the P2P topology for testing purposes and for smaller,
more focused, and self-contained applications, such as those found in a
microservices architecture. This can improve on your
application's perceived latency and throughput needs.

You can configure a Peer-To-Peer (P2P) topology with either of the following:

- [Java-based Configuration](#httpsession-gemfire-p2p-java)
- [XML-based Configuration](#httpsession-gemfire-p2p-xml)

### <a id="httpsession-gemfire-p2p-java"></a>VMware GemFire Peer-To-Peer (P2P) Java-based Configuration

This section describes how to configure VMware GemFire's
Peer-To-Peer (P2P) topology to manage `HttpSession` state using
Java-based configuration.

#### <a id="spring-java-configuration"></a>Spring Java Configuration

After adding the required dependencies and repository declarations, we
can create the Spring configuration. The Spring configuration is
responsible for creating a Servlet `Filter` that replaces the
`javax.servlet.http.HttpSession` with an
implementation backed by Spring Session and VMware GemFire.

Add the following Spring configuration:

```highlight
@PeerCacheApplication(name = "SpringSessionDataGeodeJavaConfigP2pSample", logLevel = "error") <!--SEE COMMENT 1-->
@EnableGemFireHttpSession(maxInactiveIntervalInSeconds = 30) <!--SEE COMMENT 2-->
public class Config {

}
```

1. We use the `@PeerCacheApplication` annotation to simplify the creation of a peer cache instance.

2.  We annotate the `Config` class with `@EnableGemFireHttpSession` to create the necessary server-side `Region` used to store `HttpSession` state. By default, this `Region` is `ClusteredSpringSessions`.

For more information about configuring Spring Data for VMware GemFire, refer to the <a
href="https://docs.spring.io/spring-data/geode/docs/current/reference/html">Spring Data for Apache Geode Reference Guide</a>.

The `@EnableGemFireHttpSession` annotation enables developers to
configure certain aspects of both Spring Session and VMware GemFire
out-of-the-box using the following attributes:

- `clientRegionShortcut`: Specifies VMware GemFire [data management
policy](https://docs.vmware.com/en/VMware-Tanzu-GemFire/9.15/tgf/GUID-developing-region_options-region_types.html) on the client with the [ClientRegionShortcut](https://geode.apache.org/releases/latest/javadoc/org/apache/geode/cache/client/ClientRegionShortcut.html). Default: `PROXY`. This attribute is only used when configuring the client `Region`.

- `indexableSessionAttributes`: Identifies the Session attributes by name that should be indexed for querying purposes. Only Session attributes explicitly identified by name will be indexed.

- `maxInactiveIntervalInSeconds`: Controls *HttpSession* idle-timeout expiration. Defaults to 30 minutes.

- `poolName`: Name of the dedicated VMware GemFire `Pool` used to connect a client to the cluster of servers. This attribute is only used when the application is a cache client. Default: `gemfirePool`.

- `regionName`: Specifies the name of the VMware GemFire `Region` used to store and manage `HttpSession` state. Default: `ClusteredSpringSessions`.

- `serverRegionShortcut`: Specifies VMware GemFire [data management
policy](https://docs.vmware.com/en/VMware-Tanzu-GemFire/9.15/tgf/GUID-developing-region_options-region_types.html) on the server with the [RegionShortcut](https://geode.apache.org/releases/latest/javadoc/org/apache/geode/cache/RegionShortcut.html). Default: `PARTITION`. This attribute is only used when configuring server `Regions`, or when a P2P topology is employed.

##### <a id="java-servlet-container-initialization"></a>Java Servlet Container Initialization

[Spring Java Configuration](#spring-java-configuration) created a Spring bean named
`springSessionRepositoryFilter` that implements `javax.servlet.Filter`.
The `springSessionRepositoryFilter` bean is responsible for replacing
the `javax.servlet.http.HttpSession` with a custom implementation backed
by Spring Session and VMware GemFire.

The `Filter` requires Spring to load the `Config` class. We must also ensure that the
Servlet container, Tomcat, uses `springSessionRepositoryFilter` for every HTTP request.

Spring Session provides a utility class named `AbstractHttpSessionApplicationInitializer` that
provides this functionality.

Example from `src/main/java/sample/Initializer.java`:

```highlight
public class Initializer extends AbstractHttpSessionApplicationInitializer { <!--SEE COMMENT 1-->

    public Initializer() {
        super(Config.class); <!--SEE COMMENT 2-->
	}
}
```

Comments:

1. Extends `AbstractHttpSessionApplicationInitializer` to ensure that a Spring bean named `springSessionRepositoryFilter` is registered with the Servlet container and is used for every HTTP request.

2. `AbstractHttpSessionApplicationInitializer` provides a mechanism to allow Spring to load the `Config` class.

### <a id="httpsession-gemfire-p2p-xml"></a>VMware GemFire Peer-To-Peer (P2P) XML-based Configuration

This section describes how to configure VMware GemFire's
Peer-To-Peer (P2P) topology to manage `HttpSession` state using
XML-based configuration.

#### <a id="spring-xml-configuration"></a>Spring XML Configuration

After adding the required dependencies and repository declarations, we
can create the Spring configuration.
The Spring configuration is responsible for creating a `Servlet`
`Filter` that replaces the `javax.servlet.http.HttpSession` with an
implementation backed by Spring Session and VMware GemFire.

Add the following Spring configuration in `src/main/webapp/WEB-INF/spring/session.xml`:

```highlight
<context:annotation-config/>

<context:property-placeholder/>

<!--SEE COMMENT 1-->
<util:properties id="gemfireProperties">
    <prop key="name">SpringSessionDataGeodeXmlP2pSample</prop>
    <prop key="log-level">${spring.data.gemfire.cache.log-level:error}</prop>
</util:properties>

<!--SEE COMMENT 2-->
<gfe:cache properties-ref="gemfireProperties"/>

<!--SEE COMMENT 3-->
<bean class="org.springframework.session.data.gemfire.config.annotation.web.http.GemFireHttpSessionConfiguration"
    p:maxInactiveIntervalInSeconds="30"/>
```

Comments:

1. (Optional) Include a `Properties` bean to configure certain aspects of the VMware GemFire `ClientCache` using [Tanzu GemFire Properties](https://docs.vmware.com/en/VMware-Tanzu-GemFire/9.15/tgf/GUID-reference-topics-gemfire_properties.html). In this case, we are setting VMware GemFire's `log-level` using an application-specific System property, defaulting to `warning` if unspecified.

2. Create a VMware GemFire peer `Cache` instance. Initialize it with the `gemfireProperties`.

3. Enable Spring Session functionality by registering an instance of `GemFireHttpSessionConfiguration`.

For more information about configuring Spring Data for VMware GemFire, refer to the <a
href="https://docs.spring.io/spring-data/geode/docs/current/reference/html">Spring Data for Apache Geode Reference Guide</a>.

#### <a id="xml-servlet-container-initialization"></a>XML Servlet Container Initialization

[Spring XML Configuration](#spring-xml-configuration) created a Spring bean named
`springSessionRepositoryFilter` that implements the `javax.servlet.Filter` interface.
The `springSessionRepositoryFilter` bean is responsible for replacing the
`javax.servlet.http.HttpSession` with a custom implementation that is
provided by Spring Session and VMware GemFire.

The `Filter` requires that we instruct Spring to load the `session.xml` configuration file.
We do this with the following configuration in `src/main/webapp/WEB-INF/web.xml`:

<div class="content">

```highlight
<context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>
        /WEB-INF/spring/*.xml
    </param-value>
</context-param>
<listener>
    <listener-class>
        org.springframework.web.context.ContextLoaderListener
    </listener-class>
</listener>
```

The [ContextLoaderListener](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/context/ContextLoaderListener.html)
reads the `contextConfigLocation` context parameter value and picks up the `session.xml`
configuration file.
We must also ensure that the Servlet container, Tomcat, uses the `springSessionRepositoryFilter` for every request.

The following in `src/main/webapp/WEB-INF/web.xml` performs this last step:

```highlight
<filter>
    <filter-name>springSessionRepositoryFilter</filter-name>
    <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
</filter>
<filter-mapping>
    <filter-name>springSessionRepositoryFilter</filter-name>
    <url-pattern>/*</url-pattern>
    <dispatcher>REQUEST</dispatcher>
    <dispatcher>ERROR</dispatcher>
</filter-mapping>
```

The [DelegatingFilterProxy](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/filter/DelegatingFilterProxy.html)
will look up a bean by the name of `springSessionRepositoryFilter` and cast it to a `Filter`.
For every HTTP request, the `DelegatingFilterProxy` is invoked, which delegates to the
`springSessionRepositoryFilter`.

## <a id="configuring-httpsession-management"></a>Configuring `HttpSession` Management using VMware GemFire with Properties

While the `@EnableGemFireHttpSession` annotation is easy to use and
convenient when getting started with Spring Session and
VMware GemFire in your Spring Boot applications, you quickly
run into limitations when migrating from one environment to another, for
example, like when moving from DEV to QA to PROD.

With the `@EnableGemFireHttpSession` annotation attributes, it is not
possible to vary the configuration from one environment to another.
Therefore, Spring Session for VMware GemFire introduces
well-known, documented properties for all the
`@EnableGemFireHttpSession` annotation attributes.

<table class="tableblock frame-all grid-all stretch">
  <col style="width: 28%" />
  <col style="width: 28%" />
  <col style="width: 28%" />
  <col style="width: 14%" />
  <thead>
    <tr class="header">
      <th>Property</th>
      <th>Annotation attribute</th>
      <th>Description</th>
      <th>Default</th>
    </tr>
  </thead>
  <tbody>
    <tr class="odd">
      <td>spring.session.data.gemfire.cache.client.pool.name</td>
      <td><code>EnableGemFireHttpSession.poolName</code></td>
      <td>Name of the dedicated Pool used by the client Region storing/accessing Session state.</td>
      <td>gemfirePool</td>
    </tr>
    <tr class="even">
      <td>spring.session.data.gemfire.cache.client.region.shortcut</td>
      <td><code>EnableGemFireHttpSession.clientRegionShortcut</code></td>
      <td>Sets the client Region data management policy in the client-server topology.</td>
      <td>ClientRegionShortcut.PROXY</td>
    </tr>
    <tr class="odd">
      <td>spring.session.data.gemfire.cache.server.region.shortcut</td>
      <td><code>EnableGemFireHttpSession.serverRegionShortcut</code></td>
      <td>Sets the peer Region data management policy in the peer-to-peer (P2P) topology.</td>
      <td>RegionShortcut.PARTITION</td>
    </tr>
    <tr class="even">
      <td>spring.session.data.gemfire.session.attributes.indexable</td>
      <td><code>EnableGemFireHttpSession.indexableSessionAttributes</code></td>
      <td>Comma-delimited list of Session attributes to indexed in the Session Region.</td>
      <td></td>
    </tr>
    <tr class="odd">
      <td>spring.session.data.gemfire.session.expiration.bean-name</td>
      <td><code>EnableGemFireHttpSession.sessionExpirationPolicyBeanName</code></td>
      <td>Name of the bean in the Spring container implementing the expiration strategy</td>
      <td></td>
    </tr>
    <tr class="even">
      <td>spring.session.data.gemfire.session.expiration.max-inactive-interval-seconds</td>
      <td><code>EnableGemFireHttpSession.maxInactiveIntervalInSeconds</code></td>
      <td>Session expiration timeout in seconds</td>
      <td>1800</td>
    </tr>
    <tr class="odd">
      <td>spring.session.data.gemfire.session.region.name</td>
      <td><code>EnableGemFireHttpSession.regionName</code></td>
      <td>Name of the client or peer Region used to store and access Session state.</td>
      <td>ClusteredSpringSessions</td>
    </tr>
    <tr class="even">
      <td>spring.session.data.gemfire.session.serializer.bean-name</td>
      <td><code>EnableGemFireHttpSession.sessionSerializerBeanName</code></td>
      <td>Name of the bean in the Spring container implementing the serialization strategy</td>
      <td>SessionPdxSerializer</td>
    </tr>
  </tbody>
</table>

Table 4. Well-known, documented properties for the
`@EnableGemFireHttpSession` annotation attributes.

You can adjust the configuration of Spring Session when using VMware GemFire as your provider by using
properties, as follows:

```highlight
@SpringBootApplication
@ClientCacheApplication
@EnableGemFireHttpSession(maxInactiveIntervalInSeconds = 900)
class MySpringSessionApplication {
  // ...
}
```

And then, in `application.properties`:

```highlight
#application.properties
spring.session.data.gemfire.cache.client.region.shortcut=CACHING_PROXY
spring.session.data.gemfire.session.expiration.max-inactive-internval-seconds=3600
```

Any properties explicitly defined override the corresponding
`@EnableGemFireHttpSession` annotation attribute.

In the example above, even though the `EnableGemFireHttpSession`
annotation `maxInactiveIntervalInSeconds` attribute was set to `900`
seconds, or 15 minutes, the corresponding attribute property (`spring.session.data.gemfire.session.expiration.max-inactive-interval-seconds`)
overrides the value and sets the expiration to `3600` seconds, or 60
minutes.

<p class="note"><strong>Note</strong> Properties override the annotation attribute values at runtime.</td>

### <a id="properties-of-properties"></a>Properties of Properties

You can configure your properties with other properties, as follows:

```highlight
#application.properties
spring.session.data.gemfire.session.expiration.max-inactive-internval-seconds=${app.geode.region.expiration.timeout:3600}
```

Additionally, you can use Spring profiles to vary the expiration timeout or other properties based on environment or your application,
or whatever criteria your application requirements dictate.

Property placeholders and nesting is a feature of
the core Spring Framework and not specific to Spring Session or Spring
Session for VMware GemFire.

## <a id="configuring-with-configurer"></a>Configuring `HttpSession` Management using VMware GemFire with a Configurer

In addition to properties, Spring Session for VMware GemFire
also allows you to adjust the configuration of Spring Session with
VMware GemFire using the `SpringSessionGemFireConfigurer`
interface. The interface defines a contract containing default methods
for each `@EnableGemFireHttpSession` annotation attribute that can be
overridden to adjust the configuration.

The `SpringSessionGemFireConfigurer` is similar in concept to Spring Web
MVC's Configurer interfaces (e.g.
`o.s.web.servlet.config.annotation.WebMvcConfigurer`), which adjusts
various aspects of your Web application's configuration on startup, such
as configuring async support. The advantage of declaring and
implementing a `Configurer` is that it gives you programmatic control
over your configuration. You can use this in situations where you must
express complex, conditional logic that determines whether the
configuration should be applied or not.

For example, to adjust the client Region data management policy and
Session expiration timeout, use the following:

```highlight
@Configuration
class MySpringSessionConfiguration {

  @Bean
  SpringSessionGemFireConfigurer exampleSpringSessionGemFireConfigurer() {

    return new SpringSessionGemFireConfigurer() {

      @Override
      public ClientRegionShortcut getClientRegionShortcut() {
        return ClientRegionShortcut.CACHING_PROXY;
      }

      @Override
      public int getMaxInactiveIntervalInSeconds() {
        return 3600;
      }
    };
  }
}
```

You can be as sophisticated as you like, such as by implementing your
`Configurer` in terms of other properties using Spring's `@Value`
annotation, as follows:


```highlight
@Configuration
class MySpringSessionConfiguration {

  @Bean
  @Primary
  @Profile("production")
  SpringSessionGemFireConfigurer exampleSpringSessionGemFireConfigurer(
      @Value("${app.geode.region.data-management-policy:CACHING_PROXY}") ClientRegionShortcut shortcut,
      @Value("${app.geode.region.expiration.timeout:3600}") int timeout) {

    return new SpringSessionGemFireConfigurer() {

      @Override
      public ClientRegionShortcut getClientRegionShortcut() {
        return shortcut;
      }

      @Override
      public int getMaxInactiveIntervalInSeconds() {
        return timeout;
      }
    };
  }
}
```

Spring Boot will resolves <code>@Value</code>
annotation property placeholder values or SpEL Expressions
automatically. However, if you are not using Spring Boot, then you must
explicitly register a static
<code>PropertySourcesPlaceholderConfigurer</code> bean definition.

However, you can only declare one `SpringSessionGemFireConfigurer` bean in
the Spring container at a time, unless you are also using Spring
profiles or have marked one of the multiple
`SpringSessionGemFireConfigurer` beans as primary by using Spring's
`@Primary` context annotation.


### <a id="configuration-precedence"></a>Configuration Precedence

A `SpringSessionGemFireConfigurer` takes precedence over either the
`@EnableGemFireHttpSession` annotation attributes or any of the
commonly-known and documented Spring Session for VMware GemFire
properties defined in Spring Boot `application.properties.`

If more than one configuration approach is employed by your Web
application, the following precedence will apply:

1. `SpringSessionGemFireConfigurer` "implemented" callback methods

2. Documented Spring Session for VMware GemFire properties. See corresponding `@EnableGemFireHttpSession` annotation attribute Javadoc. For example, `spring.session.data.gemfire.session.region.name`.

3. `@EnableGemFireHttpSession` annotation attributes


Spring Session for VMware GemFire is careful to only apply
configuration from a `SpringSessionGemFireConfigurer` bean declared in
the Spring container for the methods you have implemented.

In the example above, since the `getRegionName()` method was not implemented, the name of the VMware GemFire Region managing the
`HttpSession` state will not be determined by the Configurer.

#### <a id="configuration-precedence-example"></a>Example

Example Spring Session for VMware GemFire Configuration

```highlight
@ClientCacheApplication
@EnableGemFireHttpSession(
    maxInactiveIntervalInSeconds = 3600,
    poolName = "DEFAULT"
)
class MySpringSessionConfiguration {

  @Bean
  SpringSessionGemFireConfigurer sessionExpirationTimeoutConfigurer() {

    return new SpringSessionGemFireConfigurer() {

      @Override
      public int getMaxInactiveIntervalInSeconds() {
        return 300;
      }
    };
  }
}
```

Example Spring Boot `application.properties` file:

```
spring.session.data.gemfire.session.expiration.max-inactive-interval-seconds = 900
spring.session.data.gemfire.session.region.name = Sessions
```

The Session expiration timeout will be 300 seconds,
overriding both the property `spring.session.data.gemfire.session.expiration.max-inactive-interval-seconds`
of 900 seconds, as well as the explicit
`@EnableGemFireHttpSession.maxInactiveIntervalInSeconds` annotation
attribute value of 3600 seconds.


Because the "sessionExpirationTimeoutConfigurer" bean does not override
the `getRegionName()` method, the Session Region name will be determined
by the property (`spring.session.data.gemfire.session.region.name`), set to "Sessions",
which overrides the implicit `@EnableGemFireHttpSession.regionName`
annotation attribute's default value of "ClusteredSpringSessions".

The `@EnableGemFireHttpSession.poolName` annotation attribute's value of
"DEFAULT" will determine the name of the Pool used when sending Region
operations between the client and server to manage Session state on the
servers because neither the corresponding property (spring.session.data.gemfire.cache.client.pool.name\`) was set nor was
the `SpringSessionGemFireConfigurer.getPoolName()` method overridden by
the "sessionExpirationTimeoutConfigurer" bean.

The client Region used to manage Session state will have a
data management policy of `PROXY`, the default value for the
`@EnableGemFireHttpSession.clientRegionShortcut` annotation attribute,
which was not explicitly set, nor was the corresponding property (`spring.session.data.gemfire.cache.client.region.shortcut`) for this
attribute. And, because the
`SpringSessionConfigurer.getClientRegionShortcut()` method was not
overridden, the default value is used.

## <a id="gemfire-expiration"></a>VMware GemFire Expiration

By default, VMware GemFire is configured with a Region Entry,
Idle Timeout (TTI) Expiration Policy, using an expiration timeout of 30
minutes and "INVALIDATE" entry as the action. When a user's
Session remains inactive or idle for more than 30 minutes, the
Session will expire and is invalidated, and the user must begin a new
Session to continue to use the application.

If you have application specific requirements around
Session state management and expiration, and using the default, Idle
Timeout (TTI) Expiration Policy is insufficient for your Use Case,
Spring Session for VMware GemFire supports application-specific, custom expiration policies. As an application developer, you
can specify custom rules governing the expiration of a Session managed
by Spring Session, backed by VMware GemFire. Spring Session for VMware GemFire provides this `SessionExpirationPolicy`
Strategy interface.

**SessionExpirationPolicy Interface**

```highlight
@FunctionalInterface
interface SessionExpirationPolicy {

    // determine timeout for expiration of individual Session
    Optional<Duration> determineExpirationTimeout(Session session);

    // define the action taken on expiration
    default ExpirationAction getExpirationAction() {
        return ExpirationAction.INVALIDATE;
    }

    enum ExpirationAction {

        DESTROY,
        INVALIDATE

    }
}
```

You implement this interface to specify the Session expiration policies
required by your application and then register the instance as a bean in
the Spring application context.

Use the `@EnableGemFireHttpSession` annotation,
`sessionExpirationPolicyBeanName` attribute to configure the name of the
`SessionExpirationPolicy` bean implementing your custom application
policies and rules for Session expiration.

For example:

**Custom `SessionExpirationPolicy`**

```highlight
class MySessionExpirationPolicy implements SessionExpirationPolicy {

    public Duration determineExpirationTimeout(Session session) {
        // return a java.time.Duration specifying the length of time until the Session should expire
    }
}
```

Then declare the following custom `SessionExpirationPolicy` configuration in your application class:

```highlight
@SpringBootApplication
@EnableGemFireHttpSession(
    maxInactiveIntervalInSeconds = 600,
    sessionExpirationPolicyBeanName = "expirationPolicy"
)
class MySpringSessionApplication {

    @Bean
    SessionExpirationPolicy expirationPolicy() {
        return new MySessionExpirationPolicy();
    }
}
```

The name of the <code>SessionExpirationPolicy</code> bean can be configured using the
<code>spring.session.data.gemfire.session.expiration.bean-name</code>
property, or by declaring a <code>SpringSessionGemFireConfigurer</code>
bean in the Spring container and overriding the
<code>getSessionExpirationPolicyBeanName()</code> method.

You are only required to implement the
`determineExpirationTimeout(:Session):Optional<Duration>` method, which
encapsulates the rules to determine when the Session should expire. The
expiration timeout for a Session is expressed as an `Optional` of
`java.time.Duration`, which specifies the length of time until the
Session expires.

The `determineExpirationTimeout` method can be Session-specific and can
change with each invocation.

Optionally, you can implement the `getAction` method to specify the
action taken when the Session expires. By default, the Region Entry
(i.e. Session) is invalidated. Another option is to destroy the Region
Entry on expiration, which removes both the key (Session ID) and value
(Session). Invalidate only removes the value.

The <code>SessionExpirationPolicy</code> is adapted into an instance of the
<a href="https://geode.apache.org/releases/latest/javadoc/org/apache/geode/cache/CustomExpiry.html"><code>CustomExpiry</code></a>
interface. This Spring Session <code>CustomExpiry</code> object is then
set as the Session Region's <a href="https://geode.apache.org/releases/latest/javadoc/org/apache/geode/cache/RegionFactory.html#setCustomEntryIdleTimeout-org.apache.geode.cache.CustomExpiry-">custom
entry idle timeout expiration policy</a>.

During expiration determination, the
<code>CustomExpiry.getExpiry(:Region.Entry&lt;String, Session&gt;):ExpirationAttributes</code>
method is invoked for each entry (i.e. Session) in the Region every time
the expiration threads run, which in turn calls the 
<code>SessionExpirationPolicy.determineExpirationTimout(:Session):Optional&lt;Duration&gt;</code>
method. The returned <code>java.time.Duration</code> is converted to
seconds and used as the expiration timeout in the <a
href="https://geode.apache.org/releases/latest/javadoc/org/apache/geode/cache/ExpirationAttributes.html"><code>ExpirationAttributes</code></a>
returned from the <a
href="https://geode.apache.org/releases/latest/javadocorg/apache/geode/cache/CustomExpiry.html#getExpiry-org.apache.geode.cache.Region.Entry-"><code>CustomExpiry.getExpiry(..)</code></a>
method invocation.

<td class="content">VMware GemFire's expiration threads run
once every second, evaluating each entry (i.e. Session) in the Region to
determine if the entry has expired. You can control the number of
expiration threads with the <code>gemfire.EXPIRY_THREADS</code>
property. For more information, see
[Expiration](https://docs.vmware.com/en/VMware-Tanzu-GemFire/9.15/tgf/GUID-developing-expiration-chapter_overview.html) in the VMware GemFire product documentation.

### <a id="expiration-timeout-configuration"></a>Expiration Timeout Configuration

To base the expiration timeout for your custom
`SessionExpirationPolicy` on the `@EnableGemFireHttpSession` annotation,
`maxInactiveIntervalInSeconds` attribute, or alternatively, the
corresponding
`spring.session.data.gemfire.session.expiration.max-inactive-interval-seconds`
property, then your custom `SessionExpirationPolicy` implementation may
also implement the `SessionExpirationTimeoutAware` interface.


The `SessionExpirationTimeoutAware` interface is defined as follows:

```highlight
interface SessionExpirationTimeoutAware {

    void setExpirationTimeout(Duration expirationTimeout);

}
```

When your custom `SessionExpirationPolicy` implementation also
implements the `SessionExpirationTimeoutAware` interface, then Spring
Session for VMware GemFire will supply your implementation
with the value from the `@EnableGemFireHttpSession` annotation,
`maxInactiveIntervalInSeconds` attribute, or from the
`spring.session.data.gemfire.session.expiration.max-inactive-interval-seconds`
property if set, or from any `SpringSessionGemFireConfigurer` bean
declared in the Spring application context, as an instance of
`java.time.Duration`.

If more than one configuration option is used, the following order takes
precedence:

1. `SpringSessionGemFireConfigurer.getMaxInactiveIntervalInSeconds()`

2. `spring.session.data.gemfire.session.expiration.max-inactive-interval-seconds` property

3. `@EnableGemFireHttpSession` annotation, `maxInactiveIntervalInSeconds` attribute


### <a id="fixed-timeout-expiration"></a>Fixed Timeout Expiration

Spring Session for VMware GemFire
provides an implementation of the `SessionExpirationPolicy` interface
for fixed duration expiration, also known as **Absolute session timeouts**.

It is perhaps necessary, in certain cases, such as for security reasons,
to expire the user's Session after a fixed length of time (e.g. every
hour), regardless if the user's Session is still active.

Spring Session for VMware GemFire provides the
`FixedTimeoutSessionExpirationPolicy` implementation out-of-the-box for
this exact Use Case (UC). In addition to handling fixed duration
expiration, it is also careful to still consider and apply the default,
idle expiration timeout.

For instance, consider a scenario where a user logs in, beginning a
Session, is active for 10 minutes and then leaves letting the Session
sit idle. If the fixed duration expiration timeout is set for 60
minutes, but the idle expiration timeout is only set for 30 minutes, and
the user does not return, then the Session should expire in 40 minutes
and not 60 minutes when the fixed duration expiration would occur.

Conversely, if the user is busy for a full 40 minutes, thereby keeping
the Session active, thus avoiding the 30 minute idle expiration timeout,
and then leaves, then our fixed duration expiration timeout should kick
in and expire the user's Session right at 60 minutes, even though the
user's idle expiration timeout would not occur until 70 minutes in (40
min (active) + 30 min (idle) = 70 minutes).

Well, this is exactly what the `FixedTimeoutSessionExpirationPolicy`
does.


To configure the `FixedTimeoutSessionExpirationPolicy`:

**Fixed Duration Expiration Configuration**

```highlight
@SpringBootApplication
@EnableGemFireHttpSession(sessionExpirationPolicyBeanName = "fixedTimeoutExpirationPolicy")
class MySpringSessionApplication {

    @Bean
    SessionExpirationPolicy fixedTimeoutExpirationPolicy() {
        return new FixedTimeoutSessionExpirationPolicy(Duration.ofMinutes(60L));
    }
}
```

In the example above, the `FixedTimeoutSessionExpirationPolicy` was
declared as a bean in the Spring application context and initialized
with a fixed duration expiration timeout of 60 minutes. As a result, the
users Session will either expire after the idle timeout (which defaults
to 30 minutes) or after the fixed timeout (configured to 60 minutes),
which ever occurs first.

You can also implement lazy, fixed
duration expiration timeout on Session access by using the Spring
Session for VMware GemFire
<code>FixedDurationExpirationSessionRepositoryBeanPostProcessor</code>.
This BPP wraps any data store specific <code>SessionRepository</code> in
a <code>FixedDurationExpirationSessionRepository</code> implementation
that evaluates a Sessions expiration on access, only. This approach is
agnostic to the underlying data store and therefore can be used with any
Spring Session provider. The expiration determination is based solely on
the Session <code>creationTime</code> property and the required
<code>java.time.Duration</code> specifying the fixed duration expiration
timeout.

<p class="note"><strong>Note</strong>: The
<code>FixedDurationExpirationSessionRepository</code> should not be used
in strict expiration timeout cases, such as when the Session must expire
immediately after the fixed duration expiration timeout has elapsed.
Additionally, unlike the
<code>FixedTimeoutSessionExpirationPolicy</code>, the
<code>FixedDurationExpirationSessionRepository</code> does not take idle
expiration timeout into consideration. That is, it only uses the fixed
duration when determining the expiration timeout for a given
Session.</p>

### <a id="sessionexpirationpolicy-chaining"></a>`SessionExpirationPolicy` Chaining

Using the [Composite software design
pattern](https://en.wikipedia.org/wiki/Composite_pattern), you can treat
a group of `SessionExpirationPolicy` instances as a single instance,
functioning as if in a chain much like the chain of Servlet Filters
themselves.

The *Composite software design pattern* is a powerful pattern and is
supported by the `SessionExpirationPolicy`, `@FunctionalInterface`,
simply by returning an `Optional` of `java.time.Duration` from the
`determineExpirationTimeout` method.

This allows each composed `SessionExpirationPolicy` to "optionally"
return a `Duration` only if the expiration could be determined by this
instance. Alternatively, this instance may punt to the next
`SessionExpirationPolicy` in the composition, or chain until either a
non-empty expiration timeout is returned, or ultimately no expiration
timeout is returned.

In fact, this very policy is used internally by the
`FixedTimeoutSessionExpirationPolicy`, which will return
`Optional.empty()` in the case where the idle timeout will occur before
the fixed timeout. By returning no expiration timeout,
VMware GemFire will defer to the default, configured entry
idle timeout expiration policy on the Region managing Session state.

## <a id="gemfire-serialization"></a>VMware GemFire Serialization

To transfer data between clients and servers, or when data is
distributed and replicated between peer nodes in a cluster, the data
must be serialized. In this case, the data in question is the Session's
state.

Anytime a Session is persisted or accessed in a Client-Server topology,
the Session's state is communicated. Typically, a Spring Boot
application with Spring Session enabled will be a client to the
servers in a cluster of VMware GemFire nodes.

On the server-side, Session state maybe distributed across several
servers (data nodes) in the cluster to replicate the data and guarantee
a high availability of the Session state. When using
VMware GemFire, data can be partitioned, or sharded, and a
redundancy-level can be specified. When the data is distributed for
replication, it must also be serialized to transfer the Session state
among the peer nodes in the cluster.

VMware GemFire supports *Java Serialization*.
There are many advantages to *Java Serialization*, such as handling
cycles in the object graph, or being universally supported by any
application written in Java. However, *Java Serialization* is very
verbose and is not the most efficient format.

As such, VMware GemFire provides its own serialization
frameworks to serialize Java types:

* [Data Serialization](https://docs.vmware.com/en/VMware-Tanzu-GemFire/9.15/tgf/GUID-developing-data_serialization-gemfire_data_serialization.html)

* [PDX Serialization](https://docs.vmware.com/en/VMware-Tanzu-GemFire/9.15/tgf/GUID-developing-data_serialization-gemfire_pdx_serialization.html)

### <a id="data-serialization-background"></a>Data Serialization Background

Data Serialization is a very efficient format, being both fast and
compact, with little overhead when compared to Java Serialization.

Data Serialization [Delta Propagation](https://docs.vmware.com/en/VMware-Tanzu-GemFire/9.15/tgf/GUID-developing-delta_propagation-chapter_overview.html)
by sending only the bits of data that actually changed as opposed to
sending the entire object. This reduces the amount of
data sent over the network in addition to reducing the amount of IO when
data is persisted or overflowed to disk.

However, Data Serialization incurs a CPU penalty anytime data is
transferred, or persisted or overflowed to and accessed from
disk, since the receiving end performs a deserialization. Anytime Delta Propagation is used,
the object must be deserialized on the receiving end to apply the "delta".
VMware GemFire applies deltas by invoking a method on the
object that implements the `org.apache.geode.Delta` interface. You cannot invoke a method on a serialized object.

### <a id="pdx-serialization-background"></a>PDX Serialization Background

PDX (Portable Data Exchange) retains the form in which the data was sent. For example, if a client
sends data to a server in PDX format, the server will retain the data as
PDX serialized bytes and store them in the cache `Region` for which the
data access operation was targeted.

Additionally, PDX is "portable", meaning it
enables both Java and Native Language Clients, such as C, C++ and C#
clients, to interoperate on the same data set.

PDX even allows OQL queries to be performed on the serialized bytes
without causing the objects to be deserialized first in order to
evaluate the query predicate and execute the query. This can be
accomplished since VMware GemFire maintains a "*Type
Registry*" containing type meta-data for the objects that get serialized
and stored in VMware GemFire using PDX.

However, portability does come with a cost, having slightly more
overhead than Data Serialization. Still, PDX is far more efficient and
flexible than Java Serialization, which stores type meta-data in the
serialized bytes of the object rather than in a separate Type Registry
as in VMware GemFire's case when using PDX.

PDX does not support Deltas. Technically, a PDX serializable object can
be used in *Delta Propagation* by implementing the
[`org.apache.geode.Delta`](https://geode.apache.org/releases/latest/javadoc/org/apache/geode/Delta.html)
interface, and only the "delta" will be sent, even in the context of
PDX. But then, the PDX serialized object must be deserialized to apply
the delta. Remember, a method is invoked to apply the delta, which
defeats the purpose of using PDX in the first place.

When developing Native Clients (e.g. C) that manage data in a
{data-store-name} cluster, or even when mixing Native Clients with Java
clients, typically there will not be any associated Java types provided
on the classpath of the servers in the cluster. With PDX, it is not
necessary to provide the Java types on the classpath, and many users who
only develop and use Native Clients to access data stored in
{data-store-name} will not provide any Java types for their
corresponding C/C/C# types.

VMware GemFire also support JSON serialized to/from PDX. In
this case, it is very likely that Java types will not be provided on the
servers classpath since many different languages (e.g. JavaScript,
Python, Ruby) supporting JSON can be used with VMware GemFire.

Still, even with PDX in play, users must take care not to cause a PDX
serialized object on the servers in the cluster to be deserialized.

For example, consider an OQL query on an object of the following Java
type serialized as PDX…​

```highlight
@Region("People")
class Person {

  private LocalDate birthDate;
  private String name;

  public int getAge() {
    // no explicit 'age' field/property in Person
    // age is just implemented in terms of the 'birthDate' field
  }
}
```

Subsequently, if the OQL query invokes a method on a `Person` object,
such as:

`SELECT * FROM /People p WHERE p.age >= 21`

Then, this is going to cause a PDX serialized `Person` object to be
deserialized since `age` is not a field of `Person`, but rather a method
containing a computation based on another field of `Person` (i.e.
`birthDate`). Likewise, calling any `java.lang.Object` method in a OQL
query, like `Object.toString()`, is going to cause a deserialization to
happen as well.

VMware GemFire does provide the
[`read-serialized`](https://geode.apache.org/releases/latest/javadoc/org/apache/geode/cache/client/ClientCacheFactory.html#setPdxReadSerialized-boolean-)
configuration setting so that any cache `Region.get(key)` operations
that are potentially invoked inside a `Function` does not cause PDX
serialized objects to be deserialized. But, nothing will prevent an
ill-conceived OQL query from causing a deserialization, so be careful.

**Data Serialization and PDX and Java Serialization**

It is possible for VMware GemFire to support all three
serialization formats simultaneously.

For instance, your application domain model might contain objects that
implement the `java.io.Serialiable` interface, and you may be using a
combination of the *Data Serialization* framework along with PDX.

While using <em>Java Serialization</em> with
<em>Data Serialization</em> and PDX is possible, it is generally
preferable and recommended to use one serialization strategy.

Unlike <em>Java Serialization</em>, <em>Data
Serialization</em> and PDX <em>Serialization</em> do not handle object
graph cycles.

For more background about VMware GemFire's serialization mechanics, see
[Overview of Data Serialization](https://docs.vmware.com/en/VMware-Tanzu-GemFire/9.15/tgf/GUID-developing-data_serialization-data_serialization_options.html)
in the VMware GemFire product documentation.

### <a id="spring-session-serialization"></a>Serialization with Spring Session

Previously, Spring Session for VMware GemFire only supported
VMware GemFire *Data Serialization* format. The main
motivation behind this was to take advantage of
VMware GemFire's *Delta Propagation* functionality since a
Session's state can be arbitrarily large.

However, as of Spring Session for VMware GemFire 2.0, PDX is
also supported and is now the new, default serialization option. The
default was changed to PDX in Spring Session for
VMware GemFire 2.0 primarily because PDX is the most widely
used and requested format by users.

PDX is certainly the most flexible format, so much so that you do not
even need Spring Session for VMware GemFire or any of its
transitive dependencies on the classpath of the servers in the cluster
to use Spring Session with VMware GemFire. In fact, with PDX,
you do not even need to put your application domain object types stored
in the (HTTP) Session on the servers' classpath either.

Essentially, when using PDX serialization, VMware GemFire does
not require the associated Java types to be present on the servers'
classpath. So long as no deserialization happens on the servers in the
cluster, you are safe.

The `@EnableGemFireHttpSession` annotation introduces the **new**
`sessionSerializerBeanName` attribute that a user can use to configure
the name of a bean declared and registered in the Spring container
implementing the desired serialization strategy. The serialization
strategy is used by Spring Session for VMware GemFire to
serialize the Session state.

Out-of-the-box, Spring Session for VMware GemFire provides two
serialization strategies: one for PDX and one for *Data Serialization*. It
automatically registers both serialization strategy beans in the Spring
container. However, only one of those strategies is actually used at
runtime, PDX!


The two beans registered in the Spring container implementing *Data
Serialization* and PDX are named `SessionDataSerializer` and
`SessionPdxSerializer`, respectively. By default, the
`sessionSerializerBeanName` attribute is set to `SessionPdxSerializer`,
as if the user annotated his/her Spring Boot, Spring Session enabled
application configuration class with:

```highlight
@SpringBootApplication
@EnableGemFireHttpSession(sessionSerializerBeanName = "SessionPdxSerializer")
class MySpringSessionApplication {  }
```
It is a simple matter to change the serialization strategy to *Data
Serialization* instead by setting the `sessionSerializerBeanName`
attribute to `SessionDataSerializer`, as follows:

```highlight
@SpringBootApplication
@EnableGemFireHttpSession(sessionSerializerBeanName = "SessionDataSerializer")
class MySpringSessionApplication {  }
```

Since these two values are so common, Spring Session for
VMware GemFire provides constants for each value in the
`GemFireHttpSessionConfiguration` class:
`GemFireHttpSessionConfiguration.SESSION_PDX_SERIALIZER_BEAN_NAME` and
`GemFireHttpSessionConfiguration.SESSION_DATA_SERIALIZER_BEAN_NAME`. So,
you could explicitly configure PDX, as follows:

```highlight
import org.springframework.session.data.geode.config.annotation.web.http.GemFireHttpSessionConfiguration;

@SpringBootApplication
@EnableGemFireHttpSession(sessionSerializerBeanName = GemFireHttpSessionConfiguration.SESSION_PDX_SERIALIZER_BEAN_NAME)
class MySpringSessionApplication {  }
```

With one attribute and two provided bean definitions out-of-the-box, you can
specify which Serialization framework you wish to use with your Spring
Boot, Spring Session enabled application backed by
VMware GemFire.

### <a id="spring-session-serialization-framework"></a>Spring Session for VMware GemFire Serialization Framework

To abstract away the details of VMware GemFire's *Data
Serialization* and PDX *Serialization* frameworks, Spring Session for
VMware GemFire provides its own Serialization framework
(facade) wrapping VMware GemFire's Serialization frameworks.


The Serialization API exists under the
`org.springframework.session.data.gemfire.serialization` package. The
primary interface in this API is the
`org.springframework.session.data.gemfire.serialization.SessionSerializer`.


The Spring Session `SessionSerializer` interface is defined as follows:

```highlight
interface SessionSerializer<T, IN, OUT> {

  void serialize(T session, OUT out);

  T deserialize(IN in);

  boolean canSerialize(Class<?> type);

  boolean canSerialize(Object obj) {
    // calls Object.getClass() in a null-safe way and then calls and returns canSerialize(:Class)
  }
}
```

The interface allows you to serialize and deserialize a
Spring `Session` object.

The `IN` and `OUT` type parameters and corresponding method arguments of
those types provide reference to the objects responsible for writing the
`Session` to a stream of bytes or reading the `Session` from a stream of
bytes. The actual arguments will be type specific, based on the
underlying VMware GemFire Serialization strategy configured.

For instance, when using VMware GemFire's PDX *Serialization*
framework, `IN` and `OUT` will be instances of
`org.apache.geode.pdx.PdxReader` and `org.apache.geode.pdx.PdxWriter`,
respectively. When VMware GemFire's *Data Serialization*
framework has been configured, then `IN` and `OUT` will be instances of
`java.io.DataInput` and `java.io.DataOutput`, respectively.

These arguments are provided to the `SessionSerializer` implementation
by the framework automatically, and as previously mentioned, is based on
the underlying VMware GemFire Serialization strategy
configured.

Essentially, even though Spring Session for VMware GemFire
provides a facade around VMware GemFire's Serialization
frameworks, under-the-hood VMware GemFire still expects one of
these Serialization frameworks is being used to serialize data to/from
VMware GemFire.

*So what purpose does the `SessionSerializer` interface really serve
then?*

Effectively, it allows a user to customize what aspects of the Session's
state actually gets serialized and stored in VMware GemFire.
Application developers can provide their own custom,
application-specific `SessionSerializer` implementation, register it as
a bean in the Spring container, and then configure it to be used by
Spring Session for VMware GemFire to serialize the Session
state, as follows:


```highlight
@EnableGemFireHttpSession(sessionSerializerBeanName = "MyCustomSessionSerializer")
class MySpringSessionDataGemFireApplication {

  @Bean("MyCustomSessionSerializer")
  SessionSerializer<Session, ?, ?> myCustomSessionSerializer() {
    // ...
  }
}
```

#### <a id="implementing-sessionserializer"></a>Implementing a SessionSerializer

Spring Session for VMware GemFire provides assistance when a
user wants to implement a custom `SessionSerializer` that fits into one
of VMware GemFire's Serialization frameworks.

If the user just implements the
`org.springframework.session.data.gemfire.serialization.SessionSerializer`
interface directly without extending from one of Spring Session for
VMware GemFire's provided abstract base classes, related to one
of VMware GemFire's Serialization frameworks , then Spring
Session for VMware GemFire will wrap the user's custom
`SessionSerializer` implementation in an instance of
`org.springframework.session.data.gemfire.serialization.pdx.support.PdxSerializerSessionSerializerAdapter`
and register it with VMware GemFire as a
`org.apache.geode.pdx.PdxSerializer`.

Spring Session for VMware GemFire is careful not to stomp on
any existing `PdxSerializer` implementation that a user may have already
registered with VMware GemFire by some other means. Indeed,
several different, provided implementations of
VMware GemFire's `org.apache.geode.pdx.PdxSerializer`
interface exists:

- VMware GemFire itself provides the [`org.apache.geode.pdx.ReflectionBasedAutoSerializer`](https://geode.apache.org/releases/latest/javadoc/org/apache/geode/pdx/ReflectionBasedAutoSerializer.html).

- Spring Data for VMware GemFire (SDG) provides the [`org.springframework.data.gemfire.mapping.MappingPdxSerializer`](https://docs.spring.io/spring-data/geode/docs/current/api/org/springframework/data/gemfire/mapping/MappingPdxSerializer.html), which is used in the SD *Repository* abstraction and SDG's extension to handle mapping PDX serialized types to the application domain object types defined in the application *Repository* interfaces.

This is accomplished by obtaining any currently registered
`PdxSerializer` instance on the cache and composing it with the
`PdxSerializerSessionSerializerAdapter` wrapping the user's custom
application `SessionSerializer` implementation and re-registering this
"*composite*" `PdxSerializer` on the VMware GemFire cache. The
"*composite*" `PdxSerializer` implementation is provided by Spring
Session for VMware GemFire's
`org.springframework.session.data.gemfire.pdx.support.ComposablePdxSerializer`
class when entities are stored in VMware GemFire as PDX.

If no other `PdxSerializer` was currently registered with the
VMware GemFire cache, then the adapter is simply registered.

Of course, you are allowed to force the underlying
VMware GemFire Serialization strategy used with a custom
`SessionSerializer` implementation by doing one of the following:

1. The custom `SessionSerializer` implementation can implement VMware GemFire's `org.apache.geode.pdx.PdxSerializer` interface, or for convenience, extend Spring Session for VMware GemFire's `org.springframework.session.data.gemfire.serialization.pdx.AbstractPdxSerializableSessionSerializer` class and Spring Session for VMware GemFire will register the custom `SessionSerializer` as a `PdxSerializer` with VMware GemFire.

2. The custom `SessionSerializer` implementation can extend the VMware GemFire's `org.apache.geode.DataSerializer` class, or for convenience, extend Spring Session for VMware GemFire's `org.springframework.session.data.gemfire.serialization.data.AbstractDataSerializableSessionSerializer` class and Spring Session for VMware GemFire will register the custom `SessionSerializer` as a `DataSerializer` with VMware GemFire.

3. A user can create a custom `SessionSerializer` implementation as before, not specifying which VMware GemFire Serialization framework to use because the custom `SessionSeriaizer` implementation does not implement any VMware GemFire serialization interfaces or extend from any of Spring Session for VMware GemFire's provided abstract base classes, and still have it registered in VMware GemFire as a `DataSerializer` by declaring an additional Spring Session for VMware GemFire bean in the Spring container of type `org.springframework.session.data.gemfire.serialization.data.support.DataSerializerSessionSerializerAdapter`, like so…​

Forcing the registration of a custom SessionSerializer as a
DataSerializer in VMware GemFire

```highlight
@EnableGemFireHttpSession(sessionSerializerBeanName = "customSessionSerializer")
class Application {

    @Bean
    DataSerializerSessionSerializerAdapter dataSerializerSessionSerializer() {
        return new DataSerializerSessionSerializerAdapter();
    }

    @Bean
    SessionSerializer<Session, ?, ?> customSessionSerializer() {
        // ...
    }
}
```

Just by the very presence of the
`DataSerializerSessionSerializerAdapter` registered as a bean in the
Spring container, any neutral custom `SessionSerializer` implementation
will be treated and registered as a `DataSerializer` in
VMware GemFire.

#### <a id="additional-support"></a>Additional Support for Data Serialization

When using VMware GemFire's *DataSerialization* framework,
especially from the client when serializing (HTTP) Session state to the
servers in the cluster, you must take care to configure the
VMware GemFire servers in your cluster with the appropriate
dependencies. This is especially true when leveraging deltas as
explained in the earlier section on [Data
Serialization](#data-serialization-background).

When using the *DataSerialization* framework as your serialization
strategy to serialize (HTTP) Session state from your Web application
clients to the servers, then the servers must be properly configured
with the Spring Session for VMware GemFire class types used to
represent the (HTTP) Session and its contents. This means including the
Spring JARs on the servers classpath.

Additionally, using *DataSerialization* may also require you to include
the JARs containing your application domain classes that are used by
your Web application and put into the (HTTP) Session as Session
Attribute values, particularly if:

* Your types implement the `org.apache.geode.DataSerializable` interface.

* Your types implement the `org.apache.geode.Delta` interface.

* You have registered a `org.apache.geode.DataSerializer` that identifies and serializes the types.

* Your types implement the `java.io.Serializable` interface.

You must ensure your application domain object types put in
the (HTTP) Session are serializable in some form or another. However,
you are not strictly required to use *DataSerialization* nor are you
necessarily required to have your application domain object types on the
servers classpath if:

* Your types implement the `org.apache.geode.pdx.PdxSerializable` interface.

* You have registered an `org.apache.geode.pdx.PdxSerializer` that properly identifies and serializes your application domain object types.

VMware GemFire will apply the following order of precedence
when determining the serialization strategy to use to serialize an
object graph:

1. `DataSerializable` objects and any registered `DataSerializers` identifying the objects to serialize.

2. `PdxSerializable` objects and any registered `PdxSerializer` identifying the objects to serialize.

3.  All `java.io.Serializable` types.


This also means that if a particular application domain object type
(e.g. `A`) implements `java.io.Serializable`, however, a (custom)
`PdxSerializer` has been registered with VMware GemFire
identifying the same application domain object type (i.e. `A`), then
VMware GemFire will use PDX to serialize "A" and **not** Java
Serialization, in this case.

This is especially useful since then you can use *DataSerialization* to
serialize the (HTTP) Session object, leveraging Deltas and all the
powerful features of *DataSerialization*, but then use PDX to serialize
your application domain object types, which greatly simplifies the
configuration and/or effort involved.

Now that we have a general understanding of why this support exists, how
do you enable it?

##### <a id="additional-support-configuration"></a>Configuration

1. Create an VMware GemFire `cache.xml`, as follows:

    ```highlight
    <?xml version="1.0" encoding="UTF-8"?>
    <cache xmlns="http://geode.apache.org/schema/cache"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://geode.apache.org/schema/cache https://geode.apache.org/schema/cache/cache-1.0.xsd"
           version="1.0">

        <initializer>
            <class-name>
                org.springframework.session.data.gemfire.serialization.data.support.DataSerializableSessionSerializerInitializer
            </class-name>
        </initializer>

    </cache>
    ```

1. Start your servers in `gfsh` using:

    ```highlight
    gfsh> start server --name=InitializedServer --cache-xml-file=/path/to/cache.xml --classpath=...
    ```

Configuring the VMware GemFire server `classpath` with the
appropriate dependencies is the tricky part, but generally, the
following CLASSPATH configuration should work:

```highlight
gfsh> set variable --name=REPO_HOME --value=${USER_HOME}/.m2/repository

gfsh> start server ... --classpath=\
${REPO_HOME}/org/springframework/spring-core/{spring-version}/spring-core-{spring-version}.jar\
:${REPO_HOME}/org/springframework/spring-aop/{spring-version}/spring-aop-{spring-version}.jar\
:${REPO_HOME}/org/springframework/spring-beans/{spring-version}/spring-beans-{spring-version}.jar\
:${REPO_HOME}/org/springframework/spring-context/{spring-version}/spring-context-{spring-version}.jar\
:${REPO_HOME}/org/springframework/spring-context-support/{spring-version}/spring-context-support-{spring-version}.jar\
:${REPO_HOME}/org/springframework/spring-expression/{spring-version}/spring-expression-{spring-version}.jar\
:${REPO_HOME}/org/springframework/spring-jcl/{spring-version}/spring-jcl-{spring-version}.jar\
:${REPO_HOME}/org/springframework/spring-tx/{spring-version}/spring-tx-{spring-version}.jar\
:${REPO_HOME}/org/springframework/data/spring-data-commons/{spring-data-commons-version}/spring-data-commons-{spring-data-commons-version}.jar\
:${REPO_HOME}/org/springframework/data/spring-data-geode/{spring-data-geode-version}/spring-data-geode-{spring-data-geode-version}.jar\
:${REPO_HOME}/org/springframework/session/spring-session-core/{spring-session-core-version}/spring-session-core-{spring-session-core-version}.jar\
:${REPO_HOME}/org/springframework/session/spring-session-data-gemfire/ 2.7.1/spring-session-data-gemfire- 2.7.1.jar\
:${REPO_HOME}/org/slf4j/slf4j-api/1.7.25/slf4j-api-1.7.25.jar
```

You may need to add your application domain object JAR
files to the server classpath as well.

##### <a id="customizing-change-detection"></a>Customizing Change Detection

By default, anytime the Session is modified (e.g. the `lastAccessedTime`
is updated to the current time), the Session is considered dirty by
Spring Session for VMware GemFire (SSDG). When using
VMware GemFire *Data Serialization* framework, it is extremely
useful and valuable to take advantage of VMware GemFire's
[Delta Propagation](https://docs.vmware.com/en/VMware-Tanzu-GemFire/9.15/tgf/GUID-developing-delta_propagation-chapter_overview.html)
capabilities as well.

When using *Data Serialization*, SSDG also uses *Delta Propagation* to
send only changes to the Session state between the client and server.
This includes any Session attributes that may have been added, removed
or updated.

By default, anytime `Session.setAttribute(name, value)` is called, the
Session attribute is considered "dirty" and will be sent in the delta
between the client and server. This is true even if your application
domain object has not been changed.


Typically, there is never a reason to call `Session.setAttribute(..)`
unless your object has been changed. However, if this can occur, and
your objects are relatively large (with a complex object hierarchy),
then you may want to consider either:

* Implementing the [Delta](https://geode.apache.org/releases/latest/javadoc/org/apache/geode/Delta.html) interface in your application domain object model, while useful, is very invasive.

* Provide a custom implementation of SSDG's `org.springframework.session.data.gemfire.support.IsDirtyPredicate` strategy interface.

Out of the box, SSDG provides five implementations of the
`IsDirtyPredicate` strategy interface:

<table class="tableblock frame-all grid-all stretch">
  <col style="width: 28%" />
  <col style="width: 57%" />
  <col style="width: 14%" />
  <thead>
    <tr class="header">
      <th>Class</th>
      <th>Description</th>
      <th>Default</th>
    </tr>
  </thead>
  <tbody>
    <tr class="odd">
      <td><code>IsDirtyPredicate.ALWAYS_DIRTY</code></td>
      <td>New Session attribute values are always considered dirty.</td>
      <td></td>
    </tr>
    <tr class="even">
      <td><code>IsDirtyPredicate.NEVER_DIRTY</code></td>
      <td>New Session attribute values are never considered dirty.</td>
      <td></td>
    </tr>
    <tr class="odd">
      <td><code>DeltaAwareDirtyPredicate</code></td>
      <td>New Session attribute values are considered dirty when the old value and new value are different, if the new value's type does not implement <code>Delta</code> or the new value's <code>Delta.hasDelta()</code> method returns <strong>true</strong>.</td>
      <td>Yes</td>
    </tr>
    <tr class="even">
      <td><code>EqualsDirtyPredicate</code></td>
      <td>New Session attribute values are considered dirty iff the old value is not equal to the new value as determined by <code>Object.equals(:Object)</code> method.</td>
      <td></td>
    </tr>
    <tr class="odd">
      <td><code>IdentityEqualsPredicate</code></td>
      <td>New Session attributes values are considered dirty iff the old value is not the same as the new value using the identity equals operator (i.e. <code>oldValue != newValue</code>).</td>
      <td></td>
    </tr>
  </tbody>
</table>

As shown in the table above, the `DeltaAwareDirtyPredicate` is the
**default** implementation used by SSDG. The `DeltaAwareDirtyPredicate`
automatically takes into consideration application domain objects that
implement the VMware GemFire `Delta` interface. However,
`DeltaAwareDirtyPredicate` works even when your application domain
objects do not implement the `Delta` interface. SSDG will consider your
application domain object to be dirty anytime the
`Session.setAttribute(name, newValue)` is called providing the new value
is not the same as old value, or the new value does not implement the
`Delta` interface.

You can change SSDG's dirty implementation, determination strategy
simply by declaring a bean in the Spring container of the
`IsDirtyPredicate` interface type:

```highlight
@EnableGemFireHttpSession
class ApplicationConfiguration {

  @Bean
  IsDirtyPredicate equalsDirtyPredicate() {
    return EqualsDirtyPredicate.INSTANCE;
  }
}
```

##### <a id="composition"></a>Composition

The `IsDirtyPredicate` interface also provides the
`andThen(:IsDirtyPredicate)` and `orThen(:IsDirtyPredicate)` methods to
compose 2 or more `IsDirtyPredicate` implementations in a composition in
order to organize complex logic and rules for determining whether an
application domain object is dirty or not.

For instance, you could compose both `EqualsDirtyPredicate` and
`DeltaAwareDirtyPredicate` using the OR operator:

```highlight
@EnableGemFireHttpSession
class ApplicationConfiguration {

  @Bean
  IsDirtyPredicate equalsOrThenDeltaDirtyPredicate() {

    return EqualsDirtyPredicate.INSTANCE
      .orThen(DeltaAwareDirtyPredicate.INSTANCE);
  }
}
```

You may even implement your own, custom `IsDirtyPredicates` based on
specific application domain object types:

```highlight
class CustomerDirtyPredicate implements IsDirtyPredicate {

  public boolean isDirty(Object oldCustomer, Object newCustomer) {

      if (newCustomer instanceof Customer) {
        // custom logic to determine if a new Customer is dirty
      }

      return true;
  }
}

class AccountDirtyPredicate implements IsDirtyPredicate {

  public boolean isDirty(Object oldAccount, Object newAccount) {

      if (newAccount instanceof Account) {
        // custom logic to determine if a new Account is dirty
      }

      return true;
  }
}
```

Then combine `CustomerDirtyPredicate` with the `AccountDirtyPredicate`
and a default predicate for fallback, as follows:

**Composed and configured type-specific `IsDirtyPredicates`**

```highlight
@EnableGemFireHttpSession
class ApplicationConfiguration {

  @Bean
  IsDirtyPredicate typeSpecificDirtyPredicate() {

    return new CustomerDirtyPredicate()
      .andThen(new AccountDirtyPredicate())
      .andThen(IsDirtyPredicate.ALWAYS_DIRTY);
  }
}
```

<p class="note warning"><strong>Warning</strong>: Use caution when implementing custom
<code>IsDirtyPredicate</code> strategies. If you incorrectly determine
that your application domain object is not dirty when it actually is,
then it will not be sent in the Session delta from the client to the
server.</p>

##### <a id="changing-session-representation"></a>Changing the Session Representation

Internally, Spring Session for VMware GemFire maintains two
representations of the (HTTP) Session and the Session's attributes. Each
representation is based on whether VMware GemFire "*Deltas*"
are supported or not.

VMware GemFire *Delta Propagation* is only enabled by Spring
Session for VMware GemFire when using *Data Serialization* for
reasons that were discussed in [PDX Serialization Background](#pdx-serialization-background).

The strategy is:

1. If VMware GemFire *Data Serialization* is configured, then *Deltas* are supported and the `DeltaCapableGemFireSession` and `DeltaCapableGemFireSessionAttributes` representations are used.

2.  If VMware GemFire PDX *Serialization* is configured, then *Delta Propagation* will be disabled and the `GemFireSession` and `GemFireSessionAttributes` representations are used.

It is possible to override these internal representations used by Spring
Session for VMware GemFire, and for users to provide their own
Session related types. The only strict requirement is that the Session
implementation must implement the core Spring Session
`org.springframework.session.Session` interface.

By way of example, let's say you want to define your own Session
implementation.

First, you define the `Session` type. Perhaps your custom `Session` type
even encapsulates and handles the Session attributes without having to
define a separate type.

**User-defined Session interface implementation**

```highlight
class MySession implements org.springframework.session.Session {
  // ...
}
```

Then, you would need to extend the
`org.springframework.session.data.gemfire.GemFireOperationsSessionRepository`
class and override the `createSession()` method to create instances of
your custom `Session` implementation class.

**Custom SessionRepository implementation creating and returning instances
of the custom Session type**

```highlight
class MySessionRepository extends GemFireOperationsSessionRepository {

  @Override
  public Session createSession() {
    return new MySession();
  }
}
```

If you provide your own custom `SessionSerializer` implementation and
VMware GemFire PDX *Serialization* is configured, then you are finished.

However, if you configured VMware GemFire *Data Serialization*,
then you must additionally provide a custom implementation of the
`SessionSerializer` interface and either have it directly extend
VMware GemFire's `org.apache.geode.DataSerializer` class, or
extend Spring Session for VMware GemFire's
`org.springframework.session.data.gemfire.serialization.data.AbstractDataSerializableSessionSerializer`
class and override the `getSupportedClasses():Class<?>[]` method.

For example:

**Custom SessionSerializer for custom Session type**

```highlight
class MySessionSerializer extends AbstractDataSerializableSessionSerializer {

  @Override
  public Class<?>[] getSupportedClasses() {
    return new Class[] { MySession.class };
  }
}
```

Unfortunately, `getSupportedClasses()` cannot return the generic Spring
Session `org.springframework.session.Session` interface type. If it
could then we could avoid the explicit need to override the
`getSupportedClasses()` method on the custom `DataSerializer`
implementation. But, VMware GemFire's *Data Serialization*
framework can only match on exact class types since it incorrectly and
internally stores and refers to the class type by name, which then
requires the user to override and implement the `getSupportedClasses()`
method.

### How HttpSession Integration Works

Fortunately, both `javax.servlet.http.HttpSession` and
`javax.servlet.http.HttpServletRequest` (the API for obtaining an
`HttpSession`) are interfaces. This means we can provide our own
implementations for each of these APIs.

<p class="note"><strong>Note</strong>: This section describes how Spring Session provides
transparent integration with <code>javax.servlet.http.HttpSession</code>. The intent is to explain what is happening out of sight. This functionality is
already implemented and integrated so you do not need to implement this
logic yourself.</p>

First, we create a custom `javax.servlet.http.HttpServletRequest` that
returns a custom implementation of `javax.servlet.http.HttpSession`. It
looks something like the following:

```highlight
public class SessionRepositoryRequestWrapper extends HttpServletRequestWrapper {

    public SessionRepositoryRequestWrapper(HttpServletRequest original) {
        super(original);
    }

    public HttpSession getSession() {
        return getSession(true);
    }

    public HttpSession getSession(boolean createNew) {
        // create an HttpSession implementation from Spring Session
    }

    // ... other methods delegate to the original HttpServletRequest ...
}
```

Any method that returns an `javax.servlet.http.HttpSession` is
overridden. All other methods are implemented by
`javax.servlet.http.HttpServletRequestWrapper` and simply delegate to
the original `javax.servlet.http.HttpServletRequest` implementation.

We replace the `javax.servlet.http.HttpServletRequest` implementation
using a Servlet `Filter` called `SessionRepositoryFilter`. The
pseudocode can be found below:

```highlight
public class SessionRepositoryFilter implements Filter {

    public doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        SessionRepositoryRequestWrapper customRequest = new SessionRepositoryRequestWrapper(httpRequest);

        chain.doFilter(customRequest, response, chain);
    }

    // ...
}
```

By passing in a custom `javax.servlet.http.HttpServletRequest`
implementation into the `FilterChain` we ensure that anything invoked
after our `Filter` uses the custom `javax.servlet.http.HttpSession`
implementation.

This highlights why it is important that Spring Session's
`SessionRepositoryFilter` must be placed before anything that interacts
with the `javax.servlet.http.HttpSession`.

### HttpSessionListener

Spring Session supports `HttpSessionListener` by translating
`SessionCreatedEvent` and `SessionDestroyedEvent` into
`HttpSessionEvent` by declaring
`SessionEventHttpSessionListenerAdapter`.

To use this support, you must:

- Ensure your `SessionRepository` implementation supports and is configured to fire `SessionCreatedEvent` and\`SessionDestroyedEvent\`.

- Configure `SessionEventHttpSessionListenerAdapter` as a Spring bean.

- Inject every `HttpSessionListener` into the `SessionEventHttpSessionListenerAdapter`


If you are using the configuration support documented in [HttpSession
with VMware GemFire](#httpsession-gemfire), then all you need
to do is register every `HttpSessionListener` as a bean.

For example, assume you want to support Spring Security's concurrency
control and need to use `HttpSessionEventPublisher`, then you can simply
add `HttpSessionEventPublisher` as a bean.

### Session

A `Session` is a simplified `Map` of key/value pairs with support for
expiration.


### SessionRepository

A `SessionRepository` is in charge of creating, persisting and accessing
`Session` instances and state.


If possible, developers should not interact directly with a
`SessionRepository` or a `Session`. Instead, developers should prefer to
interact with `SessionRepository` and `Session` indirectly through the
`javax.servlet.http.HttpSession`, `WebSocket` and `WebSession`
integration.


### FindByIndexNameSessionRepository

Spring Session's most basic API for using a `Session` is the
`SessionRepository`. The API is intentionally very simple so that it is
easy to provide additional implementations with basic functionality.


Some `SessionRepository` implementations may choose to implement
`FindByIndexNameSessionRepository` also. For example, Spring Session's
for VMware GemFire support implements
`FindByIndexNameSessionRepository`.


The `FindByIndexNameSessionRepository` adds a single method to look up
all the sessions for a particular user. This is done by ensuring that
the session attribute with the name
`FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME` is
populated with the username. It is the responsibility of the developer
to ensure the attribute is populated since Spring Session is not aware
of the authentication mechanism being used.

<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Note
</div></td>
<td class="content">
Some implementations of <code>FindByIndexNameSessionRepository</code>
will provide hooks to automatically index other session attributes. For
example, many implementations will automatically ensure the current
Spring Security user name is indexed with the index name
<code>FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME</code>.
</div></td>
</tr>
</tbody>
</table>


### EnableSpringHttpSession

The `@EnableSpringHttpSession` annotation can be added to any
`@Configuration` class to expose the `SessionRepositoryFilter` as a bean
in the Spring container named, "springSessionRepositoryFilter".


In order to leverage the annotation, a single `SessionRepository` bean
must be provided.


### EnableGemFireHttpSession

The `@EnableGemFireHttpSession` annotation can be added to any
`@Configuration` class in place of the `@EnableSpringHttpSession`
annotation to expose the `SessionRepositoryFilter` as a bean in the
Spring container named, "springSessionRepositoryFilter" and to position
VMware GemFire as a provider managing
`javax.servlet.http.HttpSession` state.


When using the `@EnableGemFireHttpSession` annotation, additional
configuration is imported out-of-the-box that also provides a
VMware GemFire specific implementation of the
`SessionRepository` interface named,
`GemFireOperationsSessionRepository`.


### GemFireOperationsSessionRepository

`GemFireOperationsSessionRepository` is a `SessionRepository`
implementation that is implemented using Spring Session for
VMware GemFire's `GemFireOperationsSessionRepository`.


In a web environment, this repository is used in conjunction with the
`SessionRepositoryFilter`.


This implementation supports `SessionCreatedEvents`,
`SessionDeletedEvents` and `SessionDestroyedEvents` through
`SessionEventHttpSessionListenerAdapter`.

#### Using Indexes with VMware GemFire

While best practices concerning the proper definition of Indexes that
positively impact VMware GemFire's performance is beyond the
scope of this document, it is important to realize that Spring Session
for VMware GemFire creates and uses Indexes to query and find
Sessions efficiently.


Out-of-the-box, Spring Session for VMware GemFire creates one
Hash-typed Index on the principal name. There are two different built-in
strategies for finding the principal name. The first strategy is that
the value of the Session attribute with the name
`FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME` will be
Indexed to the same index name.


For example:

```highlight
String indexName = FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME;

session.setAttribute(indexName, username);
Map<String, Session> idToSessions =
    this.sessionRepository.findByIndexNameAndIndexValue(indexName, username);
```
#### Using Indexes with VMware GemFire and Spring Security

Alternatively, Spring Session for VMware GemFire will map
Spring Security's current `Authentication#getName()` to the Index
`FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME`.

For example, if you are using Spring Security you can find the current
user's sessions using:

```highlight
SecurityContext securityContext = SecurityContextHolder.getContext();
Authentication authentication = securityContext.getAuthentication();
String indexName = FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME;

Map<String, Session> idToSessions =
    this.sessionRepository.findByIndexNameAndIndexValue(indexName, authentication.getName());
```


#### Using Custom Indexes with VMware GemFire

This enables developers using the `GemFireOperationsSessionRepository`
programmatically to query and find all Sessions with a given principal
name efficiently.


Additionally, Spring Session for VMware GemFire will create a
Range-based Index on the implementing Session's Map-type `attributes`
property (i.e. on any arbitrary Session attribute) when a developer
identifies 1 or more named Session attributes that should be indexed by
VMware GemFire.


Sessions attributes to index can be specified with the
`indexableSessionAttributes` attribute on the
`@EnableGemFireHttpSession` annotation. A developer adds this annotation
to their Spring application `@Configuration` class when s/he wishes to
enable Spring Session's support for `HttpSession` backed by
VMware GemFire.


```highlight
String indexName = "name1";

session.setAttribute(indexName, indexValue);
Map<String, Session> idToSessions =
    this.sessionRepository.findByIndexNameAndIndexValue(indexName, indexValue);
```


Only Session attribute names identified in the
<code>@EnableGemFireHttpSession</code> annotation's
<code>indexableSessionAttributes</code> attribute will have an Index
defined. All other Session attributes will not be indexed.

However, there is one catch. Any values stored in an indexable Session
attributes must implement the `java.lang.Comparable<T>` interface. If
those object values do not implement `Comparable`, then
VMware GemFire will throw an error on startup when the Index
is defined for Regions with persistent Session data, or when an attempt
is made at runtime to assign the indexable Session attribute a value
that is not `Comparable` and the Session is saved to
VMware GemFire.

Any Session attribute that is not indexed may store
non-<code>Comparable</code> values.

To learn more about VMware GemFire's Range-based Indexes, see
[Creating Indexes on Map Fields](https://docs.vmware.com/en/VMware-Tanzu-GemFire/9.15/tgf/GUID-developing-query_index-creating_map_indexes.html)
in the VMware GemFire product documentation.

To learn more about VMware GemFire Indexing in general, see [Working with Indexes](https://docs.vmware.com/en/VMware-Tanzu-GemFire/9.15/tgf/GUID-developing-query_index-query_index.html)
in the VMware GemFire product documentation.

