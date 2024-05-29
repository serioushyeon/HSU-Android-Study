package com.example.practice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practice.databinding.ActivityLockerSavemusicFragmentBinding

class locker_savemusic_fragment : Fragment() {
    lateinit var binding:ActivityLockerSavemusicFragmentBinding
    private var albumdatas = ArrayList<Album>()
    private lateinit var savemusivAlbumAdapter: SavemusicRvadapter
    lateinit var songDB: SongDatabase
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityLockerSavemusicFragmentBinding.inflate(inflater,container,false)
        songDB = SongDatabase.getInstance(requireContext())!!
        return binding.root

    }

    override fun onStart() {
        super.onStart()
        initrecyclerview()
    }
    private fun initrecyclerview(){
        savemusivAlbumAdapter = SavemusicRvadapter()
        savemusivAlbumAdapter.setMyItemClickListener(object : SavemusicRvadapter.MyItemClickListener {
            override fun onRemoveAlbum(songId: Int) {
                songDB.songDao().updateislikeByid(false,songId)
            }
        })


        binding.savemusicRv.adapter = savemusivAlbumAdapter
        binding.savemusicRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        savemusivAlbumAdapter.addSongs(songDB.songDao().getLikedSongs(true) as ArrayList<Song>)
    }
   /*data class Item(
        val title:String,
        var ischecked:Boolean,
   )*/

}