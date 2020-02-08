package com.example.infra.persistance.couchbase

import com.couchbase.client.java.Bucket
import com.couchbase.client.java.Cluster
import com.couchbase.client.java.ReactiveCollection
import com.example.infra.ConcurrencyException
import com.example.infra.persistance.EventDescriptor
import com.example.infra.persistance.IEventStore
import kotlinx.coroutines.reactive.awaitFirst
import java.util.*
import kotlin.collections.HashSet

class CouchbaseEventStore : IEventStore {
    private var cluster = Cluster.connect("127.0.0.1", "pim", "123qwe")
    private var bucket: Bucket
    private var collection: ReactiveCollection

    init {
        bucket = cluster.bucket("ProductEvents")
        collection = bucket.defaultCollection().reactive()
    }

    override suspend fun getEventForAggregateAsync(id: UUID): List<Any> {
        val events = collection.get(id.toString()).awaitFirst().contentAs(HashSet::class.java) as HashSet<EventDescriptor>
        return events.sortedBy { it.version }.map { it.eventData }
    }

    override suspend fun saveAsync(aggregateId: UUID, events: List<Any>, currentVersion: Int) {
        var existingEvents = collection.get(aggregateId.toString()).awaitFirst().contentAs(HashSet::class.java) as HashSet<EventDescriptor>?

        if (existingEvents == null) {
            existingEvents = hashSetOf()
        } else if (existingEvents.last().version != currentVersion) {
            throw ConcurrencyException()
        }

        var version = currentVersion
        events.forEach {
            version++
            existingEvents.add(EventDescriptor(aggregateId, it, version))

            //Can publish event for subscribers
        }
        
        
        collection.upsert(aggregateId.toString(), existingEvents)
    }

    override fun getEventForAggregate(id: UUID): List<Any> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun save(aggregateId: UUID, events: List<Any>, currentVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

} 