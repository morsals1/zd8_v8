package com.example.mebel

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import data.DatabaseHelper
import data.Detail // Убедитесь, что вы импортировали класс Detail

class SupplierMainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var detailsListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supplier_main)

        dbHelper = DatabaseHelper(this)
        detailsListView = findViewById(R.id.detailsListView)

        loadDetails() // Загружаем детали при создании активности
    }

    private fun loadDetails() {
        val details = dbHelper.getAllDetails() // Получаем все детали из базы данных
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, details.map { it.name })
        detailsListView.adapter = adapter

        detailsListView.setOnItemClickListener { _, _, position, _ ->
            val selectedDetail = details[position]
            Toast.makeText(this, "Выбранная деталь: ${selectedDetail.name}", Toast.LENGTH_SHORT).show()

            // Удаление детали
            val isDeleted = dbHelper.deleteDetail(selectedDetail.detailId) // Используем detailId для удаления
            if (isDeleted) {
                Toast.makeText(this, "Деталь удалена", Toast.LENGTH_SHORT).show()
                loadDetails() // Обновляем список после удаления
            } else {
                Toast.makeText(this, "Ошибка при удалении детали", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun ADD(view: View) {
        startActivity(Intent(this, SupplierDetailsActivity::class.java))
    }
}
