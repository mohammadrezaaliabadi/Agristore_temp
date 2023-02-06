package com.example.agristore.fragment

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AddBillViewPagerAdapter(fragment:Fragment,private val customerId:Int):FragmentStateAdapter(fragment) {
    private val NUM_TABS = 2
    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> AddBillFragment(customerId)
            else -> AddBillCardFragment(customerId)
        }
    }
}