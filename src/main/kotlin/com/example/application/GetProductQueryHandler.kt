package com.example.application

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