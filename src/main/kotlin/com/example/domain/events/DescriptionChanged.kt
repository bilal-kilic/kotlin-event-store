package com.example.domain.events

import com.example.domain.aliases.ContentId

data class DescriptionChanged(val id: ContentId, val description: String) 