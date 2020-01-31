package test.kotlin

import com.example.domain.Attribute
import com.example.domain.Brand
import com.example.domain.Category
import com.example.domain.Product
import com.example.domain.aliases.ContentId
import com.example.domain.aliases.ProductId
import com.example.domain.aliases.VariantId
import com.example.infra.persistance.ProductRepository
import com.example.infra.persistance.inmemory.InMemoryEventStore
import org.junit.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ProductTests {
    @Test
    fun `content should be added to product`() {
        //given
        val product = Product(ProductId.randomUUID(), "ProductCode-1", Brand(1, "Adidas"), Category(1, "Electronic"))
        val slicerAttribute = Attribute(1, 2)
        //when
        product.addContent(ContentId.randomUUID(), "asdasd", slicerAttribute)

        //then
        assertTrue(product.getContents().count() == 1)
        assertEquals(product.getContents().first().slicerAttribute, slicerAttribute)
    }

    @Test
    fun `should add variant to content`() {
        //given
        val product = Product(ProductId.randomUUID(), "ProductCode-1", Brand(1, "Adidas"), Category(1, "Electronic"))
        val slicerAttribute = Attribute(1, 2)
        val varianter = Attribute(2, 4)

        //when
        product.addContent(ContentId.randomUUID(), "asdasdad", slicerAttribute)
        product.addVariant(VariantId.randomUUID(), "asdasda", slicerAttribute, varianter)

        //then
        val content = product.getContents().first { it.slicerAttribute == slicerAttribute }
        assertTrue(content.getVariants().size == 1)
        assertNotNull(content.getVariants().first { it.varianterAttribute == varianter })
    }

    @Test
    fun `should save product`() {
        //given
        val eventStore = InMemoryEventStore()
        val productRepository = ProductRepository(eventStore)

        val product = Product(ProductId.randomUUID(), "ProductCode-1", Brand(1, "Adidas"), Category(1, "Electronic"))

        //when
        productRepository.save(product)

        //then
        assertDoesNotThrow { productRepository.load(product.id) }
    }
}