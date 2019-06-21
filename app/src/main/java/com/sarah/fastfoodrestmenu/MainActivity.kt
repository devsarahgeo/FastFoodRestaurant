package com.sarah.fastfoodrestmenu

import android.annotation.SuppressLint
import android.arch.persistence.room.R.id.async
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.*
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.Toast
import com.nex3z.notificationbadge.NotificationBadge
import com.sarah.fastfoodrestmenu.DataBase.Database.CartDatabase
import com.sarah.fastfoodrestmenu.DataBase.ModalDB.Cart
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.food_ticket.view.*

class MainActivity : AppCompatActivity() {

    //create array to store foods
    val listOfFoods= ArrayList<FoodTicket>()
    var adapter:FoodAdapter?=null
    lateinit var notificationBadge:NotificationBadge
    lateinit var viewPage:ViewPager
    lateinit var db:CartDatabase
    lateinit var cart_icon:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPage  = viewPager
        val pagerAdapter = ViewPagerAdapter(this)
        viewPage.adapter = pagerAdapter

        //load foods in the arraylist
        listOfFoods.add(FoodTicket("Coffee","Coffe is tasty.Coffe is tasty.Coffe is tasty.Coffe is tasty.Coffe is tasty.Coffe is tasty",R.drawable.coffee_pot,40))
        listOfFoods.add(FoodTicket("Espresso","Espresso is tasty",R.drawable.espresso,100))
        listOfFoods.add(FoodTicket("French fries","French fries is tasty",R.drawable.french_fries,60))
        listOfFoods.add(FoodTicket("Honey","Honey is tasty",R.drawable.honey,160))
        listOfFoods.add(FoodTicket("Strawberry icecream","Strawberry icecream is tasty",R.drawable.strawberry_ice_cream,40))
        listOfFoods.add(FoodTicket("Sugar Cubes","Sugar Cubes is tasty",R.drawable.sugar_cubes,35))

        adapter=FoodAdapter(this,listOfFoods)
        gvList.adapter=adapter
        db = Room.databaseBuilder(applicationContext,CartDatabase::class.java,"Cart-Database").build()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        val view:View = menu!!.findItem(R.id.cart).actionView
        notificationBadge = view.findViewById(R.id.badge)
        cart_icon = view.findViewById(R.id.cart_icon)
        cart_icon.setOnClickListener{
            Toast.makeText(applicationContext,"this is me",Toast.LENGTH_LONG).show()
            val intent = Intent(this@MainActivity,CartPayment::class.java)
            startActivity(intent)
        }
        updateCartCount()
        return true
    }

    private fun updateCartCount() {
        if(notificationBadge==null){
            return
        }
        Single.fromCallable {

            if (db.cartDao().countCartItems() == 0) {
                notificationBadge.visibility = View.INVISIBLE
            } else {
                notificationBadge.visibility = View.VISIBLE
                notificationBadge.setText(db.cartDao().countCartItems().toString())
            }
        }
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                        onError = { error ->
                            Log.e("error", "error here", error)
                        }
                )
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return true
    }

    //create adapter to show the list on the ui/screen
    class FoodAdapter(context:Context,listOfFood: ArrayList<FoodTicket>):BaseAdapter(){

        var context:Context?=context
        var listOfFood:ArrayList<FoodTicket>?=listOfFood
        @SuppressLint("ViewHolder", "InflateParams")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val food= listOfFood!![position]
            val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val foodView = inflater.inflate(R.layout.food_ticket,null)
            foodView.ivImage.setImageResource(food.image)
            foodView.ivImage.setOnClickListener{
                val intent = Intent(context,FoodDetails::class.java)
                intent.putExtra("name",food.name)
                intent.putExtra("desc",food.desc)
                intent.putExtra("image",food.image)
                intent.putExtra("amt",food.amount)
                context!!.startActivity(intent)
            }
            foodView.tvName.text=food.name
            foodView.tvCash.text = food.amount.toString()
                    return foodView

        }

        override fun getItem(position: Int): Any {
            return listOfFood!![position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listOfFood!!.size
        }

    }


    override fun onRestart() {
        super.onRestart()
        finish()
        startActivity(intent)
    }
}
