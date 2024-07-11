package com.example.finalproject.adaptors
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.finalproject.models.Category


class CategoryAdapter(
    private val context: Context,
    private var arr: MutableList<Category>
) : ArrayAdapter<Category>(context, android.R.layout.simple_list_item_1, arr) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rootView = convertView ?: (context as Activity).layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false)
        val dt = arr[position]

        val textView = rootView.findViewById<TextView>(android.R.id.text1)
        textView.text = dt.name

        return rootView
    }

    fun updateData(newData: List<Category>) {
        arr.clear()
        arr.addAll(newData)
        notifyDataSetChanged()
    }
}
