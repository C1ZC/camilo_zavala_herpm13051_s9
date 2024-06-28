package com.cizc.camilo_zavala_herpm13051_s9

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "products.db"
        private const val TABLE_PRODUCTS = "products"
        private const val KEY_ID = "id"
        private const val KEY_TITLE = "title"
        private const val KEY_PRICE = "price"
        private const val KEY_DESCRIPTION = "description"
        private const val KEY_CATEGORY = "category"
        private const val KEY_IMAGE = "image"
        private const val KEY_RATING = "rating"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createProductsTable = ("CREATE TABLE $TABLE_PRODUCTS($KEY_ID INTEGER PRIMARY KEY,$KEY_TITLE TEXT,$KEY_PRICE REAL,$KEY_DESCRIPTION TEXT,$KEY_CATEGORY TEXT,$KEY_IMAGE TEXT,$KEY_RATING REAL)")
        db?.execSQL(createProductsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTS")
        onCreate(db)
    }

    fun addProduct(product: Product): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, product.id)
        contentValues.put(KEY_TITLE, product.title)
        contentValues.put(KEY_PRICE, product.price)
        contentValues.put(KEY_DESCRIPTION, product.description)
        contentValues.put(KEY_CATEGORY, product.category)
        contentValues.put(KEY_IMAGE, product.image)
        contentValues.put(KEY_RATING, product.rating.rate)
        return db.insert(TABLE_PRODUCTS, null, contentValues)
    }

    fun getAllProducts(): List<Product> {
        val productList: MutableList<Product> = mutableListOf()
        val selectQuery = "SELECT * FROM $TABLE_PRODUCTS"
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val product = Product(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                    title = cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE)),
                    price = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PRICE)),
                    description = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPTION)),
                    category = cursor.getString(cursor.getColumnIndexOrThrow(KEY_CATEGORY)),
                    image = cursor.getString(cursor.getColumnIndexOrThrow(KEY_IMAGE)),
                    rating = Rating(rate = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_RATING)), count = 0)
                )
                productList.add(product)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return productList
    }
}