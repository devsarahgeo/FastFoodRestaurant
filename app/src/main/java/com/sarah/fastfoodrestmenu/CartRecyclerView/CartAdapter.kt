package com.sarah.fastfoodrestmenu.CartRecyclerView

import android.arch.persistence.room.Room
import android.content.Context
import android.support.annotation.NonNull
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import com.sarah.fastfoodrestmenu.DataBase.Database.CartDatabase
import com.sarah.fastfoodrestmenu.DataBase.ModalDB.Cart
import com.sarah.fastfoodrestmenu.R
import com.squareup.picasso.Picasso

class CartAdapter(val context: Context, val cartList:MutableList<Cart>, @NonNull mListener:OnItemClickListener): RecyclerView.Adapter<CartViewHolder>() {
    interface OnItemClickListener{
        fun onDelete(position: Int)
    }
    var onItemClick : CartAdapter.OnItemClickListener

    init{
        this.onItemClick = mListener
    }
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_cart_layout, parent, false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    override fun onBindViewHolder(holder: CartViewHolder?, position: Int) {
        val db = Room.databaseBuilder(context, CartDatabase::class.java,"Cart-Database").build()
        val cartPosn = cartList[position]
        val currentPosn:Int = holder!!.adapterPosition
        holder!!.cartName.text = cartPosn.name
        holder!!.cartAmount.text = cartPosn.amount.toString()
        Picasso.with(holder!!.itemView.context).load(cartPosn.image).into(holder.cartImage)
        holder.deleteBtn.setOnClickListener{
            Thread{
                val cartItem = Cart()
                cartItem.id = cartPosn.id
                cartItem.name = cartPosn.name
                cartItem.amount = cartPosn.amount
                cartItem.image = cartPosn.image
                db.cartDao().delete(cartItem)
                db.cartDao().getCartItems().forEach{
                    Log.i("CartItems","id:${cartItem.id}")
                    Log.i("CartItems","name:${ cartItem.name }")
                    Log.i("CartItems","amt:${cartItem.amount }")

                }
            }.start()
            if(currentPosn!=RecyclerView.NO_POSITION){
                onItemClick.onDelete(currentPosn)

            }
        }
    }

}