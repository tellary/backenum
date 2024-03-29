# 2019-Sep-06, Fri

## APT generate backenum `Backward` inner class

Baeldung
[custom annotation](https://www.baeldung.com/lombok-custom-annotation).


# 2019-Sep-07, Sat

## APT generate backenum `Backward` inner class

`META-INF/services/lombok.javac.JavacAnnotationHandler`
in `lombok` jar has the following first lines

```
# Generated by org.mangosdk.spi.processor.SpiProcessor (0.2.7-SNAPSHOT)
# Tue, 7 May 2019 00:12:47 +0200
lombok.javac.handlers.HandleAccessors
lombok.javac.handlers.HandleBuilder
...
```

> [T]he newer versions of Lombok use Shadow ClassLoader (SCL) to hide the .class files in Lombok as .scl files. Thus, it forces the developers to fork the Lombok source code and implement annotations there


```
~/safeplace/projects/backenum $ jar tf lombok-1.18.8.jar | grep JavacAnnotationHandler
META-INF/services/lombok.javac.JavacAnnotationHandler
lombok/javac/JavacAnnotationHandler.SCL.lombok
```

The above is the case for versions starting from `1.16.0`.
`1.14.8` is packaged with `lombok/javac/JavacAnnotationHandler.class`

Trying the following for now:

```
    implementation 'org.projectlombok:lombok:1.14.8'
    annotationProcessor 'org.projectlombok:lombok:1.18.8'
```

Getting a error as expected with the following

```
@Backenum
public class Status {
    int ACCEPTED, REJECTED;
}
```

```
> Task :compileTestJava FAILED
/home/ilya/safeplace/projects/backenum/src/test/java/Status.java:3: error: @Backenum is only supported on an enum
@Backenum
^
1 error
```

# 2019-Sep-12, Thu

## APT generate backenum `Backward` inner class

Ivan shared [this link](https://github.com/sympower/symbok/blob/master/src/main/resources/META-INF/ShadowClassLoader)
on `META-INF/ShadowClassLoader` example.

`ShadowClassLoader` explains how this file is used:

> 
> If no overrides are present, the load order is as follows:
> 
> - First, if the resource is found in our own jar
>   (trying ".SCL.__sclSuffix__" first for any resource request
>   ending in ".class"), return that.
> - Next, check any jar files other than our own, loading them via this classloader,
>   if they have a file `META-INF/ShadowClassLoader` that contains
>   a line of text with `sclSuffix`.
> - Next, ask the `parent` loader.

`sclSuffix` is `lombok` in `lombok*.jar`:

```
$ jar tf lombok-1.18.8.jar | grep JavacAnnotationHandler
META-INF/services/lombok.javac.JavacAnnotationHandler
lombok/javac/JavacAnnotationHandler.SCL.lombok
```

The following works:

    javac -processorpath /home/ilya/safeplace/projects/backenum/build/libs/backenum-0.1-SNAPSHOT.jar:/home/ilya/.gradle/caches/modules-2/files-2.1/org.projectlombok/lombok/1.18.10/625fc0055674dff70dbc76efa36d0f2c89b04a24/lombok-1.18.10.jar -cp /home/ilya/safeplace/projects/backenum/build/libs/backenum-0.1-SNAPSHOT.jar *.java

Did multimodule build to test with Gradle.

# 2019-Sep-14, Sat

## APT generate backenum `Backward` inner class

I'm getting the damn error again after doing the multimodule project

    Lombok isn't running due to misconfigured SPI files: java.io.FileNotFoundException: JAR entry META-INF/services/lombok.javac.JavacAnnotationHandler not found in /home/ilya/safeplace/projects/backenum/backenum/build/libs/backenum-0.1-SNAPSHOT.jar

`javap` still works:

```
$ javac -processorpath /home/ilya/safeplace/projects/backenum/backenum/build/libs/backenum-0.1-SNAPSHOT.jar:/home/ilya/.gradle/caches/modules-2/files-2.1/org.projectlombok/lombok/1.18.10/625fc0055674dff70dbc76efa36d0f2c89b04a24/lombok-1.18.10.jar -cp /home/ilya/safeplace/projects/backenum/backenum/build/libs/backenum-0.1-SNAPSHOT.jar *.java
Handling!
```

I can see the following with `gradle -d`:

```
20:30:02.335 [DEBUG] [org.gradle.api.internal.tasks.compile.NormalizingJavaCompiler] \
Compiler arguments: \
-source 1.8 -target 1.8 \
-d /home/ilya/safeplace/projects/backenum/sample/build/classes/java/main \
-g -sourcepath  \
-processorpath /home/ilya/safeplace/projects/backenum/backenum/build/libs/backenum-0.1-SNAPSHOT.jar:/home/ilya/.gradle/caches/modules-2/files-2.1/org.projectlombok/lombok/1.18.10/625fc0055674dff70dbc76efa36d0f2c89b04a24/lombok-1.18.10.jar \
-s /home/ilya/safeplace/projects/backenum/sample/build/generated/sources/annotationProcessor/java/main \
-XDuseUnsharedTable=true \
-classpath /home/ilya/safeplace/projects/backenum/backenum/build/libs/backenum-0.1-SNAPSHOT.jar /home/ilya/safeplace/projects/backenum/sample/src/main/java/Status.java /home/ilya/safeplace/projects/backenum/sample/src/main/java/tmp.java /home/ilya/safeplace/projects/backenum/sample/src/main/java/Role.java
```

The following works:

```
$ javac -source 1.8 -target 1.8 -d /home/ilya/safeplace/projects/backenum/sample/build/classes/java/main -g -sourcepath '' -processorpath /home/ilya/safeplace/projects/backenum/backenum/build/libs/backenum-0.1-SNAPSHOT.jar:/home/ilya/.gradle/caches/modules-2/files-2.1/org.projectlombok/lombok/1.18.10/625fc0055674dff70dbc76efa36d0f2c89b04a24/lombok-1.18.10.jar -s /home/ilya/safeplace/projects/backenum/sample/build/generated/sources/annotationProcessor/java/main -XDuseUnsharedTable=true -classpath /home/ilya/safeplace/projects/backenum/backenum/build/libs/backenum-0.1-SNAPSHOT.jar /home/ilya/safeplace/projects/backenum/sample/src/main/java/Status.java /home/ilya/safeplace/projects/backenum/sample/src/main/java/tmp.java /home/ilya/safeplace/projects/backenum/sample/src/main/java/Role.java
Handling!
```

Solution:

```
tasks.withType(JavaCompile) {
    options.fork = true
}
```
