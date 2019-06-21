package com.sarah.fastfoodrestmenu.DataBase.DAO

import android.arch.persistence.room.*
import com.sarah.fastfoodrestmenu.DataBase.ModalDB.Cart
import io.reactivex.Flowable

@Dao
interface CartDAO {
    @Query("SELECT * FROM Cart")
    fun getCartItems (): Flowable<MutableList<Cart>>

    @Query("SELECT * FROM Cart WHERE id IN (:cartItemId)")
    fun loadAllByIds(cartItemId: IntArray): List<Cart>

    @Query("SELECT COUNT(*) FROM Cart")
    fun countCartItems():Int

    @Insert
    fun insertItem(carts: Cart)

    @Delete
    fun delete(carts: Cart)
}