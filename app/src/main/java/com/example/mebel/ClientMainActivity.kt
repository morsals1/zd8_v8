package com.example.mebel

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import data.DatabaseHelper
import data.FurnitureItem

class ClientMainActivity : AppCompatActivity() {

    private lateinit var furnitureAdapter: FurnitureAdapter
    private lateinit var allFurnitureItems: List<FurnitureItem> // Сохраняем все предметы мебели

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_main)

        // Получение данных о мебели
        val dbHelper = DatabaseHelper(this)
        allFurnitureItems = dbHelper.getAllFurnitureItems() // Получите все элементы мебели
        furnitureAdapter = FurnitureAdapter(this, allFurnitureItems)

        // Установка адаптера для ListView
        val listView: ListView = findViewById(R.id.furnitureRecyclerView)
        listView.adapter = furnitureAdapter

        // Обработка нажатий на элементы списка
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = furnitureAdapter.getItem(position) as FurnitureItem
            val intent = Intent(this, ItemDetailsActivity::class.java)
            intent.putExtra("ITEM_ID", selectedItem.furnitureItemId)
            startActivity(intent)
        }

        // Настройка поля поиска
        val searchEditText: EditText = findViewById(R.id.searchEditText)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().toLowerCase()
                filterFurnitureItems(query)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun filterFurnitureItems(query: String) {
        val filteredItems = allFurnitureItems.filter {
            it.model.toLowerCase().contains(query)
        }
        furnitureAdapter.updateData(filteredItems)
    }

    fun oformit(view: View) {
        startActivity(Intent(this, ClientOrderActivity::class.java))
    }
}

