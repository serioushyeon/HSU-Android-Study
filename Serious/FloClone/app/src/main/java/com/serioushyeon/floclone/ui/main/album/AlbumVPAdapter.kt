package com.serioushyeon.floclone.ui.main.album

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.serioushyeon.floclone.ui.main.home.detail.DetailFragment
import com.serioushyeon.floclone.ui.song.SongFragment
import com.serioushyeon.floclone.ui.main.home.video.VideoFragment

class AlbumVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> SongFragment()
            1 -> DetailFragment()
            else -> VideoFragment()
        }
    }

}