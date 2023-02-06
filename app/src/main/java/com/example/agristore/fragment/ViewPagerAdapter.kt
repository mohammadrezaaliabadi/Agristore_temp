package com.example.agristore.fragment

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {
    private val NUM_TABS = 3
    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ItemListFragment()
            1 -> BillListFragment()
            else -> CustomerListFragment()
        }
    }
}