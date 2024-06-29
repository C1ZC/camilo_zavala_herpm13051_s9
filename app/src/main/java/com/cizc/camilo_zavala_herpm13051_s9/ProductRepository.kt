package com.cizc.camilo_zavala_herpm13051_s9

class ProductRepository(private val dbHandler: DatabaseHandler) {

    fun getAllProducts(): List<Product> {
        return dbHandler.getAllProducts()
    }

    fun addProducts(products: List<Product>) {
        products.forEach { dbHandler.addProduct(it) }
    }

    fun deleteProduct(product: Product) {
        dbHandler.deleteProduct(product.id)
    }
}
