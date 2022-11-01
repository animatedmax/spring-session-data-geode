<div id="header">

# Spring Session - HttpSession with VMware GemFire Client/Server using Spring Boot


<span id="author" class="author">John Blum</span>  



<div id="toc" class="toc2">

<div id="toctitle">

Table of Contents



- [Updating Dependencies](#_updating_dependencies)
- [Spring Boot
  Configuration](#httpsession-spring-java-configuration-gemfire-boot)
  - [Spring Boot, VMware GemFire Cache
    Server](#_spring_boot_apache_geode_cache_server)
  - [Spring Boot, VMware GemFire Cache Client Web
    application](#_spring_boot_apache_geode_cache_client_web_application)
- [Spring Boot Sample Web Application with an VMware GemFire managed
  HttpSession](#spring-session-sample-boot-geode)
  - [Running the Boot Sample
    Application](#_running_the_boot_sample_application)
  - [Exploring the Boot Sample
    Application](#_exploring_the_boot_sample_application)
  - [How does it work?](#_how_does_it_work)






<div id="preamble">

<div class="sectionbody">



This guide describes how to build a Spring Boot application configured
with Spring Session to transparently leverage VMware GemFire to manage a
web application's `javax.servlet.http.HttpSession`.





In this sample, VMware GemFire's client/server topology is employed using
a pair of Spring Boot applications, one to configure and run a Apache
Geode Server and another to configure and run the cache client, Spring
MVC-based web application making use of the `HttpSession`.



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
href="#spring-session-sample-boot-geode">Spring Boot Sample Web
Application with an VMware GemFire managed HttpSession</a>.</td>
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
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```









<div class="sect1">

## Spring Boot Configuration

<div class="sectionbody">



After adding the required dependencies and repository declarations, we
can create the Spring configuration for both our VMware GemFire client and
server using Spring Boot. The Spring configuration is responsible for
creating a Servlet `Filter` that replaces the `HttpSession` with an
implementation backed by Spring Session and Apace Geode.



<div class="sect2">

### Spring Boot, VMware GemFire Cache Server



We start with a Spring Boot application to configure and bootstrap the
VMware GemFire Server:



<div class="listingblock">

<div class="content">

```highlight
Unresolved directive in boot-gemfire.adoc - include::{samples-dir}boot/gemfire/src/main/java/sample/server/GemFireServer.java[tags=class]
```





<div class="colist arabic">

1.  First, we annotate the VMware GemFire Server configuration class
    (`GemFireServer`) with `@SpringBootApplication` to indicate that
    this is a Spring Boot application leveraging all of *Spring Boot's*
    features (e.g. *auto-configuration*).

2.  Next, we use the Spring Data for VMware GemFire configuration
    annotation `@CacheServerApplication` to simplify the creation of a
    peer cache instance containing a `CacheServer` for cache clients to
    connect.

3.  (Optional) Then, the `@EnableGemFireHttpSession` annotation is used
    to create the necessary server-side `Region` (by default,
    "*ClusteredSpringSessions*") to store the `HttpSessions` state. This
    step is optional since the Session `Region` could be created
    manually, perhaps even using external means. Using
    `@EnableGemFireHttpSession` is convenient and quick.





<div class="sect2">

### Spring Boot, VMware GemFire Cache Client Web application



Now, we create a Spring Boot Web application to expose our Web service
with Spring Web MVC, running as an VMware GemFire cache client connected
to our Spring Boot, VMware GemFire Server. The Web application will use
Spring Session backed by VMware GemFire to manage `HttpSession` state in a
clustered (distributed) and replicated manner.



<div class="listingblock">

<div class="content">

```highlight
Unresolved directive in boot-gemfire.adoc - include::{samples-dir}boot/gemfire/src/main/java/sample/client/Application.java[tags=class]
```





<div class="colist arabic">

1.  Again, we declare our Web application to be a Spring Boot
    application by annotating our application class with
    `@SpringBootApplication`.

2.  `@Controller` is a Spring Web MVC annotation enabling our MVC
    handler mapping methods (i.e. methods annotated with
    `@RequestMapping`) to process HTTP requests (e.g. \<7\>)

3.  We also declare our Web application to be an VMware GemFire cache
    client by annotating our application class with
    `@ClientCacheApplication`. Additionally, we adjust a few basic,
    "DEFAULT" `Pool` settings (e.g. `readTimeout`).

4.  Next, we declare that the Web application will use Spring Session
    backed by VMware GemFire by annotating the `ClientCacheConfiguration`
    class with `@EnableGemFireHttpSession`. This will create the
    necessary client-side `Region` (by default,
    "ClusteredSpringSessions\` as a `PROXY` `Region`) corresponding to
    the same server-side `Region` by name. All `HttpSession` state will
    be sent from the cache client Web application to the server through
    `Region` data access operations. The client-side `Region` uses the
    "DEFAULT" `Pool`.

5.  Then, we wait to ensure the VMware GemFire Server is up and running
    before we proceed. This is only really useful for automated
    (integration) testing purposes.

6.  We adjust the Spring Web MVC configuration to set the home page,
    and…​

7.  Finally, we declare the `/sessions` HTTP request handler method to
    set an HTTP Session attribute and increment a count for the number
    of HTTP requests.





There are many other useful utility methods, so please refer to the
actual source code for full details.



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









<div class="sect1">

## Spring Boot Sample Web Application with an VMware GemFire managed HttpSession

<div class="sectionbody">

<div class="sect2">

### Running the Boot Sample Application



You can run the sample by obtaining the {download-url}\[source code\]
and invoking the following commands.





First, you must run the server:



<div class="listingblock">

<div class="content">

    $ ./gradlew :spring-session-sample-boot-gemfire:run







Then, in a separate terminal, run the client:



<div class="listingblock">

<div class="content">

    $ ./gradlew :spring-session-sample-boot-gemfire:bootRun







You should now be able to access the application at
<a href="http://localhost:8080/" class="bare">http://localhost:8080/</a>.





In this sample, the Web application is the Spring Boot, VMware GemFire
cache client and the server is standalone, separate (JVM) process.





<div class="sect2">

### Exploring the Boot Sample Application



Try using the application. Fill out the form with the following
information:



<div class="ulist">

- **Attribute Name:** *username*

- **Attribute Value:** *test*





Now click the **Set Attribute** button. You should now see the attribute
name and value displayed in the table along with an additional attribute
(`requestCount`) indicating the number of Session interactions (via HTTP
requests).





<div class="sect2">

### How does it work?



We interact with the standard `javax.servlet.http.HttpSession` in the
the Spring Web MVC service endpoint, shown here for convenience:



<div class="listingblock">

<div class="title">

src/main/java/sample/client/Application.java



<div class="content">

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







Instead of using the embedded HTTP server's `HttpSession`, we are
actually persisting the Session state in VMware GemFire. Spring Session
creates a cookie named SESSION in your browser that contains the id of
your Session. Go ahead and view the cookies (click for help with
[Chrome](https://developer.chrome.com/devtools/docs/resources#cookies)
or
[Firefox](https://getfirebug.com/wiki/index.php/Cookies_Panel#Cookies_List)).











<div id="footer">

<div id="footer-text">

Last updated 2022-10-27 16:45:57 -0700




