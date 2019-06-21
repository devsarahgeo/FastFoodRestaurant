package com.sarah.fastfoodrestmenu.DataBase.ModalDB

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "Cart")
class Cart {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    @ColumnInfo(name = "NAME")
    var name: String = ""

    @ColumnInfo(name = "IMAGE")
    var image: Int = 0

    @ColumnInfo(name = "AMOUNT")
    var amount: Int = 0
}