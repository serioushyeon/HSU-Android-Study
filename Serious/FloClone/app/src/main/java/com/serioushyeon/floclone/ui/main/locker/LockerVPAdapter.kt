package com.serioushyeon.floclone.ui.main.locker

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.serioushyeon.floclone.ui.main.locker.musicfile.MusicFileFragment
import com.serioushyeon.floclone.ui.main.locker.savedalbum.SavedAlbumFragment
import com.serioushyeon.floclone.ui.main.locker.savedsong.SavedSongFragment

class LockerVPAdapter (fragment : Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int  = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> SavedSongFragment()
            1 -> MusicFileFragment()
            else -> SavedAlbumFragment()
        }
    }
}