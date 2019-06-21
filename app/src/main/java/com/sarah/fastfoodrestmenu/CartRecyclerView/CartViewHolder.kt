package com.sarah.fastfoodrestmenu.CartRecyclerView

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.sarah.fastfoodrestmenu.R


class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var cartImage:ImageView
    var cartName:TextView
    var cartAmount: TextView
     var deleteBtn:Button
    init {
        this.cartImage = itemView.findViewById(R.id.ivCartImage)
        this.cartName = itemView.findViewById(R.id.tvName)
        this.cartAmount = itemView.findViewById(R.id.tvAmt)
        this.deleteBtn = itemView.findViewById(R.id.tvdelete)
    }
}