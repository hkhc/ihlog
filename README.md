# IHLog - Easy Kotlin Logging Library for Android

IHLog is a simple logging library that trying to make simple things simple.

The standard Android `Log` class is simple and sufficient for most logging needs.
However, there are case that it is not appropriate to use the Log class directly.

* [Robolectric](https://robolectric.org) is great, but we don't want to use it just because our classes
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

The logging action is as simple as it could possibly be. We don't need to declare anything besides import, because it is 
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
err("This is a error line. It is mapped to Log.e()")
fatal("This is a trace line. It is mapped to Log.wtf()")
```

In some case if we want to specify the tag explicitly. We could just do that right away

```kotlin
debug("CUSTOM_TAG", "This is a trace line. It is mapped to Log.i()")
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
err("This is an error")
```

We may also log `Throwable` object directly:

```kotlin
try {
  val myArray = arrayOf(1,2,3)
  val myValue = myArray(3) // oops
}
catch (e: ArrayIndexOutOfBoundException) {
    e.log("This is an exception")
}
```

Stacktrace of the exception is generated and logged line-by-line

```
03-03 15:45:15.431  -/AIOOBE e/This is an error
03-03 15:45:15.435  -/AIOOBE e/java.lang.ArrayIndexOutOfBoundsException: 3
03-03 15:45:15.435  -/AIOOBE e/....
03-03 15:45:15.455  -/AIOOBE e/	at com.intellij.rt.execution.junit.JUnitStarter.main(JUnitStarter.java:70)
03-03 15:45:15.455  -/AIOOBE e/```

### Filtering log

We may filter log by log severity level anytime. 

```kotlin
LogSettings.logLevel = Severity.ERROR
```

It take effect from the next log invocation.

THe severity is in the increasing order of the following:
- Trace 
- Debug
- Info
- Warn
- Error
- Fatal


### Log with Lambda

When we have the capability of filtering logs, we start to worry that our log invocation peform 
unnecessary work. For example,

```kotlin
debug("The current balance is ${account.balance}.")
```

Sometimes, it may be costly to construct the string for log, and we don't want to pay the price
if the `logLevel` property filtered the log.

We may use lambda to defer the processing of the log content:

```kotlin
debug { "The current balance is ${account.balance}." }
```

Then the actual evaluation of the string is deferred until it is needed. And no cost is paid for 
filtered log.

### Tag Creation

In its simplist form, the library create the tag string for logging implicitly. It is based on the
class name that the log methods are invoked. The class name is transformed into abbreviated form
for berevity of log output.

Why do we want to abbreviate the class name? The class name is usually long for any non-trivial app. 
Especially when the inner classes or Companion classes are involved. It may even exceed the tag 
length limit of 23 characters on pre Android N devices.

Essentially, the tag string is generated from the simple class name (i.e. regardless the package)
by two rules:

###### Rule 1

When there are not more than 2 capital characters in the name, then the tag is composed by taking 
each of the captial characters and the following small letter or digital, and keep the whole tag within 
4 characters.. For example,

| class name   | tag    |
| ----------   | ---    |
| `HelloWorld` | `HeWo` |
| `Exception`  | `Ex`   |
| `MBone`      | `MBo`  |
| `AB123`      | `AB1`  |

###### Rule 2

When there are more than 2 capitals letters, only capital characters are extracted

| class name             | tag         |
| ----------             | ---         | 
| `NullPointerException` | `NPE`       |
| `ItIsAGoodDayToDie`    | `IIAGDTD`   |

###### Additional Rule : Inner class and Companion class

The tag for inner class is defined by combined by the abbreviation of the outer class name and the 
inner class name. E.g.

| class name             | tag         |
| ----------             | ---         | 
| `HelloWorld.Listener`  | `HeWoLi`    |
| `View.OnClickListener` | `ViOCL`     |

Kotlin methods in `companion object` block are compiled to an inner class `Companion` within the
outer class. IHLog make exception to the Companion class not including the "C" in abbreviation.

### Meta tag

We may specific a global prefix to the tag, known as "Meta Tag". so taht all tags in the process
is prepended with the meta tag. With meta tag, we can easily filter log output and focus on what is 
generated by our app. For example,

```kotlin
LogSettings.metaTag = "MYAPP"
debug("This is a log in class HelloWorld")
...
debug("This is a log in class BananaOrange")

```

The logcat output would be

```
03-03 02:04:49.949  1224  5126 D MYAPP_HeWo: This is a log in class HelloWorld
...
03-03 02:04:49.949  1224  5126 D MYAPP_BaOr: This is a log in class BananaOrange
```

By default the meta tag is empty string, and the underscore will not prepend tags.


## Configuration

IHLog is capable of targetting the log to various destinations. When it is first use in the process,
it read the file `ihlog.properties` in the root of Java classpath. The file is a standard java
properties file and the content looks like this:

```
provider=io.hkhc.log.providers.AndroidLogProvider
```

If the files does not exist, or the class specified in the file is not found, then the library is
set to use `AndroidLogProvider` which effectively means standard Android Log API is used.

### `LogSettings`

Alternatively, we may use the singleton class `LogSettings` to change the cconfiguration on the fly. 
It has several properties, the library take effect immediately when the properties changed.

| property             | purpose         |
| --------             | -------         | 
| `logLevel`  | filtering of log    |
| `metaTag`   | Add a prefix globally to all tags. If it is empty string, then nothing is prepended to the log tag. If it is non-empty, the log tags become `metaTag + "_" + abbrivated tag` |
| `defaultProvider` | instance of a `LogProvider`, to change the destination of log |

## Log Providers

IHLog work with Android Log API out of the box. On the other hand, we may configure it to use different 
targets with `ihlog.properties` file or `LogSettings` class object. You may even implement your own
target.

Each of the target is handled by a class that implemented `LogProvider` interface. It is responsible
for creating object that implements `IHLog` interface which keep the state of log specific state.

There are also some classes in the library that warp up other IHLog objects to augment their 
functionalities.

### `AndroidLogProvider`

`AndroidLogProvider` maps the log call to standard Android Log API. There are a few things to note:

- There is no `trace` log level, so both `trace` and `info` level are mapped to `info` level of 
Android Log.
- Before Android N, tag string is limited to 23 characters. There is no such limitation for newer OS.
IHLog library tries to accept longer tag string on pre-N devices, by moving the part of tag string 
over 23 characters to log body. For example, for the call

```kotlin
// 30 character long tag
debug("012345678901234567890123456789", "This is a log line") 
```

the logcat output for pre-N devices will look like this:

```
03-03 02:04:49.949  1224  5126 D 01234567890123456789012: 3456789: This is a log line
```

### `PrintWriterLogProvider`



### `NullLogProvider`

### `ConsoleLogProvider`
### `StdioLogProvider`
### `StringLogProvider`

### `MoreTagLog`

### Write your own Log Providers

## Unit test with IHLog

## IHLog and Android
