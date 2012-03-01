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

* [Create a scripting DSL](http://mrhaki.blogspot.com/2011/11/groovy-goodness-create-our-own-script.html)
* [Using the Magic Package for automatic MetaClasses](http://mrhaki.blogspot.com/2011/11/groovy-goodness-magic-package-to-add.html)
* [Transforming a Reader to a Writer](http://mrhaki.blogspot.com/2011/05/groovy-goodness-transforming-reader.html)
