package com.cizc.camilo_zavala_herpm13051_s9

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray

object ApiService {
    private val client = OkHttpClient()

    suspend fun getProducts(): List<Product> = withContext(Dispatchers.IO) {
        val request = Request.Builder().url("https://fakestoreapi.com/products").build()
        val response = client.newCall(request).execute()
        val jsonArray = JSONArray(response.body?.string())

        List(jsonArray.length()) { i ->
            val jsonObject = jsonArray.getJSONObject(i)
            Product(
                id = jsonObject.getInt("id"),
                title = jsonObject.getString("title"),
                price = jsonObject.getDouble("price"),
                description = jsonObject.getString("description"),
                category = jsonObject.getString("category"),
                image = jsonObject.getString("image"),
                rating = Rating(
                    rate = jsonObject.getJSONObject("rating").getDouble("rate"),
                    count = jsonObject.getJSONObject("rating").getInt("count")
                )
            )
        }
    }
}
