package com.example.domain

import java.util.*
import kotlin.collections.HashSet

class Variant(
    val id: UUID,
    val barcode: String,
    val varianterAttribute: Attribute
) {
    private val images: HashSet<Image> = hashSetOf()

    fun addImage(image: Image) = images.add(image)
    fun getImages() = images.toList()
}