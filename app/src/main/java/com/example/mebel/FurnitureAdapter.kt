package com.example.mebel

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView
import data.FurnitureItem

class FurnitureAdapter(private val context: Context, private var furnitureItems: List<FurnitureItem>) : BaseAdapter() {

    override fun getCount(): Int = furnitureItems.size

    override fun getItem(position: Int): Any = furnitureItems[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemView = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_furniture, parent, false)
        val furnitureItem = getItem(position) as FurnitureItem
        val modelTextView = itemView.findViewById<TextView>(R.id.modelTextView)
        modelTextView.text = furnitureItem.model
        return itemView
    }

    fun updateData(newData: List<FurnitureItem>) {
        furnitureItems = newData
        notifyDataSetChanged()
    }
}