package com.example.domain.events

import com.example.domain.Brand
import com.example.domain.Category

data class ProductCreated(
    val code: String,
    val brand: Brand,
    val category: Category
)