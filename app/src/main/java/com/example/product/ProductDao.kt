package com.example.reminder

import androidx.room.*

@Dao
interface ProductDao {

    @Query("SELECT * FROM reminderTable")
    suspend fun getAllReminders(): List<Product>

    @Insert
    suspend fun insertReminder(product: Product)

    @Delete
    suspend fun deleteReminder(product: Product)

    @Update
    suspend fun updateReminder(product: Product)

}
