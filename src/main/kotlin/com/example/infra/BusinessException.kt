package com.example.infra

class BusinessException(message: String): RuntimeException()

class AggregateNotFoundException() : RuntimeException()

class ConcurrencyException() : RuntimeException()
