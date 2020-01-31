package com.example.domain.events

import com.example.domain.aliases.ContentId
import com.example.domain.aliases.VariantId

data class VariantAddedToContent(
    val id: VariantId,
    val contentId: ContentId,
    val barcode: String,
    val varianterAttributeId: Int,
    val varianterAttributeValueId: Int
)