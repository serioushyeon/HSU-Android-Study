package com.example.flo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomeVPAdapter(fragment:Fragment) : FragmentStateAdapter(fragment) {
    private val fragmentlist :ArrayList<Fragment> = ArrayList()
    override fun getItemCount(): Int = fragmentlist.size

    override fun createFragment(position: Int): Fragment = fragmentlist[position]

    fun addPannelFragment(fragment:Fragment) {
        fragmentlist.add(fragment)
        notifyItemInserted(fragmentlist.size-1)
    }
    interface OnItemClickListener {
        fun onItemClick(album : Album)
    }

    private lateinit var itemClickListener : OnItemClickListener

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

}