package com.goobar

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*

// the endpoint that will actually shorten and return a URL
fun Routing.shorten() {

    suspend fun PipelineContext<Unit, ApplicationCall>.onError() {
        call.respond(HttpStatusCode.BadRequest, "${HttpStatusCode.BadRequest.description} - missing target url")
    }

    put<Shorten> { shorten ->
        call.receive<Parameters>()
        try {
            val parameters = call.receiveParameters()
            if(parameters.contains("url")) {
                val url = parameters["url"]
                call.respondText("${shorten.url} This route will return a shortened version of $url", ContentType.Text.Plain)
            } else {
                onError()
            }
        } catch (error: ContentTransformationException) {
            onError()
        }
    }
}