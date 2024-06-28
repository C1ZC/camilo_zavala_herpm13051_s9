package com.cizc.camilo_zavala_herpm13051_s9

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddProductActivity : AppCompatActivity() {
    private lateinit var productRepository: ProductRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        productRepository = ProductRepository(DatabaseHandler(this))

        val titleInput: EditText = findViewById(R.id.productTitleInput)
        val priceInput: EditText = findViewById(R.id.productPriceInput)
        val addButton: Button = findViewById(R.id.addButton)

        addButton.setOnClickListener {
            val title = titleInput.text.toString()
            val price = priceInput.text.toString().toDoubleOrNull()

            if (title.isNotEmpty() && price != null) {
                val newProduct = Product(
                    id = 0, title = title, price = price, description = "", category = "", image = "", rating = Rating(0.0, 0)
                )
                CoroutineScope(Dispatchers.IO).launch {
                    productRepository.addProducts(listOf(newProduct))
                    withContext(Dispatchers.Main) {
                        Log.d("AddProductActivity", "Producto agregado: $newProduct")
                        finish()
                    }
                }
            }
        }
    }
}
