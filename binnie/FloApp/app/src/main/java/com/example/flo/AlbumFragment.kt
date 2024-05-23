package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import java.util.zip.Inflater

class AlbumFragment : Fragment() { // 프래그먼트의 기능을 사용할 수 있는 클래스
    lateinit var binding : FragmentAlbumBinding
    private var gson: Gson = Gson()
    private val information = arrayListOf("수록곡", "상세정보", "영상")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater,container, false)

        binding.albumBackIv.setOnClickListener() {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm,HomeFragment())
                .commitAllowingStateLoss()
        }
        val albumToJson = arguments?.getString("album")
        val album = gson.fromJson(albumToJson, Album::class.java)
        setInit(album)
        val albumAdapter = AlbumVPAdapter(this)
        binding.albumContentVp.adapter = albumAdapter
        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp) {
            tab, position ->
            tab.text = information[position]
        }.attach()


        return binding.root
    }

    private fun setInit(album : Album) {
        binding.albumAlbumIv.setImageResource(album.coverImage!!)
        binding.albumMusicTitleTv.text = album.title.toString()
        binding.albumSingerNameTv.text = album.singer.toString()
    }
}