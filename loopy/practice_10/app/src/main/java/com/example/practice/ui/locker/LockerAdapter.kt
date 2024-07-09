package com.example.practice.ui.locker

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.practice.ui.song.MusicFile
import com.example.practice.ui.album.SavedAlbumFragment
import com.example.practice.ui.song.SelectSong

class LockerAdapter (fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SelectSong()
            1 -> MusicFile()
            else -> SavedAlbumFragment()
        }
    }
}