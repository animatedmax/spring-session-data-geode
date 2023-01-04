---
title: HttpSession with VMware GemFire Client-Server using Java configuration
---

This guide describes how to configure Spring Session to transparently
leverage VMware GemFire to manage a Web application's
`javax.servlet.http.HttpSession` using Java Configuration.

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
        <version>2.7.1</version>
        <type>pom</type>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-web</artifactId>
        <version>5.3.23</version>
    </dependency>
</dependencies>
```

## <a id="spring-java-configuration"></a>Spring Java Configuration

After adding the required dependencies and repository declarations, we
can create the Spring configuration. The Spring configuration is
responsible for creating a Servlet `Filter` that replaces the
`HttpSession` with an implementation backed by Spring Session and VMware GemFire.

### <a id="client-configuration"></a>Client Configuration

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

### <a id="server-configuration"></a>Server Configuration

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

## <a id="java-servlet-container-initialization"></a>Java Servlet Container Initialization

[Spring Java Configuration](#spring-java-configuration) created a Spring bean named
`springSessionRepositoryFilter` that implements `javax.servlet.Filter`. The
`springSessionRepositoryFilter` bean is responsible for replacing the 
`javax.servlet.http.HttpSession` with a custom implementation backed by Spring Session and VMware
GemFire.

The `Filter` requires that Spring loads the `ClientConfig` class. We must also ensure that the
Servlet container, Tomcat, uses the `springSessionRepositoryFilter` for every request.

Spring Session provides a utility class named `AbstractHttpSessionApplicationInitializer` that
provides this functionality.

Example from `src/main/java/sample/Initializer.java`

```highlight
public class Initializer extends AbstractHttpSessionApplicationInitializer { <!--SEE COMMENT 1-->

    public Initializer() {
        super(ClientConfig.class); <!--SEE COMMENT 2-->
    }
}
```

Comments:

1. Extends `AbstractHttpSessionApplicationInitializer` to ensure that a Spring bean named `springSessionRepositoryFilter` is registered with the Servlet container and is used for every HTTP request.

2. `AbstractHttpSessionApplicationInitializer` provides a mechanism to allow Spring to load the `ClientConfig`.

## <a id="httpsession"></a>HttpSession Managed By a Client-Server Sample Application

This section describes an HttpSession managed by a Java-configured, VMware GemFire Client-Server sample application.

### <a id="running-sample-app"></a>Running the VMware GemFire Sample Application

To run the sample app:

1. Obtain the [source code](https://github.com/spring-projects/spring-session-data-geode/archive/2.7.1.zip).

2. In a terminal window, run the server:

    ```
    ./gradlew :spring-session-sample-javaconfig-gemfire-clientserver:run
    ```

3. In a separate terminal window, run the client:

    ```
    ./gradlew :spring-session-sample-javaconfig-gemfire-clientserver:tomcatRun
    ```

4. In a browser, access the application at <a href="http://localhost:8080/"
class="bare">http://localhost:8080/</a>.

In this sample, the web application is the VMware GemFire cache client and
the server is standalone, separate JVM process.

### <a id="exploring-sample-app"></a>Exploring the Sample Application

1. In the application, complete the form with the following information:

    - **Attribute Name:** `username`
    - **Attribute Value:** `test``

2. Click the **Set Attribute** button. You should now see the attribute name and value displayed in the table.

### <a id="how"></a>How the Application Works

We interact with the standard `HttpSession` in the `SessionServlet`, located in `src/main/java/sample/SessionServlet.java`:

```highlight
@WebServlet("/session")
public class SessionServlet extends HttpServlet {

    private static final long serialVersionUID = 2878267318695777395L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        String attributeName = request.getParameter("attributeName");
        String attributeValue = request.getParameter("attributeValue");

        request.getSession().setAttribute(attributeName, attributeValue);
        response.sendRedirect(request.getContextPath() + "/");
    }
}
```


Instead of using Tomcat's `HttpSession`, we persist the Session in VMware GemFire.
Spring Session creates a cookie named "SESSION" in your browser that contains the ID of
the Session. You can view the cookies using your browser controls.
