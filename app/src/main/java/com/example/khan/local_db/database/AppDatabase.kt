package com.example.khan.local_db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.khan.local_db.dao.CartDao
import com.example.khan.local_db.dao.OrderDao
import com.example.khan.local_db.dao.WishListDao
import com.example.khan.local_db.entity.CartItem
import com.example.khan.local_db.entity.OrderItem
import com.example.khan.local_db.entity.WishListItem


@Database(entities = [CartItem::class, WishListItem::class, OrderItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun wishListDao(): WishListDao
    abstract fun orderDao(): OrderDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
