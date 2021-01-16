package com.goobar

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*

fun Routing.saved() {
    // will provide a list of all saved URLs
    get<Saved> {
        call.respondText("This route will return all saved urls", io.ktor.http.ContentType.Text.Plain)
    }
}