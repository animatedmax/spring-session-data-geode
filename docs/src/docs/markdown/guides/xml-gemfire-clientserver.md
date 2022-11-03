---
title: HttpSession with VMware GemFire Client/Server using XML Configuration
---

This guide describes how to configure VMware GemFire as a provider in
Spring Session to transparently manage a Web application's
`javax.servlet.http.HttpSession` using XML Configuration.

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
        <artifactId>spring-web</artifactId>
        <version>{spring-version}</version>
    </dependency>
</dependencies>
```

## <a id="spring-xml-configuration"></a>Spring XML Configuration

After adding the required dependencies and repository declarations, we
can create the Spring configuration. The Spring configuration is
responsible for creating a `Servlet` `Filter` that replaces the
`javax.servlet.http.HttpSession` with an implementation backed by Spring
Session and VMware GemFire.

### <a id="client-configuration"></a>Client Configuration

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

3. Configure a `Pool` of connections to communicate with the VMware GemFire Server in this  Client/Server topology. The `Pool` has been configured to connect directly to the server using the nested `gfe:server` element.

4. A `GemFireHttpSessionConfiguration` bean is registered to enable Spring Session functionality.

<p class="note">Note</strong>: In typical VMware GemFire production deployments,
where the cluster includes potentially hundreds or thousands of servers
(a.k.a. data nodes), it is more common for clients to connect to one or
more VMware GemFire Locators running in the same cluster. A Locator passes
meta-data to clients about the servers available in the cluster, the
individual server load and which servers have the client's data of
interest, which is particularly important for direct, single-hop data
access and latency-sensitive applications. For more information, see <a
href="https://docs.vmware.com/en/VMware-Tanzu-GemFire/9.15/tgf/GUID-topologies_and_comm-cs_configuration-standard_client_server_deployment.html">Standard Client/Server Deployment</a> in the VMware GemFire product documentation.</p>

For more information about configuring Spring Data for VMware GemFire, refer to the <a
href="https://docs.spring.io/spring-data/geode/docs/current/reference/html">Spring Data for Apache Geode Reference Guide</a>.

### <a id="server-configuration"></a>Server Configuration

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

## <a id="xml-servlet-container-initialization"></a>XML Servlet Container Initialization

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

## <a id="java-servlet-container-initialization"></a>HttpSession with VMware GemFire Client/Server Sample Application

This section describes an HttpSession with VMware GemFire Client/Server using XML sample application.

### <a id="running-sample-app"></a>Running the httpsession-gemfire-clientserver-xml Sample Application

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
}```

Instead of using Tomcat's `HttpSession`, we persist the Session in VMware GemFire.
Spring Session creates a cookie named "SESSION" in your browser that contains the ID of
the Session. You can view the cookies using your browser controls.
