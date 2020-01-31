package com.example.domain

import java.util.*
import kotlin.collections.HashSet

class Variant(
    val id: UUID,
    val barcode: String,
    val varianterAttribute: Attribute
) {
    private val medias: HashSet<Media> = hashSetOf()

    fun addImage(media: Media) = medias.add(media)
    fun getImages() = medias.toList()
}