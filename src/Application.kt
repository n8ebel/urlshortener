package com.goobar

import io.ktor.application.*
import io.ktor.content.*
import io.ktor.features.*
import io.ktor.html.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.locations.*
import kotlinx.html.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

class CustomException : RuntimeException()

private val HttpStatusCode.Companion.CustomError: HttpStatusCode
    get() = HttpStatusCode(900, "Some Custom Error Occurred")

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    install(Locations)
    install(StatusPages) {
        exception<Throwable> { cause ->
            // Display generic error response to user
            call.respond(HttpStatusCode.InternalServerError, "Server Error - ${cause.message}")
            // re-throw to ensure it is not swallowed by status page
            throw cause
        }

        exception<CustomException> { cause ->
            // Display specific error response to user
            call.respond(HttpStatusCode.CustomError, "Custom Server Error")
            // re-throw to ensure it is not swallowed by status page
            throw cause
        }

        // serve custom .html pages for specific status responses
        statusFile(HttpStatusCode.NotFound, HttpStatusCode.Unauthorized, filePattern = "statuspages/error#.html")

        //
        status(HttpStatusCode.InternalServerError) {
            call.respond(TextContent("Error: ${it.value} ${it.description}", ContentType.Text.Plain.withCharset(Charsets.UTF_8), it))
        }
    }

    routing {

        home()
        shorten()

        // will provide a list of all saved URLs
        get("/saved") {
            call.respondText("This route will return all saved urls", ContentType.Text.Plain)
        }

        // will provide UI for viewing/deleting URLs
        get("/manage") {
            call.respondText("This route will display all saved urls", ContentType.Text.Plain)
        }

        // the endpoint to delete a specific URL
        post("/delete/{id}") {
            // maybe passing the id in the body woudl be better here?
            call.respondText("This route will enable deletion of url with id ${call.parameters["id"]}", ContentType.Text.Plain)
        }

        // the endpoint to actually process a shortened URL and redirect to real URL
        get("/{id}") {
            call.respondText("Will lookup the url for ${call.parameters["id"]}", ContentType.Text.Plain)
        }
    }
}
