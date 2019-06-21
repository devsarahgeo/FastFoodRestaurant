package com.sarah.fastfoodrestmenu

import android.arch.persistence.room.Room
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.sarah.fastfoodrestmenu.DataBase.Database.CartDatabase
import com.sarah.fastfoodrestmenu.DataBase.ModalDB.Cart
import kotlinx.android.synthetic.main.activity_food_details.*

class FoodDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_details)

        val bundle:Bundle=intent.extras
        ivImage.setImageResource(bundle.getInt("image"))
        tvName.text=bundle.getString("name")
        tvDesc.text=bundle.getString("desc")
        tvAmt.text = bundle.getInt("amt").toString()

        val db = Room.databaseBuilder(applicationContext, CartDatabase::class.java,"Cart-Database").build()


        addCart.setOnClickListener{
            addCart.isEnabled = false
            FoodTicket(tvName.text.toString(),tvDesc.text.toString(),bundle.getInt("image"),bundle.getInt("amt"))
            Thread{
                val cartItem = Cart()
                cartItem.id = 0
                cartItem.name = tvName.text.toString()
                cartItem.amount = bundle.getInt("amt")
                cartItem.image = bundle.getInt("image")
                db.cartDao().insertItem(cartItem)


//                db.cartDao().getCartItems().forEach{
//                    Log.i("CartItems","id:${cartItem.id}")
//                    Log.i("CartItems","name:${ cartItem.name }")
//                    Log.i("CartItems","amt:${cartItem.amount }")
//
//                }
            }.start()


        }


    }
}
