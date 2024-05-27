package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentSavedsongBinding
import com.google.gson.Gson

class SavedSongFragment:Fragment() {
    lateinit var binding : FragmentSavedsongBinding
    private var albumDatas = ArrayList<Album>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedsongBinding.inflate(inflater,container,false)
        albumDatas.apply {
            add(Album("Supernova", "에스파(aespa)", R.drawable.song_supernova))
            add(Album("소나기", "이클립스", R.drawable.song_eclipse))
            add(Album("Run Run", "이클립스", R.drawable.song_eclipse))
            add(Album("You&I", "이클립스", R.drawable.song_eclipse))

        }
        val lockerAlbumRVAdapter = LockerAlbumRVAdapter(albumDatas)
        binding.lockerSavedsongRv.adapter = lockerAlbumRVAdapter
        binding.lockerSavedsongRv.layoutManager = LinearLayoutManager(requireActivity())
        lockerAlbumRVAdapter.setItemClickListener(object : LockerAlbumRVAdapter.OnItemClickListener {
            override fun onItemClick(album: Album) {
                changeAlbumFragment(album)
            }

            override fun onRemoveAlbum(position: Int) {
                lockerAlbumRVAdapter.removeItem(position)
            }
        })
        return binding.root
    }
    private fun changeAlbumFragment(album: Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumToJson = gson.toJson(album)
                    putString("album", albumToJson)
                }
            })
            .commitAllowingStateLoss()
    }

}