package com.sarah.fastfoodrestmenu.DataBase.Database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.sarah.fastfoodrestmenu.DataBase.DAO.CartDAO
import com.sarah.fastfoodrestmenu.DataBase.ModalDB.Cart

@Database(entities = arrayOf(Cart::class), version = 2)
abstract class CartDatabase :RoomDatabase(){
        abstract fun cartDao(): CartDAO
        lateinit var instance:CartDatabase

        fun getInstance(context: Context):CartDatabase {
            if(instance == null) {
                instance = Room.databaseBuilder(
                        context,
                        CartDatabase::class.java, "Cart-Database"
                ).fallbackToDestructiveMigration().build()
            }
         return instance
        }
}
