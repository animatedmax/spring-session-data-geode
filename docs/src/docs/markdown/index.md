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
      <td>Demonstrates how to use Spring Session to manage the <code>HttpSession</code> with VMware GemFire in a Spring Boot application using a Client/Server topology.</td>
      <td><a href="guides/boot-gemfire.html">HttpSession with Spring Boot and VMware GemFire Guide</a></td>
    </tr>
    <tr class="even">
      <td><a href="https://github.com/spring-projects/spring-session-data-geode/tree/2.7.1/samples/boot/gemfire-with-gfsh-servers">HttpSession with Spring Boot and VMware GemFire using gfsh</a></td>
      <td>Demonstrates how to use Spring Session to manage the <code>HttpSession</code> with VMware GemFire in a Spring Boot application using a Client/Server topology. Additionally configures and uses VMware GemFire's <em>DataSerialization</em> framework.</td>
      <td><a href="guides/boot-gemfire-with-gfsh-servers.html">HttpSession with Spring Boot and VMware GemFire using gfsh Guide</a></td>
    </tr>
    <tr class="odd">
      <td><a href="https://github.com/spring-projects/spring-session-data-geode/tree/2.7.1/samples/boot/gemfire-with-scoped-proxies">HttpSession with Spring Boot and VMware GemFire using Scoped Proxies</a></td>
      <td>Demonstrates how to use Spring Session to manage the <code>HttpSession</code> with VMware GemFire in a Spring Boot application using a Client/Server topology. The application also makes use of Spring Request and Session Scoped Proxy beans.</td>
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
      <td><a href="https://github.com/spring-projects/spring-session-data-geode/tree/2.7.1/samples/javaconfig/gemfire-clientserver">HttpSession with VMware GemFire Client/Server</a></td>
      <td>Demonstrates how to use Spring Session to manage the <code>HttpSession</code> with VMware GemFire using a Client/Server topology.</td>
      <td><a href="guides/java-gemfire-clientserver.html">HttpSession with VMware GemFire Client/Server Guide</a></td>
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
      <td><a href="https://github.com/spring-projects/spring-session-data-geode/tree/2.7.1/samples/xml/gemfire-clientserver">HttpSession with VMware GemFire Client/Server</a></td>
      <td>Demonstrates how to use Spring Session to manage the <code>HttpSession</code> with VMware GemFire using a Client/Server topology.</td>
      <td><a href="guides/xml-gemfire-clientserver.html">HttpSession with VMware GemFire Client/Server Guide</a></td>
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
For more information, see
[Multi-site (WAN) Configuration](https://docs.vmware.com/en/VMware-Tanzu-GemFire/9.15/tgf/GUID-topologies_and_comm-multi_site_configuration-chapter_overview.html)
in the VMware GemFire product documentation.

More details on configuring VMware GemFire WAN functionality
using Spring Data for VMware GemFire can be found
[here](https://docs.spring.io/spring-data/geode/docs/current/reference/html/#bootstrap:gateway).

</div>

<div class="sect3">

#### VMware GemFire Client-Server

<div class="paragraph">

The
[Client-Server](https://geode.apache.org/docs/guide/%7Bmaster-data-store-version%7D/topologies_and_comm/cs_configuration/chapter_overview.html)
topology will likely be the most common configuration choice among users
when using VMware GemFire as a provider in Spring Session
since a VMware GemFire server has significantly different and
unique JVM heap requirements than compared to the application. Using a
Client-Server topology enables an application to manage (e.g. replicate)
application state independently from other application processes.

</div>

<div class="paragraph">

In a Client-Server topology, an application using Spring Session will
open 1 or more connections to a remote cluster of
VMware GemFire servers that will manage access to all
`HttpSession` state.

</div>

<div class="paragraph">

You can configure a Client-Server topology with either:

</div>

<div class="ulist">

- [Java-based Configuration](#httpsession-gemfire-clientserver-java)

- [XML-based Configuration](#httpsession-gemfire-clientserver-xml)

</div>

<div class="sect4">

##### VMware GemFire Client-Server Java-based Configuration

<div class="paragraph">

This section describes how to configure VMware GemFire's
Client-Server topology to manage `HttpSession` state with Java-based
configuration.

</div>

<div class="admonitionblock note">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Note
</div></td>
<td class="content">The <a href="#samples">HttpSession with
VMware GemFire (Client-Server)</a> provides a working sample
demonstrating how to integrate Spring Session with
VMware GemFire to manage <code>HttpSession</code> state using
Java configuration. You can read through the basic steps of integration
below, but you are encouraged to follow along in the detailed
<em>`HttpSession` with VMware GemFire (Client-Server)
Guide</em> when integrating with your own application.</td>
</tr>
</tbody>
</table>

</div>

</div>

<div class="sect4">

##### Spring Java Configuration

<div class="paragraph">

After adding the required dependencies and repository declarations, we
can create the Spring configuration. The Spring configuration is
responsible for creating a Servlet `Filter` that replaces the
`HttpSession` with an implementation backed by Spring Session and VMware GemFire.

</div>

<div class="sect5">

###### Client Configuration

<div class="paragraph">

Add the following Spring configuration:

</div>

<div class="listingblock">

<div class="content">

```highlight
Unresolved directive in guides/java-gemfire-clientserver.adoc - include::{samples-dir}javaconfig/gemfire-clientserver/src/main/java/sample/client/ClientConfig.java[tags=class]
```

</div>

</div>

<div class="colist arabic">

1.  First, we declare our Web application to be an VMware GemFire cache
    client by annotating our `ClientConfig` class with
    `@ClientCacheApplication`. Additionally, we adjust a few basic,
    "DEFAULT" `Pool` settings (e.g. `readTimeout`).

2.  `@EnableGemFireHttpSession` creates a Spring bean named
    `springSessionRepositoryFilter` that implements
    `javax.servlet.Filter`. The filter replaces the `HttpSession` with
    an implementation provided by Spring Session and backed by VMware GemFire. Additionally, the configuration will also create the
    necessary client-side `Region` (by default,
    "ClusteredSpringSessions\`, which is a `PROXY` `Region`)
    corresponding to the same server-side `Region` by name. All session
    state is sent from the client to the server through `Region` data
    access operations. The client-side `Region` use the "DEFAULT"
    `Pool`.

3.  Then, we wait to ensure the VMware GemFire Server is up and running
    before we proceed. This is only really useful for automated
    (integration) testing purposes.

</div>

<div class="admonitionblock tip">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Tip
</div></td>
<td class="content">In typical VMware GemFire production deployments,
where the cluster includes potentially hundreds or thousands of servers
(a.k.a. data nodes), it is more common for clients to connect to 1 or
more VMware GemFire Locators running in the same cluster. A Locator passes
meta-data to clients about the servers available in the cluster, the
individual server load and which servers have the client's data of
interest, which is particularly important for direct, single-hop data
access and latency-sensitive applications. See more details about the <a
href="https://geode.apache.org/docs/guide/%7Bmaster-data-store-version%7D/topologies_and_comm/cs_configuration/standard_client_server_deployment.html">Client/Server
Deployment</a> in the VMware GemFire User Guide.</td>
</tr>
</tbody>
</table>

</div>

<div class="admonitionblock note">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Note
</div></td>
<td class="content">For more information on configuring Spring Data for GemFire, refer to the <a
href="https://docs.spring.io/spring-data/geode/docs/current/reference/html">Reference
Guide</a>.</td>
</tr>
</tbody>
</table>

</div>

<div class="paragraph">

The `@EnableGemFireHttpSession` annotation enables developers to
configure certain aspects of both Spring Session and VMware GemFire
out-of-the-box using the following attributes:

</div>

<div class="ulist">

- `clientRegionShortcut` - specifies VMware GemFire [data management
  policy](https://geode.apache.org/docs/guide/%7Bmaster-data-store-version%7D/developing/region_options/region_types.html)
  on the client with the
  [ClientRegionShortcut](https://geode.apache.org/releases/latest/javadoc/org/apache/geode/cache/client/ClientRegionShortcut.html)
  (default is `PROXY`). This attribute is only used when configuring the
  client `Region`.

- `indexableSessionAttributes` - Identifies the Session attributes by
  name that should be indexed for querying purposes. Only Session
  attributes explicitly identified by name will be indexed.

- `maxInactiveIntervalInSeconds` - controls *HttpSession* idle-timeout
  expiration (defaults to **30 minutes**).

- `poolName` - name of the dedicated VMware GemFire `Pool` used to connect
  a client to the cluster of servers. This attribute is only used when
  the application is a cache client. Defaults to `gemfirePool`.

- `regionName` - specifies the name of the VMware GemFire `Region` used to
  store and manage `HttpSession` state (default is
  "**ClusteredSpringSessions**").

- `serverRegionShortcut` - specifies VMware GemFire [data management
  policy](https://geode.apache.org/docs/guide/%7Bmaster-data-store-version%7D/developing/region_options/region_types.html)
  on the server with the
  [RegionShortcut](https://geode.apache.org/releases/latest/javadoc/org/apache/geode/cache/RegionShortcut.html)
  (default is `PARTITION`). This attribute is only used when configuring
  server `Regions`, or when a P2P topology is employed.

</div>

<div class="admonitionblock note">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Note
</div></td>
<td class="content">It is important to remember that the VMware GemFire
client <code>Region</code> name must match a server <code>Region</code>
by the same name if the client <code>Region</code> is a
<code>PROXY</code> or <code>CACHING_PROXY</code>. Client and server
<code>Region</code> names are not required to match if the client
<code>Region</code> used to store session state is <code>LOCAL</code>.
However, keep in mind that Session state will not be propagated to the
server and you lose all the benefits of using VMware GemFire to store and
manage distributed, replicated session state information on the servers
in a distributed, replicated manner.</td>
</tr>
</tbody>
</table>

</div>

</div>

<div class="sect5">

###### Server Configuration

<div class="paragraph">

So far, we only covered one side of the equation. We also need an Apache
Geode Server for our cache client to talk to and send session state to
the server to manage.

</div>

<div class="paragraph">

In this sample, we will use the following Java configuration to
configure and run an VMware GemFire Server:

</div>

<div class="listingblock">

<div class="content">

```highlight
Unresolved directive in guides/java-gemfire-clientserver.adoc - include::{samples-dir}javaconfig/gemfire-clientserver/src/main/java/sample/server/GemFireServer.java[tags=class]
```

</div>

</div>

<div class="colist arabic">

1.  First, we use the `@CacheServerApplication` annotation to simplify
    the creation of a peer cache instance containing with a
    `CacheServer` for cache clients to connect.

2.  (Optional) Then, the `GemFireServer` class is annotated with
    `@EnableGemFireHttpSession` to create the necessary server-side
    `Region` (by default, "*ClusteredSpringSessions*") used to store
    `HttpSession` state. This step is optional since the Session
    `Region` could be created manually, perhaps using external means.
    Using `@EnableGemFireHttpSession` is convenient and quick.

</div>

</div>

</div>

<div class="sect4">

##### VMware GemFire Client-Server XML-based Configuration

<div class="paragraph">

This section describes how to configure VMware GemFire's
Client-Server topology to manage `HttpSession` state with XML-based
configuration.

</div>

<div class="admonitionblock note">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Note
</div></td>
<td class="content">The <a href="#samples">HttpSession with
VMware GemFire (Client-Server) using XML</a> provides a
working sample demonstrating how to integrate Spring Session with
VMware GemFire to manage <code>HttpSession</code> state using
XML configuration. You can read through the basic steps of integration
below, but you are encouraged to follow along in the detailed
<em>`HttpSession` with VMware GemFire (Client-Server) using
XML Guide</em> when integrating with your own application.</td>
</tr>
</tbody>
</table>

</div>

</div>

<div class="sect4">

##### Spring XML Configuration

<div class="paragraph">

After adding the required dependencies and repository declarations, we
can create the Spring configuration. The Spring configuration is
responsible for creating a `Servlet` `Filter` that replaces the
`javax.servlet.http.HttpSession` with an implementation backed by Spring
Session and VMware GemFire.

</div>

<div class="sect5">

###### Client Configuration

<div class="paragraph">

Add the following Spring configuration:

</div>

<div class="listingblock">

<div class="content">

```highlight
Unresolved directive in guides/xml-gemfire-clientserver.adoc - include::{samples-dir}xml/gemfire-clientserver/src/main/webapp/WEB-INF/spring/session-client.xml[tags=beans]
```

</div>

</div>

<div class="colist arabic">

1.  (Optional) First, we can include a `Properties` bean to configure
    certain aspects of the VMware GemFire `ClientCache` using [Pivotal
    GemFire
    Properties](https://geode.apache.org/docs/guide/%7Bmaster-data-store-version%7D/reference/topics/gemfire_properties.html).
    In this case, we are just setting VMware GemFire's "log-level" using
    an application-specific System property, defaulting to "warning" if
    unspecified.

2.  We must create an instance of an VMware GemFire `ClientCache`. We
    initialize it with our `gemfireProperties`.

3.  Then we configure a `Pool` of connections to talk to the Apache
    Geode Server in our Client/Server topology. In our configuration, we
    use sensible settings for timeouts, number of connections and so on.
    Also, our `Pool` has been configured to connect directly to the
    server (using the nested `gfe:server` element).

4.  Finally, a `GemFireHttpSessionConfiguration` bean is registered to
    enable Spring Session functionality.

</div>

<div class="admonitionblock tip">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Tip
</div></td>
<td class="content">In typical VMware GemFire production deployments,
where the cluster includes potentially hundreds or thousands of servers
(a.k.a. data nodes), it is more common for clients to connect to 1 or
more VMware GemFire Locators running in the same cluster. A Locator passes
meta-data to clients about the servers available in the cluster, the
individual server load and which servers have the client's data of
interest, which is particularly important for direct, single-hop data
access and latency-sensitive applications. See more details about the <a
href="https://geode.apache.org/docs/guide/%7Bmaster-data-store-version%7D/topologies_and_comm/cs_configuration/standard_client_server_deployment.html">Client/Server
Deployment</a> in the VMware GemFire User Guide.</td>
</tr>
</tbody>
</table>

</div>

<div class="admonitionblock note">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Note
</div></td>
<td class="content">For more information on configuring Spring Data for
VMware GemFire, refer to the <a
href="https://docs.spring.io/spring-data/geode/docs/current/reference/html">Reference
Guide</a>.</td>
</tr>
</tbody>
</table>

</div>

</div>

<div class="sect5">

###### Server Configuration

<div class="paragraph">

So far, we only covered one side of the equation. We also need an Apache
Geode Server for our cache client to talk to and send session state to
the server to manage.

</div>

<div class="paragraph">

In this sample, we will use the following XML configuration to spin up
an VMware GemFire Server:

</div>

<div class="listingblock">

<div class="content">

```highlight
Unresolved directive in guides/xml-gemfire-clientserver.adoc - include::{samples-dir}xml/gemfire-clientserver/src/main/resources/META-INF/spring/session-server.xml[tags=beans]
```

</div>

</div>

<div class="colist arabic">

1.  (Optional) First, we can include a `Properties` bean to configure
    certain aspects of the VMware GemFire peer `Cache` using [Pivotal
    GemFire
    Properties](https://geode.apache.org/docs/guide/%7Bmaster-data-store-version%7D/reference/topics/gemfire_properties.html).
    In this case, we are just setting VMware GemFire's "log-level" using
    an application-specific System property, defaulting to "warning" if
    unspecified.

2.  We must configure an VMware GemFire peer `Cache` instance. We
    initialize it with the VMware GemFire properties.

3.  Next, we define a `CacheServer` with sensible configuration for
    `bind-address` and `port` used by our cache client application to
    connect to the server and send session state.

4.  Finally, we enable the same Spring Session functionality we declared
    in the client XML configuration by registering an instance of
    `GemFireHttpSessionConfiguration`, except we set the session
    expiration timeout to **30 seconds**. We explain what this means
    later.

</div>

<div class="paragraph">

The VMware GemFire Server gets bootstrapped with the following:

</div>

<div class="listingblock">

<div class="content">

```highlight
Unresolved directive in guides/xml-gemfire-clientserver.adoc - include::{samples-dir}xml/gemfire-clientserver/src/main/java/sample/server/GemFireServer.java[tags=class]
```

</div>

</div>

<div class="admonitionblock tip">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Tip
</div></td>
<td class="content">Rather than defining a simple Java class with a
<code>main</code> method, you might consider using Spring Boot
instead.</td>
</tr>
</tbody>
</table>

</div>

<div class="colist arabic">

1.  The `@Configuration` annotation designates this Java class as a
    source of Spring configuration meta-data using 7.9. Annotation-based
    container configuration\[Spring's annotation configuration
    support\].

2.  Primarily, the configuration comes from the
    `META-INF/spring/session-server.xml` file.

</div>

</div>

</div>

<div class="sect4">

##### XML Servlet Container Initialization

<div class="paragraph">

Our [Spring XML
Configuration](#httpsession-spring-xml-configuration-clientserver)
created a Spring bean named `springSessionRepositoryFilter` that
implements `javax.servlet.Filter` interface. The
`springSessionRepositoryFilter` bean is responsible for replacing the
`javax.servlet.http.HttpSession` with a custom implementation that is
provided by Spring Session and VMware GemFire.

</div>

<div class="paragraph">

In order for our `Filter` to do its magic, we need to instruct Spring to
load our `session-client.xml` configuration file.

</div>

<div class="paragraph">

We do this with the following configuration:

</div>

<div class="listingblock">

<div class="title">

src/main/webapp/WEB-INF/web.xml

</div>

<div class="content">

```highlight
Unresolved directive in guides/xml-gemfire-clientserver.adoc - include::{samples-dir}xml/gemfire-clientserver/src/main/webapp/WEB-INF/web.xml[tags=context-param]
Unresolved directive in guides/xml-gemfire-clientserver.adoc - include::{samples-dir}xml/gemfire-clientserver/src/main/webapp/WEB-INF/web.xml[tags=listeners]
```

</div>

</div>

<div class="paragraph">

The
[ContextLoaderListener](https://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#context-create)
reads the `contextConfigLocation` context parameter value and picks up
our *session-client.xml* configuration file.

</div>

<div class="paragraph">

Finally, we need to ensure that our Servlet container (i.e. Tomcat) uses
our `springSessionRepositoryFilter` for every request.

</div>

<div class="paragraph">

The following snippet performs this last step for us:

</div>

<div class="listingblock">

<div class="title">

src/main/webapp/WEB-INF/web.xml

</div>

<div class="content">

```highlight
Unresolved directive in guides/xml-gemfire-clientserver.adoc - include::{samples-dir}xml/gemfire-clientserver/src/main/webapp/WEB-INF/web.xml[tags=springSessionRepositoryFilter]
```

</div>

</div>

<div class="paragraph">

The
[DelegatingFilterProxy](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/filter/DelegatingFilterProxy.html)
will look up a bean by the name of `springSessionRepositoryFilter` and
cast it to a `Filter`. For every HTTP request, the
`DelegatingFilterProxy` is invoked, which delegates to the
`springSessionRepositoryFilter`.

</div>

</div>

</div>

<div class="sect3">

#### VMware GemFire Peer-To-Peer (P2P)

<div class="paragraph">

A less common approach is to configure your Spring Session application
as a peer member in the VMware GemFire cluster using the
[Peer-To-Peer
(P2P)](https://geode.apache.org/docs/guide/%7Bmaster-data-store-version%7D/topologies_and_comm/p2p_configuration/chapter_overview.html)
topology. In this configuration, the Spring Session application would be
an actual server (or data node) in the VMware GemFire cluster,
and **not** just a cache client as before.

</div>

<div class="paragraph">

One advantage to this approach is the proximity of the application to
the application's state (i.e. its data), and in particular the
`HttpSession` state. However, there are other effective means of
accomplishing similar data dependent computations, such as using
VMware GemFire's [Function
Execution](https://geode.apache.org/docs/guide/%7Bmaster-data-store-version%7D/developing/function_exec/chapter_overview.html).
Any of VMware GemFire's other
[features](https://geode.apache.org/docs/guide/%7Bmaster-data-store-version%7D/getting_started/product_intro.html)
can be used when VMware GemFire is serving as a provider in
Spring Session.

</div>

<div class="paragraph">

The P2P topology is very useful for testing purposes and for smaller,
more focused and self-contained applications, such as those found in a
microservices architecture, and will most certainly improve on your
application's perceived latency and throughput needs.

</div>

<div class="paragraph">

You can configure a Peer-To-Peer (P2P) topology with either:

</div>

<div class="ulist">

- [Java-based Configuration](#httpsession-gemfire-p2p-java)

- [XML-based Configuration](#httpsession-gemfire-p2p-xml)

</div>

<div class="sect4">

##### VMware GemFire Peer-To-Peer (P2P) Java-based Configuration

<div class="paragraph">

This section describes how to configure VMware GemFire's
Peer-To-Peer (P2P) topology to manage `HttpSession` state using
Java-based configuration.

</div>

<div class="admonitionblock note">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Note
</div></td>
<td class="content">The <a href="#samples">HttpSession with
VMware GemFire (P2P)</a> provides a working sample
demonstrating how to integrate Spring Session with
VMware GemFire to manage <code>HttpSession</code> state using
Java configuration. You can read through the basic steps of integration
below, but you are encouraged to follow along in the detailed
<em>`HttpSession` with VMware GemFire (P2P) Guide</em> when
integrating with your own application.</td>
</tr>
</tbody>
</table>

</div>

</div>

<div class="sect4">

##### Spring Java Configuration

<div class="paragraph">

After adding the required dependencies and repository declarations, we
can create the Spring configuration.

</div>

<div class="paragraph">

The Spring configuration is responsible for creating a `Servlet`
`Filter` that replaces the `javax.servlet.http.HttpSession` with an
implementation backed by Spring Session and VMware GemFire.

</div>

<div class="paragraph">

Add the following Spring configuration:

</div>

<div class="listingblock">

<div class="content">

```highlight
Unresolved directive in guides/java-gemfire-p2p.adoc - include::{samples-dir}javaconfig/gemfire-p2p/src/main/java/sample/Config.java[tags=class]
```

</div>

</div>

<div class="colist arabic">

1.  First, we use the `@PeerCacheApplication` annotation to simplify the
    creation of a peer cache instance.

2.  Then, the `Config` class is annotated with
    `@EnableGemFireHttpSession` to create the necessary server-side
    `Region` (by default, "*ClusteredSpringSessions*") used to store
    `HttpSession` state.

</div>

<div class="admonitionblock note">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Note
</div></td>
<td class="content">For more information on configuring Spring Data for
VMware GemFire, refer to the <a
href="https://docs.spring.io/spring-data/geode/docs/current/reference/html">Reference
Guide</a>.</td>
</tr>
</tbody>
</table>

</div>

<div class="paragraph">

The `@EnableGemFireHttpSession` annotation enables developers to
configure certain aspects of both Spring Session and VMware GemFire
out-of-the-box using the following attributes:

</div>

<div class="ulist">

- `clientRegionShortcut` - specifies VMware GemFire [data management
  policy](https://geode.apache.org/docs/guide/%7Bmaster-data-store-version%7D/developing/region_options/region_types.html)
  on the client with the
  [ClientRegionShortcut](https://geode.apache.org/releases/latest/javadoc/org/apache/geode/cache/client/ClientRegionShortcut.html)
  (default is `PROXY`). This attribute is only used when configuring the
  client `Region`.

- `indexableSessionAttributes` - Identifies the Session attributes by
  name that should be indexed for querying purposes. Only Session
  attributes explicitly identified by name will be indexed.

- `maxInactiveIntervalInSeconds` - controls *HttpSession* idle-timeout
  expiration (defaults to **30 minutes**).

- `poolName` - name of the dedicated VMware GemFire `Pool` used to connect
  a client to the cluster of servers. This attribute is only used when
  the application is a cache client. Defaults to `gemfirePool`.

- `regionName` - specifies the name of the VMware GemFire `Region` used to
  store and manage `HttpSession` state (default is
  "**ClusteredSpringSessions**").

- `serverRegionShortcut` - specifies VMware GemFire [data management
  policy](https://geode.apache.org/docs/guide/%7Bmaster-data-store-version%7D/developing/region_options/region_types.html)
  on the server with the
  [RegionShortcut](https://geode.apache.org/releases/latest/javadoc/org/apache/geode/cache/RegionShortcut.html)
  (default is `PARTITION`). This attribute is only used when configuring
  server `Regions`, or when a P2P topology is employed.

</div>

</div>

<div class="sect4">

##### Java Servlet Container Initialization

<div class="paragraph">

Our \<\<\[httpsession-spring-java-configuration-p2p,Spring Java
Configuration\>\> created a Spring bean named
`springSessionRepositoryFilter` that implements `javax.servlet.Filter`.
The `springSessionRepositoryFilter` bean is responsible for replacing
the `javax.servlet.http.HttpSession` with a custom implementation backed
by Spring Session and VMware GemFire.

</div>

<div class="paragraph">

In order for our `Filter` to do its magic, Spring needs to load our
`Config` class. We also need to ensure our Servlet container (i.e.
Tomcat) uses our `springSessionRepositoryFilter` on every HTTP request.

</div>

<div class="paragraph">

Fortunately, Spring Session provides a utility class named
`AbstractHttpSessionApplicationInitializer` to make both steps extremely
easy.

</div>

<div class="paragraph">

You can find an example below:

</div>

<div class="listingblock">

<div class="title">

src/main/java/sample/Initializer.java

</div>

<div class="content">

```highlight
Unresolved directive in guides/java-gemfire-p2p.adoc - include::{samples-dir}javaconfig/gemfire-p2p/src/main/java/sample/Initializer.java[tags=class]
```

</div>

</div>

<div class="admonitionblock note">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Note
</div></td>
<td class="content">The name of our class (<code>Initializer</code>)
does not matter. What is important is that we extend
<code>AbstractHttpSessionApplicationInitializer</code>.</td>
</tr>
</tbody>
</table>

</div>

<div class="colist arabic">

1.  The first step is to extend
    `AbstractHttpSessionApplicationInitializer`. This ensures that a
    Spring bean named `springSessionRepositoryFilter` is registered with
    our Servlet container and used on every HTTP request.

2.  `AbstractHttpSessionApplicationInitializer` also provides a
    mechanism to easily allow Spring to load our `Config` class.

</div>

</div>

<div class="sect4">

##### VMware GemFire Peer-To-Peer (P2P) XML-based Configuration

<div class="paragraph">

This section describes how to configure VMware GemFire's
Peer-To-Peer (P2P) topology to manage `HttpSession` state using
XML-based configuration.

</div>

<div class="admonitionblock note">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Note
</div></td>
<td class="content">The <a href="#samples">HttpSession with
VMware GemFire (P2P) using XML</a> provides a working sample
demonstrating how to integrate Spring Session with
VMware GemFire to manage <code>HttpSession</code> state using
XML configuration. You can read through the basic steps of integration
below, but you are encouraged to follow along in the detailed
<em>`HttpSession` with VMware GemFire (P2P) using XML
Guide</em> when integrating with your own application.</td>
</tr>
</tbody>
</table>

</div>

</div>

<div class="sect4">

##### Spring XML Configuration

<div class="paragraph">

After adding the required dependencies and repository declarations, we
can create the Spring configuration.

</div>

<div class="paragraph">

The Spring configuration is responsible for creating a `Servlet`
`Filter` that replaces the `javax.servlet.http.HttpSession` with an
implementation backed by Spring Session and VMware GemFire.

</div>

<div class="paragraph">

Add the following Spring configuration:

</div>

<div class="listingblock">

<div class="title">

src/main/webapp/WEB-INF/spring/session.xml

</div>

<div class="content">

```highlight
Unresolved directive in guides/xml-gemfire-p2p.adoc - include::{samples-dir}xml/gemfire-p2p/src/main/webapp/WEB-INF/spring/session.xml[tags=beans]
```

</div>

</div>

<div class="colist arabic">

1.  (Optional) First, we can include a `Properties` bean to configure
    certain aspects of the VMware GemFire peer `Cache` using [VMware Tanzu
    GemFire
    Properties](https://geode.apache.org/docs/guide/%7Bmaster-data-store-version%7D/reference/topics/gemfire_properties.html).
    In this case, we are just setting VMware GemFire's "log-level" using
    an application-specific System property, defaulting to "warning" if
    unspecified.

2.  We must configure an VMware GemFire peer `Cache` instance. We
    initialize it with the VMware GemFire properties.

3.  Finally, we enable Spring Session functionality by registering an
    instance of `GemFireHttpSessionConfiguration`.

</div>

<div class="admonitionblock note">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Note
</div></td>
<td class="content">For more information on configuring Spring Data for
VMware GemFire, refer to the <a
href="https://docs.spring.io/spring-data/geode/docs/current/reference/html">Reference
Guide</a>.</td>
</tr>
</tbody>
</table>

</div>

</div>

<div class="sect4">

##### XML Servlet Container Initialization

<div class="paragraph">

The [Spring XML
Configuration](#httpsession-spring-xml-configuration-p2p) created a
Spring bean named `springSessionRepositoryFilter` that implements
`javax.servlet.Filter`. The `springSessionRepositoryFilter` bean is
responsible for replacing the `javax.servlet.http.HttpSession` with a
custom implementation that is backed by Spring Session and VMware GemFire.

</div>

<div class="paragraph">

In order for our `Filter` to do its magic, we need to instruct Spring to
load our `session.xml` configuration file.

</div>

<div class="paragraph">

We do this with the following configuration:

</div>

<div class="listingblock">

<div class="title">

src/main/webapp/WEB-INF/web.xml

</div>

<div class="content">

```highlight
Unresolved directive in guides/xml-gemfire-p2p.adoc - include::{samples-dir}xml/gemfire-p2p/src/main/webapp/WEB-INF/web.xml[tags=context-param]
Unresolved directive in guides/xml-gemfire-p2p.adoc - include::{samples-dir}xml/gemfire-p2p/src/main/webapp/WEB-INF/web.xml[tags=listeners]
```

</div>

</div>

<div class="paragraph">

The
[ContextLoaderListener](https://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#context-create)
reads the `contextConfigLocation` context parameter value and picks up
our *session.xml* configuration file.

</div>

<div class="paragraph">

Finally, we need to ensure that our Servlet container (i.e. Tomcat) uses
our `springSessionRepositoryFilter` for every HTTP request.

</div>

<div class="paragraph">

The following snippet performs this last step for us:

</div>

<div class="listingblock">

<div class="title">

src/main/webapp/WEB-INF/web.xml

</div>

<div class="content">

```highlight
Unresolved directive in guides/xml-gemfire-p2p.adoc - include::{samples-dir}xml/gemfire-p2p/src/main/webapp/WEB-INF/web.xml[tags=springSessionRepositoryFilter]
```

</div>

</div>

<div class="paragraph">

The
[DelegatingFilterProxy](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/filter/DelegatingFilterProxy.html)
will look up a bean by the name of `springSessionRepositoryFilter` and
cast it to a `Filter`. For every HTTP request the
`DelegatingFilterProxy` is invoked, delegating to the
`springSessionRepositoryFilter`.

</div>

</div>

</div>

</div>

<div class="sect2">

### Configuring `HttpSession` Management using VMware GemFire with Properties

<div class="paragraph">

While the `@EnableGemFireHttpSession` annotation is easy to use and
convenient when getting started with Spring Session and
VMware GemFire in your Spring Boot applications, you quickly
run into limitations when migrating from one environment to another, for
example, like when moving from DEV to QA to PROD.

</div>

<div class="paragraph">

With the `@EnableGemFireHttpSession` annotation attributes, it is not
possible to vary the configuration from one environment to another.
Therefore, Spring Session for VMware GemFire introduces
well-known, documented properties for all the
`@EnableGemFireHttpSession` annotation attributes.

</div>

<table class="tableblock frame-all grid-all stretch">
<caption>Table 4. Well-known, documented properties for the
<code>@EnableGemFireHttpSession</code> annotation attributes.</caption>
<colgroup>
<col style="width: 28%" />
<col style="width: 28%" />
<col style="width: 28%" />
<col style="width: 14%" />
</colgroup>
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
<td>Name of the dedicated
Pool used by the client Region storing/accessing Session state.</td>
<td>gemfirePool</td>
</tr>
<tr class="even">
<td>spring.session.data.gemfire.cache.client.region.shortcut</td>
<td><code>EnableGemFireHttpSession.clientRegionShortcut</code></td>
<td>Sets the client Region
data management policy in the client-server topology.</td>
<td>ClientRegionShortcut.PROXY</td>
</tr>
<tr class="odd">
<td>spring.session.data.gemfire.cache.server.region.shortcut</td>
<td><code>EnableGemFireHttpSession.serverRegionShortcut</code></td>
<td>Sets the peer Region
data management policy in the peer-to-peer (P2P) topology.</td>
<td>RegionShortcut.PARTITION</td>
</tr>
<tr class="even">
<td>spring.session.data.gemfire.session.attributes.indexable</td>
<td><code>EnableGemFireHttpSession.indexableSessionAttributes</code></td>
<td>Comma-delimited list of
Session attributes to indexed in the Session Region.</td>
<td></td>
</tr>
<tr class="odd">
<td>spring.session.data.gemfire.session.expiration.bean-name</td>
<td><code>EnableGemFireHttpSession.sessionExpirationPolicyBeanName</code></td>
<td>Name of the bean in the
Spring container implementing the expiration strategy</td>
<td></td>
</tr>
<tr class="even">
<td>spring.session.data.gemfire.session.expiration.max-inactive-interval-seconds</td>
<td><code>EnableGemFireHttpSession.maxInactiveIntervalInSeconds</code></td>
<td>Session expiration
timeout in seconds</td>
<td>1800</td>
</tr>
<tr class="odd">
<td>spring.session.data.gemfire.session.region.name</td>
<td><code>EnableGemFireHttpSession.regionName</code></td>
<td>Name of the client or
peer Region used to store and access Session state.</td>
<td>ClusteredSpringSessions</td>
</tr>
<tr class="even">
<td>spring.session.data.gemfire.session.serializer.bean-name</td>
<td><code>EnableGemFireHttpSession.sessionSerializerBeanName</code></td>
<td>Name of the bean in the
Spring container implementing the serialization strategy</td>
<td>SessionPdxSerializer</td>
</tr>
</tbody>
</table>

Table 4. Well-known, documented properties for the
`@EnableGemFireHttpSession` annotation attributes.

<div class="admonitionblock tip">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Tip
</div></td>
<td class="content">All the properties are documented in the
<code>@EnableGemFireHttpSession</code> annotation attribute Javadoc as
well.</td>
</tr>
</tbody>
</table>

</div>

<div class="paragraph">

Therefore, it is very simple to adjust the configuration of Spring
Session when using VMware GemFire as your provider by using
properties, as follows:

</div>

<div class="listingblock">

<div class="content">

```highlight
@SpringBootApplication
@ClientCacheApplication
@EnableGemFireHttpSession(maxInactiveIntervalInSeconds = 900)
class MySpringSessionApplication {
  // ...
}
```

</div>

</div>

<div class="paragraph">

And then, in `application.properties`:

</div>

<div class="listingblock">

<div class="content">

```highlight
#application.properties
spring.session.data.gemfire.cache.client.region.shortcut=CACHING_PROXY
spring.session.data.gemfire.session.expiration.max-inactive-internval-seconds=3600
```

</div>

</div>

<div class="paragraph">

Any properties explicitly defined override the corresponding
`@EnableGemFireHttpSession` annotation attribute.

</div>

<div class="paragraph">

In the example above, even though the `EnableGemFireHttpSession`
annotation `maxInactiveIntervalInSeconds` attribute was set to `900`
seconds, or 15 minutes, the corresponding attribute property (i.e.
`spring.session.data.gemfire.session.expiration.max-inactive-interval-seconds`)
overrides the value and sets the expiration to `3600` seconds, or 60
minutes.

</div>

<div class="admonitionblock note">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Note
</div></td>
<td class="content">Keep in mind, properties override the annotation
attribute values at runtime.</td>
</tr>
</tbody>
</table>

</div>

<div class="sect3">

#### Properties of Properties

<div class="paragraph">

You can even get more sophisticated and configure your properties with
other properties, as follows:

</div>

<div class="listingblock">

<div class="content">

```highlight
#application.properties
spring.session.data.gemfire.session.expiration.max-inactive-internval-seconds=${app.geode.region.expiration.timeout:3600}
```

</div>

</div>

<div class="paragraph">

Additionally, you could use Spring profiles to vary the expiration
timeout (or other properties) based on environment or your application,
or whatever criteria your application requirements dictate.

</div>

<div class="admonitionblock note">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Note
</div></td>
<td class="content">Property placeholders and nesting is a feature of
the core Spring Framework and not specific to Spring Session or Spring
Session for VMware GemFire.</td>
</tr>
</tbody>
</table>

</div>

</div>

</div>

<div class="sect2">

### Configuring `HttpSession` Management using VMware GemFire with a Configurer

<div class="paragraph">

In addition to properties, Spring Session for VMware GemFire
also allows you to adjust the configuration of Spring Session with
VMware GemFire using the `SpringSessionGemFireConfigurer`
interface. The interface defines a contract containing default methods
for each `@EnableGemFireHttpSession` annotation attribute that can be
overridden to adjust the configuration.

</div>

<div class="paragraph">

The `SpringSessionGemFireConfigurer` is similar in concept to Spring Web
MVC's Configurer interfaces (e.g.
`o.s.web.servlet.config.annotation.WebMvcConfigurer`), which adjusts
various aspects of your Web application's configuration on startup, such
as configuring async support. The advantage of declaring and
implementing a `Configurer` is that it gives you programmatical control
over your configuration. This is useful in situations where you need to
easily express complex, conditional logic that determines whether the
configuration should be applied or not.

</div>

<div class="paragraph">

For example, to adjust the client Region data management policy and
Session expiration timeout as we did previously, use the following:

</div>

<div class="listingblock">

<div class="content">

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

</div>

</div>

<div class="paragraph">

Of course, this example is not very creative. You could most certainly
use more complex logic to determine the configuration of each
configuration attribute.

</div>

<div class="paragraph">

You can be as sophisticated as you like, such as by implementing your
`Configurer` in terms of other properties using Spring's `@Value`
annotation, as follows:

</div>

<div class="listingblock">

<div class="content">

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

</div>

</div>

<div class="admonitionblock tip">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Tip
</div></td>
<td class="content">Spring Boot will resolve <code>@Value</code>
annotation property placeholder values or SpEL Expressions
automatically. However, if you are not using Spring Boot, then you must
explicitly register a static
<code>PropertySourcesPlaceholderConfigurer</code> bean definition.</td>
</tr>
</tbody>
</table>

</div>

<div class="paragraph">

However, you can only declare 1 `SpringSessionGemFireConfigurer` bean in
the Spring container at a time, unless you are also using Spring
profiles or have marked 1 of the multiple
`SpringSessionGemFireConfigurer` beans as primary by using Spring's
`@Primary` context annotation.

</div>

<div class="sect3">

#### Configuration Precedence

<div class="paragraph">

A `SpringSessionGemFireConfigurer` takes precedence over either the
`@EnableGemFireHttpSession` annotation attributes or any of the
well-known and documented Spring Session for VMware GemFire
properties (e.g.
`spring.session.data.gemfire.session.expiration.max-inactive-interval-seconds`)
defined in Spring Boot `application.properties.`

</div>

<div class="paragraph">

If more than 1 configuration approach is employed by your Web
application, the following precedence will apply:

</div>

<div class="olist arabic">

1.  `SpringSessionGemFireConfigurer` "implemented" callback methods

2.  Documented Spring Session for VMware GemFire properties
    (See corresponding `@EnableGemFireHttpSession` annotation attribute
    Javadoc; e.g. `spring.session.data.gemfire.session.region.name`)

3.  `@EnableGemFireHttpSession` annotation attributes

</div>

<div class="paragraph">

Spring Session for VMware GemFire is careful to only apply
configuration from a `SpringSessionGemFireConfigurer` bean declared in
the Spring container for the methods you have actually implemented.

</div>

<div class="paragraph">

In our example above, since you did not implement the `getRegionName()`
method, the name of the VMware GemFire Region managing the
`HttpSession` state will not be determined by the Configurer.

</div>

<div class="sect4">

##### Example

<div class="paragraph">

By way of example, consider the following configuration:

</div>

<div class="listingblock">

<div class="title">

Example Spring Session for VMware GemFire Configuration

</div>

<div class="content">

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

</div>

</div>

<div class="paragraph">

In addition, consider the following Spring Boot `application.properties`
file:

</div>

<div class="olist arabic">

1.  Spring Boot `application.properties`

</div>

<div class="listingblock">

<div class="content">

    spring.session.data.gemfire.session.expiration.max-inactive-interval-seconds = 900
    spring.session.data.gemfire.session.region.name = Sessions

</div>

</div>

<div class="paragraph">

The Session expiration timeout will be 300 seconds, or 5 minutes,
overriding both the property (i.e.
`spring.session.data.gemfire.session.expiration.max-inactive-interval-seconds`)
of 900 seconds, or 15 minutes, as well as the explicit
`@EnableGemFireHttpSession.maxInactiveIntervalInSeconds` annotation
attribute value of 3600 seconds, or 1 hour.

</div>

<div class="paragraph">

Since the "sessionExpirationTimeoutConfigurer" bean does not override
the `getRegionName()` method, the Session Region name will be determined
by the property (i.e.
`spring.session.data.gemfire.session.region.name`), set to "Sessions",
which overrides the implicit `@EnableGemFireHttpSession.regionName`
annotation attribute's default value of "ClusteredSpringSessions".

</div>

<div class="paragraph">

The `@EnableGemFireHttpSession.poolName` annotation attribute's value of
"DEFAULT" will determine the name of the Pool used when sending Region
operations between the client and server to manage Session state on the
server(s) since neither the corresponding property (i.e.
spring.session.data.gemfire.cache.client.pool.name\`) was set nor was
the `SpringSessionGemFireConfigurer.getPoolName()` method overridden by
the "sessionExpirationTimeoutConfigurer" bean.

</div>

<div class="paragraph">

And finally, the client Region used to manage Session state will have a
data management policy of `PROXY`, the default value for the
`@EnableGemFireHttpSession.clientRegionShortcut` annotation attribute,
which was not explicitly set, nor was the corresponding property (i.e.
`spring.session.data.gemfire.cache.client.region.shortcut`) for this
attribute. And, because the
`SpringSessionConfigurer.getClientRegionShortcut()` method was not
overridden, the default value is used.

</div>

</div>

</div>

</div>

<div class="sect2">

### VMware GemFire Expiration

<div class="paragraph">

By default, VMware GemFire is configured with a Region Entry,
Idle Timeout (TTI) Expiration Policy, using an expiration timeout of 30
minutes and INVALIDATE entry as the action. This means when a user's
Session remains inactive (i.e. idle) for more than 30 minutes, the
Session will expire and is invalidated, and the user must begin a new
Session in order to continue to use the application.

</div>

<div class="paragraph">

However, what if you have application specific requirements around
Session state management and expiration, and using the default, Idle
Timeout (TTI) Expiration Policy is insufficient for your Use Case (UC)?

</div>

<div class="paragraph">

Now, Spring Session for VMware GemFire supports application
specific, custom expiration policies. As an application developer, you
may specify custom rules governing the expiration of a Session managed
by Spring Session, backed by VMware GemFire.

</div>

<div class="paragraph">

Spring Session for VMware GemFire provides the new
`SessionExpirationPolicy`
[*Strategy*](https://en.wikipedia.org/wiki/Strategy_pattern) interface.

</div>

<div class="listingblock">

<div class="title">

SessionExpirationPolicy interface

</div>

<div class="content">

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

</div>

</div>

<div class="paragraph">

You implement this interface to specify the Session expiration policies
required by your application and then register the instance as a bean in
the Spring application context.

</div>

<div class="paragraph">

Use the `@EnableGemFireHttpSession` annotation,
`sessionExpirationPolicyBeanName` attribute to configure the name of the
`SessionExpirationPolicy` bean implementing your custom application
policies and rules for Session expiration.

</div>

<div class="paragraph">

For example:

</div>

<div class="listingblock">

<div class="title">

Custom `SessionExpirationPolicy`

</div>

<div class="content">

```highlight
class MySessionExpirationPolicy implements SessionExpirationPolicy {

    public Duration determineExpirationTimeout(Session session) {
        // return a java.time.Duration specifying the length of time until the Session should expire
    }
}
```

</div>

</div>

<div class="paragraph">

Then, in your application class, simple declare the following:

</div>

<div class="listingblock">

<div class="title">

Custom `SessionExpirationPolicy` configuration

</div>

<div class="content">

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

</div>

</div>

<div class="admonitionblock tip">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Tip
</div></td>
<td class="content">Alternatively, the name of the
<code>SessionExpirationPolicy</code> bean can be configured using the
<code>spring.session.data.gemfire.session.expiration.bean-name</code>
property, or by declaring a <code>SpringSessionGemFireConfigurer</code>
bean in the Spring container and overriding the
<code>getSessionExpirationPolicyBeanName()</code> method.</td>
</tr>
</tbody>
</table>

</div>

<div class="paragraph">

You are only required to implement the
`determineExpirationTimeout(:Session):Optional<Duration>` method, which
encapsulates the rules to determine when the Session should expire. The
expiration timeout for a Session is expressed as an `Optional` of
`java.time.Duration`, which specifies the length of time until the
Session expires.

</div>

<div class="paragraph">

The `determineExpirationTimeout` method can be Session specific and may
change with each invocation.

</div>

<div class="paragraph">

Optionally, you may implement the `getAction` method to specify the
action taken when the Session expires. By default, the Region Entry
(i.e. Session) is invalidated. Another option is to destroy the Region
Entry on expiration, which removes both the key (Session ID) and value
(Session). Invalidate only removes the value.

</div>

<div class="admonitionblock note">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Note
</div></td>
<td class="content">Under-the-hood, the
<code>SessionExpirationPolicy</code> is adapted into an instance of the
VMware GemFire <a
href="https://geode.apache.org/releases/latest/javadoc/org/apache/geode/cache/CustomExpiry.html"><code>CustomExpiry</code></a>
interface. This Spring Session <code>CustomExpiry</code> object is then
set as the Session Region's <a
href="https://geode.apache.org/releases/latest/javadoc/org/apache/geode/cache/RegionFactory.html#setCustomEntryIdleTimeout-org.apache.geode.cache.CustomExpiry-">custom
entry idle timeout expiration policy</a>.</td>
</tr>
</tbody>
</table>

</div>

<div class="admonitionblock note">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Note
</div></td>
<td class="content">During expiration determination, the
<code>CustomExpiry.getExpiry(:Region.Entry&lt;String, Session&gt;):ExpirationAttributes</code>
method is invoked for each entry (i.e. Session) in the Region every time
the expiration thread(s) run, which in turn calls our
<code>SessionExpirationPolicy.determineExpirationTimout(:Session):Optional&lt;Duration&gt;</code>
method. The returned <code>java.time.Duration</code> is converted to
seconds and used as the expiration timeout in the <a
href="https://geode.apache.org/releases/latest/javadoc/org/apache/geode/cache/ExpirationAttributes.html"><code>ExpirationAttributes</code></a>
returned from the <a
href="https://geode.apache.org/releases/latest/javadocorg/apache/geode/cache/CustomExpiry.html#getExpiry-org.apache.geode.cache.Region.Entry-"><code>CustomExpiry.getExpiry(..)</code></a>
method invocation.</td>
</tr>
</tbody>
</table>

</div>

<div class="admonitionblock tip">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Tip
</div></td>
<td class="content">VMware GemFire's expiration thread(s) run
once every second, evaluating each entry (i.e. Session) in the Region to
determine if the entry has expired. You can control the number of
expiration threads with the <code>gemfire.EXPIRY_THREADS</code>
property. See the VMware GemFire <a
href="https://geode.apache.org/docs/guide/%7Bmaster-data-store-version%7D/developing/expiration/chapter_overview.html">docs</a>
for more details.</td>
</tr>
</tbody>
</table>

</div>

<div class="sect3">

#### Expiration Timeout Configuration

<div class="paragraph">

If you would like to base the expiration timeout for your custom
`SessionExpirationPolicy` on the `@EnableGemFireHttpSession` annotation,
`maxInactiveIntervalInSeconds` attribute, or alternatively, the
corresponding
`spring.session.data.gemfire.session.expiration.max-inactive-interval-seconds`
property, then your custom `SessionExpirationPolicy` implementation may
also implement the `SessionExpirationTimeoutAware` interface.

</div>

<div class="paragraph">

The `SessionExpirationTimeoutAware` interface is defined as:

</div>

<div class="listingblock">

<div class="title">

SessionExpirationTimeoutAware interface

</div>

<div class="content">

```highlight
interface SessionExpirationTimeoutAware {

    void setExpirationTimeout(Duration expirationTimeout);

}
```

</div>

</div>

<div class="paragraph">

When your custom `SessionExpirationPolicy` implementation also
implements the `SessionExpirationTimeoutAware` interface, then Spring
Session for VMware GemFire will supply your implementation
with the value from the `@EnableGemFireHttpSession` annotation,
`maxInactiveIntervalInSeconds` attribute, or from the
`spring.session.data.gemfire.session.expiration.max-inactive-interval-seconds`
property if set, or from any `SpringSessionGemFireConfigurer` bean
declared in the Spring application context, as an instance of
`java.time.Duration`.

</div>

<div class="paragraph">

If more than 1 configuration option is used, the following order takes
precedence:

</div>

<div class="olist arabic">

1.  `SpringSessionGemFireConfigurer.getMaxInactiveIntervalInSeconds()`

2.  `spring.session.data.gemfire.session.expiration.max-inactive-interval-seconds`
    property

3.  `@EnableGemFireHttpSession` annotation,
    `maxInactiveIntervalInSeconds` attribute

</div>

</div>

<div class="sect3">

#### Fixed Timeout Expiration

<div class="paragraph">

For added convenience, Spring Session for VMware GemFire
provides an implementation of the `SessionExpirationPolicy` interface
for fixed duration expiration (or "*Absolute session timeouts*" as
described in core Spring Session [Issue
\#922](https://github.com/spring-projects/spring-session/issues/922)).

</div>

<div class="paragraph">

It is perhaps necessary, in certain cases, such as for security reasons,
to expire the user's Session after a fixed length of time (e.g. every
hour), regardless if the user's Session is still active.

</div>

<div class="paragraph">

Spring Session for VMware GemFire provides the
`FixedTimeoutSessionExpirationPolicy` implementation out-of-the-box for
this exact Use Case (UC). In addition to handling fixed duration
expiration, it is also careful to still consider and apply the default,
idle expiration timeout.

</div>

<div class="paragraph">

For instance, consider a scenario where a user logs in, beginning a
Session, is active for 10 minutes and then leaves letting the Session
sit idle. If the fixed duration expiration timeout is set for 60
minutes, but the idle expiration timeout is only set for 30 minutes, and
the user does not return, then the Session should expire in 40 minutes
and not 60 minutes when the fixed duration expiration would occur.

</div>

<div class="paragraph">

Conversely, if the user is busy for a full 40 minutes, thereby keeping
the Session active, thus avoiding the 30 minute idle expiration timeout,
and then leaves, then our fixed duration expiration timeout should kick
in and expire the user's Session right at 60 minutes, even though the
user's idle expiration timeout would not occur until 70 minutes in (40
min (active) + 30 min (idle) = 70 minutes).

</div>

<div class="paragraph">

Well, this is exactly what the `FixedTimeoutSessionExpirationPolicy`
does.

</div>

<div class="paragraph">

To configure the `FixedTimeoutSessionExpirationPolicy`, do the
following:

</div>

<div class="listingblock">

<div class="title">

Fixed Duration Expiraton Configuration

</div>

<div class="content">

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

</div>

</div>

<div class="paragraph">

In the example above, the `FixedTimeoutSessionExpirationPolicy` was
declared as a bean in the Spring application context and initialized
with a fixed duration expiration timeout of 60 minutes. As a result, the
users Session will either expire after the idle timeout (which defaults
to 30 minutes) or after the fixed timeout (configured to 60 minutes),
which ever occurs first.

</div>

<div class="admonitionblock tip">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Tip
</div></td>
<td class="content">It is also possible to implement lazy, fixed
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
timeout.</td>
</tr>
</tbody>
</table>

</div>

<div class="admonitionblock caution">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Caution
</div></td>
<td class="content">The
<code>FixedDurationExpirationSessionRepository</code> should not be used
in strict expiration timeout cases, such as when the Session must expire
immediately after the fixed duration expiration timeout has elapsed.
Additionally, unlike the
<code>FixedTimeoutSessionExpirationPolicy</code>, the
<code>FixedDurationExpirationSessionRepository</code> does not take idle
expiration timeout into consideration. That is, it only uses the fixed
duration when determining the expiration timeout for a given
Session.</td>
</tr>
</tbody>
</table>

</div>

</div>

<div class="sect3">

#### `SessionExpirationPolicy` Chaining

<div class="paragraph">

Using the [Composite software design
pattern](https://en.wikipedia.org/wiki/Composite_pattern), you can treat
a group of `SessionExpirationPolicy` instances as a single instance,
functioning as if in a chain much like the chain of Servlet Filters
themselves.

</div>

<div class="paragraph">

The *Composite software design pattern* is a powerful pattern and is
supported by the `SessionExpirationPolicy`, `@FunctionalInterface`,
simply by returning an `Optional` of `java.time.Duration` from the
`determineExpirationTimeout` method.

</div>

<div class="paragraph">

This allows each composed `SessionExpirationPolicy` to "optionally"
return a `Duration` only if the expiration could be determined by this
instance. Alternatively, this instance may punt to the next
`SessionExpirationPolicy` in the composition, or chain until either a
non-empty expiration timeout is returned, or ultimately no expiration
timeout is returned.

</div>

<div class="paragraph">

In fact, this very policy is used internally by the
`FixedTimeoutSessionExpirationPolicy`, which will return
`Optional.empty()` in the case where the idle timeout will occur before
the fixed timeout. By returning no expiration timeout,
VMware GemFire will defer to the default, configured entry
idle timeout expiration policy on the Region managing Session state.

</div>

<div class="admonitionblock note">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Note
</div></td>
<td class="content">This exact behavior is also documented in the <a
href="https://geode.apache.org/releases/latest/javadoc/org/apache/geode/cache/CustomExpiry.html#getExpiry-org.apache.geode.cache.Region.Entry-"><code>org.apache.geode.cache.CustomExpiry.getExpiry(:Region.Entry&lt;String, Session&gt;):ExpirationAttributes</code></a>
method.</td>
</tr>
</tbody>
</table>

</div>

</div>

</div>

<div class="sect2">

### VMware GemFire Serialization

<div class="paragraph">

In order to transfer data between clients and servers, or when data is
distributed and replicated between peer nodes in a cluster, the data
must be serialized. In this case, the data in question is the Session's
state.

</div>

<div class="paragraph">

Anytime a Session is persisted or accessed in a client/server topology,
the Session's state is sent over-the-wire. Typically, a Spring Boot
application with Spring Session enabled will be a client to the
server(s) in a cluster of VMware GemFire nodes.

</div>

<div class="paragraph">

On the server-side, Session state maybe distributed across several
servers (data nodes) in the cluster to replicate the data and guarantee
a high availability of the Session state. When using
VMware GemFire, data can be partitioned, or sharded, and a
redundancy-level can be specified. When the data is distributed for
replication, it must also be serialized to transfer the Session state
among the peer nodes in the cluster.

</div>

<div class="paragraph">

Out-of-the-box, VMware GemFire supports *Java Serialization*.
There are many advantages to *Java Serialization*, such as handling
cycles in the object graph, or being universally supported by any
application written in Java. However, *Java Serialization* is very
verbose and is not the most efficient over-the-wire format.

</div>

<div class="paragraph">

As such, VMware GemFire provides its own serialization
frameworks to serialize Java types:

</div>

<div class="olist arabic">

1.  [Data
    Serialization](https://geode.apache.org/docs/guide/%7Bmaster-data-store-version%7D/developing/data_serialization/gemfire_data_serialization.html)

2.  [PDX
    Serialization](https://geode.apache.org/docs/guide/%7Bmaster-data-store-version%7D/developing/data_serialization/gemfire_pdx_serialization.html)

</div>

<div class="sect3">

#### VMware GemFire Serialization Background

<div class="paragraph">

As mentioned above, VMware GemFire provide 2 additional
serialization frameworks: *Data Serialization* and PDX *Serialization*.

</div>

<div class="sect4">

##### *Data Serialization*

<div class="paragraph">

*Data Serialization* is a very efficient format (i.e. *fast* and
*compact*), with little overhead when compared to *Java Serialization*.

</div>

<div class="paragraph">

It supports [Delta
Propagation](https://geode.apache.org/docs/guide/%7Bmaster-data-store-version%7D/developing/delta_propagation/chapter_overview.html)
by sending only the bits of data that actually changed as opposed to
sending the entire object, which certainly cuts down on the amount of
data sent over the network in addition to reducing the amount of IO when
data is persisted or overflowed to disk.

</div>

<div class="paragraph">

However, *Data Serialization* incurs a CPU penalty anytime data is
transferred over-the-wire, or persisted/overflowed to and accessed from
disk, since the receiving end performs a deserialization. In fact,
anytime *Delta Propagation* is used, the object must be deserialized on
the receiving end in order to apply the "delta".
VMware GemFire applies deltas by invoking a method on the
object that implements the `org.apache.geode.Delta` interface. Clearly,
you cannot invoke a method on a serialized object.

</div>

</div>

<div class="sect4">

##### PDX

<div class="paragraph">

PDX, on the other hand, which stands for *Portable Data Exchange*,
retains the form in which the data was sent. For example, if a client
sends data to a server in PDX format, the server will retain the data as
PDX serialized bytes and store them in the cache `Region` for which the
data access operation was targeted.

</div>

<div class="paragraph">

Additionally, PDX, as the name implies, is "*portable*", meaning it
enables both Java and Native Language Clients, such as C, C++ and C#
clients, to inter-operate on the same data set.

</div>

<div class="paragraph">

PDX even allows OQL queries to be performed on the serialized bytes
without causing the objects to be deserialized first in order to
evaluate the query predicate and execute the query. This can be
accomplished since VMware GemFire maintains a "*Type
Registry*" containing type meta-data for the objects that get serialized
and stored in VMware GemFire using PDX.

</div>

<div class="paragraph">

However, portability does come with a cost, having slightly more
overhead than *Data Serialization*. Still, PDX is far more efficient and
flexible than *Java Serialization*, which stores type meta-data in the
serialized bytes of the object rather than in a separate Type Registry
as in VMware GemFire's case when using PDX.

</div>

<div class="paragraph">

PDX does not support Deltas. Technically, a PDX serializable object can
be used in *Delta Propagation* by implementing the
[`org.apache.geode.Delta`](https://geode.apache.org/releases/latest/javadoc/org/apache/geode/Delta.html)
interface, and only the "delta" will be sent, even in the context of
PDX. But then, the PDX serialized object must be deserialized to apply
the delta. Remember, a method is invoked to apply the delta, which
defeats the purpose of using PDX in the first place.

</div>

<div class="paragraph">

When developing Native Clients (e.g. C) that manage data in a
{data-store-name} cluster, or even when mixing Native Clients with Java
clients, typically there will not be any associated Java types provided
on the classpath of the servers in the cluster. With PDX, it is not
necessary to provide the Java types on the classpath, and many users who
only develop and use Native Clients to access data stored in
{data-store-name} will not provide any Java types for their
corresponding C/C/C# types.

</div>

<div class="paragraph">

VMware GemFire also support JSON serialized to/from PDX. In
this case, it is very likely that Java types will not be provided on the
servers classpath since many different languages (e.g. JavaScript,
Python, Ruby) supporting JSON can be used with VMware GemFire.

</div>

<div class="paragraph">

Still, even with PDX in play, users must take care not to cause a PDX
serialized object on the servers in the cluster to be deserialized.

</div>

<div class="paragraph">

For example, consider an OQL query on an object of the following Java
type serialized as PDX…​

</div>

<div class="listingblock">

<div class="content">

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

</div>

</div>

<div class="paragraph">

Subsequently, if the OQL query invokes a method on a `Person` object,
such as:

</div>

<div class="paragraph">

`SELECT * FROM /People p WHERE p.age >= 21`

</div>

<div class="paragraph">

Then, this is going to cause a PDX serialized `Person` object to be
deserialized since `age` is not a field of `Person`, but rather a method
containing a computation based on another field of `Person` (i.e.
`birthDate`). Likewise, calling any `java.lang.Object` method in a OQL
query, like `Object.toString()`, is going to cause a deserialization to
happen as well.

</div>

<div class="paragraph">

VMware GemFire does provide the
[`read-serialized`](https://geode.apache.org/releases/latest/javadoc/org/apache/geode/cache/client/ClientCacheFactory.html#setPdxReadSerialized-boolean-)
configuration setting so that any cache `Region.get(key)` operations
that are potentially invoked inside a `Function` does not cause PDX
serialized objects to be deserialized. But, nothing will prevent an
ill-conceived OQL query from causing a deserialization, so be careful.

</div>

</div>

<div class="sect4">

##### *Data Serialization* + PDX + *Java Serialization*

<div class="paragraph">

It is possible for VMware GemFire to support all 3
serialization formats simultaneously.

</div>

<div class="paragraph">

For instance, your application domain model might contain objects that
implement the `java.io.Serialiable` interface, and you may be using a
combination of the *Data Serialization* framework along with PDX.

</div>

<div class="admonitionblock tip">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Tip
</div></td>
<td class="content">While using <em>Java Serialization</em> with
<em>Data Serialization</em> and PDX is possible, it is generally
preferable and recommended to use 1 serialization strategy.</td>
</tr>
</tbody>
</table>

</div>

<div class="admonitionblock note">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Note
</div></td>
<td class="content">Unlike <em>Java Serialization</em>, <em>Data
Serialization</em> and PDX <em>Serialization</em> do not handle object
graph cycles.</td>
</tr>
</tbody>
</table>

</div>

<div class="paragraph">

More background on VMware GemFire's serialization mechanics
can be found
[here](https://geode.apache.org/docs/guide/%7Bmaster-data-store-version%7D/developing/data_serialization/data_serialization_options.html).

</div>

</div>

</div>

<div class="sect3">

#### Serialization with Spring Session

<div class="paragraph">

Previously, Spring Session for VMware GemFire only supported
VMware GemFire *Data Serialization* format. The main
motivation behind this was to take advantage of
VMware GemFire's *Delta Propagation* functionality since a
Session's state can be arbitrarily large.

</div>

<div class="paragraph">

However, as of Spring Session for VMware GemFire 2.0, PDX is
also supported and is now the new, default serialization option. The
default was changed to PDX in Spring Session for
VMware GemFire 2.0 primarily because PDX is the most widely
used and requested format by users.

</div>

<div class="paragraph">

PDX is certainly the most flexible format, so much so that you do not
even need Spring Session for VMware GemFire or any of its
transitive dependencies on the classpath of the servers in the cluster
to use Spring Session with VMware GemFire. In fact, with PDX,
you do not even need to put your application domain object types stored
in the (HTTP) Session on the servers' classpath either.

</div>

<div class="paragraph">

Essentially, when using PDX serialization, VMware GemFire does
not require the associated Java types to be present on the servers'
classpath. So long as no deserialization happens on the servers in the
cluster, you are safe.

</div>

<div class="paragraph">

The `@EnableGemFireHttpSession` annotation introduces the **new**
`sessionSerializerBeanName` attribute that a user can use to configure
the name of a bean declared and registered in the Spring container
implementing the desired serialization strategy. The serialization
strategy is used by Spring Session for VMware GemFire to
serialize the Session state.

</div>

<div class="paragraph">

Out-of-the-box, Spring Session for VMware GemFire provides 2
serialization strategies: 1 for PDX and 1 for *Data Serialization*. It
automatically registers both serialization strategy beans in the Spring
container. However, only 1 of those strategies is actually used at
runtime, PDX!

</div>

<div class="paragraph">

The 2 beans registered in the Spring container implementing *Data
Serialization* and PDX are named `SessionDataSerializer` and
`SessionPdxSerializer`, respectively. By default, the
`sessionSerializerBeanName` attribute is set to `SessionPdxSerializer`,
as if the user annotated his/her Spring Boot, Spring Session enabled
application configuration class with:

</div>

<div class="listingblock">

<div class="content">

```highlight
@SpringBootApplication
@EnableGemFireHttpSession(sessionSerializerBeanName = "SessionPdxSerializer")
class MySpringSessionApplication {  }
```

</div>

</div>

<div class="paragraph">

It is a simple matter to change the serialization strategy to *Data
Serialization* instead by setting the `sessionSerializerBeanName`
attribute to `SessionDataSerializer`, as follows:

</div>

<div class="listingblock">

<div class="content">

```highlight
@SpringBootApplication
@EnableGemFireHttpSession(sessionSerializerBeanName = "SessionDataSerializer")
class MySpringSessionApplication {  }
```

</div>

</div>

<div class="paragraph">

Since these 2 values are so common, Spring Session for
VMware GemFire provides constants for each value in the
`GemFireHttpSessionConfiguration` class:
`GemFireHttpSessionConfiguration.SESSION_PDX_SERIALIZER_BEAN_NAME` and
`GemFireHttpSessionConfiguration.SESSION_DATA_SERIALIZER_BEAN_NAME`. So,
you could explicitly configure PDX, as follows:

</div>

<div class="listingblock">

<div class="content">

```highlight
import org.springframework.session.data.geode.config.annotation.web.http.GemFireHttpSessionConfiguration;

@SpringBootApplication
@EnableGemFireHttpSession(sessionSerializerBeanName = GemFireHttpSessionConfiguration.SESSION_PDX_SERIALIZER_BEAN_NAME)
class MySpringSessionApplication {  }
```

</div>

</div>

<div class="paragraph">

With 1 attribute and 2 provided bean definitions out-of-the-box, you can
specify which Serialization framework you wish to use with your Spring
Boot, Spring Session enabled application backed by
VMware GemFire.

</div>

</div>

<div class="sect3">

#### Spring Session for VMware GemFire Serialization Framework

<div class="paragraph">

To abstract away the details of VMware GemFire's *Data
Serialization* and PDX *Serialization* frameworks, Spring Session for
VMware GemFire provides its own Serialization framework
(facade) wrapping VMware GemFire's Serialization frameworks.

</div>

<div class="paragraph">

The Serialization API exists under the
`org.springframework.session.data.gemfire.serialization` package. The
primary interface in this API is the
`org.springframework.session.data.gemfire.serialization.SessionSerializer`.

</div>

<div class="paragraph">

The interface is defined as:

</div>

<div class="listingblock">

<div class="title">

Spring Session `SessionSerializer` interface

</div>

<div class="content">

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

</div>

</div>

<div class="paragraph">

Basically, the interface allows you to serialize and deserialize a
Spring `Session` object.

</div>

<div class="paragraph">

The `IN` and `OUT` type parameters and corresponding method arguments of
those types provide reference to the objects responsible for writing the
`Session` to a stream of bytes or reading the `Session` from a stream of
bytes. The actual arguments will be type specific, based on the
underlying VMware GemFire Serialization strategy configured.

</div>

<div class="paragraph">

For instance, when using VMware GemFire's PDX *Serialization*
framework, `IN` and `OUT` will be instances of
`org.apache.geode.pdx.PdxReader` and `org.apache.geode.pdx.PdxWriter`,
respectively. When VMware GemFire's *Data Serialization*
framework has been configured, then `IN` and `OUT` will be instances of
`java.io.DataInput` and `java.io.DataOutput`, respectively.

</div>

<div class="paragraph">

These arguments are provided to the `SessionSerializer` implementation
by the framework automatically, and as previously mentioned, is based on
the underlying VMware GemFire Serialization strategy
configured.

</div>

<div class="paragraph">

Essentially, even though Spring Session for VMware GemFire
provides a facade around VMware GemFire's Serialization
frameworks, under-the-hood VMware GemFire still expects one of
these Serialization frameworks is being used to serialize data to/from
VMware GemFire.

</div>

<div class="paragraph">

*So what purpose does the `SessionSerializer` interface really serve
then?*

</div>

<div class="paragraph">

Effectively, it allows a user to customize what aspects of the Session's
state actually gets serialized and stored in VMware GemFire.
Application developers can provide their own custom,
application-specific `SessionSerializer` implementation, register it as
a bean in the Spring container, and then configure it to be used by
Spring Session for VMware GemFire to serialize the Session
state, as follows:

</div>

<div class="listingblock">

<div class="content">

```highlight
@EnableGemFireHttpSession(sessionSerializerBeanName = "MyCustomSessionSerializer")
class MySpringSessionDataGemFireApplication {

  @Bean("MyCustomSessionSerializer")
  SessionSerializer<Session, ?, ?> myCustomSessionSerializer() {
    // ...
  }
}
```

</div>

</div>

<div class="sect4">

##### Implementing a SessionSerializer

<div class="paragraph">

Spring Session for VMware GemFire provides assistance when a
user wants to implement a custom `SessionSerializer` that fits into one
of VMware GemFire's Serialization frameworks.

</div>

<div class="paragraph">

If the user just implements the
`org.springframework.session.data.gemfire.serialization.SessionSerializer`
interface directly without extending from one of Spring Session for
VMware GemFire's provided abstract base classes, related to 1
of VMware GemFire's Serialization frameworks , then Spring
Session for VMware GemFire will wrap the user's custom
`SessionSerializer` implementation in an instance of
`org.springframework.session.data.gemfire.serialization.pdx.support.PdxSerializerSessionSerializerAdapter`
and register it with VMware GemFire as a
`org.apache.geode.pdx.PdxSerializer`.

</div>

<div class="paragraph">

Spring Session for VMware GemFire is careful not to stomp on
any existing `PdxSerializer` implementation that a user may have already
registered with VMware GemFire by some other means. Indeed,
several different, provided implementations of
VMware GemFire's `org.apache.geode.pdx.PdxSerializer`
interface exists:

</div>

<div class="ulist">

- VMware GemFire itself provides the
  [`org.apache.geode.pdx.ReflectionBasedAutoSerializer`](https://geode.apache.org/releases/latest/javadoc/org/apache/geode/pdx/ReflectionBasedAutoSerializer.html).

- Spring Data for VMware GemFire (SDG) provides the
  [`org.springframework.data.gemfire.mapping.MappingPdxSerializer`](https://docs.spring.io/spring-data/geode/docs/current/api/org/springframework/data/gemfire/mapping/MappingPdxSerializer.html),
  which is used in the SD *Repository* abstraction and SDG's extension
  to handle mapping PDX serialized types to the application domain
  object types defined in the application *Repository* interfaces.

</div>

<div class="paragraph">

This is accomplished by obtaining any currently registered
`PdxSerializer` instance on the cache and composing it with the
`PdxSerializerSessionSerializerAdapter` wrapping the user's custom
application `SessionSerializer` implementation and re-registering this
"*composite*" `PdxSerializer` on the VMware GemFire cache. The
"*composite*" `PdxSerializer` implementation is provided by Spring
Session for VMware GemFire's
`org.springframework.session.data.gemfire.pdx.support.ComposablePdxSerializer`
class when entities are stored in VMware GemFire as PDX.

</div>

<div class="paragraph">

If no other `PdxSerializer` was currently registered with the
VMware GemFire cache, then the adapter is simply registered.

</div>

<div class="paragraph">

Of course, you are allowed to force the underlying
VMware GemFire Serialization strategy used with a custom
`SessionSerializer` implementation by doing 1 of the following:

</div>

<div class="olist arabic">

1.  The custom `SessionSerializer` implementation can implement
    VMware GemFire's `org.apache.geode.pdx.PdxSerializer`
    interface, or for convenience, extend Spring Session for
    VMware GemFire's
    `org.springframework.session.data.gemfire.serialization.pdx.AbstractPdxSerializableSessionSerializer`
    class and Spring Session for VMware GemFire will register
    the custom `SessionSerializer` as a `PdxSerializer` with
    VMware GemFire.

2.  The custom `SessionSerializer` implementation can extend the
    VMware GemFire's `org.apache.geode.DataSerializer` class,
    or for convenience, extend Spring Session for
    VMware GemFire's
    `org.springframework.session.data.gemfire.serialization.data.AbstractDataSerializableSessionSerializer`
    class and Spring Session for VMware GemFire will register
    the custom `SessionSerializer` as a `DataSerializer` with
    VMware GemFire.

3.  Finally, a user can create a custom `SessionSerializer`
    implementation as before, not specifying which
    VMware GemFire Serialization framework to use because the
    custom `SessionSeriaizer` implementation does not implement any
    VMware GemFire serialization interfaces or extend from any
    of Spring Session for VMware GemFire's provided abstract
    base classes, and still have it registered in
    VMware GemFire as a `DataSerializer` by declaring an
    additional Spring Session for VMware GemFire bean in the
    Spring container of type
    `org.springframework.session.data.gemfire.serialization.data.support.DataSerializerSessionSerializerAdapter`,
    like so…​

</div>

<div class="listingblock">

<div class="title">

Forcing the registration of a custom SessionSerializer as a
DataSerializer in VMware GemFire

</div>

<div class="content">

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

</div>

</div>

<div class="paragraph">

Just by the very presence of the
`DataSerializerSessionSerializerAdapter` registered as a bean in the
Spring container, any neutral custom `SessionSerializer` implementation
will be treated and registered as a `DataSerializer` in
VMware GemFire.

</div>

</div>

<div class="sect4">

##### Additional Support for Data Serialization

<div class="admonitionblock note">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Note
</div></td>
<td class="content">Please feel free to skip this section if you are
configuring and bootstraping VMware GemFire servers in your
cluster using Spring (Boot) since generally, the information that
follows will not apply. Of course, it all depends on your declared
dependencies and your Spring configuration. However, if you are using
<strong><em>Gfsh</em></strong> to start the servers in your cluster,
then definitely read on.</td>
</tr>
</tbody>
</table>

</div>

<div class="sect5">

###### Background

<div class="paragraph">

When using VMware GemFire's *DataSerialization* framework,
especially from the client when serializing (HTTP) Session state to the
servers in the cluster, you must take care to configure the
VMware GemFire servers in your cluster with the appropriate
dependencies. This is especially true when leveraging deltas as
explained in the earlier section on [*Data
Serialization*](#httpsession-gemfire-serialization-data).

</div>

<div class="paragraph">

When using the *DataSerialization* framework as your serialization
strategy to serialize (HTTP) Session state from your Web application
clients to the servers, then the servers must be properly configured
with the Spring Session for VMware GemFire class types used to
represent the (HTTP) Session and its contents. This means including the
Spring JARs on the servers classpath.

</div>

<div class="paragraph">

Additionally, using *DataSerialization* may also require you to include
the JARs containing your application domain classes that are used by
your Web application and put into the (HTTP) Session as Session
Attribute values, particularly if:

</div>

<div class="olist arabic">

1.  Your types implement the `org.apache.geode.DataSerializable`
    interface.

2.  Your types implement the `org.apache.geode.Delta` interface.

3.  You have registered a `org.apache.geode.DataSerializer` that
    identifies and serializes the types.

4.  Your types implement the `java.io.Serializable` interface.

</div>

<div class="paragraph">

Of course, you must ensure your application domain object types put in
the (HTTP) Session are serializable in some form or another. However,
you are not strictly required to use *DataSerialization* nor are you
necessarily required to have your application domain object types on the
servers classpath if:

</div>

<div class="olist arabic">

1.  Your types implement the `org.apache.geode.pdx.PdxSerializable`
    interface.

2.  Or, you have registered an `org.apache.geode.pdx.PdxSerializer` that
    properly identifies and serializes your application domain object
    types.

</div>

<div class="paragraph">

VMware GemFire will apply the following order of precedence
when determining the serialization strategy to use to serialize an
object graph:

</div>

<div class="olist arabic">

1.  First, `DataSerializable` objects and/or any registered
    `DataSerializers` identifying the objects to serialize.

2.  Then `PdxSerializable` objects and/or any registered `PdxSerializer`
    identifying the objects to serialize.

3.  And finally, all `java.io.Serializable` types.

</div>

<div class="paragraph">

This also means that if a particular application domain object type
(e.g. `A`) implements `java.io.Serializable`, however, a (custom)
`PdxSerializer` has been registered with VMware GemFire
identifying the same application domain object type (i.e. `A`), then
VMware GemFire will use PDX to serialize "A" and **not** Java
Serialization, in this case.

</div>

<div class="paragraph">

This is especially useful since then you can use *DataSerialization* to
serialize the (HTTP) Session object, leveraging Deltas and all the
powerful features of *DataSerialization*, but then use PDX to serialize
your application domain object types, which greatly simplifies the
configuration and/or effort involved.

</div>

<div class="paragraph">

Now that we have a general understanding of why this support exists, how
do you enable it?

</div>

</div>

<div class="sect5">

###### Configuration

<div class="paragraph">

First, create an VMware GemFire `cache.xml`, as follows:

</div>

<div class="listingblock">

<div class="title">

VMware GemFire `cache.xml` configuration

</div>

<div class="content">

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

</div>

</div>

<div class="paragraph">

Then, start your servers in *\*Gfsh\** using:

</div>

<div class="listingblock">

<div class="title">

Starting Server with Gfsh

</div>

<div class="content">

```highlight
gfsh> start server --name=InitializedServer --cache-xml-file=/path/to/cache.xml --classpath=...
```

</div>

</div>

<div class="paragraph">

Configuring the VMware GemFire server `classpath` with the
appropriate dependencies is the tricky part, but generally, the
following should work:

</div>

<div class="listingblock">

<div class="title">

CLASSPATH configuration

</div>

<div class="content">

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

</div>

</div>

<div class="paragraph">

Keep in mind, you may need to add your application domain object JAR
files to the server classpath as well.

</div>

<div class="paragraph">

To get a complete picture of how this works, see the
<a href="https://github.com/spring-projects/spring-session-data-geode/tree/2.7.1/samples/boot/gemfire">-with-gfsh-servers\[sample\].

</div>

</div>

</div>

<div class="sect4">

##### Customizing Change Detection

<div class="paragraph">

By default, anytime the Session is modified (e.g. the `lastAccessedTime`
is updated to the current time), the Session is considered dirty by
Spring Session for VMware GemFire (SSDG). When using
VMware GemFire *Data Serialization* framework, it is extremely
useful and valuable to take advantage of VMware GemFire's
[Delta
Propagation](https://geode.apache.org/docs/guide/%7Bmaster-data-store-version%7D/developing/delta_propagation/chapter_overview.html)
capabilities as well.

</div>

<div class="paragraph">

When using *Data Serialization*, SSDG also uses *Delta Propagation* to
send only changes to the Session state between the client and server.
This includes any Session attributes that may have been added, removed
or updated.

</div>

<div class="paragraph">

By default, anytime `Session.setAttribute(name, value)` is called, the
Session attribute is considered "dirty" and will be sent in the delta
between the client and server. This is true even if your application
domain object has not been changed.

</div>

<div class="paragraph">

Typically, there is never a reason to call `Session.setAttribute(..)`
unless your object has been changed. However, if this can occur, and
your objects are relatively large (with a complex object hierarchy),
then you may want to consider either:

</div>

<div class="olist arabic">

1.  Implementing the
    [Delta](https://geode.apache.org/releases/latest/javadoc/org/apache/geode/Delta.html)
    interface in your application domain object model, while useful, is
    very invasive, or…​

2.  Provide a custom implementation of SSDG's
    `org.springframework.session.data.gemfire.support.IsDirtyPredicate`
    strategy interface.

</div>

<div class="paragraph">

Out of the box, SSDG provides 5 implementations of the
`IsDirtyPredicate` strategy interface:

</div>

<table class="tableblock frame-all grid-all stretch">
<caption>Table 5. <code>IsDirtyPredicate</code>
implementations</caption>
<colgroup>
<col style="width: 28%" />
<col style="width: 57%" />
<col style="width: 14%" />
</colgroup>
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
<td>New Session attribute
values are always considered dirty.</td>
<td></td>
</tr>
<tr class="even">
<td><code>IsDirtyPredicate.NEVER_DIRTY</code></td>
<td>New Session attribute
values are never considered dirty.</td>
<td></td>
</tr>
<tr class="odd">
<td><code>DeltaAwareDirtyPredicate</code></td>
<td>New Session attribute
values are considered dirty when the old value and new value are
different, if the new value's type does not implement <code>Delta</code>
or the new value's <code>Delta.hasDelta()</code> method returns
<strong>true</strong>.</td>
<td>Yes</td>
</tr>
<tr class="even">
<td><code>EqualsDirtyPredicate</code></td>
<td>New Session attribute
values are considered dirty iff the old value is not equal to the new
value as determined by <code>Object.equals(:Object)</code>
method.</td>
<td></td>
</tr>
<tr class="odd">
<td><code>IdentityEqualsPredicate</code></td>
<td>New Session attributes
values are considered dirty iff the old value is not the same as the new
value using the identity equals operator (i.e.
<code>oldValue != newValue</code>).</td>
<td></td>
</tr>
</tbody>
</table>

Table 5. `IsDirtyPredicate` implementations

<div class="paragraph">

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

</div>

<div class="paragraph">

You can change SSDG's dirty implementation, determination strategy
simply by declaring a bean in the Spring container of the
`IsDirtyPredicate` interface type:

</div>

<div class="listingblock">

<div class="title">

Overriding SSDG's default `IsDirtyPredicate` strategy

</div>

<div class="content">

```highlight
@EnableGemFireHttpSession
class ApplicationConfiguration {

  @Bean
  IsDirtyPredicate equalsDirtyPredicate() {
    return EqualsDirtyPredicate.INSTANCE;
  }
}
```

</div>

</div>

<div class="sect5">

###### Composition

<div class="paragraph">

The `IsDirtyPredicate` interface also provides the
`andThen(:IsDirtyPredicate)` and `orThen(:IsDirtyPredicate)` methods to
compose 2 or more `IsDirtyPredicate` implementations in a composition in
order to organize complex logic and rules for determining whether an
application domain object is dirty or not.

</div>

<div class="paragraph">

For instance, you could compose both `EqualsDirtyPredicate` and
`DeltaAwareDirtyPredicate` using the OR operator:

</div>

<div class="listingblock">

<div class="title">

Composing `EqualsDirtyPredicate` with `DeltaAwareDirtyPredicate` using
the logical OR operator

</div>

<div class="content">

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

</div>

</div>

<div class="paragraph">

You may even implement your own, custom `IsDirtyPredicates` based on
specific application domain object types:

</div>

<div class="listingblock">

<div class="title">

Application Domain Object Type-specific `IsDirtyPredicate`
implementations

</div>

<div class="content">

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

</div>

</div>

<div class="paragraph">

Then combine `CustomerDirtyPredicate` with the `AccountDirtyPredicate`
and a default predicate for fallback, as follows:

</div>

<div class="listingblock">

<div class="title">

Composed and configured type-specific `IsDirtyPredicates`

</div>

<div class="content">

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

</div>

</div>

<div class="paragraph">

The combinations and possibilities are endless.

</div>

<div class="admonitionblock warning">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Warning
</div></td>
<td class="content">Use caution when implementing custom
<code>IsDirtyPredicate</code> strategies. If you incorrectly determine
that your application domain object is not dirty when it actually is,
then it will not be sent in the Session delta from the client to the
server.</td>
</tr>
</tbody>
</table>

</div>

</div>

</div>

<div class="sect4">

##### Changing the Session Representation

<div class="paragraph">

Internally, Spring Session for VMware GemFire maintains 2
representations of the (HTTP) Session and the Session's attributes. Each
representation is based on whether VMware GemFire "*Deltas*"
are supported or not.

</div>

<div class="paragraph">

VMware GemFire *Delta Propagation* is only enabled by Spring
Session for VMware GemFire when using *Data Serialization* for
reasons that were discussed
[earlier](#httpsession-gemfire-serialization-pdx).

</div>

<div class="paragraph">

Effectively, the strategy is:

</div>

<div class="olist arabic">

1.  If VMware GemFire *Data Serialization* is configured, then
    *Deltas* are supported and the `DeltaCapableGemFireSession` and
    `DeltaCapableGemFireSessionAttributes` representations are used.

2.  If VMware GemFire PDX *Serialization* is configured, then
    *Delta Propagation* will be disabled and the `GemFireSession` and
    `GemFireSessionAttributes` representations are used.

</div>

<div class="paragraph">

It is possible to override these internal representations used by Spring
Session for VMware GemFire, and for users to provide their own
Session related types. The only strict requirement is that the Session
implementation must implement the core Spring Session
`org.springframework.session.Session` interface.

</div>

<div class="paragraph">

By way of example, let's say you want to define your own Session
implementation.

</div>

<div class="paragraph">

First, you define the `Session` type. Perhaps your custom `Session` type
even encapsulates and handles the Session attributes without having to
define a separate type.

</div>

<div class="listingblock">

<div class="title">

User-defined Session interface implementation

</div>

<div class="content">

```highlight
class MySession implements org.springframework.session.Session {
  // ...
}
```

</div>

</div>

<div class="paragraph">

Then, you would need to extend the
`org.springframework.session.data.gemfire.GemFireOperationsSessionRepository`
class and override the `createSession()` method to create instances of
your custom `Session` implementation class.

</div>

<div class="listingblock">

<div class="title">

Custom SessionRepository implementation creating and returning instances
of the custom Session type

</div>

<div class="content">

```highlight
class MySessionRepository extends GemFireOperationsSessionRepository {

  @Override
  public Session createSession() {
    return new MySession();
  }
}
```

</div>

</div>

<div class="paragraph">

If you provide your own custom `SessionSerializer` implementation and
VMware GemFire PDX *Serialization* is configured, then you
done.

</div>

<div class="paragraph">

However, if you configured VMware GemFire *Data Serialization*
then you must additionally provide a custom implementation of the
`SessionSerializer` interface and either have it directly extend
VMware GemFire's `org.apache.geode.DataSerializer` class, or
extend Spring Session for VMware GemFire's
`org.springframework.session.data.gemfire.serialization.data.AbstractDataSerializableSessionSerializer`
class and override the `getSupportedClasses():Class<?>[]` method.

</div>

<div class="paragraph">

For example:

</div>

<div class="listingblock">

<div class="title">

Custom SessionSerializer for custom Session type

</div>

<div class="content">

```highlight
class MySessionSerializer extends AbstractDataSerializableSessionSerializer {

  @Override
  public Class<?>[] getSupportedClasses() {
    return new Class[] { MySession.class };
  }
}
```

</div>

</div>

<div class="paragraph">

Unfortunately, `getSupportedClasses()` cannot return the generic Spring
Session `org.springframework.session.Session` interface type. If it
could then we could avoid the explicit need to override the
`getSupportedClasses()` method on the custom `DataSerializer`
implementation. But, VMware GemFire's *Data Serialization*
framework can only match on exact class types since it incorrectly and
internally stores and refers to the class type by name, which then
requires the user to override and implement the `getSupportedClasses()`
method.

</div>

</div>

</div>

</div>

<div class="sect2">

### How HttpSession Integration Works

<div class="paragraph">

Fortunately, both `javax.servlet.http.HttpSession` and
`javax.servlet.http.HttpServletRequest` (the API for obtaining an
`HttpSession`) are interfaces. This means we can provide our own
implementations for each of these APIs.

</div>

<div class="admonitionblock note">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Note
</div></td>
<td class="content">This section describes how Spring Session provides
transparent integration with
<code>javax.servlet.http.HttpSession</code>. The intent is so users
understand what is happening under-the-hood. This functionality is
already implemented and integrated so you do not need to implement this
logic yourself.</td>
</tr>
</tbody>
</table>

</div>

<div class="paragraph">

First, we create a custom `javax.servlet.http.HttpServletRequest` that
returns a custom implementation of `javax.servlet.http.HttpSession`. It
looks something like the following:

</div>

<div class="listingblock">

<div class="content">

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

</div>

</div>

<div class="paragraph">

Any method that returns an `javax.servlet.http.HttpSession` is
overridden. All other methods are implemented by
`javax.servlet.http.HttpServletRequestWrapper` and simply delegate to
the original `javax.servlet.http.HttpServletRequest` implementation.

</div>

<div class="paragraph">

We replace the `javax.servlet.http.HttpServletRequest` implementation
using a Servlet `Filter` called `SessionRepositoryFilter`. The
pseudocode can be found below:

</div>

<div class="listingblock">

<div class="content">

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

</div>

</div>

<div class="paragraph">

By passing in a custom `javax.servlet.http.HttpServletRequest`
implementation into the `FilterChain` we ensure that anything invoked
after our `Filter` uses the custom `javax.servlet.http.HttpSession`
implementation.

</div>

<div class="paragraph">

This highlights why it is important that Spring Session's
`SessionRepositoryFilter` must be placed before anything that interacts
with the `javax.servlet.http.HttpSession`.

</div>

</div>

<div class="sect2">

### HttpSessionListener

<div class="paragraph">

Spring Session supports `HttpSessionListener` by translating
`SessionCreatedEvent` and `SessionDestroyedEvent` into
`HttpSessionEvent` by declaring
`SessionEventHttpSessionListenerAdapter`.

</div>

<div class="paragraph">

To use this support, you need to:

</div>

<div class="ulist">

- Ensure your `SessionRepository` implementation supports and is
  configured to fire `SessionCreatedEvent` and\`SessionDestroyedEvent\`.

- Configure `SessionEventHttpSessionListenerAdapter` as a Spring bean.

- Inject every `HttpSessionListener` into the
  `SessionEventHttpSessionListenerAdapter`

</div>

<div class="paragraph">

If you are using the configuration support documented in [HttpSession
with VMware GemFire](#httpsession-gemfire), then all you need
to do is register every `HttpSessionListener` as a bean.

</div>

<div class="paragraph">

For example, assume you want to support Spring Security's concurrency
control and need to use `HttpSessionEventPublisher`, then you can simply
add `HttpSessionEventPublisher` as a bean.

</div>

</div>

<div class="sect2">

### Session

<div class="paragraph">

A `Session` is a simplified `Map` of key/value pairs with support for
expiration.

</div>

</div>

<div class="sect2">

### SessionRepository

<div class="paragraph">

A `SessionRepository` is in charge of creating, persisting and accessing
`Session` instances and state.

</div>

<div class="paragraph">

If possible, developers should not interact directly with a
`SessionRepository` or a `Session`. Instead, developers should prefer to
interact with `SessionRepository` and `Session` indirectly through the
`javax.servlet.http.HttpSession`, `WebSocket` and `WebSession`
integration.

</div>

</div>

<div class="sect2">

### FindByIndexNameSessionRepository

<div class="paragraph">

Spring Session's most basic API for using a `Session` is the
`SessionRepository`. The API is intentionally very simple so that it is
easy to provide additional implementations with basic functionality.

</div>

<div class="paragraph">

Some `SessionRepository` implementations may choose to implement
`FindByIndexNameSessionRepository` also. For example, Spring Session's
for VMware GemFire support implements
`FindByIndexNameSessionRepository`.

</div>

<div class="paragraph">

The `FindByIndexNameSessionRepository` adds a single method to look up
all the sessions for a particular user. This is done by ensuring that
the session attribute with the name
`FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME` is
populated with the username. It is the responsibility of the developer
to ensure the attribute is populated since Spring Session is not aware
of the authentication mechanism being used.

</div>

<div class="admonitionblock note">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Note
</div></td>
<td class="content"><div class="paragraph">
Some implementations of <code>FindByIndexNameSessionRepository</code>
will provide hooks to automatically index other session attributes. For
example, many implementations will automatically ensure the current
Spring Security user name is indexed with the index name
<code>FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME</code>.
</div></td>
</tr>
</tbody>
</table>

</div>

</div>

<div class="sect2">

### EnableSpringHttpSession

<div class="paragraph">

The `@EnableSpringHttpSession` annotation can be added to any
`@Configuration` class to expose the `SessionRepositoryFilter` as a bean
in the Spring container named, "springSessionRepositoryFilter".

</div>

<div class="paragraph">

In order to leverage the annotation, a single `SessionRepository` bean
must be provided.

</div>

</div>

<div class="sect2">

### EnableGemFireHttpSession

<div class="paragraph">

The `@EnableGemFireHttpSession` annotation can be added to any
`@Configuration` class in place of the `@EnableSpringHttpSession`
annotation to expose the `SessionRepositoryFilter` as a bean in the
Spring container named, "springSessionRepositoryFilter" and to position
VMware GemFire as a provider managing
`javax.servlet.http.HttpSession` state.

</div>

<div class="paragraph">

When using the `@EnableGemFireHttpSession` annotation, additional
configuration is imported out-of-the-box that also provides a
VMware GemFire specific implementation of the
`SessionRepository` interface named,
`GemFireOperationsSessionRepository`.

</div>

</div>

<div class="sect2">

### GemFireOperationsSessionRepository

<div class="paragraph">

`GemFireOperationsSessionRepository` is a `SessionRepository`
implementation that is implemented using Spring Session for
VMware GemFire's\_ `GemFireOperationsSessionRepository`.

</div>

<div class="paragraph">

In a web environment, this repository is used in conjunction with the
`SessionRepositoryFilter`.

</div>

<div class="paragraph">

This implementation supports `SessionCreatedEvents`,
`SessionDeletedEvents` and `SessionDestroyedEvents` through
`SessionEventHttpSessionListenerAdapter`.

</div>

<div class="sect3">

#### Using Indexes with VMware GemFire

<div class="paragraph">

While best practices concerning the proper definition of Indexes that
positively impact VMware GemFire's performance is beyond the
scope of this document, it is important to realize that Spring Session
for VMware GemFire creates and uses Indexes to query and find
Sessions efficiently.

</div>

<div class="paragraph">

Out-of-the-box, Spring Session for VMware GemFire creates 1
Hash-typed Index on the principal name. There are two different built-in
strategies for finding the principal name. The first strategy is that
the value of the Session attribute with the name
`FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME` will be
Indexed to the same index name.

</div>

<div class="paragraph">

For example:

</div>

<div class="listingblock">

<div class="content">

```highlight
Unresolved directive in index.adoc - include::{docs-itest-dir}docs/gemfire/indexing/HttpSessionGemFireIndexingIntegrationTests.java[tags=findbyindexname-set]
Unresolved directive in index.adoc - include::{docs-itest-dir}docs/gemfire/indexing/HttpSessionGemFireIndexingIntegrationTests.java[tags=findbyindexname-get]
```

</div>

</div>

</div>

<div class="sect3">

#### Using Indexes with VMware GemFire & Spring Security

<div class="paragraph">

Alternatively, Spring Session for VMware GemFire will map
Spring Security's current `Authentication#getName()` to the Index
`FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME`.

</div>

<div class="paragraph">

For example, if you are using Spring Security you can find the current
user's sessions using:

</div>

<div class="listingblock">

<div class="content">

```highlight
Unresolved directive in index.adoc - include::{docs-itest-dir}docs/gemfire/indexing/HttpSessionGemFireIndexingIntegrationTests.java[tags=findbyspringsecurityindexname-context]
Unresolved directive in index.adoc - include::{docs-itest-dir}docs/gemfire/indexing/HttpSessionGemFireIndexingIntegrationTests.java[tags=findbyspringsecurityindexname-get]
```

</div>

</div>

</div>

<div class="sect3">

#### Using Custom Indexes with VMware GemFire

<div class="paragraph">

This enables developers using the `GemFireOperationsSessionRepository`
programmatically to query and find all Sessions with a given principal
name efficiently.

</div>

<div class="paragraph">

Additionally, Spring Session for VMware GemFire will create a
Range-based Index on the implementing Session's Map-type `attributes`
property (i.e. on any arbitrary Session attribute) when a developer
identifies 1 or more named Session attributes that should be indexed by
VMware GemFire.

</div>

<div class="paragraph">

Sessions attributes to index can be specified with the
`indexableSessionAttributes` attribute on the
`@EnableGemFireHttpSession` annotation. A developer adds this annotation
to their Spring application `@Configuration` class when s/he wishes to
enable Spring Session's support for `HttpSession` backed by
VMware GemFire.

</div>

<div class="listingblock">

<div class="content">

```highlight
Unresolved directive in index.adoc - include::{docs-itest-dir}docs/gemfire/indexing/HttpSessionGemFireCustomIndexingIntegrationTests.java[tags=findbyindexname-set]
Unresolved directive in index.adoc - include::{docs-itest-dir}docs/gemfire/indexing/HttpSessionGemFireCustomIndexingIntegrationTests.java[tags=findbyindexname-get]
```

</div>

</div>

<div class="admonitionblock note">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Note
</div></td>
<td class="content">Only Session attribute names identified in the
<code>@EnableGemFireHttpSession</code> annotation's
<code>indexableSessionAttributes</code> attribute will have an Index
defined. All other Session attributes will not be indexed.</td>
</tr>
</tbody>
</table>

</div>

<div class="paragraph">

However, there is one catch. Any values stored in an indexable Session
attributes must implement the `java.lang.Comparable<T>` interface. If
those object values do not implement `Comparable`, then
VMware GemFire will throw an error on startup when the Index
is defined for Regions with persistent Session data, or when an attempt
is made at runtime to assign the indexable Session attribute a value
that is not `Comparable` and the Session is saved to
VMware GemFire.

</div>

<div class="admonitionblock note">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Note
</div></td>
<td class="content">Any Session attribute that is not indexed may store
non-<code>Comparable</code> values.</td>
</tr>
</tbody>
</table>

</div>

<div class="paragraph">

To learn more about VMware GemFire's Range-based Indexes, see
[Creating Indexes on Map
Fields](https://geode.apache.org/docs/guide/%7Bmaster-data-store-version%7D/developing/query_index/creating_map_indexes.html).

</div>

<div class="paragraph">

To learn more about VMware GemFire Indexing in general, see
[Working with
Indexes](https://geode.apache.org/docs/guide/%7Bmaster-data-store-version%7D/developing/query_index/query_index.html).

</div>

</div>

</div>

</div>

</div>

<div class="sect1">

## Spring Session Community

<div class="sectionbody">

<div class="paragraph">

We are glad to consider you a part of our community. Please find
additional information below.

</div>

<div class="sect2">

### Support

<div class="paragraph">

You can get help by asking questions on [StackOverflow with the tag
`spring-session`](https://stackoverflow.com/questions/tagged/spring-session).
Similarly we encourage helping others by answering questions on
*StackOverflow*.

</div>

</div>

<div class="sect2">

### Source Code

<div class="paragraph">

The source code can be found on GitHub at
<a href="https://github.com/spring-projects/spring-session-data-gemfire"
class="bare">https://github.com/spring-projects/spring-session-data-gemfire</a>

</div>

</div>

<div class="sect2">

### Issue Tracking

<div class="paragraph">

We track issues in GitHub Issues at <a
href="https://github.com/spring-projects/spring-session-data-gemfire/issues"
class="bare">https://github.com/spring-projects/spring-session-data-gemfire/issues</a>

</div>

</div>

<div class="sect2">

### Contributing

<div class="paragraph">

We appreciate [Pull
Requests](https://help.github.com/articles/using-pull-requests/).

</div>

</div>

<div class="sect2">

### License

<div class="paragraph">

Spring Session for VMware GemFire and Spring Session for
Pivotal GemFire are Open Source Software released under the [Apache 2.0
license](https://www.apache.org/licenses/LICENSE-2.0.html).

</div>

</div>

</div>

</div>

<div class="sect1">

## Minimum Requirements

<div class="sectionbody">

<div class="paragraph">

The minimum requirements for Spring Session are:

</div>

<div class="ulist">

- Java 8+

- If you are running in a Servlet container (not required), Servlet 2.5+

- If you are using other Spring libraries (not required), the minimum
  required version is Spring Framework 5.0.x.

- `@EnableGemFireHttpSession` requires Spring Data for
  VMware GemFire 2.0.x and Spring Data for Pivotal GemFire
  2.0.x.

- `@EnableGemFireHttpSession` requires VMware GemFire 1.2.x or
  Pivotal GemFire 9.1.x.

</div>

<div class="admonitionblock note">

<table>
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="icon"><div class="title">
Note
</div></td>
<td class="content"><div class="paragraph">
At its core Spring Session only has a required dependency on
<code>spring-jcl</code>.
</div></td>
</tr>
</tbody>
</table>

</div>

</div>

</div>

</div>

<div id="footer">

<div id="footer-text">

Last updated 2022-10-27 16:45:57 -0700

</div>

</div>
