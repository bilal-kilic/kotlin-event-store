package com.example.domain.events

import com.example.domain.aliases.VariantId

data class MediaAddedToVariant(val id: VariantId, val path: String, val order: Int)