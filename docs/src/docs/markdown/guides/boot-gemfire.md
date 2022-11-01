---
title: HttpSession with VMware GemFire Client/Server using Spring Boot
---

This guide describes how to build a Spring Boot application configured
with Spring Session to transparently leverage VMware GemFire to manage a
web application's `javax.servlet.http.HttpSession`.

In this sample, VMware GemFire's client/server topology is employed using
a pair of Spring Boot applications, one to configure and run a VMware GemFire
Server and another to configure and run the cache client, a Spring
MVC-based web application making use of the `HttpSession`.

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
can create the Spring configuration for our VMware GemFire
client using Spring Boot. The Spring configuration is responsible for
creating a Servlet `Filter` that replaces the Web container's
`HttpSession` with an implementation backed by Spring Session, which is
then stored and managed in VMware GemFire.

### <a id="spring-boot-gemfire-cache-server"></a>Spring Boot, VMware GemFire Cache Server

We start with a Spring Boot application to configure and bootstrap the VMware GemFire Server:

```highlight
@SpringBootApplication <!--SEE COMMENT 1-->
@CacheServerApplication(name = "SpringSessionDataGeodeBootSampleServer", logLevel = "error") <!--SEE COMMENT 2-->
@EnableGemFireHttpSession(maxInactiveIntervalInSeconds = 20) <!--SEE COMMENT 3-->
public class GemFireServer {

    public static void main(String[] args) {

        new SpringApplicationBuilder(GemFireServer.class)
            .web(WebApplicationType.NONE)
            .build()
            .run(args);
    }
}
```

Comments:

1. We annotate the VMware GemFire Server configuration class, `GemFireServer`, with `@SpringBootApplication` to indicate that this is a Spring Boot application leveraging all of Spring Boot's features.

2. We use the Spring Data for VMware GemFire configuration annotation `@CacheServerApplication` to simplify the creation of a peer cache instance containing a `CacheServer` for cache clients to connect.

3. (Optional) We use the `@EnableGemFireHttpSession` annotation to create the necessary server-side `Region` (by default, `ClusteredSpringSessions`) to store the `HttpSessions` state. This
    step is optional because the Session `Region` can be created manually.

### <a id="spring-boot-gemfire-cache-client"></a>Spring Boot, VMware GemFire Cache Client Web Application

We create a Spring Boot Web application to expose our Web service
with Spring Web MVC, running as an VMware GemFire cache client connected
to our Spring Boot, VMware GemFire Server. The Web application will use
Spring Session backed by VMware GemFire to manage `HttpSession` state in a
clustered (distributed) and replicated manner.

```highlight
@SpringBootApplication <!--SEE COMMENT 1-->
@Controller <!--SEE COMMENT 2-->
public class Application {

    static final String INDEX_TEMPLATE_VIEW_NAME = "index";
    static final String PING_RESPONSE = "PONG";
    static final String REQUEST_COUNT_ATTRIBUTE_NAME = "requestCount";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @ClientCacheApplication(name = "SpringSessionDataGeodeBootSampleClient", logLevel = "error",
        readTimeout = 15000, retryAttempts = 1, subscriptionEnabled = true) <!--SEE COMMENT 3-->
    @EnableGemFireHttpSession(poolName = "DEFAULT") <!--SEE COMMENT 4-->
    static class ClientCacheConfiguration extends ClientServerIntegrationTestsSupport {

        @Bean
        ClientCacheConfigurer gemfireServerReadyConfigurer( <!--SEE COMMENT 5-->
            @Value("${spring.data.gemfire.cache.server.port:40404}") int cacheServerPort) {

            return (beanName, clientCacheFactoryBean) -> waitForServerToStart("localhost", cacheServerPort);
        }
    }

    @Configuration
    static class SpringWebMvcConfiguration { <!--SEE COMMENT 6-->

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

    @RequestMapping(method = RequestMethod.POST, path = "/session")
    public String session(HttpSession session, ModelMap modelMap,
        @RequestParam(name = "attributeName", required = false) String name,
        @RequestParam(name = "attributeValue", required = false) String value) { <!--SEE COMMENT 7-->

        modelMap.addAttribute("sessionAttributes",
            attributes(setAttribute(updateRequestCount(session), name, value)));

        return INDEX_TEMPLATE_VIEW_NAME;
    }
```

Comments:

1. We declare our Web application to be a Spring Boot application by annotating our application class with `@SpringBootApplication`.

2. `@Controller` is a Spring Web MVC annotation enabling our MVC handler mapping methods (i.e. methods annotated with `@RequestMapping`) to process HTTP requests, for example \<7\>.

3. We declare our Web application to be an VMware GemFire cache client by annotating our application class with  `@ClientCacheApplication`. Wdditionally, we adjust a few basic,
    "DEFAULT" `Pool` settings, for example `readTimeout`.

4. We declare that the Web application will use Spring Session backed by VMware GemFire by annotating the `ClientCacheConfiguration` class with `@EnableGemFireHttpSession`. This will create the necessary client-side `Region` (by default, "ClusteredSpringSessions" as a `PROXY` `Region`) corresponding to the same server-side `Region` by name. All `HttpSession` state will be sent from the cache client Web application to the server through `Region` data access operations. The client-side `Region` uses the "DEFAULT" `Pool`.

5. We wait to ensure the VMware GemFire Server is up and running before we proceed. This is used for automated integration testing purposes.

6. We adjust the Spring Web MVC configuration to set the home page.

