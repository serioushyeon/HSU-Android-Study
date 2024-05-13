package com.example.practice

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.practice.albumfragmentviewpager.DetailFragment
import com.example.practice.albumfragmentviewpager.Songfragment
import com.example.practice.albumfragmentviewpager.VideoFragment

class AlbumVpAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> Songfragment()
            1-> DetailFragment()
            else -> VideoFragment()

        }
    }
}