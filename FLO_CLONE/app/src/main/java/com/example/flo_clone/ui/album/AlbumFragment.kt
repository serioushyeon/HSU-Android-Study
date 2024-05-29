package com.example.flo_clone.ui.album

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.flo_clone.MainActivity
import com.example.flo_clone.R
import com.example.flo_clone.data.AlbumData
import com.example.flo_clone.databinding.FragmentAlbumBinding
import com.example.flo_clone.room.AlbumEntity
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
        val album = gson.fromJson(albumJson, AlbumEntity::class.java)
        setInit(album)

        when (binding.albumTitleTv.text.toString()) {
            "LILAC" -> {
                setAlbumImage(R.drawable.img_album_exp2)
            }

            "NEXT LEVEL" -> {
                setAlbumImage(R.drawable.img_album_exp3)
            }

            "Map Of The Soul" -> {
                setAlbumImage(R.drawable.img_album_exp4)
            }

            "BAAM" -> {
                setAlbumImage(R.drawable.img_album_exp5)
            }

            "Weekend" -> {
                setAlbumImage(R.drawable.img_album_exp6)
            }
        }
    }

    private fun setAlbumImage(drawable: Int) {
        binding.imgAlbumExpIv.setImageResource(drawable)
    }

    private fun setInit(album: AlbumEntity) {
        binding.imgAlbumExpIv.setImageResource(album.coverImg!!)
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