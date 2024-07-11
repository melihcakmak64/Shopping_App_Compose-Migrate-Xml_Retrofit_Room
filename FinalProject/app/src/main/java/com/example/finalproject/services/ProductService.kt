import android.content.ContentValues
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.finalproject.configs.ProductDatabaseHelper
import com.example.finalproject.models.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ProductService private constructor(context: Context) : ProductDatabaseHelper(context) {

    private val gson = Gson()

    // LiveData to observe changes in products
    private val _productsLiveData = MutableLiveData<List<Product>>()
    val productsLiveData: LiveData<List<Product>> get() = _productsLiveData

    companion object {
        @Volatile private var instance: ProductService? = null

        fun initialize(context: Context) {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = ProductService(context)
                    }
                }
            }
        }

        fun getInstance(): ProductService {
            return instance ?: throw IllegalStateException("ProductService must be initialized by calling initialize(context: Context) first.")
        }
    }

    // Function to add a product
    fun addProduct(product: Product): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_ID, product.id)  // Adding the ID provided externally
            put(COLUMN_TITLE, product.title)
            put(COLUMN_DESCRIPTION, product.description)
            put(COLUMN_PRICE, product.price)
            put(COLUMN_DISCOUNT_PERCENTAGE, product.discountPercentage)
            put(COLUMN_RATING, product.rating)
            put(COLUMN_STOCK, product.stock)
            put(COLUMN_BRAND, product.brand)
            put(COLUMN_CATEGORY, product.category)
            put(COLUMN_THUMBNAIL, product.thumbnail)
            put(COLUMN_IMAGES, gson.toJson(product.images))
        }
        val newProductId = db.insert(TABLE_PRODUCTS, null, contentValues)

        // Notify LiveData observers about the change
        _productsLiveData.value = getAllProducts()

        return newProductId
        println("eklendii")
        println(_productsLiveData.value)
    }

    // Function to update a product
    fun updateProduct(product: Product) {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_TITLE, product.title)
            put(COLUMN_DESCRIPTION, product.description)
            put(COLUMN_PRICE, product.price)
            put(COLUMN_DISCOUNT_PERCENTAGE, product.discountPercentage)
            put(COLUMN_RATING, product.rating)
            put(COLUMN_STOCK, product.stock)
            put(COLUMN_BRAND, product.brand)
            put(COLUMN_CATEGORY, product.category)
            put(COLUMN_THUMBNAIL, product.thumbnail)
            put(COLUMN_IMAGES, gson.toJson(product.images))
        }
        db.update(TABLE_PRODUCTS, contentValues, "$COLUMN_ID=?", arrayOf(product.id.toString()))

        // Notify LiveData observers about the change
        _productsLiveData.value = getAllProducts()
    }

    // Function to delete a product
    fun deleteProduct(productId: Long) {
        val db = this.writableDatabase
        db.delete(TABLE_PRODUCTS, "$COLUMN_ID=?", arrayOf(productId.toString()))

        // Notify LiveData observers about the change
        _productsLiveData.value = getAllProducts()
        println("silindiii")
        println(_productsLiveData.value)
    }

    fun getProductById(productId: Long): Product? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_PRODUCTS,
            null,
            "$COLUMN_ID=?",
            arrayOf(productId.toString()),
            null,
            null,
            null
        )
        if (cursor.moveToFirst()) {
            val product = Product(
                id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)),
                discountPercentage = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISCOUNT_PERCENTAGE)),
                rating = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_RATING)),
                stock = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_STOCK)),
                brand = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BRAND)),
                category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                thumbnail = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_THUMBNAIL)),
                images = gson.fromJson(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGES)), object : TypeToken<List<String>>() {}.type)
            )
            cursor.close()
            return product
        }
        cursor.close()
        return null
    }

    fun getAllProducts(): List<Product> {
        val products = mutableListOf<Product>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_PRODUCTS,
            null,
            null,
            null,
            null,
            null,
            null
        )
        if (cursor.moveToFirst()) {
            do {
                val product = Product(
                    id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                    description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)),
                    discountPercentage = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISCOUNT_PERCENTAGE)),
                    rating = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_RATING)),
                    stock = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_STOCK)),
                    brand = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BRAND)),
                    category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                    thumbnail = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_THUMBNAIL)),
                    images = gson.fromJson(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGES)), object : TypeToken<List<String>>() {}.type)
                )
                products.add(product)
            } while (cursor.moveToNext())
        }
        cursor.close()

        // Update LiveData with the latest list of products
        _productsLiveData.value = products
        println("TÜM ÜRÜNLER GELİYORR")
        println(_productsLiveData.value)

        return products
    }
}