7. We declare the `/sessions` HTTP request handler method to set an HTTP Session attribute and increment a count for the number of HTTP requests.

Many other useful utility methods exist. Refer to the actual source code for full details.

<p class="note">Note</strong>: In typical VMware GemFire production
deployments, where the cluster includes potentially hundreds or
thousands of servers (a.k.a. data nodes), it is more common for clients
to connect to one or more VMware GemFire Locators running in the
same cluster. A Locator passes meta-data to clients about the servers
available in the cluster, the individual server load, and which servers
have the client's data of interest, which is particularly important for
direct, single-hop data access and latency-sensitive applications. For more information, see <a
href="https://docs.vmware.com/en/VMware-Tanzu-GemFire/9.15/tgf/GUID-topologies_and_comm-cs_configuration-standard_client_server_deployment.html">Standard Client/Server Deployment</a> in the VMware GemFire product documentation.</p>

For more information about configuring Spring Data for VMware GemFire, see <a
href="https://docs.spring.io/spring-data/geode/docs/current/reference/html">Spring Data for VMware GemFire Reference Guide</a> in the Spring product documentation.

#### <a id="enabling-httpsession-management"></a>Enabling `HttpSession` Management 

The `@EnableGemFireHttpSession` annotation enables developers to
configure certain aspects of both Spring Session and VMware GemFire
out-of-the-box using the following attributes:

- `clientRegionShortcut`: Configures the [data management policy](https://docs.vmware.com/en/VMware-Tanzu-GemFire/9.15/tgf/GUID-developing-region_options-region_types.html) on the client using the [ClientRegionShortcut](https://geode.apache.org/releases/latest/javadoc/org/apache/geode/cache/client/ClientRegionShortcut.html). Defaults to `PROXY`. Only applicable on the client.

- `indexableSessionAttributes`: Identifies the `HttpSession` attributes by name that should be indexed for queries. Only Session attributes explicitly identified by name will be indexed.

- `maxInactiveIntervalInSeconds`:Controls `HttpSession` Idle Expiration Timeout (TTI; defaults to **30 minutes**).

- `poolName`: Name of the dedicated connection `Pool` connecting the client to a cluster of servers. Defaults to `gemfirePool`. Only applicable on the client.

- `regionName`: Declares the name of the `Region` used to store and manage `HttpSession` state. Defaults to "*ClusteredSpringSessions*".

- `serverRegionShortcut`: Configures the [data management policy](https://docs.vmware.com/en/VMware-Tanzu-GemFire/9.15/tgf/GUID-developing-region_options-region_types.html) on the client using the [RegionShortcut](https://geode.apache.org/releases/latest/javadoc/org/apache/geode/cache/RegionShortcut.html). Defaults to `PARTITION`. Only applicable on the server, or when the P2P topology is employed.

<p class="note">Note</strong>: The VMware GemFire client <code>Region</code> name must match a
server <code>Region</code> by the same name if the client
<code>Region</code> is a <code>PROXY</code> or
<code>CACHING_PROXY</code>. Client and server <code>Region</code> names
are not required to match if the client <code>Region</code> is
<code>LOCAL</code>. However, by using a client
<code>LOCAL</code> Region, <code>HttpSession</code> state will not be
propagated to the server and you lose all the benefits of using
VMware GemFire to store and manage <code>HttpSession</code>
state on the servers in a distributed, replicated manner.</p>

## <a id="sampe-spring-boot-web-app"></a>Spring Boot Sample Web Application

The following is a sample Spring Boot web application with a VMware GemFire-Managed HttpSession.

### <a id="running-boot-sample-app"></a>Running the Boot Sample Application

To run the sample app:

1. Obtain the [source code](https://github.com/spring-projects/spring-session-data-geode/archive/2.7.1.zip).

2. In a terminal window, run the server:

    ```
    ./gradlew :spring-session-sample-boot-gemfire:run
    ```

3. In a separate terminal window, run the client:

    ```
    ./gradlew :spring-session-sample-boot-gemfire:bootRun
    ```

4. In a browser, access the application at <a href="http://localhost:8080/"
class="bare">http://localhost:8080/</a>.

In this sample, the Web application is the Spring Boot, VMware GemFire cache client and the server is standalone, separate JVM process.

### <a id="exploring-sample-app"></a>Exploring the Sample Application

1. In the application, complete the form with the following information:

    - **Attribute Name:** `username`
    - **Attribute Value:** `test``

2. Click the **Set Attribute** button. You should see the attribute name and value displayed in the table along with an additional attribute, `requestCount`, which shows the number of HTTP requests made.

### <a id="how"></a>How the Application Works

We interact with the standard `javax.servlet.http.HttpSession` in the
the Spring Web MVC service endpoint, located in `src/main/java/sample/client/Application.java`.

```highlight
@Controller
class Controller {

    @RequestMapping(method = RequestMethod.POST, path = "/session")
    public String session(HttpSession session, ModelMap modelMap,
        @RequestParam(name = "attributeName", required = false) String name,
        @RequestParam(name = "attributeValue", required = false) String value) {

        modelMap.addAttribute("sessionAttributes",
            attributes(setAttribute(updateRequestCount(session), name, value)));

        return INDEX_TEMPLATE_VIEW_NAME;
    }
}
```

Instead of using the embedded HTTP server's `HttpSession`, we persist the Session state in VMware GemFire. Spring Session creates a cookie named "SESSION" in your browser that contains the ID of
the Session. You can view the cookies using your browser controls.
