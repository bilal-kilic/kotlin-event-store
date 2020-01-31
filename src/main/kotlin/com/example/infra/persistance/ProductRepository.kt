package com.example.infra.persistance

import com.example.domain.Product
import com.example.domain.aliases.ProductId
import com.example.domain.persistance.IProductRepository
import com.example.infra.persistance.IEventStore

class ProductRepository(private val eventStore: IEventStore) : IProductRepository {
    override fun load(id: ProductId): Product {
        val events = eventStore.getEventForAggregate(id)
        return Product.from(id, events)
    }

    override fun save(product: Product) {
        eventStore.save(product.id, product.getUncommittedEvents(), product.version)
        product.clearUncommittedEvents()
    }
}