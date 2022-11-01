<div id="header">

# Spring Session - HttpSession with VMware GemFire P2P using Java Configuration


<span id="author" class="author">John Blum</span>  



<div id="toc" class="toc2">

<div id="toctitle">

Table of Contents



- [Updating Dependencies](#spring-session-dependencies)
- [Spring Java
  Configuration](#httpsession-spring-java-configuration-p2p)
- [Java Servlet Container
  Initialization](#_java_servlet_container_initialization)
- [HttpSession with VMware GemFire (P2P) Sample
  Application](#spring-session-sample-java-geode-p2p)
  - [Running the VMware GemFire P2P Java Sample
    Application](#_running_the_apache_geode_p2p_java_sample_application)
  - [Exploring the VMware GemFire P2P Java Sample
    Application](#_exploring_the_apache_geode_p2p_java_sample_application)
  - [How does it work?](#_how_does_it_work)






<div id="preamble">

<div class="sectionbody">



This guide describes how to configure VMware GemFire as a provider in
Spring Session to transparently manage a Web application's
`javax.servlet.http.HttpSession` using Java configuration.



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
href="#spring-session-sample-java-geode-p2p">HttpSession with Apache
Geode (P2P) Sample Application</a>.</td>
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
can create the Spring configuration.





The Spring configuration is responsible for creating a `Servlet`
`Filter` that replaces the `javax.servlet.http.HttpSession` with an
implementation backed by Spring Session and VMware GemFire.





Add the following Spring configuration:



<div class="listingblock">

<div class="content">

```highlight
Unresolved directive in java-gemfire-p2p.adoc - include::{samples-dir}javaconfig/gemfire-p2p/src/main/java/sample/Config.java[tags=class]
```





<div class="colist arabic">

1.  First, we use the `@PeerCacheApplication` annotation to simplify the
    creation of a peer cache instance.

2.  Then, the `Config` class is annotated with
    `@EnableGemFireHttpSession` to create the necessary server-side
    `Region` (by default, "*ClusteredSpringSessions*") used to store
    `HttpSession` state.



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
<td class="content">For more information on configuring Spring Data for
VMware GemFire, refer to the <a
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







<div class="sect1">

## Java Servlet Container Initialization

<div class="sectionbody">



Our \<\<\[httpsession-spring-java-configuration-p2p,Spring Java
Configuration\>\> created a Spring bean named
`springSessionRepositoryFilter` that implements `javax.servlet.Filter`.
The `springSessionRepositoryFilter` bean is responsible for replacing
the `javax.servlet.http.HttpSession` with a custom implementation backed
by Spring Session and VMware GemFire.





In order for our `Filter` to do its magic, Spring needs to load our
`Config` class. We also need to ensure our Servlet container (i.e.
Tomcat) uses our `springSessionRepositoryFilter` on every HTTP request.





Fortunately, Spring Session provides a utility class named
`AbstractHttpSessionApplicationInitializer` to make both steps extremely
easy.





You can find an example below:



<div class="listingblock">

<div class="title">

src/main/java/sample/Initializer.java



<div class="content">

```highlight
Unresolved directive in java-gemfire-p2p.adoc - include::{samples-dir}javaconfig/gemfire-p2p/src/main/java/sample/Initializer.java[tags=class]
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
    mechanism to easily allow Spring to load our `Config` class.







<div class="sect1">

## HttpSession with VMware GemFire (P2P) Sample Application

<div class="sectionbody">

<div class="sect2">

### Running the VMware GemFire P2P Java Sample Application



You can run the sample by obtaining the {download-url}\[source code\]
and invoking the following command:



<div class="listingblock">

<div class="content">

    $ ./gradlew :spring-session-sample-javaconfig-gemfire-p2p:tomcatRun







You should now be able to access the application at
<a href="http://localhost:8080/" class="bare">http://localhost:8080/</a>.





<div class="sect2">

### Exploring the VMware GemFire P2P Java Sample Application



Try using the application. Fill out the form with the following
information:



<div class="ulist">

- **Attribute Name:** *username*

- **Attribute Value:** *john*





Now click the **Set Attribute** button. You should now see the values
displayed in the table.





<div class="sect2">

### How does it work?



We interact with the standard `HttpSession` in the `SessionServlet`
shown below:



<div class="listingblock">

<div class="title">

src/main/java/sample/SessionServlet.java



<div class="content">

```highlight
Unresolved directive in java-gemfire-p2p.adoc - include::{samples-dir}javaconfig/gemfire-p2p/src/main/java/sample/SessionServlet.java[tags=class]
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




