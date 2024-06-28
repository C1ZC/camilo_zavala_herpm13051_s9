package com.cizc.camilo_zavala_herpm13051_s9

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
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add -> {
                startActivity(Intent(this, AddProductActivity::class.java))
                return true
            }
            R.id.menu_chart -> {
                startActivity(Intent(this, ChartActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadProducts() {
        CoroutineScope(Dispatchers.IO).launch {
            val products = productRepository.getAllProducts()
            withContext(Dispatchers.Main) {
                productAdapter.setProducts(products)
            }
        }
    }
}
