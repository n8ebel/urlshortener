package com.goobar

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*

fun Routing.delete() {

    suspend fun PipelineContext<Unit, ApplicationCall>.onError() {
        call.respond(HttpStatusCode.BadRequest, "${HttpStatusCode.BadRequest.description} - missing id")
    }

    // the endpoint to delete a specific URL
    post<Delete> { delete ->
        try {
            val parameters = call.receiveParameters()
            if(parameters.contains("id")) {
                val id = parameters["id"]
                call.respondText("This route will enable deletion of url with id: $id", ContentType.Text.Plain)
            } else {
                onError()
            }
        } catch (error: ContentTransformationException) {
            onError()
        }
    }
}