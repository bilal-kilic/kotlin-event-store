package com.example.application.commands

import com.example.domain.Product
import com.example.domain.persistance.IProductRepository
import com.trendyol.kediatr.AsyncCommandHandler

class CreateProductCommandHandler(private val productRepository: IProductRepository) : AsyncCommandHandler<CreateProductCommand> {
    override suspend fun handleAsync(command: CreateProductCommand) = with(command) {
        val product = Product(code = code, brand = brand, category = category)
        productRepository.save(product)
    }
}