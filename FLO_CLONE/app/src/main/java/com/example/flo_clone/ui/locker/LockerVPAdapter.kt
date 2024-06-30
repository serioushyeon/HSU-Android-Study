package com.example.flo_clone.ui.locker

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.flo.SavedAlbumFragment
import com.example.flo_clone.ui.locker.musicfile.MusicFileFragment
import com.example.flo_clone.ui.locker.savedsongs.SavedSongsFragment

class LockerVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> SavedSongsFragment()
            1 -> MusicFileFragment()
            else -> SavedAlbumFragment()
        }
    }
}