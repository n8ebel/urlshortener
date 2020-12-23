# Url Shortener
A Kotlin-based url shortener built with Ktor

## Development

### Enabling Development Mode
Enabling `development` mode will turn on auto-reload so that you may quickly deploy code changes to a running service.

To enable `development` mode for the project you may do 1 of 2 things:

- add `development=true` to the `ktor {}` block within `application.conf`
- add `-Dio.ktor.development=true` to the `VM` options of the IntelliJ run configuration

### Using Development Mode
When the application has been run with `development` mode turned on, code changes can then be deployed without a full recompile of the application.

#### From IntelliJ
- Run the application with `development` mode enabled
- Make a change to your code
- Click the `rebuild` option
- Reload your webpage

After this, your new changes should take effect