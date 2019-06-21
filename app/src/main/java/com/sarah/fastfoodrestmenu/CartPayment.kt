package com.sarah.fastfoodrestmenu


import android.app.Activity
import android.arch.persistence.room.Room
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.paypal.android.sdk.payments.*
import com.sarah.fastfoodrestmenu.CartRecyclerView.CartAdapter
import com.sarah.fastfoodrestmenu.Config.Config
import com.sarah.fastfoodrestmenu.DataBase.Database.CartDatabase
import com.sarah.fastfoodrestmenu.DataBase.ModalDB.Cart
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_cart_payment.*
import kotlinx.android.synthetic.main.item_cart_layout.*
import org.json.JSONException
import java.math.BigDecimal

class CartPayment : AppCompatActivity(),CartAdapter.OnItemClickListener {
    private var recyclerCart: RecyclerView? = null
    lateinit var cartPay:Button
    lateinit var adapter: CartAdapter
    lateinit var db:CartDatabase
    var listDelete:MutableList<Cart> = ArrayList()
    lateinit var compositeDisposable:CompositeDisposable
    lateinit var finalAmt:TextView
    var oldAmt:Int = 0
    var newAmt:Int = 0
    var amount:String = ""

    companion object {
        val PAYPAL_REQUEST_CODE = 711
        private val CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX

        private val config = PayPalConfiguration()
                .environment(CONFIG_ENVIRONMENT)
                .clientId(Config.PAYPAL_CLIENT_ID)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_payment)

        val intent:Intent = Intent(this@CartPayment,PayPalService::class.java)
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config)
        startService(intent)
        finalAmt = tvFinalAmt
        db = Room.databaseBuilder(applicationContext, CartDatabase::class.java,"Cart-Database").build()
        recyclerCart = cartpayment_recycler as RecyclerView
        recyclerCart!!.layoutManager = LinearLayoutManager(this)
        recyclerCart!!.setHasFixedSize(true)
        compositeDisposable = CompositeDisposable()

        cartPay = btnpay

        loadCartItems()
        cartPay.setOnClickListener{
            processPayment()
        }

    }

    private fun processPayment() {
        amount = finalAmt.text.toString()
        val paypalPayment:PayPalPayment = PayPalPayment(BigDecimal(amount), "USD", "Pay for cart Items",
                PayPalPayment.PAYMENT_INTENT_SALE)
        val intent:Intent = Intent(this@CartPayment,PaymentActivity::class.java)
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config)
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,paypalPayment)
        startActivityForResult(intent, PAYPAL_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val confirmation: PaymentConfirmation = data!!.getParcelableExtra<PaymentConfirmation>(PaymentActivity.EXTRA_RESULT_CONFIRMATION)
                if (confirmation != null) {
                    try {
                        val paymentDetails = confirmation.toJSONObject().toString(4)
                        startActivity(Intent(this@CartPayment, PaymentDetails::class.java)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", amount))


                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Log.e("error", "an extremely unlikely failure occurred: ", e)
                    }
                }else if(resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(this, "CANCEL", Toast.LENGTH_LONG).show()
                }
            }else if(resultCode ==PaymentActivity.RESULT_EXTRAS_INVALID){
                Toast.makeText(this,"INVALID",Toast.LENGTH_LONG).show()

            }
        }
    }

    override fun onDelete(position: Int) {
        listDelete.removeAt(position)
        adapter.notifyItemRemoved(position)
        adapter.notifyItemRangeChanged(position,listDelete.size)
        oldAmt = 0
        getFinalamt(listDelete)
    }

    fun loadCartItems() {
        compositeDisposable.add(
                db.cartDao().getCartItems()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(object :Consumer<MutableList<Cart>>{
                            override fun accept(carts: MutableList<Cart>) {
                                displayCartItems(carts)
                            }

                        })
        )
    }
    private fun displayCartItems(carts: MutableList<Cart>) {
        listDelete = carts
        adapter = CartAdapter(this,carts,this)
        recyclerCart!!.adapter = adapter
        getFinalamt(carts)
//        cartpayment_recycler.adapter=adapter
    }

    private fun getFinalamt(carts: MutableList<Cart>) {
        val cartSize = carts.size-1
        for(i in 0..cartSize){
            val a = carts[i].amount
            newAmt = a
            val total = oldAmt + newAmt
            oldAmt = total
        }
        finalAmt.text = oldAmt.toString()
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        stopService(Intent(this@CartPayment,PayPalService::class.java))
        super.onDestroy()
    }

    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }
}
