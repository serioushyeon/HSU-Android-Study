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
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityLockerSavemusicFragmentBinding.inflate(inflater,container,false)
        albumdatas.apply {
            add(Album("Butter", "방탄소년단", R.drawable.img_album_exp))
            add(Album("헤어지자 말해요", "박재정", R.drawable.ballad_image1))
            add(Album("라일락", "아이유", R.drawable.img_album_exp2))
            add(Album("Love me", "BE'O", R.drawable.hiphop_iv1))
            add(Album("hiphop", "artist", R.drawable.ic_flo_logo))
            add(Album("hiphop", "artist", R.drawable.img_first_album_default))
            add(Album("hiphop", "artist", R.drawable.home_pannel_iv3))
            add(Album("hiphop", "artist", R.drawable.home_pannel_iv3))
            add(Album("hiphop", "artist", R.drawable.home_pannel_iv3))
            add(Album("hiphop", "artist", R.drawable.home_pannel_iv3))
            add(Album("hiphop", "artist", R.drawable.home_pannel_iv3))
        }
        val items = listOf(
            Item("",true) ,
            Item("",true),
            Item("",true)
        )

         savemusivAlbumAdapter = SavemusicRvadapter(albumdatas,object: SavemusicRvadapter.MyItemClickListener {
            override fun onitemClick(album: Album) {
                //
            }
            override fun onRemoveAlbum(position: Int) {
                albumdatas.removeAt(position)
                savemusivAlbumAdapter.notifyItemRemoved(position)
                savemusivAlbumAdapter.notifyItemRangeChanged(position, albumdatas.size)
            }
        })


        binding.savemusicRv.adapter = savemusivAlbumAdapter
        binding.savemusicRv.layoutManager = LinearLayoutManager(context)
        return binding.root

    }
   data class Item(
        val title:String,
        var ischecked:Boolean,
   )
}