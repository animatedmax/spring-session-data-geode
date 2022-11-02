<div id="header">

# Spring Session - HttpSession with VMware GemFire P2P using XML Configuration


<span id="author" class="author">John Blum</span>  



<div id="toc" class="toc2">

<div id="toctitle">

Table of Contents



- [Updating Dependencies](#spring-session-dependencies)
- [Spring XML Configuration](#httpsession-spring-xml-configuration-p2p)
- [XML Servlet Container
  Initialization](#_xml_servlet_container_initialization)
- [HttpSession with VMware GemFire (P2P) using XML Sample
  Application](#spring-session-sample-xml-geode-p2p)
  - [Running the VMware GemFire XML Sample
    Application](#_running_the_apache_geode_xml_sample_application)
  - [Exploring the VMware GemFire XML Sample
    Application](#_exploring_the_apache_geode_xml_sample_application)
  - [How does it work?](#_how_does_it_work)






<div id="preamble">

<div class="sectionbody">



This guide describes how to configure VMware GemFire as a provider in
Spring Session to transparently manage a Web application's
`javax.servlet.http.HttpSession` using XML configuration.



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
href="#spring-session-sample-xml-geode-p2p">HttpSession with VMware GemFire (P2P) using XML Sample Application</a>.</td>
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

## Spring XML Configuration

<div class="sectionbody">



After adding the required dependencies and repository declarations, we
can create the Spring configuration.





The Spring configuration is responsible for creating a `Servlet`
`Filter` that replaces the `javax.servlet.http.HttpSession` with an
implementation backed by Spring Session and VMware GemFire.





Add the following Spring configuration:



<div class="listingblock">

<div class="title">

src/main/webapp/WEB-INF/spring/session.xml



<div class="content">

```highlight
Unresolved directive in xml-gemfire-p2p.adoc - include::{samples-dir}xml/gemfire-p2p/src/main/webapp/WEB-INF/spring/session.xml[tags=beans]
```





<div class="colist arabic">

1.  (Optional) First, we can include a `Properties` bean to configure
    certain aspects of the VMware GemFire peer `Cache` using [VMware Tanzu
    GemFire
    Properties](https://geode.apache.org/docs/guide/%7Bmaster-data-store-version%7D/reference/topics/gemfire_properties.html).
    In this case, we are just setting VMware GemFire's “log-level” using
    an application-specific System property, defaulting to “warning” if
    unspecified.

2.  We must configure an VMware GemFire peer `Cache` instance. We
    initialize it with the VMware GemFire properties.

3.  Finally, we enable Spring Session functionality by registering an
    instance of `GemFireHttpSessionConfiguration`.



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







<div class="sect1">

## XML Servlet Container Initialization

<div class="sectionbody">



The [Spring XML
Configuration](#httpsession-spring-xml-configuration-p2p) created a
Spring bean named `springSessionRepositoryFilter` that implements
`javax.servlet.Filter`. The `springSessionRepositoryFilter` bean is
responsible for replacing the `javax.servlet.http.HttpSession` with a
custom implementation that is backed by Spring Session and VMware GemFire.





In order for our `Filter` to do its magic, we need to instruct Spring to
load our `session.xml` configuration file.





We do this with the following configuration:



<div class="listingblock">

<div class="title">

src/main/webapp/WEB-INF/web.xml



<div class="content">

```highlight
Unresolved directive in xml-gemfire-p2p.adoc - include::{samples-dir}xml/gemfire-p2p/src/main/webapp/WEB-INF/web.xml[tags=context-param]
Unresolved directive in xml-gemfire-p2p.adoc - include::{samples-dir}xml/gemfire-p2p/src/main/webapp/WEB-INF/web.xml[tags=listeners]
```







The
[ContextLoaderListener](https://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#context-create)
reads the `contextConfigLocation` context parameter value and picks up
our *session.xml* configuration file.





Finally, we need to ensure that our Servlet container (i.e. Tomcat) uses
our `springSessionRepositoryFilter` for every HTTP request.





The following snippet performs this last step for us:



<div class="listingblock">

<div class="title">

src/main/webapp/WEB-INF/web.xml



<div class="content">

```highlight
Unresolved directive in xml-gemfire-p2p.adoc - include::{samples-dir}xml/gemfire-p2p/src/main/webapp/WEB-INF/web.xml[tags=springSessionRepositoryFilter]
```







The
[DelegatingFilterProxy](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/filter/DelegatingFilterProxy.html)
will look up a bean by the name of `springSessionRepositoryFilter` and
cast it to a `Filter`. For every HTTP request the
`DelegatingFilterProxy` is invoked, delegating to the
`springSessionRepositoryFilter`.







<div class="sect1">

## HttpSession with VMware GemFire (P2P) using XML Sample Application

<div class="sectionbody">

<div class="sect2">

### Running the VMware GemFire XML Sample Application



You can run the sample by obtaining the {download-url}\[source code\]
and invoking the following command:



<div class="listingblock">

<div class="content">

    $ ./gradlew :spring-session-sample-xml-gemfire-p2p:tomcatRun







You should now be able to access the application at
<a href="http://localhost:8080/" class="bare">http://localhost:8080/</a>.





<div class="sect2">

### Exploring the VMware GemFire XML Sample Application



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
Unresolved directive in xml-gemfire-p2p.adoc - include::{samples-dir}xml/gemfire-p2p/src/main/java/sample/SessionServlet.java[tags=class]
```







Instead of using Tomcat's `HttpSession`, we are actually persisting the
session in VMware GemFire.





Spring Session creates a cookie named SESSION in your browser that
contains the id of your session. Go ahead and view the cookies (click
for help with
[Chrome](https://developer.chrome.com/devtools/docs/resources#cookies)
or
[Firefox](https://getfirebug.com/wiki/index.php/Cookies_Panel#Cookies_List)).











<div id="footer">

<div id="footer-text">

Last updated 2022-10-27 16:45:57 -0700




