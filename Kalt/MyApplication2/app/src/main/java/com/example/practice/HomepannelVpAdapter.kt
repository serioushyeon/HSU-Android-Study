package com.example.practice

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.practice.homepannel_IV.Homepannel_IV1
import com.example.practice.homepannel_IV.Homepannel_IV2
import com.example.practice.homepannel_IV.Homepannel_IV3


class HomepannelVpAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> Homepannel_IV1()
            1-> Homepannel_IV2()
            else-> Homepannel_IV3()
        }

    }
}