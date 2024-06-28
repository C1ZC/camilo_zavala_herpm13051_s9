package com.cizc.camilo_zavala_herpm13051_s9

class ProductRepository(private val dbHandler: DatabaseHandler) {

    suspend fun addProducts(products: List<Product>) {
        for (product in products) {
            dbHandler.addProduct(product)
        }
    }

    suspend fun getAllProducts(): List<Product> {
        return dbHandler.getAllProducts()
    }
}
