package com.example.practice

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BannerAdapter (fragment: Fragment) : FragmentStateAdapter(fragment){

    private val fragmentList : ArrayList<Fragment> = ArrayList()

    override fun getItemCount() = fragmentList.size

    override fun createFragment(position: Int) = fragmentList[position]

    fun addFragment(fragment: Fragment){
        fragmentList.add(fragment)
        notifyItemInserted(fragmentList.size-1)
    }

}
