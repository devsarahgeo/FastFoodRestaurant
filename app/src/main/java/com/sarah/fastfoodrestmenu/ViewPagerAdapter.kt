package com.sarah.fastfoodrestmenu

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kotlinx.android.synthetic.main.custom_slider.view.*

class ViewPagerAdapter(val context: Context): PagerAdapter() {
   var layoutInflater:LayoutInflater?=null
    val images = arrayOf(R.drawable.coffee_pot,R.drawable.espresso,R.drawable.french_fries)
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val slider =  layoutInflater!!.inflate(R.layout.custom_slider,null)
        val image  = slider.findViewById(R.id.ivSlider) as ImageView
        image.setImageResource(images[position])

        val vp = container as ViewPager
        vp.addView(slider,0)

        return slider
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp =container as ViewPager
        val v = `object` as View
        vp.removeView(v)
    }
}