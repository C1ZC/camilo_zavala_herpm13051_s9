package com.cizc.camilo_zavala_herpm13051_s9

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.io.ByteArrayOutputStream

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
            saveProduct()
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                PICK_IMAGE -> {
                    imageUri = data.data
                    productImage.setImageURI(imageUri)
                }
                TAKE_PHOTO -> {
                    val bitmap = data.extras?.get("data") as Bitmap
                    productImage.setImageBitmap(bitmap)
                    imageUri = getImageUri(bitmap)
                }
            }
        }
    }

    private fun getImageUri(bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Title", null)
        return Uri.parse(path)
    }

    private fun convertImageUriToBase64(uri: Uri): String {
        val inputStream = contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun saveProduct() {
        val imageBase64 = imageUri?.let { convertImageUriToBase64(it) }
        val product = Product(
            id = 0, // Valor temporal para el ID
            title = productName.text.toString(),
            price = productPrice.text.toString().toDouble(),
            description = productDescription.text.toString(),
            category = productCategory.text.toString(),
            image = imageBase64 ?: "",
            rating = Rating(productRating.rating.toDouble(), 0)
        )
        val dbHandler = DatabaseHandler(this)
        dbHandler.addProduct(product)
    }
}
