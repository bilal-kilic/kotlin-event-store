package com.example.infra.persistance

import java.util.*

data class EventDescriptor(val id: UUID, val eventData: Any, val version: Int)