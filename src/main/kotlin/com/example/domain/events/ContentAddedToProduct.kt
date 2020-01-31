package com.example.domain.events

import com.example.domain.aliases.ContentId

data class ContentAddedToProduct(
    val id: ContentId,
    val description: String,
    val slicerAttributeId: Int,
    val slicerAttributeValueId: Int
)
