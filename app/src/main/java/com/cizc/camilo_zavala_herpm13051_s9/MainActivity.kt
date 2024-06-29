package com.cizc.camilo_zavala_herpm13051_s9

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var productRepository: ProductRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        productAdapter = ProductAdapter(mutableListOf())
        recyclerView.adapter = productAdapter

        productRepository = ProductRepository(DatabaseHandler(this))

        CoroutineScope(Dispatchers.IO).launch {
            if (productRepository.getAllProducts().isEmpty()) {
                val products = ApiService.getProducts()
                productRepository.addProducts(products)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Productos cargados correctamente", Toast.LENGTH_SHORT).show()
                    Log.d("MainActivity", "Productos cargados correctamente")
                }
            }
            loadProducts()
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (layoutManager.findLastCompletelyVisibleItemPosition() == productAdapter.itemCount - 1) {
                    // Load more products here
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_add -> {
                val intent = Intent(this, AddProductActivity::class.java)
                startActivityForResult(intent, ADD_PRODUCT_REQUEST)
                true
            }
            R.id.menu_delete -> {
                deleteSelectedProducts()
                true
            }
            R.id.menu_chart -> {
                val intent = Intent(this, ChartActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_PRODUCT_REQUEST && resultCode == Activity.RESULT_OK) {
            loadProducts()
        }
    }

    private fun loadProducts() {
        CoroutineScope(Dispatchers.IO).launch {
            val products = productRepository.getAllProducts()
            withContext(Dispatchers.Main) {
                productAdapter.setProducts(products)
            }
        }
    }

    private fun deleteProduct(product: Product) {
        CoroutineScope(Dispatchers.IO).launch {
            productRepository.deleteProduct(product)
            loadProducts()
        }
    }

    private fun deleteSelectedProducts() {
        val selectedProducts = productAdapter.getSelectedProducts()
        CoroutineScope(Dispatchers.IO).launch {
            selectedProducts.forEach { productRepository.deleteProduct(it) }
            withContext(Dispatchers.Main) {
                loadProducts()
            }
        }
    }

    companion object {
        private const val ADD_PRODUCT_REQUEST = 1
    }
}