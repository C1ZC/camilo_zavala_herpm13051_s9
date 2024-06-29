package com.cizc.camilo_zavala_herpm13051_s9

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createProductsTable = "CREATE TABLE $TABLE_PRODUCTS (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_TITLE TEXT," +
                "$COLUMN_PRICE REAL," +
                "$COLUMN_DESCRIPTION TEXT," +
                "$COLUMN_CATEGORY TEXT," +
                "$COLUMN_IMAGE TEXT," +
                "$COLUMN_RATING REAL," +
                "$COLUMN_COUNT INTEGER)"
        db?.execSQL(createProductsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTS")
        onCreate(db)
    }

    fun addProduct(product: Product) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, product.title)
            put(COLUMN_PRICE, product.price)
            put(COLUMN_DESCRIPTION, product.description)
            put(COLUMN_CATEGORY, product.category)
            put(COLUMN_IMAGE, product.image)
            put(COLUMN_RATING, product.rating.rate)
            put(COLUMN_COUNT, product.rating.count)
        }
        db.insert(TABLE_PRODUCTS, null, values)
        db.close()
    }

    fun deleteProduct(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_PRODUCTS, "$COLUMN_ID=?", arrayOf(id.toString()))
        db.close()
    }

    fun getAllProducts(): List<Product> {
        val productList = mutableListOf<Product>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_PRODUCTS", null)
        if (cursor.moveToFirst()) {
            do {
                val product = Product(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                    price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)),
                    description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                    image = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE)),
                    rating = Rating(
                        rate = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_RATING)),
                        count = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COUNT))
                    )
                )
                productList.add(product)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return productList
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "productsManager"
        private const val TABLE_PRODUCTS = "products"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_PRICE = "price"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_CATEGORY = "category"
        private const val COLUMN_IMAGE = "image"
        private const val COLUMN_RATING = "rating"
        private const val COLUMN_COUNT = "count"
    }
}
