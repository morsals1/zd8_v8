package com.example.mebel

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import data.DatabaseHelper
import data.PartType

class SupplierDetailsActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var partTypeSpinner: Spinner
    private lateinit var detailNameEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supplier_details)

        dbHelper = DatabaseHelper(this)
        partTypeSpinner = findViewById(R.id.partTypeSpinner)
        detailNameEditText = findViewById(R.id.detailNameEditText)
        val addButton: Button = findViewById(R.id.addButton)

        // Загружаем типы деталей в Spinner
        loadPartTypes()

        addButton.setOnClickListener {
            val name = detailNameEditText.text.toString()
            val selectedPartType = partTypeSpinner.selectedItem as PartType

            if (name.isNotEmpty()) {
                // Добавляем деталь в новую таблицу Details
                val newDetailId = dbHelper.addDetail(selectedPartType.partTypeId, name) // Обновите метод для добавления детали
                if (newDetailId != -1L) {
                    Toast.makeText(this, "Деталь добавлена: $name", Toast.LENGTH_SHORT).show()
                    finish() // Закрыть экран после добавления детали
                } else {
                    Toast.makeText(this, "Ошибка при добавлении детали", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Введите название детали", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadPartTypes() {
        val partTypes = dbHelper.getAllPartTypes() // Получаем все типы деталей из БД
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, partTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        partTypeSpinner.adapter = adapter
    }
}
