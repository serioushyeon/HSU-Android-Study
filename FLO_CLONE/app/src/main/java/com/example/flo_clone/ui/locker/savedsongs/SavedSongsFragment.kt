package com.example.flo_clone.ui.locker.savedsongs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo_clone.R
import com.example.flo_clone.data.SavedSongs
import com.example.flo_clone.databinding.FragmentSavedSongsBinding
import com.example.flo_clone.room.SongDatabase
import com.example.flo_clone.room.SongEntity

class SavedSongsFragment : Fragment() {

    lateinit var binding: FragmentSavedSongsBinding
    lateinit var songDB: SongDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedSongsBinding.inflate(layoutInflater)

        songDB = SongDatabase.getInstance(requireContext())!!

        setRecyclerView()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setRecyclerView()
    }

    private fun setRecyclerView() {

        // 리사이클러뷰 생성
        binding.savedSongsPlayListRv.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, false
        )

        // 어댑터 생성 및 바인딩
        val savedSongAdapter = SavedSongAdapter()
        binding.savedSongsPlayListRv.adapter = savedSongAdapter

        savedSongAdapter.addSongs(songDB.songDao().getLikedSongs(true) as ArrayList<SongEntity>)

        savedSongAdapter.setOnItemClickListener(object : SavedSongAdapter.OnItemClickListener {
            override fun onItemClick() {
                // 아이템 클릭 시 수행할 동작
            }
        })


    }
}