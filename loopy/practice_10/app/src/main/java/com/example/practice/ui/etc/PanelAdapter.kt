package com.example.practice.ui.etc

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.util.logging.Handler

class PanelAdapter (fragment: Fragment) : FragmentStateAdapter(fragment){

    private val fragmentList : ArrayList<Fragment> = ArrayList()

    override fun getItemCount() = fragmentList.size

    override fun createFragment(position: Int) = fragmentList[position]

    fun addFragment(fragment: Fragment){
        fragmentList.add(fragment)
        notifyItemInserted(fragmentList.size-1)
    }
}
