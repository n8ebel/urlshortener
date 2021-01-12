package com.goobar

import io.ktor.application.*
import io.ktor.content.*
import io.ktor.features.*
import io.ktor.html.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import kotlinx.html.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

class CustomException : RuntimeException()

private val HttpStatusCode.Companion.CustomError: HttpStatusCode
    get() = HttpStatusCode(900, "Some Custom Error Occurred")

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(StatusPages) {
        exception<Throwable> { cause ->
            // Display generic error response to user
            call.respond(HttpStatusCode.InternalServerError, "Server Error")
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

        // our home route
        // will eventually provide the UI to shorten a URL
        get("/") {
            call.respondHtml {
                head {
                    title("Url Shortener")
                }
                body {
                    h1 {
                        +"Ktor Url Shortener"
                    }
                    p {
                        +"This is where we will eventually enter urls to be shortened"
                    }
                }
            }
        }

        // the endpoint that will actually shorten and return a URL
        put("/shorten") {
            call.respondText("This route will return a shortened url", ContentType.Text.Plain)
        }

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
            call.respondText("This route will enable deletion of url with id ${call.parameters["id"]}", ContentType.Text.Plain)
        }

        // the endpoint to actually process a shortened URL and redirect to real URL
        get("/{id}") {
            call.respondText("Will lookup the url for ${call.parameters["id"]}", ContentType.Text.Plain)
        }
    }
}
