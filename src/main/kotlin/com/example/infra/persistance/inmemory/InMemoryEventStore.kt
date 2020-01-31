package com.example.infra.persistance.inmemory

import com.example.infra.persistance.IEventStore
import com.example.infra.AggregateNotFoundException
import com.example.infra.ConcurrencyException
import com.example.infra.persistance.EventDescriptor
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class InMemoryEventStore : IEventStore {
    private val eventStore: HashMap<UUID, HashSet<EventDescriptor>> = hashMapOf()

    override fun getEventForAggregate(id: UUID): List<Any> {
        val events = eventStore[id] ?: throw AggregateNotFoundException()
        return events.sortedBy { it.version }.map { it.eventData }
    }

    override fun save(aggregateId: UUID, events: List<Any>, currentVersion: Int) {
        var existingEvents = eventStore[aggregateId]

        if (existingEvents == null) {
            existingEvents = hashSetOf()
            eventStore.putAll(hashMapOf(Pair(aggregateId, existingEvents)))
        } else if (existingEvents.last().version != currentVersion) {
            throw ConcurrencyException()
        }

        var version = currentVersion
        events.forEach {
            version++
            existingEvents.add(EventDescriptor(aggregateId, it, version))

            //Can publish event for subscribers
        }
    }
}