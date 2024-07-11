package com.example.finalproject.adaptors

import android.app.Activity
import android.content.Context

import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.models.Product
import com.example.finalproject.viewmodel.HomeViewModel


class ProductAdaptors(
    private val context: Context,
    private var arr: MutableList<Product>,
    private val onItemClick: (Product) -> Unit



) : ArrayAdapter<Product>(context, R.layout.product_row, arr) {

    val productService=ProductService.getInstance()

    override fun getItemId(position: Int): Long {
        return arr[position].id // Burada id veya başka bir benzersiz kimlik döndürülmeli
    }



    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rootView = convertView ?: (context as Activity).layoutInflater.inflate(R.layout.product_row, parent, false)
        val dt = arr[position]

        val r_image = rootView.findViewById<ImageView>(R.id.r_image)
        val r_title = rootView.findViewById<TextView>(R.id.r_title)
        val r_price = rootView.findViewById<TextView>(R.id.r_price)
        rootView.findViewById<Button>(R.id.btnAddToCart).setOnClickListener {
            onItemClick(dt) // Tıklanan ürünü HomeFragment'e iletiyoruz
        }
        val checkBoxFav = rootView.findViewById<CheckBox>(R.id.checkBoxFav)

        r_title.text = dt.title
        r_price.text = "${dt.price}₺"

        val url = dt.thumbnail
        Glide.with(rootView).load(url).into(r_image)

        checkBoxFav.setOnCheckedChangeListener(null) // Remove the listener temporarily

        checkBoxFav.isChecked = productService.getProductById(dt.id) != null

        checkBoxFav.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                productService.addProduct(dt)
            } else {
                productService.deleteProduct(dt.id)
            }
        }





        return rootView
    }

    fun updateData(newData: List<Product>) {
        arr.clear()
        arr.addAll(newData) // Yeni verileri mevcut verilere ekle
        notifyDataSetChanged()
    }


}
