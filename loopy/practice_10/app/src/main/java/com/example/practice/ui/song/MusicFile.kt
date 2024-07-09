package com.example.practice.ui.song

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practice.data.local.SongDatabase
import com.example.practice.data.entity.Song
import com.example.practice.databinding.FragmentMusicFileBinding

class MusicFile : Fragment() {
    private var selectDatas = ArrayList<Song>()
    lateinit var selectAdapter : SavedSongRVAdapter
    lateinit var songDB : SongDatabase
    private lateinit var binding : FragmentMusicFileBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMusicFileBinding.inflate(inflater, container, false)
        songDB = SongDatabase.getInstance(requireContext())!!
        onClickBtn()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initAdapter()

    }
    private fun initAdapter(){
        selectAdapter = SavedSongRVAdapter(selectDatas)
        binding.rvSelect.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL,false)

        selectAdapter.setMyItemClickListener(object : SavedSongRVAdapter.MyItemClickListener {
            override fun onItemClick(item: Song) {
                TODO("Not yet implemented")
            }

            override fun onRemoveAlbum(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onToggle(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onRemoveSong(position: Int) {
                songDB.songDao().updateIsLikeById(false,position)
            }

        })

        binding.rvSelect.adapter = selectAdapter

        selectAdapter.addSongs(songDB.songDao().getLikeSongs(true) as ArrayList<Song>)
    }
    private var allSelectBtnState = false
    private fun btStageChange(){
        allSelectBtnState != allSelectBtnState
    }
    private fun onClickBtn(){
        with(binding) {
            ibAllSelect.setOnClickListener {
                btStageChange()
                if(allSelectBtnState){
                    btnAllSelect.visibility = View.GONE
                }
                else{
                    btnAllSelect.visibility = View.VISIBLE
                    songDB.songDao().deleteAllSongs()
                }
            }
            btnAllSelect.setOnClickListener{
                songDB.songDao().deleteAllSongs()
            }
            tvAllSelect.setOnClickListener{
                btStageChange()
                if(allSelectBtnState){
                    btnAllSelect.visibility = View.GONE
                }
                else{
                    btnAllSelect.visibility = View.VISIBLE

                }
            }
        }
    }
}
