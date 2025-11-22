# Maven plugin

Maven is still a very common build system for JVM projects. This is the plugin you should use if you're looking to use 
Log Format Enforcer in your maven project.

## Configuration
In the "main" `pom.xml`:
```xml
<plugin>
    <groupId>com.leandronunes85.lfe</groupId>
    <artifactId>log-format-enforcer-maven-plugin</artifactId>
    <version>2.0.0</version>
    <configuration>
        <packageName>com.leandronunes85.tests</packageName>
        <language>java-11</language>
        <fields>
            <field>
                <name>op</name>
                <mandatory>true</mandatory>
            </field>
            <field>
                <name>msg</name>
                <mandatory>true</mandatory>
            </field>
            <field>
                <name>input</name>
                <mandatory>false</mandatory>
            </field>
            <field>
                <name>output</name>
                <mandatory>false</mandatory>
            </field>
        </fields>
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


### Properties
| Name                 | Type                                             | Mandatory?                     |
|----------------------|--------------------------------------------------|--------------------------------|
| packageName          | String.                                          | True.                          |
| language             | String. `java-11`, `kotlin-1.3` or `kotlin-1.5`. | False. Defaults to `java-11`.  |
| fields               | List of fields.                                  | False. Defaults to empty list. |
| entrySeparator       | String.                                          | False. Defaults to `, `.       |
| valueDelimiterPrefix | String.                                          | False. Defaults to `'`.        |
| valueDelimiterSuffix | String.                                          | False. Defaults to `'`.        |
| keyValueSeparator    | String.                                          | False. Defaults to `=`.        |

### Field

A Field, in its simplest form can be described as:
```xml
...
<field>
    <name>fieldName</name>
</field>
...
```
This will generate a method called `fieldName` that will log a value with the label "fieldName". Sometimes,
however, one may want to have different values for the method name and the label of the field that actually gets logged.
This can be achieved by declaring such fields using this form instead:
```xml
...
<field>
    <name>methodName</name>
    <text>logLabel</text>
</field>
...
```

So, going back to our original example, one could have used:
```xml
...
<field>
    <name>operation</name>
    <text>op</text>
    <mandatory>true</mandatory>
</field>
<field>
    <name>message</name>
    <text>msg</text>
    <mandatory>true</mandatory>
</field>
...
```
to have more meaningful code (`log.info { operation("someMethod").message("some message that ...`) while still complying with
the previously agreed message format.