# Gradle plugin
 
This is the plugin you should use if you're looking to use Log Format Enforcer in your gradle project.

## Configuration
In the "main" `build.gradle.kts`:

```kotlin
import com.leandronunes85.lfe.Language

plugins {
    id("com.leandronunes85.log-format-enforcer") version "2.0.0"
}

logFormatEnforcer {
    packageName = "com.leandronunes85.tests"
    language = Language.JAVA11
    fields = listOf(
        mandatory("op"),
        mandatory("msg"),
        optional("input"),
        optional("output"),
    )
}
```

### Properties
One of the advantages of using gradle is that it allows us to use type-safe configuration so you can leverage your IDE's 
auto-complete features to explore available properties.
