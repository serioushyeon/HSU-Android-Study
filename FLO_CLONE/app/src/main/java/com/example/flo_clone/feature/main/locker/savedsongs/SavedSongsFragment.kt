package com.example.flo_clone.feature.main.locker.savedsongs

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo_clone.databinding.FragmentSavedSongsBinding
import com.example.flo_clone.core.data.model.local.database.SongDatabase
import com.example.flo_clone.core.data.model.local.entities.SongEntity
import com.example.flo_clone.feature.main.locker.BottomSheetFragment

class SavedSongsFragment : Fragment() {

    lateinit var binding: FragmentSavedSongsBinding
    lateinit var savedSongAdapter: SavedSongAdapter
    lateinit var songDB: SongDatabase

    private lateinit var bottomSheetFragment: BottomSheetFragment
    private var isSelect = false

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
        selectAll()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setRecyclerView()
    }

    private fun setBottomSheetFragment() {
        bottomSheetFragment = BottomSheetFragment()
    }

    private fun selectAll() {

        // 전체듣기 클릭리스너
        binding.selectAllContainer.setOnClickListener{

            if (!isSelect) {
                binding.savedSongsSelectSongImgIv.setColorFilter(Color.BLUE)
                binding.savedSongsSelectSongTv.setTextColor(Color.BLUE)
                binding.savedSongsSelectSongTv.text = "선택해제"
                setBottomSheetFragment()
                bottomSheetFragment.show(this@SavedSongsFragment.parentFragmentManager, "BottomSheetDialog")
                savedSongAdapter.notifyDataSetChanged()
                isSelect = true
            }
            else {
                binding.savedSongsSelectSongImgIv.setColorFilter(Color.BLACK)
                binding.savedSongsSelectSongTv.setTextColor(Color.BLACK)
                binding.savedSongsSelectSongTv.text = "전체선택"
                isSelect = false
                bottomSheetFragment.dismiss()
            }
        }
    }

    // 리사이클러뷰 생성 및 어댑터 연결 함수
    private fun setRecyclerView() {

        // 리사이클러뷰 생성
        binding.savedSongsPlayListRv.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, false
        )

        // 어댑터 생성 및 바인딩
        savedSongAdapter = SavedSongAdapter()
        binding.savedSongsPlayListRv.adapter = savedSongAdapter

        savedSongAdapter.addSongs(songDB.songDao().getLikedSongs(true) as ArrayList<SongEntity>)

        savedSongAdapter.setMyItemClickListener(object : SavedSongAdapter.MyItemClickListener {
            override fun onRemovedSong(songId: Int) {
                songDB.songDao().updateIsLikeById(false, songId)
            }
        })
    }

}