package com.example

import com.example.model.Publication
import com.example.model.PublicationsRepo
import io.ktor.http.*
import io.ktor.serialization.JsonConvertException
import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.AutoHeadResponse
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    install(AutoHeadResponse)
    routing {
        get("/all_tasks") {
            call.respond(PublicationsRepo.allPublications())
        }

        get("/task/{pubId}") {
            val pubId = call.parameters["pubId"]
            if (pubId == null) {
                call.respond(HttpStatusCode.BadRequest)
            } else {
                val publication = PublicationsRepo.publicationById(pubId)
                if (publication != null) {
                    call.respond(publication)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }

        get("/auth") {
            call.respondText(text = "This is auth", contentType = ContentType.parse("application/json"))
        }

        route("/create_task") {
            post {
                try {
                    val publication = call.receive<Publication>()
                    PublicationsRepo.addPublication(publication)
                    call.respond(HttpStatusCode.NoContent)
                } catch (ex: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (ex: JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }

        route("/edit_task") {
            patch("/{pubId}") {
                val pubId = call.parameters["pubId"]
                if (pubId == null) {
                    call.respond(HttpStatusCode.BadRequest)
                } else {
                    val pubBody = call.receiveText()
                    if (PublicationsRepo.editPublication(pubId, pubBody)) {
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                }
            }

            delete("/{pubId}") {
                val pubId = call.parameters["pubId"]
                if (pubId == null) {
                    call.respond(HttpStatusCode.BadRequest)
                } else {
                    if (PublicationsRepo.removePublication(pubId)) {
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                }
            }
        }
    }
}
