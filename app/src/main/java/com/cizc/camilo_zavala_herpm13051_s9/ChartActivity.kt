package com.cizc.camilo_zavala_herpm13051_s9

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChartActivity : AppCompatActivity() {
    private lateinit var productRepository: ProductRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        productRepository = ProductRepository(DatabaseHandler(this))
        val barChart: BarChart = findViewById(R.id.barChart)

        CoroutineScope(Dispatchers.IO).launch {
            val products = productRepository.getAllProducts().sortedByDescending { it.rating.rate }.take(5)
            val entries = products.mapIndexed { index, product -> BarEntry(index.toFloat(), product.rating.rate.toFloat()) }
            val dataSet = BarDataSet(entries, "Top 5 Products")
            val barData = BarData(dataSet)

            withContext(Dispatchers.Main) {
                barChart.data = barData
                barChart.invalidate()
            }
        }
    }
}
