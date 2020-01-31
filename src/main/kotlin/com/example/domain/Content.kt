package com.example.domain

import com.example.domain.aliases.ContentId
import kotlin.collections.HashSet

class Content(
    val id: ContentId,
    val description: String,
    val slicerAttribute: Attribute
) {
    private val variants: HashSet<Variant> = hashSetOf()
    
    fun getVariants() = variants.toList()
    
    fun addVariant(variant: Variant) = variants.add(variant)
}