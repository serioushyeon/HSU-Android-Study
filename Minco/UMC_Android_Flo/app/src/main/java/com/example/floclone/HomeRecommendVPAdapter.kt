package com.example.floclone

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomeRecommendVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> HomeRecommendFragment()
            1 -> HomeRecommendFragment2()
            2 -> HomeRecommendFragment3()
            else -> VideoFragment()
        }
    }
}