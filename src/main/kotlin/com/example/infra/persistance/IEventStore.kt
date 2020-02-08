package com.example.infra.persistance

import java.util.*
import kotlin.collections.HashSet

interface IEventStore {
    fun getEventForAggregate(id: UUID): List<Any>
    
    suspend fun getEventForAggregateAsync(id: UUID): List<Any>
    
    fun save(aggregateId: UUID, events: List<Any>, currentVersion: Int)
    
    suspend fun saveAsync(aggregateId: UUID, events: List<Any>, currentVersion: Int)
}
