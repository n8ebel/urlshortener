package com.goobar

import io.ktor.application.*
import io.ktor.html.*
import io.ktor.routing.*
import kotlinx.html.*

fun Routing.home() {
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
}