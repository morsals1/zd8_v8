package com.example.mebel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import data.DatabaseHelper
import java.text.SimpleDateFormat
import java.util.Locale

class ClientOrderActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var furnitureTypeSpinner: Spinner
    private lateinit var orderButton: Button
    private lateinit var clientIdEditText: EditText
    private lateinit var orderDateEditText: EditText
    private lateinit var statusEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_order)

        databaseHelper = DatabaseHelper(this)

        furnitureTypeSpinner = findViewById(R.id.furnitureTypeSpinner)
        orderButton = findViewById(R.id.orderButton)
        orderDateEditText = findViewById(R.id.orderDateEditText)
        statusEditText = findViewById(R.id.statusEditText)

        // Получаем ID клиента из SharedPreferences
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val clientId = sharedPreferences.getInt("user_id", -1) // -1 - значение по умолчанию, если не найден

        // Заполняем Spinner доступными типами мебели
        loadFurnitureTypes()

        orderButton.setOnClickListener {
            val selectedItem = furnitureTypeSpinner.selectedItem.toString()
            val furnitureTypeId = selectedItem.split(":")[0].trim().toInt() // Получаем ID выбранного типа мебели
            val orderDate = orderDateEditText.text.toString()
            val status = statusEditText.text.toString()

            if (clientId != -1 && furnitureTypeId != -1 && orderDate.isNotEmpty() && status.isNotEmpty()) {
                // Проверка корректности даты
                if (isValidDate(orderDate)) {
                    try {
                        val newOrderId = databaseHelper.addOrder(clientId, furnitureTypeId, orderDate, status)
                        Toast.makeText(this, "Заказ оформлен", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Log.e("ClientOrderActivity", "Error adding order: ${e.message}")
                        Toast.makeText(this, "Ошибка при добавлении заказа: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Введите корректную дату в формате дд.мм.гггг", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Пожалуйста, заполните все поля корректно", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadFurnitureTypes() {
        val allFurnitureItems = databaseHelper.getAllFurnitureItemsWithIds() // Получаем все предметы мебели с их ID
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, allFurnitureItems.map { "${it.first}: ${it.second}" }) // Форматируем строку как "ID: Название"
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        furnitureTypeSpinner.adapter = adapter
    }

    private fun isValidDate(date: String): Boolean {
        return try {
            val format = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            format.isLenient = false // Установите в false, чтобы исключить нечеткие даты
            format.parse(date) != null // Если дата корректная, метод parse вернет непустое значение
        } catch (e: Exception) {
            false // Если возникло исключение, дата некорректная
        }
    }
}
