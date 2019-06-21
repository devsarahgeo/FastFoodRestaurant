package com.sarah.fastfoodrestmenu

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_payment_details.*
import org.json.JSONObject

class PaymentDetails : AppCompatActivity() {
    lateinit var txtID:TextView
    lateinit var txtamount:TextView
    lateinit var txtstatus:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_details)

        txtID = txtId
        txtamount = txtAmount
        txtstatus = txtStatus

        val intent:Intent = getIntent()

        try{
            val jsonObject:JSONObject = JSONObject(intent.getStringExtra("PaymentDetails"))
            showDetails(jsonObject.getJSONObject("response"),intent.getStringExtra("PaymentAmount"))

        }catch (ex:Exception){
            ex.printStackTrace()
        }
    }

    private fun showDetails(response:JSONObject,paymentAmount:String) {
        try{
            txtID.text = response.getString("id")
            txtamount.text = "$"+paymentAmount
            txtstatus.text = response.getString("state")



        }catch (ex:Exception){
            ex.printStackTrace()
        }
    }
}
