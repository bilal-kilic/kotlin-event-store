package com.example.application.queries

import com.example.application.queries.GetProductQuery
import com.trendyol.kediatr.AsyncQueryHandler
import com.example.domain.Product
import com.example.domain.persistance.IProductRepository
import java.util.*

class GetProductQueryHandler(private val repository: IProductRepository) :
    AsyncQueryHandler<Product, GetProductQuery> {
    override suspend fun handleAsync(query: GetProductQuery): Product = with(query) {
        repository.load(UUID.fromString(id))
    }
}