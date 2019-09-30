package com.example.reminder

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class ProductRoomDatabase : RoomDatabase() {

    abstract fun reminderDao(): ProductDao

    companion object {
        private const val DATABASE_NAME = "REMINDER_DATABASE"

        @Volatile
        private var productRoomDatabaseInstance: ProductRoomDatabase? = null

        fun getDatabase(context: Context): ProductRoomDatabase? {
            if (productRoomDatabaseInstance == null) {
                synchronized(ProductRoomDatabase::class.java) {
                    if (productRoomDatabaseInstance == null) {
                        productRoomDatabaseInstance = Room.databaseBuilder(
                            context.applicationContext,
                            ProductRoomDatabase::class.java, DATABASE_NAME
                        )
                            .build()
                    }
                }
            }
            return productRoomDatabaseInstance
        }
    }

}
