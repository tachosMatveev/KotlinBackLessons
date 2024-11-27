package com.example.model

import kotlinx.datetime.Clock
import kotlin.time.TimeSource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

object PublicationsRepo {
    private var publications = mutableListOf<Publication>()

    fun allPublications(): List<Publication> = publications

    fun addPublication(publication: Publication) {
        publications.add(publication)
    }

    fun editPublication(pubId: String, body: String): Boolean {
        var editDone = false
        publications.filter { it.id == pubId }?.forEach {
            it.editingTime = Clock.System.now().toString()
            it.body = body
            editDone = true
        }

        return editDone
    }

    fun removePublication(pubId: String): Boolean {
        return publications.removeIf { it.id == pubId }
    }

    fun publicationById(pubId: String): Publication? {
        return publications.find { it.id == pubId }
    }
}