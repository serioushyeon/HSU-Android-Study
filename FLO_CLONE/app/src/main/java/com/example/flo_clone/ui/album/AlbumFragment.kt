package com.example.flo_clone.ui.album

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.flo_clone.MainActivity
import com.example.flo_clone.R
import com.example.flo_clone.data.Album
import com.example.flo_clone.databinding.FragmentAlbumBinding
import com.example.flo_clone.ui.home.HomeFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson


class AlbumFragment : Fragment() {

    lateinit var binding: FragmentAlbumBinding
    private var gson: Gson = Gson()

    private val information = arrayListOf("수록곡", "상세정보", "영상")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(layoutInflater)

        setLayout()
        setButton()
        setVPAdapter()

        return binding.root
    }

    private fun setLayout() {
        val albumJson = arguments?.getString("album")
        val album = gson.fromJson(albumJson, Album::class.java)
        setInit(album)

        binding
    }

    private fun setInit(album: Album) {
        binding.imgAlbumExpIv.setImageResource(album.converImg!!)
        binding.albumTitleTv.text = album.title.toString()
        binding.singerNameTv.text = album.singer.toString()
    }


    private fun setButton() {

        // 뒤로가기 버튼 클릭
        binding.btnAlbumBackIb.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, HomeFragment()).commitAllowingStateLoss()
        }
    }

    private fun setVPAdapter() {
        val albumVPAdapter = AlbumVPAdapter(this)
        binding.albumContentVp.adapter = albumVPAdapter

        // TabLayout + ViewPager2 연결
        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp) {
            tab, postion ->
            tab.text = information[postion]
        }.attach()
    }

}