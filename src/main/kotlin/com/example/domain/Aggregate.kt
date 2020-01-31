package com.example.domain

abstract class Aggregate : IAggregate {
    private val handlers = (emptyMap<Class<*>?, (Any) -> Unit>()).toMutableMap()

    private val uncommittedEvents: HashSet<Any> = hashSetOf()

    override var version: Int = 0

    internal inline fun <reified T> register(noinline handler: ((T) -> Unit)?) {
        if (handler == null) throw NullPointerException("Expression 'handler' must not be null")

        handlers[T::class.java] = { handler(it as T) }
    }

    fun raiseEvent(event: Any) {
        applyEvent(event)
        this.uncommittedEvents.add(event)
    }

    override fun applyEvent(event: Any?) {
        if (event == null) throw NullPointerException("Expression 'event' must not be null")

        val handler = handlers[event::class.java] ?: throw NullPointerException("Expression 'handler' must not be null")

        handler(event)
        this.version++
    }

    override fun clearUncommittedEvents() = this.uncommittedEvents.clear()

    override fun getUncommittedEvents(): List<Any> = uncommittedEvents.toList()
}

