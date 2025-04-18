package com.example.mebel

import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import data.DatabaseHelper
import data.PartCharacteristics

class ItemDetailsActivity : AppCompatActivity() {

    private lateinit var partsAdapter: PartsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)

        val itemId = intent.getIntExtra("ITEM_ID", -1)

        val dbHelper = DatabaseHelper(this)
        // Получаем предмет мебели и его детали
        val (furnitureItem, parts) = dbHelper.getFurnitureItemById(itemId)

        val itemDetailsTextView: TextView = findViewById(R.id.itemDetailsTextView)
        itemDetailsTextView.text = furnitureItem?.model ?: "Неизвестный предмет"
        Log.d("ItemDetailsActivity", "Parts Count: ${parts.size}")
        // Убедитесь, что parts - это список PartCharacteristics
        val partsListView: ListView = findViewById(R.id.partsListView)
        partsAdapter = PartsAdapter(this, parts) // Здесь parts - это список PartCharacteristics
        partsListView.adapter = partsAdapter
    }
}


