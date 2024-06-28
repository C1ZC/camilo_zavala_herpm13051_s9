package com.cizc.camilo_zavala_herpm13051_s9

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
class ProductAdapter(private var products: MutableList<Product>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.productTitle)
        val price: TextView = itemView.findViewById(R.id.productPrice)
        val rating: TextView = itemView.findViewById(R.id.productRating)
        val image: ImageView = itemView.findViewById(R.id.productImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.title.text = product.title
        holder.price.text = product.price.toString()
        holder.rating.text = product.rating.rate.toString()
        Glide.with(holder.itemView.context).load(product.image).into(holder.image)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun setProducts(products: List<Product>) {
        this.products = products.toMutableList()
        notifyDataSetChanged()
    }
}