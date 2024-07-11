package com.example.finalproject.configs

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

open class ProductDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "products.db"
        const val DATABASE_VERSION = 1
        const val TABLE_PRODUCTS = "products"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_PRICE = "price"
        const val COLUMN_DISCOUNT_PERCENTAGE = "discountPercentage"
        const val COLUMN_RATING = "rating"
        const val COLUMN_STOCK = "stock"
        const val COLUMN_BRAND = "brand"
        const val COLUMN_CATEGORY = "category"
        const val COLUMN_THUMBNAIL = "thumbnail"
        const val COLUMN_IMAGES = "images"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_PRODUCTS_TABLE = ("CREATE TABLE $TABLE_PRODUCTS ("
                + "$COLUMN_ID INTEGER PRIMARY KEY,"
                + "$COLUMN_TITLE TEXT,"
                + "$COLUMN_DESCRIPTION TEXT,"
                + "$COLUMN_PRICE REAL,"
                + "$COLUMN_DISCOUNT_PERCENTAGE REAL,"
                + "$COLUMN_RATING REAL,"
                + "$COLUMN_STOCK INTEGER,"
                + "$COLUMN_BRAND TEXT,"
                + "$COLUMN_CATEGORY TEXT,"
                + "$COLUMN_THUMBNAIL TEXT,"
                + "$COLUMN_IMAGES TEXT)")
        db.execSQL(CREATE_PRODUCTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTS")
        onCreate(db)
    }
}
