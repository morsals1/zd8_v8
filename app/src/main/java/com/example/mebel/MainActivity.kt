package com.example.mebel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.*
import data.DatabaseHelper

class MainActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginButton: Button = findViewById(R.id.loginButton)
        val registerButton: Button = findViewById(R.id.registerButton)
        val roleSpinner: Spinner = findViewById(R.id.roleSpinner)
        databaseHelper = DatabaseHelper(this)

        // Создаем массив ролей
        val roles = arrayOf("Работник", "Клиент", "Поставщик")

        // Создаем адаптер для Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        roleSpinner.adapter = adapter

        loginButton.setOnClickListener {
            val login = findViewById<EditText>(R.id.loginEditText).text.toString()
            val password = findViewById<EditText>(R.id.passwordEditText).text.toString()
            val role = roleSpinner.selectedItem.toString()

            if (login.isNotEmpty() && password.isNotEmpty()) {
                // Проверяем пользователя
                if (databaseHelper.checkUser (login, password, role)) {
                    // Получаем ID пользователя
                    val userId = databaseHelper.getClientIdByLogin(login) // Убедитесь, что этот метод возвращает правильный ID для всех ролей

                    // Сохраняем ID в SharedPreferences
                    val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
                    with(sharedPreferences.edit()) {
                        putInt("user_id", userId ?: -1) // Сохраняем ID или -1, если не найден
                        apply()
                    }

                    // Переход на экран работника/клиента/поставщика в зависимости от роли
                    val intent = when (role) {
                        "Работник" -> Intent(this, WorkerMainActivity::class.java)
                        "Клиент" -> Intent(this, ClientMainActivity::class.java)
                        "Поставщик" -> Intent(this, SupplierMainActivity::class.java)
                        else -> null
                    }

                    // Запускаем активити, если intent не null
                    intent?.let {
                        startActivity(it)
                    }

                } else {
                    Toast.makeText(this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            }
        }

        registerButton.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }
}
