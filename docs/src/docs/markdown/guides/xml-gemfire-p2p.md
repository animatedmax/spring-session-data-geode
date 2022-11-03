---
title: HttpSession with VMware GemFire P2P using XML Configuration
---

This guide describes how to configure VMware GemFire as a provider in
Spring Session to transparently manage a Web application's
`javax.servlet.http.HttpSession` using XML configuration.

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

## <a id="xml-servlet-container-initialization"></a>XML Servlet Container Initialization

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

## <a id="java-servlet-container-initialization"></a>HttpSession with VMware GemFire P2P using XML Sample Application

This section describes an HttpSession with VMware GemFire P2P using XML sample application.

### <a id="running-sample-app"></a>Running the VMware GemFire XML Sample Application

To run the sample app:

1. Obtain the [source code](https://github.com/spring-projects/spring-session-data-geode/archive/2.7.1.zip).

2. In a terminal window, run:

    ```
    ./gradlew :spring-session-sample-xml-gemfire-p2p:tomcatRun
    ```

4. In a browser, access the application at <a href="http://localhost:8080/"
class="bare">http://localhost:8080/</a>.

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
}
```

Instead of using Tomcat's `HttpSession`, we persist the Session in VMware GemFire.
Spring Session creates a cookie named "SESSION" in your browser that contains the ID of
the Session. You can view the cookies using your browser controls.
