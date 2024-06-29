package com.cizc.camilo_zavala_herpm13051_s9

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ProductAdapter(
    private val products: MutableList<Product>
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val selectedProducts = mutableListOf<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = products.size

    fun setProducts(newProducts: List<Product>) {
        products.clear()
        products.addAll(newProducts)
        notifyDataSetChanged()
    }

    fun getSelectedProducts(): List<Product> = selectedProducts

    fun toggleSelection(product: Product) {
        if (selectedProducts.contains(product)) {
            selectedProducts.remove(product)
        } else {
            selectedProducts.add(product)
        }
        notifyDataSetChanged()
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.productTitle)
        private val price: TextView = itemView.findViewById(R.id.productPrice)
        private val rating: TextView = itemView.findViewById(R.id.productRating)
        private val image: ImageView = itemView.findViewById(R.id.productImage)

        fun bind(product: Product) {
            title.text = product.title
            price.text = product.price.toString()
            rating.text = "Rating: ${product.rating.rate} (${product.rating.count})"

            Glide.with(itemView.context)
                .load(product.image)
                .apply(RequestOptions().placeholder(R.drawable.placeholder).error(R.drawable.error))
                .into(image)

            itemView.setOnLongClickListener {
                toggleSelection(product)
                true
            }

            itemView.setBackgroundColor(
                if (selectedProducts.contains(product)) {
                    itemView.context.getColor(R.color.selected_item)
                } else {
                    itemView.context.getColor(R.color.default_item)
                }
            )
        }
    }
}
