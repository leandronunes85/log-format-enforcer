[![Maven build](https://github.com/leandronunes85/log-format-enforcer/actions/workflows/maven_build.yml/badge.svg?branch=master)](https://github.com/leandronunes85/log-format-enforcer/actions/workflows/maven_build.yml)

# Log Format Enforcer

The goal of this plugin is to provide teams with a way of enforcing a certain logging message style in their projects. 

## Motivation
I often find myself working in projects where certain log guidelines were agreed upon. In my opinion this is awesome 
because these kinds of logs are easier to parse, it introduces consistency amongst the logs created by every team member,
etc.

The only problem with this approach is that usually there's no way for enforcing such rules which leads to 
problems of all sorts:
* typos in the log structure: "strings should be enclosed in double quotes" but someone missed the closing double quote 
* missing agreed log data: the team agreed that every log should have the `userId` but someone forgot to add it in one 
particular log invocation
* new team members don't actually know what the agreed rules are
* you spend more time checking for log correctness in your code-reviews than actually reviewing interesting bits of the
code logic
* if the team realizes that the log format should change a bit, it's already too late because you would have to go back 
to update every single log entry in your code base to make everything consistent again
* ...

All these are problems that this library tries to address.

## Example

### Before
```java
private static final Logger log = LoggerFactory.getLogger(MyClass.class); // or something similar
...
log.info("op='someMethod', msg='This is a message', someValue='{}'", someValue);
...
log.trace("op='otherMethod', msg='Other message', input='{}', output='{}'", input, output);
...
log.warn("op='otherMethod', msg='Something bad happened', input='{}', output='{}'", input, output, exception);
...
if (log.isDebugEnabled()) {
    log.debug("op='otherMethod', msg='Something costly will be logged', heavyObject='{}'", this.computeHeavyObject());
}
...
```
This is an actual example of some messages a team where I worked before agreed upon: 'msg' and 'op' would always be 
present. For some cases (typically trace messages) we would log 'input' and 'output'. Every message could have any 
number of generic fields.

> [!NOTE]
> Even with these simple rules there was always problems: some people would use 'message' instead of 'msg' or 'operation'
> instead of 'op', quotes were commonly forgotten, same for commas between each field

### After

> [!IMPORTANT]
> Behind the scenes LogFormatEnforcer relies on [slf4j](https://www.slf4j.org) for the actual logging work so you must 
> provide it in your project dependencies. Luckily if you're using a known logging library (which you should), chances 
> are that you already have it in your classpath.

---

Using Java:
```java
private static final LogFormatEnforcer log = LogFormatEnforcer.loggerFor(MyClass.class); 
...
log.info(messageBuilder -> messageBuilder.op("someMethod").msg("This is a message").and("someValue", someValue));
...
log.trace(messageBuilder -> messageBuilder.op("otherMethod").msg("Other message").input(input).output(output));
...
log.warn(messageBuilder -> messageBuilder.op("otherMethod").msg("Something bad happened").input(input).output(output).exception(exception));
...
log.debug(messageBuilder -> messageBuilder.op"otherMethod").msg("Something costly will be logged").and("heavyObject", this.computeHeavyObject()); 
...
```

---

Using Kotlin:
```kotlin
private val log = LogFormatEnforcer.loggerFor<MyClass>()
...
log.info { op("someMethod").msg("This is a message").and("someValue", someValue) }
...
log.trace { op("otherMethod").msg("Other message").input(input).output(output) }
...
log.warn { op("otherMethod").msg("Something bad happened").input(input).output(output).exception(exception) }
...
log.debug { op"otherMethod").msg("Something costly will be logged").and("heavyObject", this.computeHeavyObject()) } 
...
```

This is starting to look better, but it's only the beginning. If you're using Kotlin, Log Format Enforcer gives you a 
few more goodies...

Let's say we have the following method:
```kotlin
fun sumOfDoubledEvenNumbers(allNumbers: Collection<Int>) : Int =
    allNumbers.asSequence()
        .filter { it % 2 == 0 }
        .map { it * 2 }
        .sum()
```

and that we want to have a look at the values passing through intermediate operations (this may seem overkill given this 
is such a simple example, but I've been in situations where I wished I had the ability to see these intermediate values 
more times than I can count). With LogFormatEnforcer we can do something like:

```kotlin
fun sumOfDoubledEvenNumbers(allNumbers: Collection<Int>) : Int =
    allNumbers.asSequence()
        .traceEach(log) { op("sumOfDoubledEvenNumbers").msg("from").and("element", it) }
        .filter { it % 2 == 0 }
        .traceEach(log) { op("sumOfDoubledEvenNumbers").msg("is even").and("element", it)}
        .map { it * 2 }
        .traceEach(log) { op("sumOfDoubledEvenNumbers").msg("doubled").and("element", it)}
        .sum()
        .debug(log) { op("sumOfDoubledEvenNumbers").msg("result").and("result", it)}
```

Note the addition of methods `` `logLevel`Each(LogFormatEnforcer, `message building block`)`` that can be applied to 
`Sequences`, `Collections`, etc. (pretty much like Kotlin's `map` function) and 
`` `logLevel`(LogFormatEnforcer, `message building block`)`` that is applied to any type (like Kotlin's `let` function). 

---

## How to use it

Most JVM projects these days use either Gradle or Maven build systems so we provide one plugin for each of the two:
 * [Maven Plugin](log-format-enforcer-maven-plugin/README.md)
 * [Gradle Plugin](log-format-enforcer-gradle-plugin/README.md)

## Wrap up
Your comments, suggestions, concerns, etc, are all welcome so please drop me a line!
