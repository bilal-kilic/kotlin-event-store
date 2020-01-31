package com.example.domain

import com.example.domain.aliases.ContentId
import com.example.domain.aliases.ProductId
import com.example.domain.aliases.VariantId
import com.example.domain.events.ContentAddedToProduct
import com.example.domain.events.ProductCreated
import com.example.domain.events.VariantAddedToContent
import com.example.infra.BusinessException
import java.util.*

class Product(
    val id: ProductId
) : Aggregate() {

    private lateinit var code: String
    private lateinit var brand: Brand
    private lateinit var category: Category

    constructor(productId: ProductId, code: String, brand: Brand, category: Category) : this(productId) {
        this.raiseEvent(ProductCreated(code, brand, category))
    }

    init {
        this.register<VariantAddedToContent>(this::apply)
        this.register<ContentAddedToProduct>(this::apply)
        this.register<ProductCreated>(this::apply)
    }

    private val contents: HashSet<Content> = hashSetOf()

    private val attributes: HashSet<Attribute> = hashSetOf()

    fun getContents() = contents.toList()

    fun getAttributes() = attributes.toList()

    fun addContent(id: ContentId, description: String, slicerAttribute: Attribute) {
        if (contents.any()) {
            validate(contents.any { it.slicerAttribute.id == slicerAttribute.id }) { "Content must have the same slicer type as existing contents" }
        }

        validate(contents.all { it.slicerAttribute != slicerAttribute }) { "Another content with given slicer exists" }

        raiseEvent(ContentAddedToProduct(id, description, slicerAttribute.id, slicerAttribute.valueId))
    }

    fun addVariant(id: VariantId, barcode: String, slicerAttribute: Attribute, varianterAttribute: Attribute) {
        val content = contents.firstOrNull { it.slicerAttribute.id == slicerAttribute.id }
            ?: throw BusinessException("Content not found")

        val variantsOfContent = content.getVariants()
        if (variantsOfContent.any()) {
            validate(variantsOfContent.any { it.varianterAttribute.id == varianterAttribute.id }) { "Variant must have the same slicer type as existing variants" }
        }

        validate(variantsOfContent.all { it.varianterAttribute != varianterAttribute }) { "Another variant with given varianter exists" }

        raiseEvent(VariantAddedToContent(id, content.id, barcode, varianterAttribute.id, varianterAttribute.valueId))
    }

    private fun apply(event: VariantAddedToContent) {
        val content = contents.first { it.id == event.contentId }
        content.addVariant(Variant(event.id, event.barcode, Attribute(event.varianterAttributeId, event.varianterAttributeValueId)))
    }

    private fun apply(event: ContentAddedToProduct) {
        contents.add(Content(event.id, event.description, Attribute(event.slicerAttributeId, event.slicerAttributeValueId)))
    }

    private fun apply(event: ProductCreated) {
        this.code = event.code
        this.brand = event.brand
        this.category = event.category
    }

    companion object {
        fun from(id: ProductId, events: Collection<Any>): Product =
            events.fold(Product(id)) { product, event ->
                product.applyEvent(event)
                product
            }
    }
}