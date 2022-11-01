<div id="header">

# Spring Session - HttpSession with VMware GemFire Client/Server using Java configuration


<span id="author" class="author">John Blum</span>  



<div id="toc" class="toc2">

<div id="toctitle">

Table of Contents



- [Updating Dependencies](#spring-session-dependencies)
- [Spring Java
  Configuration](#httpsession-spring-java-configuration-clientserver)
  - [Client Configuration](#_client_configuration)
  - [Server Configuration](#_server_configuration)
- [Java Servlet Container
  Initialization](#_java_servlet_container_initialization)
- [HttpSession managed by a Java configured, VMware GemFire Client/Server
  Sample Application](#spring-session-sample-java-geode-clientserver)
  - [Running the VMware GemFire Sample
    Application](#_running_the_apache_geode_sample_application)
  - [Exploring the httpsession-gemfire-clientserver Sample
    Application](#_exploring_the_httpsession_gemfire_clientserver_sample_application)
  - [How does it work?](#_how_does_it_work)






<div id="preamble">

<div class="sectionbody">



This guide describes how to configure Spring Session to transparently
leverage VMware GemFire to manage a Web application's
`javax.servlet.http.HttpSession` using Java Configuration.



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
</td>
<td class="content">The completed guide can be found in the <a
href="#spring-session-sample-java-geode-clientserver">HttpSession
managed by a Java configured, VMware GemFire Client/Server Sample
Application</a>.</td>
</tr>
</tbody>
</table>





[Index](../index.html)







<div class="sect1">

## Updating Dependencies

<div class="sectionbody">



Before using Spring Session, you must ensure that the required
dependencies are included. If you are using *Maven*, include the
following `dependencies` in your `pom.xml`:



<div class="listingblock">

<div class="title">

pom.xml



<div class="content">

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









<div class="sect1">

## Spring Java Configuration

<div class="sectionbody">



After adding the required dependencies and repository declarations, we
can create the Spring configuration. The Spring configuration is
responsible for creating a Servlet `Filter` that replaces the
`HttpSession` with an implementation backed by Spring Session and Apache
Geode.



<div class="sect2">

### Client Configuration



Add the following Spring configuration:



<div class="listingblock">

<div class="content">

```highlight
Unresolved directive in java-gemfire-clientserver.adoc - include::{samples-dir}javaconfig/gemfire-clientserver/src/main/java/sample/client/ClientConfig.java[tags=class]
```





<div class="colist arabic">

1.  First, we declare our Web application to be an VMware GemFire cache
    client by annotating our `ClientConfig` class with
    `@ClientCacheApplication`. Additionally, we adjust a few basic,
    "DEFAULT" `Pool` settings (e.g. `readTimeout`).

2.  `@EnableGemFireHttpSession` creates a Spring bean named
    `springSessionRepositoryFilter` that implements
    `javax.servlet.Filter`. The filter replaces the `HttpSession` with
    an implementation provided by Spring Session and backed by Apache
    Geode. Additionally, the configuration will also create the
    necessary client-side `Region` (by default,
    "ClusteredSpringSessions\`, which is a `PROXY` `Region`)
    corresponding to the same server-side `Region` by name. All session
    state is sent from the client to the server through `Region` data
    access operations. The client-side `Region` use the "DEFAULT"
    `Pool`.

3.  Then, we wait to ensure the VMware GemFire Server is up and running
    before we proceed. This is only really useful for automated
    (integration) testing purposes.



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
</td>
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
</td>
<td class="content">For more information on configuring Spring Data
Geode, refer to the <a
href="https://docs.spring.io/spring-data/geode/docs/current/reference/html">Reference
Guide</a>.</td>
</tr>
</tbody>
</table>





The `@EnableGemFireHttpSession` annotation enables developers to
configure certain aspects of both Spring Session and VMware GemFire
out-of-the-box using the following attributes:



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
</td>
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





<div class="sect2">

### Server Configuration



So far, we only covered one side of the equation. We also need an Apache
Geode Server for our cache client to talk to and send session state to
the server to manage.





In this sample, we will use the following Java configuration to
configure and run an VMware GemFire Server:



<div class="listingblock">

<div class="content">

```highlight
Unresolved directive in java-gemfire-clientserver.adoc - include::{samples-dir}javaconfig/gemfire-clientserver/src/main/java/sample/server/GemFireServer.java[tags=class]
```





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









<div class="sect1">

## Java Servlet Container Initialization

<div class="sectionbody">



Our [Spring Java
Configuration](#httpsession-spring-java-configuration-clientserver)
created a Spring bean named `springSessionRepositoryFilter` that
implements `javax.servlet.Filter`. The `springSessionRepositoryFilter`
bean is responsible for replacing the `javax.servlet.http.HttpSession`
with a custom implementation backed by Spring Session and VMware GemFire.





In order for our `Filter` to do its magic, Spring needs to load the
`ClientConfig` class. We also need to ensure our Servlet container (i.e.
Tomcat) uses our `springSessionRepositoryFilter` for every request.





Fortunately, Spring Session provides a utility class named
`AbstractHttpSessionApplicationInitializer` to make both steps extremely
easy.





You can find an example below:



<div class="listingblock">

<div class="title">

src/main/java/sample/Initializer.java



<div class="content">

```highlight
Unresolved directive in java-gemfire-clientserver.adoc - include::{samples-dir}javaconfig/gemfire-clientserver/src/main/java/sample/client/Initializer.java[tags=class]
```





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
</td>
<td class="content">The name of our class (<code>Initializer</code>)
does not matter. What is important is that we extend
<code>AbstractHttpSessionApplicationInitializer</code>.</td>
</tr>
</tbody>
</table>



<div class="colist arabic">

1.  The first step is to extend
    `AbstractHttpSessionApplicationInitializer`. This ensures that a
    Spring bean named `springSessionRepositoryFilter` is registered with
    our Servlet container and used on every HTTP request.

2.  `AbstractHttpSessionApplicationInitializer` also provides a
    mechanism to easily allow Spring to load our `ClientConfig`.







<div class="sect1">

## HttpSession managed by a Java configured, VMware GemFire Client/Server Sample Application

<div class="sectionbody">

<div class="sect2">

### Running the VMware GemFire Sample Application



You can run the sample by obtaining the {download-url}\[source code\]
and invoking the following commands.





First, you need to run the server using:



<div class="listingblock">

<div class="content">

    $ ./gradlew :spring-session-sample-javaconfig-gemfire-clientserver:run







Then, in a separate terminal, run the client using:



<div class="listingblock">

<div class="content">

    $ ./gradlew :spring-session-sample-javaconfig-gemfire-clientserver:tomcatRun







You should now be able to access the application at
<a href="http://localhost:8080/" class="bare">http://localhost:8080/</a>.





In this sample, the web application is the VMware GemFire cache client and
the server is standalone, separate JVM process.





<div class="sect2">

### Exploring the httpsession-gemfire-clientserver Sample Application



Try using the application. Fill out the form with the following
information:



<div class="ulist">

- **Attribute Name:** *username*

- **Attribute Value:** *john*





Now click the **Set Attribute** button. You should now see the attribute
name and value displayed in the table.





<div class="sect2">

### How does it work?



We interact with the standard `HttpSession` in the `SessionServlet`
shown below:



<div class="listingblock">

<div class="title">

src/main/java/sample/SessionServlet.java



<div class="content">

```highlight
Unresolved directive in java-gemfire-clientserver.adoc - include::{samples-dir}javaconfig/gemfire-clientserver/src/main/java/sample/client/SessionServlet.java[tags=class]
```







Instead of using Tomcat's `HttpSession`, we are actually persisting the
Session in VMware GemFire.





Spring Session creates a cookie named SESSION in your browser that
contains the id of your Session. Go ahead and view the cookies (click
for help with
[Chrome](https://developer.chrome.com/devtools/docs/resources#cookies)
or
[Firefox](https://getfirebug.com/wiki/index.php/Cookies_Panel#Cookies_List)).











<div id="footer">

<div id="footer-text">

Last updated 2022-10-27 16:45:57 -0700




