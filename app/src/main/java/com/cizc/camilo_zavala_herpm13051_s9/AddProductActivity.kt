package com.cizc.camilo_zavala_herpm13051_s9

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.cizc.camilo_zavala_herpm13051_s9.DatabaseHandler
import com.cizc.camilo_zavala_herpm13051_s9.Product
import com.cizc.camilo_zavala_herpm13051_s9.R
import com.cizc.camilo_zavala_herpm13051_s9.Rating

class AddProductActivity : AppCompatActivity() {

    private lateinit var productName: EditText
    private lateinit var productPrice: EditText
    private lateinit var productDescription: EditText
    private lateinit var productCategory: EditText
    private lateinit var productRating: RatingBar
    private lateinit var addProductButton: Button
    private lateinit var productImage: ImageView
    private val PICK_IMAGE = 1
    private val TAKE_PHOTO = 2
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        productName = findViewById(R.id.productName)
        productPrice = findViewById(R.id.productPrice)
        productDescription = findViewById(R.id.productDescription)
        productCategory = findViewById(R.id.productCategory)
        productRating = findViewById(R.id.productRating)
        addProductButton = findViewById(R.id.addProductButton)
        productImage = findViewById(R.id.productImage)

        productImage.setOnClickListener {
            val options = arrayOf("Select from Gallery", "Take a Photo")
            val builder = AlertDialog.Builder(this)
            builder.setItems(options) { _, which ->
                when (which) {
                    0 -> {
                        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                        startActivityForResult(gallery, PICK_IMAGE)
                    }
                    1 -> {
                        val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(camera, TAKE_PHOTO)
                    }
                }
            }
            builder.show()
        }

        addProductButton.setOnClickListener {
            addProduct()
        }
    }

    private fun addProduct() {
        val name = productName.text.toString()
        val price = productPrice.text.toString().toDoubleOrNull()
        val description = productDescription.text.toString()
        val category = productCategory.text.toString()
        val rating = productRating.rating
        if (name.isNotEmpty() && price != null && description.isNotEmpty() && category.isNotEmpty() && imageUri != null) {
            val newProduct = Product(
                id = 0,
                title = name,
                price = price,
                description = description,
                category = category,
                image = imageUri.toString(),
                rating = Rating(rate = rating.toDouble(), count = 0)
            )
            val dbHandler = DatabaseHandler(this)
            dbHandler.addProduct(newProduct)
            Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE -> {
                    imageUri = data?.data
                    productImage.setImageURI(imageUri)
                }
                TAKE_PHOTO -> {
                    val photo = data?.extras?.get("data") as Bitmap
                    val path = MediaStore.Images.Media.insertImage(contentResolver, photo, "Title", null)
                    imageUri = Uri.parse(path)
                    productImage.setImageURI(imageUri)
                }
            }
        }
    }
}
