package com.example.mebel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import data.DatabaseHelper
import data.Order

class WorkerMainActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var ordersListView: ListView
    private lateinit var orderDescriptions: List<String>
    private lateinit var orders: List<Order> // Предполагается, что Order - это ваша модель данных заказа

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker_main)

        databaseHelper = DatabaseHelper(this)
        ordersListView = findViewById(R.id.ordersRecyclerView)

        // Загрузка заказов при создании активности
        loadOrders()

        // Обработчик нажатия на элемент списка
        ordersListView.setOnItemClickListener { parent, view, position, id ->
            val selectedOrder = orders[position]
            deleteOrder(selectedOrder)
        }
    }

    // Метод для загрузки заказов
    private fun loadOrders() {
        orders = databaseHelper.getAllOrders() // Получаем все заказы
        orderDescriptions = orders.map { order ->
            val furnitureType = databaseHelper.getFurnitureTypeById(order.furnitureTypeId)
            val furnitureTypeName = furnitureType?.name ?: "Неизвестный тип"
            "Order ID: ${order.orderId}, Status: ${order.status}, Furniture Type: $furnitureTypeName, Order Date: ${order.orderDate}"
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, orderDescriptions)
        ordersListView.adapter = adapter
    }

    // Метод для удаления заказа
    private fun deleteOrder(order: Order) {
        // Удаление заказа из базы данных
        val isDeleted = databaseHelper.deleteOrder(order.orderId) // Предполагается, что вы добавите этот метод в DatabaseHelper

        if (isDeleted) {
            Toast.makeText(this, "Заказ с удален", Toast.LENGTH_SHORT).show()
            loadOrders() // Обновление списка заказов
        } else {
            Toast.makeText(this, "Ошибка при удалении заказа", Toast.LENGTH_SHORT).show()
        }
    }
}
