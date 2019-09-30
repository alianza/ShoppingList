package com.example.reminder

import android.content.Context

class ProductRepository(context: Context) {

    private var productDao: ProductDao

    init {
        val reminderRoomDatabase = ProductRoomDatabase.getDatabase(context)
        productDao = reminderRoomDatabase!!.reminderDao()
    }

    suspend fun getAllReminders(): List<Product> {
        return productDao.getAllReminders()
    }

    suspend fun insertReminder(product: Product) {
        productDao.insertReminder(product)
    }

    suspend fun deleteReminder(product: Product) {
        productDao.deleteReminder(product)
    }

    suspend fun updateReminder(product: Product) {
        productDao.updateReminder(product)
    }

}
