package com.example.domain

interface IAggregate {
    var version: Int
    
    fun applyEvent(event: Any?)

    fun clearUncommittedEvents()

    fun getUncommittedEvents(): List<Any>
}