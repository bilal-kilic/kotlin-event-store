package com.example.application.commands

import com.example.domain.Brand
import com.example.domain.Category
import com.trendyol.kediatr.Command

data class CreateProductCommand(val code: String, val brand: Brand, val category: Category) : Command 