package com.example.practice

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.viewpager2.adapter.FragmentStateAdapter

class bannerVpAdapter(fragment: Fragment):FragmentStateAdapter(fragment) {
    private val fragmentList : ArrayList<Fragment> = ArrayList()

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment = fragmentList[position]

    fun addFragment(fragment: Fragment){
        fragmentList.add(fragment)
        notifyItemInserted(fragmentList.size-1) // 새로운 값이 추가되었다는 것을 알리는 기능
    }
}