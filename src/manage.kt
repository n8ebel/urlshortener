package com.goobar

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*

fun Routing.manage() {
    // will provide UI for viewing/deleting URLs
    get<Manage> {
        call.respondText("This route will display all saved urls", io.ktor.http.ContentType.Text.Plain)
    }
}