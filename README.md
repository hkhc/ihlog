# IHLog - Easy Kotlin Logging Library for Android

IHLog is a simple logging library that trying to make simple things simple.

The standard Android `Log` class is simple and sufficient for most logging needs.
However, there are case that it is not appropriate to use the Log class directly.

* [Robolectric][https://robolectric.org] is great, but we don't want to use it just because our classes
under test contains a `Log.d` call.
* We don't want to provide Log tag for every Log method calls. However we do
want the logcat output contains a tag that reflect the context of the log line.

## Installation

Just like most library on Android, add line at `dependencies` block will do

```groovy
dependencies {
    // ...
    implementation 'io.hkhc.log:ihlog:0.5'
    // ...
}
```

The library is available at both Maven Central and JCenter, so chances are that you don't need to 
change the `repository` settings in Gradle script.

## Basic usage

By default the log line issued by this library will be sent to Android logcat. It can be changed and 
we will come back to this later.

### Log a line

We may just log a line within class methods, extension methods, or package functions

```kotlin
class HelloWorld {
    fun testOfLog() {
        debug("Hello. Thanks for using IHLog")
    }
}
```

``` kotlin
fun View.testOfLog() {
    debug("Hello. log in extension functions")
}
```

``` kotlin
package myPackage
fun testOfLog() {
    debug("Hello. log in package functions")
}
```

[[TDOO]] does it work as top level statements?

The logging action is as simple as it could possibly be. No import, no declarion, because it is 
implemented as Kotlin extension. When method `testOfLog` is executed, the 
following log line simpler to the following can be observed in logcat. 

```
03-03 02:04:49.949  1224  5126 D HeWo: Hello. Thanks for using IHLog
```

where `HeWo` is an automatically generated log tag. It is an abbreviation of the class `HelloWorld`. 

Code to log for other severity levels are similar

```kotlin
trace("This is a trace line. It is mapped to Log.i()")
info("This is a info line. It is mapped to Log.i()")
debug("This is a debug line. It is mapped to Log.d()")
warn("This is a warn line. It is mapped to Log.w()")
error("This is a error line. It is mapped to Log.e()")
fatal("This is a trace line. It is mapped to Log.wtf()")
```

### Multi-line text

Log content with multiple lines will be broken down.

```kotlin
debug("""
    This os a multi-line text
    It will be broken down
""")
```

will generate log like this:


```
03-03 02:04:49.949  1224  5126 D HeWo: This is a multi-line text
03-03 02:04:50.123  1224  5126 D HeWo: It will be broken down
```

### Logging exception

We have seen log with error level:

```kotlin
error("This is an error")
```



* lambda
* tag generation
* exception

## Configuration

* ihlog.properties

* Log level

* Meta Tag

## Log Providers

* Log provider vs Log

* String
* PrintWriter
* Stdio
* Android
 * PreN and PostN
* Null
* MoreTag
* extra log

* Write your own Log Providers


