package com.goobar

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

class CustomException : RuntimeException()

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(StatusPages) {
        exception<Throwable> { cause ->
            // Display generic error response to user
            call.respond(HttpStatusCode.InternalServerError, "Internal Server Error")
            // re-throw to ensure it is not swallowed by status page
            throw cause
        }

        exception<CustomException> { cause ->
            // Display specific error response to user
            call.respond(HttpStatusCode.InternalServerError, "Custom Server Error")
            // re-throw to ensure it is not swallowed by status page
            throw cause
        }
    }

    routing {
        get("/") {
            throw RuntimeException("oops")
            call.respondText("We're going to shorten some URLs!!", contentType = ContentType.Text.Plain)
        }
        get("/custom") {
            throw CustomException()
            call.respondText("Testing StatusPages feature", contentType = ContentType.Text.Plain)
        }

    }
}
