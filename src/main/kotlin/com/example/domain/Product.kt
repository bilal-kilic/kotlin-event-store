package com.example.domain

import com.example.domain.aliases.ContentId
import com.example.domain.aliases.ProductId
import com.example.domain.aliases.VariantId
import com.example.domain.events.*
import com.example.infra.BusinessException

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
        this.register<MediaAddedToVariant>(this::apply)
        this.register<DescriptionChanged>(this::apply)
        this.register<BrandChanged>(this::apply)
        this.register<CategoryChanged>(this::apply)
        this.register<SlicerChanged>(this::apply)
    }

    private val contents: HashSet<Content> = hashSetOf()

    private val attributes: HashSet<Attribute> = hashSetOf()

    //region Product
    fun getAttributes() = attributes.toList()

    fun changeBrand(brand: Brand) = raiseEvent(BrandChanged(brand.id, brand.name))

    fun changeCategory(category: Category) = raiseEvent(CategoryChanged(category.id, category.name))

    //endregion

    //region Content
    fun getContents() = contents.toList()

    fun addContent(id: ContentId, description: String, slicerAttribute: Attribute) {
        if (contents.any()) {
            validate(contents.any { it.slicerAttribute.id == slicerAttribute.id }) { "Content must have the same slicer type as existing contents" }
        }

        validate(contents.none { it.slicerAttribute == slicerAttribute }) { "Another content with given slicer exists" }

        raiseEvent(ContentAddedToProduct(id, description, slicerAttribute.id, slicerAttribute.valueId))
    }

    fun changeDescription(id: ContentId, description: String) {
        val content = contents.firstOrNull { it.id == id }

        validate(content != null) { "Content not found" }

        raiseEvent(DescriptionChanged(id, description))
    }

    fun changeSlicer(id: ContentId, slicerAttribute: Attribute) {
        val content = contents.firstOrNull { it.id == id }

        validate(content != null) { "Content not found" }

        validate(this.getContents().none { it.slicerAttribute == slicerAttribute }) { "Another content with given slicer exists" }

        raiseEvent(SlicerChanged(id, slicerAttribute.id, slicerAttribute.valueId))
    }
    //endregion

    //region Variant
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

    fun addMedia(id: VariantId, media: Media) {
        val variant = contents.flatMap { it.getVariants() }.firstOrNull()

        validate(variant != null) { "Variant not found" }

        raiseEvent(MediaAddedToVariant(id, media.path, media.order))
    }
    //endregion

    //region Domain Event Appliers
    private fun apply(event: VariantAddedToContent) {
        val content = contents.first { it.id == event.contentId }
        content.addVariant(
            Variant(
                event.id,
                event.barcode,
                Attribute(event.varianterAttributeId, event.varianterAttributeValueId)
            )
        )
    }

    private fun apply(event: ContentAddedToProduct) {
        contents.add(
            Content(
                event.id,
                event.description,
                Attribute(event.slicerAttributeId, event.slicerAttributeValueId)
            )
        )
    }

    private fun apply(event: ProductCreated) {
        this.code = event.code
        this.brand = event.brand
        this.category = event.category
    }

    private fun apply(event: MediaAddedToVariant) {
        val variant = contents.flatMap { it.getVariants() }.first()

        variant.addImage(Media(event.path, event.order))
    }

    private fun apply(event: DescriptionChanged) {
        val content = contents.first { it.id == event.id }
        content.description = event.description
    }

    private fun apply(event: BrandChanged) {
        this.brand = Brand(event.id, event.name)
    }

    private fun apply(event: CategoryChanged) {
        category = Category(event.id, event.name)
    }

    private fun apply(event: SlicerChanged) {
        val content = contents.first { it.id == event.contenId }
        content.slicerAttribute = Attribute(event.id, event.valueId)
    }
    //endregion

    companion object {
        fun from(id: ProductId, events: Collection<Any>): Product =
            events.fold(Product(id)) { product, event ->
                product.applyEvent(event)
                product
            }
    }
}