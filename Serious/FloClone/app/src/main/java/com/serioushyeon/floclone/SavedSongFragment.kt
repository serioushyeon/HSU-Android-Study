package com.serioushyeon.floclone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.serioushyeon.floclone.databinding.FragmentSavedSongBinding

class SavedSongFragment : Fragment() {

    lateinit var binding : FragmentSavedSongBinding
    private var saveSongDatas = ArrayList<SaveSong>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedSongBinding.inflate(inflater, container, false)
        saveSongDatas.apply {
            add(SaveSong("숲", "최유리", R.drawable.img_pannel_album1))
            add(SaveSong("Lucky Girl Syndrome", "아일릿(ILLIT)", R.drawable.img_pannel_album2))
            add(SaveSong("bad idea right?", "Olivia Rodrigo", R.drawable.img_pannel_album3))
            add(SaveSong("These Tears", "Andy Grammer", R.drawable.img_pannel_album4))
            add(SaveSong("ETA", "NewJeans", R.drawable.img_pannel_album5))
            add(SaveSong("네가 좋은 사람일 수는 없을까", "윤지영", R.drawable.img_pannel_album6))
            add(SaveSong("숲", "최유리", R.drawable.img_pannel_album1))
            add(SaveSong("Lucky Girl Syndrome", "아일릿(ILLIT)", R.drawable.img_pannel_album2))
            add(SaveSong("bad idea right?", "Olivia Rodrigo", R.drawable.img_pannel_album3))
            add(SaveSong("These Tears", "Andy Grammer", R.drawable.img_pannel_album4))
            add(SaveSong("ETA", "NewJeans", R.drawable.img_pannel_album5))
            add(SaveSong("네가 좋은 사람일 수는 없을까", "윤지영", R.drawable.img_pannel_album6))
            add(SaveSong("숲", "최유리", R.drawable.img_pannel_album1))
            add(SaveSong("Lucky Girl Syndrome", "아일릿(ILLIT)", R.drawable.img_pannel_album2))
            add(SaveSong("bad idea right?", "Olivia Rodrigo", R.drawable.img_pannel_album3))
            add(SaveSong("These Tears", "Andy Grammer", R.drawable.img_pannel_album4))
            add(SaveSong("ETA", "NewJeans", R.drawable.img_pannel_album5))
            add(SaveSong("네가 좋은 사람일 수는 없을까", "윤지영", R.drawable.img_pannel_album6))
        }

        val saveSongRVAdapter = SaveSongRVAdapter(saveSongDatas)
        saveSongRVAdapter.setMyItemClickListener(object : SaveSongRVAdapter.MyItemClickListener{
            override fun onRemoveMusicFile(position: Int) {
                saveSongRVAdapter.removeItem(position)
            }
        })
        binding.saveSongRv.adapter = saveSongRVAdapter

        return binding.root
    }
    private fun changeSaveSongFragment(saveSong: SaveSong) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(saveSong)
                    putString("album", albumJson)
                }
            })
            .commitAllowingStateLoss()
    }
}