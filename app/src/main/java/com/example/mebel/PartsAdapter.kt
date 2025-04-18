package com.example.mebel

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import data.PartCharacteristics

class PartsAdapter(context: Context, private val parts: List<PartCharacteristics>) : ArrayAdapter<PartCharacteristics>(context, 0, parts) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val part = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.part_item, parent, false)

        val partMaterialTextView: TextView = view.findViewById(R.id.partMaterialTextView)
        Log.d("PartsAdapter", "Displaying Part Material: ${part?.material}")
        // Отображаем только материал детали
        partMaterialTextView.text = part?.material ?: "Неизвестный материал"

        return view
    }
}

