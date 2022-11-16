---
title: HttpSession using VMware GemFire with Spring Boot and Scoped Proxy Beans
---

This guide describes how to build a Spring Boot Web application
configured with Spring Session to transparently manage a Web
application's `javax.servlet.http.HttpSession` using VMware GemFire in a
clustered (distributed) and replicated.

In addition, this samples explores the effects of using Spring Session
and VMware GemFire to manage the `HttpSession` when the Spring Boot Web
application also declares both "*session*" and "*request*" scoped bean
definitions to process client HTTP requests.

This sample is based on a [StackOverflow post](https://stackoverflow.com/questions/45674137/can-session-scope-beans-be-used-with-spring-session-and-gemfire), which posed the following question…​

> *Can session scope beans be used with Spring Session and VMware Tanzu
> GemFire?*

The poster of the question when on to state and ask...

> When using Spring Session for "session" scope beans, Spring creates an
> extra `HttpSession` for this bean. Is this an existing issue? What is
> the solution for this?

The answer to the first question is most definitely, **yes**. And, the
second statement/question is not correct, nor even valid, as explained
in the answer.

This sample uses VMware GemFire's Client-Server topology with a pair of
Spring Boot applications, one to configure and run an VMware GemFire
server, and another to configure and run an VMware GemFire client, which
is also a Spring Web MVC application making use of an `HttpSession`.

<p class="note">Note</strong>: The completed guide can be found below, in section
<a href="#spring-session-sample-boot-geode-with-scoped-proxies">Sample Spring Boot Web Application using VMware GemFire-managed HttpSessions with Request and Session Scoped Proxy Beans</a>.</p>

## <a id="updating-dependencies"></a>Updating Dependencies

Before using Spring Session, you must ensure that the required
dependencies are included. If you are using Maven, include the
following `dependencies` in your `pom.xml`:

```highlight
<dependencies>
    <!-- ... -->

    <dependency>
        <groupId>org.springframework.session</groupId>
        <artifactId>spring-session-data-gemfire</artifactId>
        <version> 2.7.1</version>
        <type>pom</type>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

## <a id="spring-boot-configuration"></a>Spring Boot Configuration

After adding the required dependencies and repository declarations, we
can create the Spring configuration for both our VMware GemFire client and
server using Spring Boot. The Spring configuration is responsible for
creating a Servlet `Filter` that replaces the `HttpSession` with an
implementation backed by Spring Session and VMware GemFire.

### <a id="spring-boot-gemfire-cache-server"></a>Spring Boot, VMware GemFire Cache Server

We start with a Spring Boot application to configure and bootstrap the
VMware GemFire server:

```highlight
@SpringBootApplication <!--SEE COMMENT 1-->
@CacheServerApplication(name = "SpringSessionDataGeodeBootSampleWithScopedProxiesServer", logLevel = "error") <!--SEE COMMENT 2-->
@EnableGemFireHttpSession(maxInactiveIntervalInSeconds = 10) <!--SEE COMMENT 3-->
public class GemFireServer {

    public static void main(String[] args) {

        new SpringApplicationBuilder(GemFireServer.class)
            .web(WebApplicationType.NONE)
            .build()
            .run(args);
    }
}```

Comments: 

1.  We annotate the `GemFireServer` class with  `@SpringBootApplication` to declare that this is a Spring Boot application, allowing us to leverage all of Spring Boot's features.

2. We use the Spring Data for VMware GemFire configuration annotation `@CacheServerApplication` to simplify the creation of a peer cache instance containing a `CacheServer` for cache clients to connect.

3. (Optional) We use the `@EnableGemFireHttpSession` annotation to create the necessary server-side `Region` to store the `HttpSessions` state. By default, this is `ClusteredSpringSessions`. This step is optional because we could create the Session `Region` manually.

### <a id="spring-boot-gemfire-cache-client-web-app"></a>Spring Boot, VMware GemFire Cache Client Web Application

Now, we create a Spring Boot Web application exposing our Web service
with Spring Web MVC, running as an VMware GemFire cache client connected
to our Spring Boot, VMware GemFire server. The Web application will use
Spring Session backed by VMware GemFire to manage `HttpSession` state in a
clustered (distributed) and replicated manner.

```highlight
@SpringBootApplication <!--SEE COMMENT 1-->
@Controller <!--SEE COMMENT 2-->
public class Application {

    static final String INDEX_TEMPLATE_VIEW_NAME = "index";
    static final String PING_RESPONSE = "PONG";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @ClientCacheApplication(name = "SpringSessionDataGeodeBootSampleWithScopedProxiesClient", logLevel = "error",
        readTimeout = 15000, retryAttempts = 1, subscriptionEnabled = true) <!--SEE COMMENT 3-->
    @EnableGemFireHttpSession(poolName = "DEFAULT") <!--SEE COMMENT 4-->
    static class ClientCacheConfiguration { }

    @Configuration
    static class SpringWebMvcConfiguration { <!--SEE COMMENT 5-->

        @Bean
        public WebMvcConfigurer webMvcConfig() {

            return new WebMvcConfigurer() {

                @Override
                public void addViewControllers(ViewControllerRegistry registry) {
                    registry.addViewController("/").setViewName(INDEX_TEMPLATE_VIEW_NAME);
                }
            };
        }
    }

    @Autowired
    private RequestScopedProxyBean requestBean;

    @Autowired
    private SessionScopedProxyBean sessionBean;

    @ExceptionHandler
    @ResponseBody
    public String errorHandler(Throwable error) {
        StringWriter writer = new StringWriter();
        error.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/ping")
    @ResponseBody
    public String ping() {
        return PING_RESPONSE;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/counts")
    public String requestAndSessionInstanceCount(HttpServletRequest request, HttpSession session, Model model) { <!--SEE COMMENT 6-->

        model.addAttribute("sessionId", session.getId());
        model.addAttribute("requestCount", this.requestBean.getCount());
        model.addAttribute("sessionCount", this.sessionBean.getCount());

        return INDEX_TEMPLATE_VIEW_NAME;
    }
}
```

Comments:

1. Like the server, we declare our Web application to be a Spring Boot application by annotating our `Application` class with `@SpringBootApplication`.

2. `@Controller` is a Spring Web MVC annotation enabling our MVC handler mapping methods (i.e. methods annotated with `@RequestMapping`) to process HTTP requests, for example \<6\>.

3.  We also declare our Web application to be an VMware GemFire cache client by annotating our `Application` class with `@ClientCacheApplication`. Additionally, we adjust a few basic, "DEFAULT" `Pool` settings, for example `readTimeout`.

4. We declare that the Web application will use Spring Session backed by VMware GemFire to manage the `HttpSession` state by annotating the nested `ClientCacheConfiguration` class with `@EnableGemFireHttpSession`. This creates the necessary client-side `PROXY` Region (by default, `ClusteredSpringSessions`) corresponding to the same server Region by name. All session state will be sent from the client to the server through Region data access operations. The client-side Region uses the "DEFAULT" `Pool`.

5. We adjust the Spring Web MVC configuration to set the home page.

6. We declare the `/counts` HTTP request mapping handler method to track the number of instances created by the Spring container for both request-scoped proxy beans (`RequestScopedProxyBean`) and session-scoped proxy beans (`SessionScopedProxyBean`) when a request is processed by the handler method.

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

#### <a id="enabling-gemfire-httpsession-management"></a>Enabling VMware Tanzu GemFire HttpSession Management

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

### <a id="session-scoped-proxy-bean"></a>Session-Scoped Proxy Bean

The Spring Boot VMware GemFire cache client Web application defines the
`SessionScopedProxyBean` domain class.

```highlight
@Component <!--SEE COMMENT 1-->
@SessionScope(proxyMode = ScopedProxyMode.TARGET_CLASS) <!--SEE COMMENT 2-->
public class SessionScopedProxyBean implements Serializable {

    private static final AtomicInteger INSTANCE_COUNTER = new AtomicInteger(0);

    private final int count;

    public SessionScopedProxyBean() {
        this.count = INSTANCE_COUNTER.incrementAndGet(); <!--SEE COMMENT 3-->
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return String.format("{ @type = '%s', count = %d }", getClass().getName(), getCount());
    }
}
```

Comments:

1. The `SessionScopedProxyBean` domain class is stereotyped as a Spring `@Component` picked up by Spring's classpath component-scan.

2. Instances of this class are scoped to the `HttpSession`. Each time a client request results in creating a new `HttpSession` (such as during a login event), a single instance of this class is created and lasts for the duration of the `HttpSession`. When the `HttpSession` expires or is invalidated, this instance is destroyed by the Spring container. If the client re-establishes a new `HttpSession`, a new instance of this class will be provided to the application's beans. Only one instance of this class ever exists for the duration of the `HttpSession`.

3. This class tracks the number of instances of this type created by the Spring container throughout the entire application lifecycle.

For more information about Spring's <code>@SessionScope</code>, see [Request, Session, Application, and WebSocket Scopes](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-scopes-other) and [Session Scope](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-scopes-session) in the Spring documentation.

### <a id="request-scoped-proxy-bean"></a>Request-Scoped Proxy Bean

The Spring Boot VMware GemFire cache client Web application additionally
defines the `RequestScopedProxyBean` domain class.

```highlight
@Component <!--SEE COMMENT 1-->
@RequestScope(proxyMode = ScopedProxyMode.TARGET_CLASS) <!--SEE COMMENT 2-->
public class RequestScopedProxyBean {

    private static final AtomicInteger INSTANCE_COUNTER = new AtomicInteger(0);

    private final int count;

    public RequestScopedProxyBean() {
        this.count = INSTANCE_COUNTER.incrementAndGet(); <!--SEE COMMENT 3-->
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return String.format("{ @type = '%s', count = %d }", getClass().getName(), getCount());
    }
}
```

1. The `RequestScopedProxyBean` domain class is stereotyped as a Spring `@Component` picked up by Spring's classpath component-scan.

2. Instances of this class are scoped to the `HttpServletRequest`. Each time a client HTTP request is sent, for example to process a Thread-scoped transaction, a single instance of this class is created and lasts for the duration of the `HttpServletRequest`. When the request ends, this instance is destroyed by the Spring container. Any subsequent client `HttpServletRequests` results in tje creation of a new instance of this class, which will be provided to the application's beans. Only one instance of this class ever exists for the duration of the `HttpServletRequest`.

3. This class tracks the number of instances of this class created by the Spring container throughout the entire application lifecycle.

For more information about Spring's <code>@RequestScope</code>, see [Request, Session, Application, and WebSocket Scopes](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-scopes-other) and [Request Scope](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-scopes-request) in the Spring documentation.

## <a id="sample-spring-boot-web-app"></a>Sample Spring Boot Web Application

The following is a sample Spring Boot web application using VMware GemFire-managed HttpSessions with request and session scoped proxy beans

### <a id="running-boot-sample-app"></a>Running the Boot Sample Application

To run the sample app:

1. Obtain the [source code](https://github.com/spring-projects/spring-session-data-geode/archive/2.7.1.zip).

2. In a terminal window, run the server:

    ```
    ./gradlew :spring-session-sample-boot-gemfire-with-scoped-proxies:run
    ```

3. In a separate terminal window, run the client:

    ```
    ./gradlew :spring-session-sample-boot-gemfire-with-scoped-proxies:bootRun
    ```

4. In a browser, access the application at <a href="http://localhost:8080/counts"
class="bare">http://localhost:8080/counts</a>.

### <a id="exploring-sample-app"></a>Exploring the Sample Application

In a browser, the sample application looks similar to the following:

![Sample app with scoped proxies as described below](./images/sample-boot-gemfire-with-scoped-proxies.png)

The table in the app shows one row with three columns of information.

* The `Session ID` column shows the session ID of the current HttpSession.
* The `Session Count` column shows the number of `HttpSessions` created during the current run of the application.
* The `Request Count` column shows the number of requests made by the client, your web browser.

You can use your web browser's refresh button to increase both the session and request count. The session count only increases after the current session expires and a new session has been created for the client.

The session will time out after 10 seconds. This is configured on the server using the `@EnableGemFireHttpSession` annotation in `src/main/java/sample/server/GemFireServer.java`.

    ```highlight
    @SpringBootApplication
    @CacheServerApplication(name = "SpringSessionDataGeodeServerWithScopedProxiesBootSample")
    @EnableGemFireHttpSession(maxInactiveIntervalInSeconds = 10) // (3)
    public class GemFireServer {
      // ...
    }
    ```

In this, `maxInactiveIntervalInSeconds` is set to `10`. After 10 seconds, VMware GemFire will expire the `HttpSession`. When your web browser is refreshed, a new session is created and
the session count is incremented. Every request results in incrementing the request count.

### <a id="how"></a>How the Application Works

From the defined Web service endpoint in our Spring MVC `@Controller` class on the client in `src/main/java/sample/client/Application.java​`:

```highlight
@Controller
class Controller {

    @RequestMapping(method = RequestMethod.GET, path = "/counts")
    public String requestAndSessionInstanceCount(HttpServletRequest request, HttpSession session, Model model) { // (7)

        model.addAttribute("sessionId", session.getId());
        model.addAttribute("requestCount", this.requestBean.getCount());
        model.addAttribute("sessionCount", this.sessionBean.getCount());

        return INDEX_TEMPLATE_VIEW_NAME;
    }
}
```

In this we see that we have injected a reference to the `HttpSession` as a request mapping handler method parameter. This results in a new `HttpSession` on the client's first HTTP request. Subsequent requests from the same client within the duration of the existing, current `HttpSession` result in the same `HttpSession` being injected.

An `HttpSession` is identified by the session's identifier, which is stored in a Cookie sent between the client and the server during HTTP request processing.

<hr>

From the @Controller` class in `src/main/java/sample/client/Application.java`:

```highlight
@Autowired
private RequestScopedProxyBean requestBean;

@Autowired
private SessionScopedProxyBean sessionBean;
```

We have injected references to the `SessionScopedProxyBean` and `RequestScopedProxyBean` in the  @Controller` class.

Based on the class definitions of these two types, these bean instances are scoped according to Spring's _request_ and _session_ scopes. These two scopes can only be used in Web applications.

For each HTTP request sent by the client, Spring creates a new instance of the `RequestScopedProxyBean`. This results in the request count increasing with every refresh

After each new `HttpSession`, a new instance of `SessionScopedProxyBean` is created. This instance persists for the duration of the session. If the `HttpSession` remains inactive  for longer than 10 seconds, the client's current `HttpSession` expires. On any subsequent client HTTP request, a new `HttpSession` is created by the Web container, which is replaced by Spring Session and backed with
VMware GemFire.


This _session_ scoped bean is stored in the `HttpSession`, referenced by a session attribute. Note that the `SessionScopedProxyBean` class, unlike the `RequestScopedProxyBean` class, is also `java.io.Serializable`.

From `src/main/java/sample/client/model/SessionScopedProxyBean.java`

```highlight
@Component
@SessionScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionScopedProxyBean implements Serializable {
  // ...
}
```

This class is stored in the `HttpSession`, which will be transferred as part of the `HttpSession` when sent to the VMware GemFire cluster to be managed. Because of this, its type must be
`Serializable`.


Any `RequestScopedProxyBeans` are not stored in the `HttpSession` and are not sent to the server. The _request_ scoped bean does not need to implement `java.io.Serializable`.
