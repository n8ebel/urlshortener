package com.goobar

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*

fun Routing.redirectUrl() {
    // the endpoint to actually process a shortened URL and redirect to real URL
    get<RedirectUrl> { redirect ->
        call.respondText("This route will lookup, and redirect to, the saved url for id: ${redirect.id}", io.ktor.http.ContentType.Text.Plain)
    }
}