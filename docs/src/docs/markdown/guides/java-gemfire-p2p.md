---
title: HttpSession with VMware GemFire P2P using Java Configuration
---

This guide describes how to configure VMware GemFire as a provider in
Spring Session to transparently manage a Web application's
`javax.servlet.http.HttpSession` using Java configuration.


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

## <a id="spring-java-configuration"></a>Spring Java Configuration

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

## <a id="java-servlet-container-initialization"></a>Java Servlet Container Initialization

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

## <a id="httpsession"></a>HttpSession with VMware GemFire P2P Sample Application

This section describes an HttpSession with VMware GemFire (P2P) Sample Application.

### <a id="running-sample-app"></a>Running the VMware GemFire P2P Java Sample Application

To run the sample app:

1. Obtain the [source code](https://github.com/spring-projects/spring-session-data-geode/archive/2.7.1.zip).

2. In a terminal window, run the server:

    ```
    ./gradlew :spring-session-sample-javaconfig-gemfire-p2p:tomcatRun
    ```

3. In a browser, access the application at <a href="http://localhost:8080/"
class="bare">http://localhost:8080/</a>.

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

