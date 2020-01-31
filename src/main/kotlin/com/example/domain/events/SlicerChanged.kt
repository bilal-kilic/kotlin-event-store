package com.example.domain.events

import com.example.domain.aliases.ContentId

data class SlicerChanged(val contenId: ContentId, val id: Int, val valueId: Int)