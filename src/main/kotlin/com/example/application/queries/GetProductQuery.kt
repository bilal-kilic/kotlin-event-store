package com.example.application.queries

import com.example.domain.Product
import com.trendyol.kediatr.Query

data class GetProductQuery(val id: String) : Query<Product>


