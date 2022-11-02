<div id="header">

# Spring Session - HttpSession with VMware GemFire Client/Server using XML Configuration


<span id="author" class="author">John Blum</span>  



<div id="toc" class="toc2">

<div id="toctitle">

Table of Contents



- [Updating Dependencies](#spring-session-dependencies)
- [Spring XML
  Configuration](#httpsession-spring-xml-configuration-clientserver)
  - [Client Configuration](#_client_configuration)
  - [Server Configuration](#_server_configuration)
- [XML Servlet Container
  Initialization](#_xml_servlet_container_initialization)
- [HttpSession with VMware GemFire (Client/Server) using XML Sample
  Application](#spring-session-sample-xml-geode-clientserver)
  - [Running the httpsession-gemfire-clientserver-xml Sample
    Application](#_running_the_httpsession_gemfire_clientserver_xml_sample_application)
  - [Exploring the httpsession-gemfire-clientserver-xml Sample
    Application](#_exploring_the_httpsession_gemfire_clientserver_xml_sample_application)
  - [How does it work?](#_how_does_it_work)






<div id="preamble">

<div class="sectionbody">



This guide describes how to configure VMware GemFire as a provider in
Spring Session to transparently manage a Web application's
`javax.servlet.http.HttpSession` using XML Configuration.



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
href="#spring-session-sample-xml-geode-clientserver">Spring XML
Configuation</a>.</td>
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

## Spring XML Configuration

<div class="sectionbody">



After adding the required dependencies and repository declarations, we
can create the Spring configuration. The Spring configuration is
responsible for creating a `Servlet` `Filter` that replaces the
`javax.servlet.http.HttpSession` with an implementation backed by Spring
Session and VMware GemFire.



<div class="sect2">

### Client Configuration



Add the following Spring configuration:



<div class="listingblock">

<div class="content">

```highlight
Unresolved directive in xml-gemfire-clientserver.adoc - include::{samples-dir}xml/gemfire-clientserver/src/main/webapp/WEB-INF/spring/session-client.xml[tags=beans]
```





<div class="colist arabic">

1.  (Optional) First, we can include a `Properties` bean to configure
    certain aspects of the VMware GemFire `ClientCache` using [Pivotal
    GemFire
    Properties](https://geode.apache.org/docs/guide/%7Bmaster-data-store-version%7D/reference/topics/gemfire_properties.html).
    In this case, we are just setting VMware GemFire's “log-level” using
    an application-specific System property, defaulting to “warning” if
    unspecified.

2.  We must create an instance of an VMware GemFire `ClientCache`. We
    initialize it with our `gemfireProperties`.

3.  Then we configure a `Pool` of connections to talk to the VMware GemFire Server in our Client/Server topology. In our configuration, we
    use sensible settings for timeouts, number of connections and so on.
    Also, our `Pool` has been configured to connect directly to the
    server (using the nested `gfe:server` element).

4.  Finally, a `GemFireHttpSessionConfiguration` bean is registered to
    enable Spring Session functionality.



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
<td class="content">For more information on configuring Spring Data for
VMware GemFire, refer to the <a
href="https://docs.spring.io/spring-data/geode/docs/current/reference/html">Reference
Guide</a>.</td>
</tr>
</tbody>
</table>





<div class="sect2">

### Server Configuration



So far, we only covered one side of the equation. We also need an VMware GemFire Server for our cache client to talk to and send session state to
the server to manage.





In this sample, we will use the following XML configuration to spin up
an VMware GemFire Server:



<div class="listingblock">

<div class="content">

```highlight
Unresolved directive in xml-gemfire-clientserver.adoc - include::{samples-dir}xml/gemfire-clientserver/src/main/resources/META-INF/spring/session-server.xml[tags=beans]
```





<div class="colist arabic">

1.  (Optional) First, we can include a `Properties` bean to configure
    certain aspects of the VMware GemFire peer `Cache` using [Pivotal
    GemFire
    Properties](https://geode.apache.org/docs/guide/%7Bmaster-data-store-version%7D/reference/topics/gemfire_properties.html).
    In this case, we are just setting VMware GemFire's “log-level” using
    an application-specific System property, defaulting to “warning” if
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





The VMware GemFire Server gets bootstrapped with the following:



<div class="listingblock">

<div class="content">

```highlight
Unresolved directive in xml-gemfire-clientserver.adoc - include::{samples-dir}xml/gemfire-clientserver/src/main/java/sample/server/GemFireServer.java[tags=class]
```





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
<td class="content">Rather than defining a simple Java class with a
<code>main</code> method, you might consider using Spring Boot
instead.</td>
</tr>
</tbody>
</table>



<div class="colist arabic">

1.  The `@Configuration` annotation designates this Java class as a
    source of Spring configuration meta-data using 7.9. Annotation-based
    container configuration\[Spring's annotation configuration
    support\].

2.  Primarily, the configuration comes from the
    `META-INF/spring/session-server.xml` file.









<div class="sect1">

## XML Servlet Container Initialization

<div class="sectionbody">



Our [Spring XML
Configuration](#httpsession-spring-xml-configuration-clientserver)
created a Spring bean named `springSessionRepositoryFilter` that
implements `javax.servlet.Filter` interface. The
`springSessionRepositoryFilter` bean is responsible for replacing the
`javax.servlet.http.HttpSession` with a custom implementation that is
provided by Spring Session and VMware GemFire.





In order for our `Filter` to do its magic, we need to instruct Spring to
load our `session-client.xml` configuration file.





We do this with the following configuration:



<div class="listingblock">

<div class="title">

src/main/webapp/WEB-INF/web.xml



<div class="content">

```highlight
Unresolved directive in xml-gemfire-clientserver.adoc - include::{samples-dir}xml/gemfire-clientserver/src/main/webapp/WEB-INF/web.xml[tags=context-param]
Unresolved directive in xml-gemfire-clientserver.adoc - include::{samples-dir}xml/gemfire-clientserver/src/main/webapp/WEB-INF/web.xml[tags=listeners]
```







The
[ContextLoaderListener](https://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#context-create)
reads the `contextConfigLocation` context parameter value and picks up
our *session-client.xml* configuration file.





Finally, we need to ensure that our Servlet container (i.e. Tomcat) uses
our `springSessionRepositoryFilter` for every request.





The following snippet performs this last step for us:



<div class="listingblock">

<div class="title">

src/main/webapp/WEB-INF/web.xml



<div class="content">

```highlight
Unresolved directive in xml-gemfire-clientserver.adoc - include::{samples-dir}xml/gemfire-clientserver/src/main/webapp/WEB-INF/web.xml[tags=springSessionRepositoryFilter]
```







The
[DelegatingFilterProxy](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/filter/DelegatingFilterProxy.html)
will look up a bean by the name of `springSessionRepositoryFilter` and
cast it to a `Filter`. For every HTTP request, the
`DelegatingFilterProxy` is invoked, which delegates to the
`springSessionRepositoryFilter`.







<div class="sect1">

## HttpSession with VMware GemFire (Client/Server) using XML Sample Application

<div class="sectionbody">

<div class="sect2">

### Running the httpsession-gemfire-clientserver-xml Sample Application



You can run the sample by obtaining the {download-url}\[source code\]
and invoking the following commands.





First, you need to run the server using:



<div class="listingblock">

<div class="content">

    $ ./gradlew :spring-session-sample-javaconfig-gemfire-clientserver:run







Now, in a separate terminal, you can run the client using:



<div class="listingblock">

<div class="content">

    $ ./gradlew :spring-session-sample-javaconfig-gemfire-clientserver:tomcatRun







You should now be able to access the application at
<a href="http://localhost:8080/" class="bare">http://localhost:8080/</a>.





In this sample, the Web application is the VMware GemFire cache client and
the server is a standalone, separate JVM process.





<div class="sect2">

### Exploring the httpsession-gemfire-clientserver-xml Sample Application



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
Unresolved directive in xml-gemfire-clientserver.adoc - include::{samples-dir}xml/gemfire-clientserver/src/main/java/sample/client/SessionServlet.java[tags=class]
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



