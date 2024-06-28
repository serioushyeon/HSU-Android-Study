package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import java.util.zip.Inflater

class AlbumFragment : Fragment() { // 프래그먼트의 기능을 사용할 수 있는 클래스
    lateinit var binding : FragmentAlbumBinding
    private var gson: Gson = Gson()
    private val information = arrayListOf("수록곡", "상세정보", "영상")
    private var isLiked : Boolean = false

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

        isLiked = isLikedAlbum(album.id)
        setInit(album) // Home에서 받아온 데이터를 반영
        setOnClikLikeListener(album)



        val albumAdapter = AlbumVPAdapter(this)
        binding.albumContentVp.adapter = albumAdapter
        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp) {
            tab, position ->
            tab.text = information[position]
        }.attach()


        return binding.root
    }

    private fun setInit(album : Album) {
        binding.albumAlbumIv.setImageResource(album.coverImg!!)
        binding.albumMusicTitleTv.text = album.title.toString()
        binding.albumSingerNameTv.text = album.singer.toString()

        if(isLiked) {
            binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_on)
        } else {
            binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }
    }

    private fun getJwt():Int {
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getInt("jwt", 0)
    }

    private fun likeAlbum(userId : Int, albumId : Int) {
        val songDB = SongDatabase.getInstance(requireContext())!!
        val like = Like(userId, albumId)

        songDB.albumDao().likeAlbum(like)
    }

    // 좋아요 누른 앨범
    private fun isLikedAlbum(albumId: Int) : Boolean {
        val songDB = SongDatabase.getInstance(requireContext())!!
        var userId = getJwt()

        val likeId : Int? = songDB.albumDao().isLikedAlbum(userId, albumId)

        return likeId != null
    }

    // 좋아요 누르지 않은 앨범
    private fun disLikeAlbum(albumId: Int) {
        val songDB = SongDatabase.getInstance(requireContext())!!
        var userId = getJwt()

        songDB.albumDao().disLikeAlbum(userId, albumId)
    }
    private fun setOnClikLikeListener(album: Album) {
        val userId = getJwt()
        binding.albumLikeIv.setOnClickListener {
            if(isLiked) {
                binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_off)
                disLikeAlbum(album.id)
            } else {
                binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_on)
                likeAlbum(userId, album.id)
            }
        }

    }
}