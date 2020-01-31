package com.example.application

import com.example.domain.Brand
import com.trendyol.kediatr.Command

data class CreateProductCommand(val code: String, val brand: Brand, val category: CharCategory) : Command 