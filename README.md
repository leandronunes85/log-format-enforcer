[![Build Status](https://travis-ci.org/leandronunes85/log-format-enforcer.svg?branch=master)](https://travis-ci.org/leandronunes85/log-format-enforcer)

# Log Format Enforcer

The goal of this maven plugin is to provide teams with a way of enforcing a certain logging message style in their projects. 

## Motivation
I often find myself working in projects where certain log guidelines were agreed upon. In my opinion this is awesome 
because this kind of logs are easier to parse, it introduces consistency amongst the logs created by every team member,
etc, etc.

The only problem with this approach is that usually there's no way for enforcing these "agreed rules" which leads to 
problems of all sorts:
* typos in the log structure: "strings should be enclosed in double quotes" but someone missed the close double quote 
* missing agreed log data: the team agreed that every log should have the 'userId' but someone forgot to add it in one 
particular log invocation
* new team members don't actually know what the agreed rules are
* you spend more time checking for log correctness in your code-reviews than actually reviewing interesting bits of the
code logic
* if the team realizes that the log format should change a bit, it's already too late because you would have to go back 
to update every single log entry in your code base to make everything consistent again
* ...

All these are problems that this library tries to address.

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
###### Note 
>Even with these simple rules there was always problems: some people would use 'message' instead of 'msg' or 'operation'
>instead of 'op', quotes were commonly forgotten, same for commas between each field

### After
In your class:
```java
private static final LogFormatEnforcer log = LogFormatEnforcer.loggerFor(MyClass.class); 
...
log.info(messageBuilder -> messageBuilder.op("someMethod").msg("This is a message").and("someValue", someValue));
...
log.trace(messageBuilder -> messageBuilder.op("otherMethod").msg("Other message").input(input).output(output));
...
log.warn(messageBuilder -> messageBuilder.op("otherMethod").msg("Something bad happened").input(input).output(output).exception(exception));
...
log.debug(messageBuilder -> messageBuilder.op"otherMethod").msg("Something costly will be logged").and("heavyObject", this::computeHeavyObject); 
...
```
And I dare you trying to break the agreed rules now. 

In the pom.xml:
```xml
<plugin>
    <groupId>com.leandronunes85.lfe</groupId>
    <artifactId>log-format-enforcer-maven-plugin</artifactId>
    <version>1.4</version>
    <configuration>
        <packageName>com.leandronunes85.tests</packageName>
        <mandatoryFields>
            <op />
            <msg />
        </mandatoryFields>
        <optionalFields>
            <input />
            <output />
        </optionalFields>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>create</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

## Kotlin
More and more teams are migrating to Kotlin these days. Even though I still see many projects where there's a 
mix of Kotlin and Java source code I noticed the number of Kotlin-only projects is growing. With this in mind we provided Kotlin 
specific code generation so that you can use this library even if you project does not compile Java source code. 

This can be achieved by simply adding an optional configuration parameter `language` with the value "kotlin-1.3" like so:
```xml
<plugin>
    <groupId>com.leandronunes85.lfe</groupId>
    <artifactId>log-format-enforcer-maven-plugin</artifactId>
    <version>1.4</version>
    <configuration>
        <packageName>com.leandronunes85.tests.kotlin</packageName>
        <language>kotlin-1.3</language>
        <mandatoryFields>
            <op />
            <msg />
        </mandatoryFields>
        <optionalFields>
            <input />
            <output />
        </optionalFields>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>create</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

This will generate you a LogFormatEnforcer.kt file that will enable you to:
```kotlin
private val log = LogFormatEnforcer.loggerFor<MyClass>()
...
log.info { op("someMethod").msg("This is a message").and("someValue", someValue) }
...
log.trace { op("otherMethod").msg("Other message").input(input).output(output) }
...
log.warn { op("otherMethod").msg("Something bad happened").input(input).output(output).exception(exception) }
...
log.debug { op"otherMethod").msg("Something costly will be logged").and("heavyObject", this::computeHeavyObject) } 
...
```

This is starting to look better already but it's only the beginning. Let's say we have the following method:
```kotlin
fun sumOfDoubledEvenNumbers(allNumbers: Collection<Int>) : Int =
    allNumbers.asSequence()
        .filter { it % 2 == 0 }
        .map { it * 2 }
        .sum()
```

And that we want to have a look at the values passing through intermediate operations (this may seem overkill given this 
is such a simple example but I've been in situations where I wished I had the ability to see these intermediate values 
more times than I can count). So now we can do something like:

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

Note the addition of methods `` `logLevel`Each(LogFormatEnforcer, `message building block`)`` that can be applied to `Sequences`, 
`Collections`, etc (pretty much like Kotlin's own `map` function) and 
`` `logLevel`(LogFormatEnforcer, `message building block`)`` that is applied to any type (like Kotlin's) `let` function. 


## Configuring the way your log messages actually look like
Now, do you want to enclose your values in square brackets (because it's easier for you to parse it) and use "->" to 
separate keys and values (because you fancy lambdas)? Just update your pom, like this:
```xml
<plugin>
    <groupId>com.leandronunes85.lfe</groupId>
    <artifactId>log-format-enforcer-maven-plugin</artifactId>
    <version>1.4</version>
    <configuration>
        <packageName>com.leandronunes85.tests</packageName>
        <mandatoryFields>
            <op />
            <msg />
        </mandatoryFields>
        <optionalFields>
            <input />
            <output />
        </optionalFields>
        <valueDelimiterPrefix>[</valueDelimiterPrefix>
        <valueDelimiterSuffix>]</valueDelimiterSuffix>
        <keyValueSeparator>-></keyValueSeparator>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>create</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```
Rebuild it, release it and that's it!

###### Note 
>Behind the scenes LogFormatEnforcer relies on slf4j for the actual logging work so you must provide it in your 
>project dependencies. Luckily if you're using a known logging library (which you should), chances are that you 
>already have that. 

## Configuration properties
| Name                 | Type                                     | Mandatory                      |
|----------------------|------------------------------------------|--------------------------------|
| packageName          | String.                                  | True.                          |
| language             | String. Either `java-8` or `kotlin-1.3`. | False. Defaults to `java-8`.   |
| mandatoryFields      | List of fields.                          | False. Defaults to empty list. |
| optionalFields       | List of fields.                          | False. Defaults to empty list. |
| entrySeparator       | String.                                  | False. Defaults to `, `.       |
| valueDelimiterPrefix | String.                                  | False. Defaults to `'`.        |
| valueDelimiterSuffix | String.                                  | False. Defaults to `'`.        |
| keyValueSeparator    | String.                                  | False. Defaults to `=`.        |

### Field

Both `mandatoryFields` and `optionalFields` take a list of Fields. A Field, in its simplest form can be described as:  
```xml
...
    <fieldName />
...
```
This will generate a method called `fieldName` that will log a value with the label "fieldName". Sometimes,
however, one may want to have different values for the method name and the label of the field that actually gets logged. 
This can be achieved by declaring such fields using this form instead: 
```xml
...
    <methodName>logLabel</methodName>
...
```

So, going back to our original example, one could have used: 
```xml
...
        <mandatoryFields>
            <operation>op</operation>
            <message>msg</message>
        </mandatoryFields>
...
```
to have more meaningful code `log.info { operation("someMethod").message("some message that ...` while still complying with 
the previously agreed message format.

## Wrap up
Your comments, suggestions, concerns, etc, are all welcome so please drop me a line!
