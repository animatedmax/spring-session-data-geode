---
title: HttpSession with GemFire Client/Server using Spring Boot and gfsh Started Servers
---

This guide describes how to build a Spring Boot application configured
with Spring Session to transparently leverage VMware GemFire
to manage a Web application's `javax.servlet.http.HttpSession`.

In this sample, we will use VMware GemFire's client/server
topology with a Spring Boot application that is both a Web application
and a VMware GemFire client configured to manage `HttpSession`
state stored in a cluster of VMware GemFire servers, which are
configured and started with
[*gfsh*](https://docs.vmware.com/en/VMware-Tanzu-GemFire/9.15/tgf/GUID-tools_modules-gfsh-chapter_overview.html).

In addition, this sample configures and uses VMware GemFire's
[*DataSerialization*](https://docs.vmware.com/en/VMware-Tanzu-GemFire/9.15/tgf/GUID-developing-data_serialization-gemfire_data_serialization.html)
framework and [Delta
Propagation](https://docs.vmware.com/en/VMware-Tanzu-GemFire/9.15/tgf/GUID-developing-delta_propagation-chapter_overview.html)
functionality to serialize the `HttpSession`. Therefore, it is necessary
to perform additional configuration steps to properly setup
VMware GemFire's *DataSerialization* capabilities on the
servers so VMware GemFire properly recognizes the Spring
Session types.

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

### <a id="spring-boot-app"></a>The Spring Boot, VMware GemFire `ClientCache`, Web Application

Let's start by creating a Spring Boot, Web application to expose our Web
Service using Spring Web MVC, running as an VMware GemFire
client, connected to our VMware GemFire servers. The Web
application will use Spring Session backed by VMware GemFire
to manage `HttpSession` state in a distributed and replicated manner.

```highlight
@SpringBootApplication <!--SEE COMMENT 1-->
@ClientCacheApplication(subscriptionEnabled = true) <!--SEE COMMENT 2-->
@EnableGemFireHttpSession(regionName = "Sessions", poolName = "DEFAULT") <!--SEE COMMENT 3-->
@Controller <!--SEE COMMENT 4-->
public class Application {

    private static final String INDEX_TEMPLATE_VIEW_NAME = "index";
    private static final String PING_RESPONSE = "PONG";
    private static final String REQUEST_COUNT_ATTRIBUTE_NAME = "requestCount";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

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

    @ExceptionHandler <!--SEE COMMENT 6-->
    public String errorHandler(Throwable error) {
        StringWriter writer = new StringWriter();
        error.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }

    @GetMapping("/ping")
    @ResponseBody
    public String ping() {
        return PING_RESPONSE;
    }

    @PostMapping("/session")
    public String session(HttpSession session, ModelMap modelMap,
            @RequestParam(name = "attributeName", required = false) String name,
            @RequestParam(name = "attributeValue", required = false) String value) { <!--SEE COMMENT 7-->

        modelMap.addAttribute("sessionAttributes",
            attributes(setAttribute(updateRequestCount(session), name, value)));

        return INDEX_TEMPLATE_VIEW_NAME;
    }
```

Comments:

1.  We start by declaring our Web application to be a Spring Boot
    application simply by annotating our application class with
    `@SpringBootApplication`.

2.  We also declare our Web application to be an VMware GemFire client by
    annotating our application class with `@ClientCacheApplication`.
    Additionally, we set `subscriptionEnabled` to receive notifications
    for any updates to the `HttpSession` that may have originated from a
    different application client accessing the same `HttpSession`.

3.  Next, we declare that the Web application will use Spring Session
    backed by VMware GemFire by annotating the application
    class with `@EnableGemFireHttpSession`. This will create the
    necessary client-side PROXY `Region`, which we have explicitly named
    "</em>Sessions</em>". This name must correspond to a server-side `Region`
    with the same name. All `HttpSession` state will be sent from the
    client to the server through `Region` data access operations using
    the "DEFAULT" connection `Pool`.

4.  `@Controller` is a Spring Web MVC annotation enabling our MVC
    handler mapping methods (i.e. methods annotated with
    `@RequestMapping`, etc) to process HTTP requests (e.g. \<7\>).

5.  We adjust the Spring Web MVC configuration to set the home
    page.

6.  We add an error handler to print out the Stack Trace of any Exception
    thrown by the server.

7.  Finally, we declare the `/session` HTTP request handler method to
    set an `HttpSession` attribute and increment a count for the number
    of HTTP requests that have occurred during this `HttpSession`.

Many other useful utility methods exist. Refer to the actual source code for full details.

<p class="note">Note</strong>: In typical VMware GemFire production
deployments, where the cluster includes potentially hundreds or
thousands of servers (a.k.a. data nodes), it is more common for clients
to connect to one or more VMware GemFire Locators running in the
same cluster. A Locator passes meta-data to clients about the servers
available in the cluster, the individual server load and which servers
have the client's data of interest, which is particularly important for
direct, single-hop data access and latency-sensitive applications. For more information, see <a
href="https://docs.vmware.com/en/VMware-Tanzu-GemFire/9.15/tgf/GUID-topologies_and_comm-cs_configuration-standard_client_server_deployment.html">Standard Client/Server Deployment</a> in the VMware GemFire product documentation.</p>

For more information about configuring Spring Data for VMware GemFire, see <a
href="https://docs.spring.io/spring-data/geode/docs/current/reference/html">Spring Data for VMware GemFire Reference Guide</a> in the Spring product documentation.

#### <a id="enabling-httpsession-management"></a>Enabling `HttpSession` Management 

The `@EnableGemFireHttpSession` annotation enables developers to
configure certain aspects of both Spring Session and
VMware GemFire out-of-the-box, using the following attributes:

- `clientRegionShortcut`: Configures the [data management policy](https://docs.vmware.com/en/VMware-Tanzu-GemFire/9.15/tgf/GUID-developing-region_options-region_types.html) on the client using the [ClientRegionShortcut](https://geode.apache.org/releases/latest/javadoc/org/apache/geode/cache/client/ClientRegionShortcut.html). Defaults to `PROXY`. Only applicable on the client.

- `indexableSessionAttributes`: Identifies the `HttpSession` attributes by name that should be indexed for queries. Only Session attributes explicitly identified by name will be indexed.

- `maxInactiveIntervalInSeconds`:Controls `HttpSession` Idle Expiration Timeout (TTI; defaults to **30 minutes**).

- `poolName`: Name of the dedicated connection `Pool` connecting the client to a cluster of servers. Defaults to `gemfirePool`. Only applicable on the client.

- `regionName`: Declares the name of the `Region` used to store and manage `HttpSession` state. Defaults to "*ClusteredSpringSessions*".

- `serverRegionShortcut`: Configures the [data management policy](https://docs.vmware.com/en/VMware-Tanzu-GemFire/9.15/tgf/GUID-developing-region_options-region_types.html) on the client using the [RegionShortcut](https://geode.apache.org/releases/latest/javadoc/org/apache/geode/cache/RegionShortcut.html). Defaults to `PARTITION`. Only applicable on the server, or when the P2P topology is employed.

- `sessionExpirationPolicyBeanName`: Configures the name of the bean declared in the Spring context implementing the Expiration Policy used by VMware GemFire to expire stale `HttpSessions`. Defaults
to unset.

- `sessionSerializerBeanName`: Configures the name of the bean declared in the Spring context used to handle de/serialization of the `HttpSession` between client and server. Defaults to PDX.

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


### <a id="starting-servers-with-gfsh"></a>Starting VMware GemFire Servers with gfsh

In this section we start a small VMware GemFire cluster.

For this sample, we will start one Locator and two Servers. In addition, we
will create the "Sessions" `Region` used to store and manage the
`HttpSession` in the cluster as a `PARTITION` `Region` using an Idle
Expiration (TTI) timeout of 15 seconds.

The following example shows the gfsh shell script we will use to set up
the cluster:

```highlight
#!$GEODE_HOME/bin/gfsh

set variable --name=USER_HOME --value=${SYS_USER_HOME}
set variable --name=REPO_HOME --value=${USER_HOME}/.m2/repository
set variable --name=SPRING_VERSION --value=@spring.version@
set variable --name=SPRING_DATA_VERSION --value=@spring-data.version@
set variable --name=SPRING_SESSION_VERSION --value=@spring-session.version@
set variable --name=SPRING_SESSION_DATA_GEODE_VERSION --value=@spring-session-data-gemfire.version@
set variable --name=MEMBER_TIMEOUT --value=5000
set variable --name=CACHE_XML_FILE --value=${USER_HOME}/spring-session-data-gemfire/samples/boot/gemfire-with-gfsh-servers/src/main/resources/initializer-cache.xml

#set variable --name=SERVER_CLASSPATH --value=${REPO_HOME}/org/springframework/spring-core/${SPRING_VERSION}/spring-core-${SPRING_VERSION}.jar\
#:${REPO_HOME}/org/springframework/spring-aop/${SPRING_VERSION}/spring-aop-${SPRING_VERSION}.jar\
#:${REPO_HOME}/org/springframework/spring-beans/${SPRING_VERSION}/spring-beans-${SPRING_VERSION}.jar\
#:${REPO_HOME}/org/springframework/spring-context/${SPRING_VERSION}/spring-context-${SPRING_VERSION}.jar\
#:${REPO_HOME}/org/springframework/spring-context-support/${SPRING_VERSION}/spring-context-support-${SPRING_VERSION}.jar\
#:${REPO_HOME}/org/springframework/spring-expression/${SPRING_VERSION}/spring-expression-${SPRING_VERSION}.jar\
#:${REPO_HOME}/org/springframework/spring-jcl/${SPRING_VERSION}/spring-jcl-${SPRING_VERSION}.jar\
#:${REPO_HOME}/org/springframework/spring-tx/${SPRING_VERSION}/spring-tx-${SPRING_VERSION}.jar\
#:${REPO_HOME}/org/springframework/data/spring-data-commons/${SPRING_DATA_VERSION}/spring-data-commons-${SPRING_DATA_VERSION}.jar\
#:${REPO_HOME}/org/springframework/data/spring-data-geode/${SPRING_DATA_VERSION}/spring-data-geode-${SPRING_DATA_VERSION}.jar\
#:${REPO_HOME}/org/springframework/session/spring-session-core/${SPRING_SESSION_VERSION}/spring-session-core-${SPRING_SESSION_VERSION}.jar\
#:${REPO_HOME}/org/springframework/session/spring-session-data-gemfire/${SPRING_SESSION_DATA_GEODE_VERSION}/spring-session-data-gemfire-${SPRING_SESSION_DATA_GEODE_VERSION}.jar\
#:${REPO_HOME}/org/slf4j/slf4j-api/1.7.25/slf4j-api-1.7.25.jar

set variable --name=SERVER_CLASSPATH --value=${REPO_HOME}/org/springframework/spring-core/${SPRING_VERSION}/spring-core-${SPRING_VERSION}.jar:${REPO_HOME}/org/springframework/spring-aop/${SPRING_VERSION}/spring-aop-${SPRING_VERSION}.jar:${REPO_HOME}/org/springframework/spring-beans/${SPRING_VERSION}/spring-beans-${SPRING_VERSION}.jar:${REPO_HOME}/org/springframework/spring-context/${SPRING_VERSION}/spring-context-${SPRING_VERSION}.jar:${REPO_HOME}/org/springframework/spring-context-support/${SPRING_VERSION}/spring-context-support-${SPRING_VERSION}.jar:${REPO_HOME}/org/springframework/spring-expression/${SPRING_VERSION}/spring-expression-${SPRING_VERSION}.jar:${REPO_HOME}/org/springframework/spring-jcl/${SPRING_VERSION}/spring-jcl-${SPRING_VERSION}.jar:${REPO_HOME}/org/springframework/spring-tx/${SPRING_VERSION}/spring-tx-${SPRING_VERSION}.jar:${REPO_HOME}/org/springframework/data/spring-data-commons/${SPRING_DATA_VERSION}/spring-data-commons-${SPRING_DATA_VERSION}.jar:${REPO_HOME}/org/springframework/data/spring-data-geode/${SPRING_DATA_VERSION}/spring-data-geode-${SPRING_DATA_VERSION}.jar:${REPO_HOME}/org/springframework/session/spring-session-core/${SPRING_SESSION_VERSION}/spring-session-core-${SPRING_SESSION_VERSION}.jar:${REPO_HOME}/org/springframework/session/spring-session-data-gemfire/${SPRING_SESSION_DATA_GEODE_VERSION}/spring-session-data-gemfire-${SPRING_SESSION_DATA_GEODE_VERSION}.jar:${REPO_HOME}/org/slf4j/slf4j-api/1.7.25/slf4j-api-1.7.25.jar

start locator --name=Locator1 --log-level=config
#start locator --name=Locator1 --log-level=config --J=-Dgemfire.member-timeout=${MEMBER_TIMEOUT}

start server --name=Server1 --log-level=config --cache-xml-file=${CACHE_XML_FILE} --classpath=${SERVER_CLASSPATH}
#start server --name=Server1 --log-level=config --cache-xml-file=${CACHE_XML_FILE} --classpath=${SERVER_CLASSPATH} --J=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 --J=-Dgemfire.member-timeout=${MEMBER_TIMEOUT}

start server --name=Server2 --server-port=0 --log-level=config --cache-xml-file=${CACHE_XML_FILE} --classpath=${SERVER_CLASSPATH}
#start server --name=Server2 --server-port=0 --log-level=config --cache-xml-file=${CACHE_XML_FILE} --classpath=${SERVER_CLASSPATH} --J=-Dgemfire.member-timeout=${MEMBER_TIMEOUT}

create region --name=Sessions --type=PARTITION --skip-if-exists --enable-statistics=true --entry-idle-time-expiration=15 --entry-idle-time-expiration-action=INVALIDATE
```

<p class="note">Note</strong>: You will minimally need to replace path to the
<code>CACHE_XML_FILE</code> depending on where you cloned the <a href="https://github.com/spring-projects/spring-session-data-gemfire/tree/2.7.1">Spring Session for VMware GemFire</a> to on your system.</p>

This gfsh shell script file contains two additional pieces of key information.

First, the shell script configures the CLASSPATH of the servers to
contain all the Spring JARs. If there were application domain classes
being stored in the `HttpSession` (i.e. in Session attributes), then a
JAR file containing the application types must also be on the CLASSPATH
of the servers.

This is necessary since, when VMware GemFire applies a delta
(i.e. the client only sends `HttpSession` changes to the servers), it
must deserialize the `HttpSession` object in order to apply the delta.
Therefore, it is also necessary to have your application domain objects
present on the CLASSPATH as well.

Second, we must include a small snippet of `cache.xml` to initialize the
VMware GemFire *DataSerialization* framework in order to
register and enable VMware GemFire to recognize the Spring
Session types representing the `HttpSession`. VMware GemFire
is very precise and will only use *DataSerialization* for the types it
knows about through registration.

But, as a user, you do not need to worry about which Spring Session
types VMware GemFire needs to know about. That is the job of
the Spring Session for VMware GemFire's
`o.s..session.data.gemfire.serialization.data.support.DataSerializableSessionSerializerInitializer`
class.

You simply just need to declare the provided Initializer in `cache.xml`,
as follows:

```highlight
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

Then, you include the `initializer-cache.xml` in the configuration of the server on startup:

```highlight
gfsh> start server --name=Server1 ... --cache-xml-file=/absolute/filesystem/path/to/initializer-cache.xml
```

The `start-cluster.gfsh` shell script shown above handles these details for us.

## <a id="running-the-sample"></a>Running the Sample

Now it is time to run our sample.

To run the sample, you must install a full installation of VMware GemFire.

After installing VMware GemFire, run gfsh:

```highlight
$ gfsh
    _________________________     __
   / _____/ ______/ ______/ /____/ /
  / /  __/ /___  /_____  / _____  /
 / /__/ / ____/  _____/ / /    / /
/______/_/      /______/_/    /_/    1.6.0

Monitor and Manage VMware GemFire
gfsh>
```

### <a id="running-the-server-side-cluster"></a>Running the Server-Side Cluster

We start the cluster by executing our `start-cluster.gfsh` shell script:

```highlight
gfsh> run --file=${SYS_USER_HOME}/spring-session-data-gemfire/samples/boot/gemfire-with-gfsh-servers/src/main/resources/geode/bin/start-cluster.gfsh
```

In the shell, you will see each command listed out as it is executed by
gfsh and you will see the Locator and Servers startup, and the
"*Sessions*" Region get created.

You should be able to `list members`, `describe region`, and run other commands. For example:

```highlight
gfsh> list members
  Name   | Id
-------- | ---------------------------------------------------------------
Locator1 | 10.99.199.41(Locator1:80666:locator)<ec><v0>:1024 [Coordinator]
Server1  | 10.99.199.41(Server1:80669)<v1>:1025
Server2  | 10.99.199.41(Server2:80672)<v2>:1026

gfsh> list regions
List of regions
---------------
Sessions

gfsh> describe region --name=/Sessions
..........................................................
Name            : Sessions
Data Policy     : partition
Hosting Members : Server1
                  Server2

Non-Default Attributes Shared By Hosting Members

 Type  |          Name           | Value
------ | ----------------------- | ---------
Region | data-policy             | PARTITION
       | entry-idle-time.timeout | 15
       | size                    | 0
       | statistics-enabled      | true
```

The "<em>Sessions</em>" Region configuration shown above is exactly the same
configuration that Spring would have created for you if you were to
configure and bootstrap your VMware GemFire servers using
Spring Boot instead.

For example, you can achieve a similar effect with the following Spring
Boot application class, which can be used to configure and bootstrap an
VMware GemFire server:

```highlight
@SpringBootApplication
@CacheServerApplication
@EnableGemFireHttpSession(regionName = "Sessions",
    maxInactiveIntervalInSeconds = 15)
public class ServerApplication {

    public static void main(String[] args) {

        new SpringApplicationBuilder(ServerApplication.class)
            .web(WebApplicationType.NONE)
            .build()
            .run(args);
    }
}
```

The nice thing about this approach is, whether you are launching the
`ServerApplication` class from you IDE, or by using a Spring Boot JAR,
since the Maven POM (or alternatively, Gradle build file) defines all
your dependencies, you do not need to worry about the CLASSPATH (other
than, perhaps your own application domain object types).

This approach is shown in [HttpSession with VMware GemFire Client/Server using Spring Boot](boot-gemfire.html).

### <a id="running-the-client"></a>Running the Client

Now, it is time to run the Spring Boot, VMware GemFire client, Web application.

You can start the Web application from your IDE, or alternatively use
the `bootRun` Gradle task to launch the application:

```highlight
$ gradlew :spring-session-sample-boot-gemfire-with-gfsh-servers:bootRun
```
Once you have started the client, you can navigate your Web browser to
<a href="http://localhost:8080/ping"><code>http://localhost:8080/ping</code></a>.
You should see the "PONG" response.

Then navigate to <a href="http://localhost:8080"><code>http://localhost:8080</code></a>. This will return
the `index.html` page where you can submit HTTP requests and add session
attributes. The page will refresh with a count of the number of HTTP
requests for the current session. After 15 seconds, the HTTP session
will expire and you will not longer see any session attributes.
Additionally, your HTTP request count will reset to 0.

![sample boot GemFire with gfsh servers](./images/sample-boot-gemfire-with-gfsh-servers.png)


While the application is running and before the HTTP session times out
(the TTI expiration timeout is set to 15 seconds), you can issue
queries in gfsh to see the contents of the "\_Sessions)" Region.

For example:

```highlight
gfsh>query --query="SELECT session.id FROM /Sessions session"
Result : true
Limit  : 100
Rows   : 1

Result
------------------------------------
9becc38f-7249-4bd0-94eb-acff70f92b87


gfsh>query --query="SELECT session.getClass().getName() FROM /Sessions session"
Result : true
Limit  : 100
Rows   : 1

Result
--------------------------------------------------------------------------------------------------------------
org.springframework.session.data.gemfire.AbstractGemFireOperationsSessionRepository$DeltaCapableGemFireSession


gfsh>query --query="SELECT attributes.key, attributes.value FROM /Sessions session, session.attributes attributes"
Result : true
Limit  : 100
Rows   : 3

    key      | value
------------ | -----
requestCount | 2
testTwo      | bar
testOne      | foo
```

The list of VMware GemFire OQL queries used in this sample can be found in `src/main/resources/geode/oql/queries.txt`.

```highlight
SELECT session.id FROM /Sessions session
SELECT session.getClass().getName() FROM /Sessions session
SELECT attributes.key, attributes.value FROM /Sessions session, session.attributes attributes
```

## <a id="conclusion"></a>Conclusion

In this sample, we saw how to specifically configure and bootstrap a
VMware GemFire cluster of servers with gfsh and then connect
to the cluster using a Spring Boot application enabled with Spring
Session, configured with VMware GemFire as the backing store
used to manage the `HttpSessions` for the Web application.

Additionally, the application used VMware GemFire's
*DataSerialization* framework to serialize the `HttpSession` state to
the servers and also send deltas. The setup and configuration expressed
in the gfsh shell script was necessary in order for
VMware GemFire to properly identify the Spring Session types
to handle.

Source code can be found at [https://github.com/spring-projects/spring-session-data-gemfire/tree/2.7.1/samples/boot/gemfire-with-gfsh-servers](https://github.com/spring-projects/spring-session-data-gemfire/tree/2.7.1/samples/boot/gemfire-with-gfsh-servers)
in GitHub.
