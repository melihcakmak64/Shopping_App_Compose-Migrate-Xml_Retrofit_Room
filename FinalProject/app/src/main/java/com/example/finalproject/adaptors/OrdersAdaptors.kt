package com.example.finalproject.adaptors



import android.app.Activity
import android.content.Context

import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.models.CartProduct
import com.example.finalproject.models.Product


class OrdersAdaptors(
    private val context: Context,
    private var arr: MutableList<CartProduct>,



    ) : ArrayAdapter<CartProduct>(context, R.layout.product_cart_row, arr) {

    val productService=ProductService.getInstance()





    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rootView = convertView ?: (context as Activity).layoutInflater.inflate(R.layout.product_cart_row, parent, false)
        val dt = arr[position]

        val r_image = rootView.findViewById<ImageView>(R.id.r_image)
        val r_title = rootView.findViewById<TextView>(R.id.r_title)
        val r_price = rootView.findViewById<TextView>(R.id.r_price)


        r_title.text = dt.title
        r_price.text = "${dt.price}₺"

        val url = dt.thumbnail
        Glide.with(rootView).load(url).into(r_image)

        return rootView
    }

    fun updateData(newData: List<CartProduct>) {
        arr.clear()
        arr.addAll(newData) // Yeni verileri mevcut verilere ekle
        notifyDataSetChanged()
    }


}
