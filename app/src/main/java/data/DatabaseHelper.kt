package data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import kotlin.Pair as Pair

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "furniture_db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Создание таблиц

            db.execSQL("""
        CREATE TABLE Clients (
            client_id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT,
            discount REAL
        )
    """)

            db.execSQL("""
        CREATE TABLE FurnitureTypes (
            furniture_type_id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT,
            photo_url TEXT
        )
    """)
            db.execSQL("""
        CREATE TABLE FurnitureItems (
            furniture_item_id INTEGER PRIMARY KEY AUTOINCREMENT,
            furniture_type_id INTEGER,
            model TEXT,
            FOREIGN KEY (furniture_type_id) REFERENCES FurnitureTypes(furniture_type_id)
        )
    """)
            db.execSQL("""
        CREATE TABLE PartTypes (
            part_type_id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT
        )
    """)
            db.execSQL("""
        CREATE TABLE Parts (
            part_id INTEGER PRIMARY KEY AUTOINCREMENT,
            part_type_id INTEGER,
            name TEXT,
            FOREIGN KEY (part_type_id) REFERENCES PartTypes(part_type_id)
        )
    """)
            db.execSQL("""
        CREATE TABLE PartCharacteristics (
            characteristic_id INTEGER PRIMARY KEY AUTOINCREMENT,
            part_id INTEGER,
            weight REAL,
            material TEXT,
            diameter REAL,
            FOREIGN KEY (part_id) REFERENCES Parts(part_id)
        )
    """)
            db.execSQL("""
        CREATE TABLE Orders (
            order_id INTEGER PRIMARY KEY AUTOINCREMENT,
            client_id INTEGER,
            furniture_type_id INTEGER,
            order_date DATETIME,
            status TEXT,
            FOREIGN KEY (client_id) REFERENCES Clients(client_id),
            FOREIGN KEY (furniture_type_id) REFERENCES FurnitureTypes(furniture_type_id)
        )
    """)

            // Добавьте создание таблицы Users
            db.execSQL("""
        CREATE TABLE Users (
            user_id INTEGER PRIMARY KEY AUTOINCREMENT,
            login TEXT UNIQUE,
            password TEXT,
            email TEXT,
            role TEXT
        )
    """)
        db.execSQL("""
        CREATE TABLE Details (
            detail_id INTEGER PRIMARY KEY AUTOINCREMENT,
            part_id INTEGER,
            name TEXT,
            FOREIGN KEY (part_id) REFERENCES Parts(part_id)
        )
    """)
        addSampleData(db)
    }

    private fun addSampleData(db: SQLiteDatabase) {
        // Добавление типов мебели
        val furnitureTypes = listOf(
            "Стол",
            "Стул",
            "Диван",
            "Кровать",
            "Шкаф",
            "Тумбочка"
        )

        for (type in furnitureTypes) {
            val values = ContentValues().apply {
                put("name", type)
                put("photo_url", "") // Если у вас есть URL для фото, добавьте его
            }
            db.insert("FurnitureTypes", null, values)
        }

        // Добавление предметов мебели с конкретными моделями
        val items = listOf(
            Pair(1, "Деревянный стол Guchi"),
            Pair(1, "Стеклянный стол Lux"),
            Pair(2, "Кожаный стул Comfort"),
            Pair(2, "Деревянный стул Classic"),
            Pair(3, "Угловой диван Relax"),
            Pair(4, "Двуспальная кровать Dream"),
            Pair(5, "Шкаф-купе Scroll"),
            Pair(5, "Шкаф угловой Space"),
            Pair(6, "Тумбочка Modern"),
            Pair(6, "Тумбочка Vintage")
        )

        for (item in items) {
            val values = ContentValues().apply {
                put("furniture_type_id", item.first)
                put("model", item.second)
            }
            db.insert("FurnitureItems", null, values)
        }

        // Добавление типов деталей
        val partTypes = listOf("Ножка", "Столешница", "Спинка", "Матрас", "Дверца", "Полка", "Ящик")

        for (partType in partTypes) {
            val values = ContentValues().apply {
                put("name", partType)
            }
            db.insert("PartTypes", null, values)
        }

        // Добавление деталей с уникальными материалами для каждого предмета мебели
        val parts = listOf(
            Pair(1, "Столешница стола"),
            Pair(1, "Ножка стола"),
            Pair(2, "Спинка стула"),
            Pair(2, "Ножка стула"),
            Pair(3, "Спинка дивана"),
            Pair(4, "Матрас кровати"),
            Pair(5, "Дверца шкафа"),
            Pair(5, "Полка шкафа"),
            Pair(5, "Ящик шкафа"),
            Pair(6, "Ящик тумбочки"),
            Pair(6, "Полка тумбочки")
        )

        for (part in parts) {
            val values = ContentValues().apply {
                put("part_type_id", part.first) // Используем id типа детали
                put("name", part.second)
            }
            val partId = db.insert("Parts", null, values)

            // Добавление характеристик для деталей с разнообразными материалами
            val characteristics = when (part.second) {
                "Столешница стола" -> listOf(
                    PartCharacteristic(partId, 5.0, "Дерево", 1.0),
                    PartCharacteristic(partId, 4.0, "Стекло", 0.0),
                    PartCharacteristic(partId, 0.5, "Ламинат", 0.0),
                    PartCharacteristic(partId, 3.0, "МДФ", 0.0),
                    PartCharacteristic(partId, 2.5, "Керамика", 0.0)
                )
                "Ножка стола" -> listOf(
                    PartCharacteristic(partId, 2.5, "Дерево", 0.0),
                    PartCharacteristic(partId, 1.5, "Металл", 0.0),
                    PartCharacteristic(partId, 0.3, "Пластик", 0.0),
                    PartCharacteristic(partId, 1.0, "Композитный материал", 0.0)
                )
                "Спинка стула" -> listOf(
                    PartCharacteristic(partId, 1.0, "Кожа", 0.0),
                    PartCharacteristic(partId, 0.8, "Ткань", 0.0),
                    PartCharacteristic(partId, 0.5, "Экокожа", 0.0)
                )
                "Матрас кровати" -> listOf(
                    PartCharacteristic(partId, 3.0, "Пенопласт", 0.0),
                    PartCharacteristic(partId, 2.5, "Латекс", 0.0),
                    PartCharacteristic(partId, 3.5, "Пружинный блок", 0.0)
                )
                "Дверца шкафа" -> listOf(
                    PartCharacteristic(partId, 2.0, "Дерево", 0.0),
                    PartCharacteristic(partId, 1.0, "Стекло", 0.0),
                    PartCharacteristic(partId, 1.5, "МДФ", 0.0)
                )
                "Полка шкафа" -> listOf(
                    PartCharacteristic(partId, 3.0, "Дерево", 0.0),
                    PartCharacteristic(partId, 1.5, "Стекло", 0.0),
                    PartCharacteristic(partId, 2.0, "Ламинат", 0.0)
                )
                "Ящик шкафа" -> listOf(
                    PartCharacteristic(partId, 2.0, "Дерево", 0.0),
                    PartCharacteristic(partId, 0.5, "Металл", 0.0),
                    PartCharacteristic(partId, 0.3, "Пластик", 0.0)
                )
                "Ящик тумбочки" -> listOf(
                    PartCharacteristic(partId, 1.5, "Дерево", 0.0),
                    PartCharacteristic(partId, 0.3, "Металл", 0.0),
                    PartCharacteristic(partId, 0.2, "Пластик", 0.0)
                )
                else -> emptyList()
            }

            for (characteristic in characteristics) {
                val values = ContentValues().apply {
                    put("part_id", partId) // Используем ID детали
                    put("weight", characteristic.weight)
                    put("material", characteristic.material)
                    put("diameter", characteristic.diameter)
                }
                db.insert("PartCharacteristics", null, values)
            }
        }
    }
    @SuppressLint("Range")
    fun getAllFurnitureItemsWithIds(): List<Pair<Int, String>> {
        val furnitureItems = mutableListOf<Pair<Int, String>>()
        val db = readableDatabase
        val cursor: Cursor? = db.rawQuery("""
        SELECT FurnitureItems.furniture_item_id, FurnitureItems.model, FurnitureTypes.name AS furniture_type_name 
        FROM FurnitureItems  
        INNER JOIN FurnitureTypes ON FurnitureItems.furniture_type_id = FurnitureTypes.furniture_type_id
    """, null)

        cursor?.let {
            if (it.moveToFirst()) {
                do {
                    val furnitureItemId = it.getInt(it.getColumnIndexOrThrow("furniture_item_id"))
                    val model = it.getString(it.getColumnIndexOrThrow("model"))

                    // Добавляем в список пару (ID, Название)
                    furnitureItems.add(Pair(furnitureItemId, model))
                } while (it.moveToNext())
            } else {
                Log.d("DatabaseHelper", "No furniture items found") // Логируем отсутствие данных
            }
            it.close()
        }
        db.close()
        return furnitureItems
    }
    @SuppressLint("Range")
    fun getAllDetails(): List<Detail> {
        val details = mutableListOf<Detail>()
        val db = readableDatabase
        val cursor: Cursor? = db.rawQuery("SELECT detail_id, name FROM Details", null) // Извлекаем только id и имя

        cursor?.let {
            if (it.moveToFirst()) {
                do {
                    val detailId = it.getInt(it.getColumnIndexOrThrow("detail_id"))
                    val name = it.getString(it.getColumnIndexOrThrow("name"))
                    details.add(Detail(detailId, name)) // Создаем объект Detail с id и именем
                } while (it.moveToNext())
            }
            it.close()
        }
        db.close()
        return details
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) { // Если вы обновляете с версии 1 до 2
            db.execSQL("ALTER TABLE Orders ADD COLUMN furniture_type_id INTEGER")
        }
        db.execSQL("DROP TABLE IF EXISTS Orders")
        db.execSQL("DROP TABLE IF EXISTS PartCharacteristics")
        db.execSQL("DROP TABLE IF EXISTS Parts")
        db.execSQL("DROP TABLE IF EXISTS PartTypes")
        db.execSQL("DROP TABLE IF EXISTS FurnitureItems")
        db.execSQL("DROP TABLE IF EXISTS FurnitureTypes")
        db.execSQL("DROP TABLE IF EXISTS Clients")
        onCreate(db)
    }



    @SuppressLint("Range")
    fun getAllPartTypes(): List<PartType> {
        val partTypes = mutableListOf<PartType>()
        val db = readableDatabase
        val cursor: Cursor? = db.rawQuery("SELECT * FROM PartTypes", null)

        cursor?.let {
            if (it.moveToFirst()) {
                do {
                    val partTypeId = it.getInt(it.getColumnIndexOrThrow("part_type_id"))
                    val name = it.getString(it.getColumnIndexOrThrow("name"))
                    partTypes.add(PartType(partTypeId, name)) // Предполагается, что у вас есть класс PartType
                } while (it.moveToNext())
            }
            it.close()
        }
        db.close()
        return partTypes
    }
    @SuppressLint("Range")
    fun getFurnitureItemById(itemId: Int): Pair<FurnitureItem?, List<PartCharacteristics>> {
        val db = readableDatabase
        val cursor: Cursor? = db.rawQuery("""
        SELECT FurnitureItems.furniture_item_id, FurnitureItems.model, FurnitureTypes.name AS furniture_type_name 
        FROM FurnitureItems 
        INNER JOIN FurnitureTypes ON FurnitureItems.furniture_type_id = FurnitureTypes.furniture_type_id
        WHERE FurnitureItems.furniture_item_id = ?
    """, arrayOf(itemId.toString()))

        var furnitureItem: FurnitureItem? = null
        cursor?.let {
            if (it.moveToFirst()) {
                val furnitureItemId = it.getInt(it.getColumnIndexOrThrow("furniture_item_id"))
                val model = it.getString(it.getColumnIndexOrThrow("model"))
                val furnitureTypeName = it.getString(it.getColumnIndexOrThrow("furniture_type_name"))

                furnitureItem = FurnitureItem(furnitureItemId, model, furnitureTypeName)
            }
            it.close() // Закрываем курсор
        }
        db.close() // Закрываем базу данных

        // Получение деталей для предмета мебели
        val partsCharacteristics = getPartsForFurnitureItem(itemId)

        return Pair(furnitureItem, partsCharacteristics)
    }
    fun addDetail(partTypeId: Int, name: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("part_id", partTypeId) // Указываем id типа детали
            put("name", name) // Указываем имя детали
        }
        return db.insert("Details", null, values) // Вставляем данные в таблицу Details
    }

    @SuppressLint("Range")
    fun getPartsForFurnitureItem(furnitureItemId: Int): List<PartCharacteristics> {
        val partsCharacteristics = mutableListOf<PartCharacteristics>()
        val db = readableDatabase
        val cursor: Cursor? = db.rawQuery("""
        SELECT DISTINCT PartCharacteristics.material 
        FROM Parts 
        INNER JOIN PartCharacteristics ON Parts.part_id = PartCharacteristics.part_id
        INNER JOIN FurnitureItems ON FurnitureItems.furniture_item_id = ?
    """, arrayOf(furnitureItemId.toString()))

        cursor?.let {
            if (it.moveToFirst()) {
                do {
                    val material = it.getString(it.getColumnIndexOrThrow("material"))
                    // Добавляем только материал, без других характеристик
                    partsCharacteristics.add(PartCharacteristics(0, 0, "", 0.0, material, 0.0))
                } while (it.moveToNext())
            }
            it.close() // Закрываем курсор
        }
        db.close() // Закрываем базу данных
        return partsCharacteristics
    }

    fun deleteDetail(detailId: Int): Boolean {
        val db = writableDatabase
        val result = db.delete("Details", "detail_id = ?", arrayOf(detailId.toString()))
        db.close()
        return result > 0 // Возвращает true, если удаление прошло успешно
    }

    // Методы для управления заказами
    fun addOrder(clientId: Int, furnitureTypeId: Int, orderDate: String, status: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("client_id", clientId)
            put("furniture_type_id", furnitureTypeId)
            put("order_date", orderDate)
            put("status", status)
        }

        // Логируем данные, которые мы пытаемся вставить
        Log.d("DatabaseHelper", "Adding order: clientId=$clientId, furnitureTypeId=$furnitureTypeId, orderDate=$orderDate, status=$status")

        val newRowId = db.insert("Orders", null, values)
        db.close()

        // Проверяем, был ли добавлен заказ
        if (newRowId == -1L) {
            Log.e("DatabaseHelper", "Failed to insert order")
        } else {
            Log.d("DatabaseHelper", "Order added with ID: $newRowId")
        }

        return newRowId
    }

    @SuppressLint("Range")
    fun getAllOrders(): List<Order> {
        val orders = mutableListOf<Order>()
        val db = readableDatabase
        val cursor: Cursor? = db.rawQuery("SELECT * FROM Orders", null)

        cursor?.let {
            if (it.moveToFirst()) {
                do {
                    val orderId = it.getInt(it.getColumnIndexOrThrow("order_id"))
                    val clientId = it.getInt(it.getColumnIndexOrThrow("client_id"))
                    val furnitureTypeId = it.getInt(it.getColumnIndexOrThrow("furniture_type_id"))
                    val orderDate = it.getString(it.getColumnIndexOrThrow("order_date"))
                    val status = it.getString(it.getColumnIndexOrThrow("status"))

                    orders.add(Order(orderId, clientId, furnitureTypeId, orderDate, status))
                } while (it.moveToNext())
            }
            it.close() // Закрываем курсор
        }
        db.close() // Закрываем базу данных
        return orders
    }

    fun addUser (login: String, password: String, email: String, role: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("login", login)
            put("password", password)
            put("email", email)
            put("role", role)
        }
        val newRowId = db.insert("Users", null, values)
        db.close()
        return newRowId
    }

    // Метод для проверки существующего логина
    fun isLoginExists(login: String): Boolean {
        val db = readableDatabase
        val cursor: Cursor? = db.rawQuery("SELECT * FROM Users WHERE login = ?", arrayOf(login))
        val exists = cursor != null && cursor.count > 0
        cursor?.close()
        db.close()
        return exists
    }
    fun checkUser (login: String, password: String, role: String): Boolean {
        val db = readableDatabase
        val cursor: Cursor? = db.rawQuery(
            "SELECT * FROM Users WHERE login = ? AND password = ? AND role = ?",
            arrayOf(login, password, role)
        )
        val exists = cursor != null && cursor.count > 0
        cursor?.close()
        db.close()
        return exists
    }

    @SuppressLint("Range")
    fun getFurnitureTypeById(furnitureTypeId: Int): FurnitureType? {
        val db = readableDatabase
        val cursor: Cursor? = db.rawQuery("SELECT * FROM FurnitureTypes WHERE furniture_type_id = ?", arrayOf(furnitureTypeId.toString()))

        val furnitureType: FurnitureType? = if (cursor != null && cursor.moveToFirst()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            FurnitureType(furnitureTypeId, name) // Убедитесь, что у вас есть класс FurnitureType
        } else {
            null
        }

        cursor?.close()
        db.close()
        return furnitureType
    }

    @SuppressLint("Range")
    fun getClientIdByLogin(login: String): Int? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT user_id FROM Users WHERE login = ?", arrayOf(login))

        return if (cursor.moveToFirst()) {
            val clientId = cursor.getInt(cursor.getColumnIndex("user_id"))
            cursor.close()
            clientId
        } else {
            cursor.close()
            null // Если клиент не найден
        }
    }

    fun deleteOrder(orderId: Int): Boolean {
        val db = writableDatabase
        val result = db.delete("Orders", "order_id = ?", arrayOf(orderId.toString()))
        db.close()
        return result > 0 // Возвращает true, если удаление прошло успешно
    }
    @SuppressLint("Range")
    fun getAllFurnitureItems(): List<FurnitureItem> {
        val furnitureItems = mutableListOf<FurnitureItem>()
        val db = readableDatabase
        val cursor: Cursor? = db.rawQuery("""
        SELECT FurnitureItems.furniture_item_id, FurnitureItems.model, FurnitureTypes.name AS furniture_type_name 
        FROM FurnitureItems  
        INNER JOIN FurnitureTypes ON FurnitureItems.furniture_type_id = FurnitureTypes.furniture_type_id
    """, null)

        cursor?.let {
            if (it.moveToFirst()) {
                do {
                    val furnitureItemId = it.getInt(it.getColumnIndexOrThrow("furniture_item_id"))
                    val model = it.getString(it.getColumnIndexOrThrow("model"))
                    val furnitureTypeName = it.getString(it.getColumnIndexOrThrow("furniture_type_name"))

                    furnitureItems.add(FurnitureItem(furnitureItemId, model, furnitureTypeName))
                } while (it.moveToNext())
            } else {
                Log.d("DatabaseHelper", "No furniture items found") // Логируем отсутствие данных
            }
            it.close()
        }
        db.close()
        return furnitureItems
    }



}

// Классы для представления данных
data class Order(val orderId: Int, val clientId: Int, val furnitureTypeId: Int, val orderDate: String, val status: String)
data class FurnitureItem(val furnitureItemId: Int, val model: String, val furnitureTypeName: String)
data class FurnitureType(val furnitureTypeId: Int, val name: String)
data class PartType(val partTypeId: Int, val name: String) {
    override fun toString(): String {
        return name // Это будет отображаться в Spinner
    }
}
data class Detail(
    val detailId: Int,
    val name: String
)
data class PartCharacteristic(
    val partId: Long,
    val weight: Double,
    val material: String,
    val diameter: Double
)
data class PartCharacteristics(
    val partId: Int,
    val partTypeId: Int,
    val name: String,
    val weight: Double,
    val material: String,
    val diameter: Double
)
