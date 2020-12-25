# Url Shortener
A Kotlin-based url shortener built with Ktor

## Enabling Development Mode
Enabling `development` mode will turn on auto-reload so that you may quickly deploy code changes to a running service.

There are several different options for enabling `development` mode.  Picking an option will depend on your development workflows and how you are deploying your application.

## Enabling Development Mode For All Deployments
If you would like to always enable `development` mode, there's a simple configuration to do so.
- add `development=true` to the `ktor {}` block within `application.conf`

```
ktor {
    development = true
    deployment {
        port = 8080
        port = ${?PORT}
    }
    ...
}
```

## Optionally Enabling Development Mode
You may not want `development` mode to always be enabled, and therefore, may want to avoid hardcoding `development=true` 

To enable/disable `development` mode on a case-by-case basis, there are several options; depending on how you plan to deploy the application.

### Running The Kotlin Application Build Configuration
When you start your Ktor application by clicking the green "play" button next to the `main()` function, this creates and launches a Kotlin Application Build Configuration.

To enable `development` mode for this build configuration:

-  add `-Dio.ktor.development=true` to the `VM` options of the IntelliJ run configuration.


#### Executing the Gradle `run` task
This works whether executing the `run` task from the command line using the Gradle Wrapper or when executing Gradle tasks from IntelliJ

- add `-Dio.ktor.development=true` to the `applicationDefaultJvmArgs` property of the `application {}` block in the project's `build.gradle` file

The following snippet demonstrates this final example
```groovy
// build.gradle
application {
    mainClassName = "io.ktor.server.netty.EngineMain"
    applicationDefaultJvmArgs = [
            '-Dio.ktor.development=true',
            '-Xms128m',
            '-Xmx256m',
            ...
    ]
}
```
In practice, this approach doesn't bring much flexibility to our deployment workflows.

If we run from Gradle, `development` mode is on, if we run via the Kotlin Application build configuration, `development` mode would be turned off.

What if we wanted to build from Gradle, but be able to turn on/off `development` mode?

We can extend the `applicationDefaultJvmArgs` approach to pull from our Gradle System Properties.

```groovy
application {
    mainClassName = "io.ktor.server.netty.EngineMain"
    applicationDefaultJvmArgs = [
    "-Dio.ktor.development=${System.getProperty("io.ktor.development") ?: "false"}",
        '-Xms128m',
        '-Xmx256m',
        ...
    ]
}
```
With this in place, we can do the following:
- `./gradlew run -Dio.ktor.development=true` to run with `development` mode turned on
- `./gradlew run -Dio.ktor.development=false` or `./gradlew run` to run with `development` mode turned off

Thanks to [Ryan Harter](@rharter) for pointing me to this approach.

### Using Development Mode
When the application has been run with `development` mode turned on, code changes can then be deployed without a full recompile of the application.

#### From IntelliJ
- Run the application with `development` mode enabled
- Make a change to your code
- Click the `rebuild` option
- Reload your webpage

After this, your new changes should take effect