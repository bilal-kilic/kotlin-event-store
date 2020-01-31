package com.example.infra.persistance

import java.util.*

class EventStream(val aggregateId: UUID) {
    val changes: HashSet<EventDescriptor> = hashSetOf()

    fun addEvents(events: Collection<EventDescriptor>) {
        changes.addAll(events)
    }

    fun getEvents(): List<EventDescriptor> =
        changes
            .sortedBy { it.version }
            .toList()
}