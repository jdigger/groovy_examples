# Description #

Examples of Groovy techniques, tools and idioms.


# Building #

The project is built using [Gradle](http://gradle.org), which provides bootstrapping wrapper support so that
the only thing that needs to be installed on the build machine is Java (because of Java licensing distribution rules).

You can run "`gradlew`" (i.e., "`./gradlew`" on *nix or "`gradlew.bat`" on Windows) from the top level directory,
followed by the desired tasks.

For example, to do a clean build -- including running all quality tests and creating the distribution jars --
simply run "`gradlew clean build`"

To see a complete list of available tasks, run "`gradlew tasks`".

To create project files for IntelliJ IDEA, specify that as the Gradle task.
(i.e., "`gradlew idea`")


# Resources #

* [Hubert A. Klein Ikkink (mrhaki) Blog](http://mrhaki.blogspot.com/search/label/Groovy): Pure awesomeness
* [Create a scripting DSL](http://mrhaki.blogspot.com/2011/11/groovy-goodness-create-our-own-script.html)
* [Using the Magic Package for automatic MetaClasses](http://mrhaki.blogspot.com/2011/11/groovy-goodness-magic-package-to-add.html)
* [Transforming a Reader to a Writer](http://mrhaki.blogspot.com/2011/05/groovy-goodness-transforming-reader.html)
* [Category and Mixin transformations](http://groovy.codehaus.org/Category+and+Mixin+transformations)
* [@Canonical](http://mrhaki.blogspot.com/2011/05/groovy-goodness-canonical-annotation-to.html)
* [GPars multi-threading (now included in Groovy 1.8)](http://gpars.codehaus.org/)
* [Paging support in Groovy SQL](http://mrhaki.blogspot.com/2011/05/groovy-goodness-paging-support-in.html)
* [Inject Logging with Annotations](http://mrhaki.blogspot.com/2011/04/groovy-goodness-inject-logging-using.html)
* [Build](http://mrhaki.blogspot.com/2011/04/groovy-goodness-build-json-with.html) and [Parse](http://mrhaki.blogspot.com/2011/04/groovy-goodness-parse-json-with.html) JSON
* [CodeNarc](http://mrhaki.blogspot.com/2011/01/groovy-goodness-create-codenarc-reports.html) and [GMetrics](http://mrhaki.blogspot.com/2011/01/groovy-goodness-create-gmetrics-report.html) reports with XSLT
* [Using GroovyWS for SOAP](http://mrhaki.blogspot.com/2010/11/groovy-goodness-use-groovyws-to-access_15.html)
* [GContracts: Programming by Contract](https://github.com/andresteingress/gcontracts/wiki)
