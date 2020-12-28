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

This will ensure that `development` mode is enabled regardless of how the application is deployed.

## Optionally Enabling Development Mode
You may not want `development` mode to always be enabled, and therefore, may want to avoid hardcoding `ktor.development=true` 

To enable/disable `development` mode on a case-by-case basis, there are several options; depending on how you plan to deploy the application.

### Running The Kotlin Application Build Configuration
When you start your Ktor application by clicking the green "play" button next to the `main()` function, this creates and launches a Kotlin Application Build Configuration.

This configuration will run the `:classes` Gradle task, but does not rely on the `application` plugin or the associated `:run` task.

This means that any `applicationDefaultJvmArgs` configured within the `application {}` of the `build.gradle` file will not be present when the application launches.

Instead, to enable `development` mode for this build configuration:

-  add `-Dio.ktor.development=true` to the `VM` options of the IntelliJ run configuration.


### Executing the Gradle `run` task
The Gradle `application` plugin assists in creating an executable JVM application and provides a `:run` task to start our application.

To enable `development` mode when deploying via the `:run` task
- add `-Dio.ktor.development=true` to the `applicationDefaultJvmArgs` property of the `application {}` block in the project's `build.gradle` file

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
This works whether executing the `run` task from the command line using the Gradle Wrapper or when executing Gradle tasks from IntelliJ

In practice, this approach doesn't bring much flexibility to our deployment workflows.

What if we wanted to start the app using the `:run` task, but be able to turn on/off `development` mode?

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

## Using Development Mode
When the application has been run with `development` mode turned on, code changes can then be deployed without a full recompile of the application.

### From IntelliJ
- Run the application with `development` mode enabled
- Make a change to your code
- Click the `rebuild` option
- Reload your webpage

After this, your new changes should take effect

### From Command Line via Gradle
- Run the `-t installDist` command using the Gradle wrapper

`./gradlew -t installDist`
  
- From another terminal tab, run the application with `development` mode enabled
  
`./gradlew run -Dio.ktor.development=true`

- Make a change to your code
- The code should take a few seconds to recompile
- Reload your webpage

#### Continuous Gradle Builds
The `-t` flag in the previous example is synonymous with `--continuous`

This means that Gradle will not exit after a command is executed, and Gradle will re-execute a task when file inputs change.

By running this first, Gradle will automatically rebuild and distribute the application on code changes.  And by running the application with `development` mode enabled, it will automatically reload those changes when they are received.