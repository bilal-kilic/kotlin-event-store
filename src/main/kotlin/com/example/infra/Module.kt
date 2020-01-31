package com.example.infra

import com.example.application.GetProductQuery
import com.trendyol.kediatr.CommandBusBuilder
import org.koin.dsl.module

val diModule = module { 
    single {
        CommandBusBuilder(GetProductQuery::class.java).build()
    }
}