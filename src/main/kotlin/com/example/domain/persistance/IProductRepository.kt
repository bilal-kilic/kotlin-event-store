package com.example.domain.persistance

import com.example.domain.Product
import java.util.*

interface IProductRepository {
    fun load(id: UUID): Product
    fun save(product: Product)
}