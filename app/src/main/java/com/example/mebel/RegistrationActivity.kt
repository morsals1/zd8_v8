package com.example.mebel

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import data.DatabaseHelper

class RegistrationActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        databaseHelper = DatabaseHelper(this)

        val registerSubmitButton: Button = findViewById(R.id.registerSubmitButton)
        val roleSpinner: Spinner = findViewById(R.id.registerRoleSpinner)
        val roles = arrayOf("Работник", "Клиент", "Поставщик")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        roleSpinner.adapter = adapter
        registerSubmitButton.setOnClickListener {
            val login = findViewById<EditText>(R.id.registerLoginEditText).text.toString()
            val password = findViewById<EditText>(R.id.registerPasswordEditText).text.toString()
            val email = findViewById<EditText>(R.id.registerEmailEditText).text.toString()
            val role = findViewById<Spinner>(R.id.registerRoleSpinner).selectedItem.toString()
            if (login.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()) {
                if (databaseHelper.isLoginExists(login)) {
                    Toast.makeText(this, "Логин уже существует", Toast.LENGTH_SHORT).show()
                } else {
                    // Логика регистрации пользователя
                    databaseHelper.addUser (login, password, email, role)
                    Toast.makeText(this, "Пользователь зарегистрирован ", Toast.LENGTH_SHORT).show()
                    finish() // Закрыть экран регистрации
                }
            } else {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            }
        }
    }
}