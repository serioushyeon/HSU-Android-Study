package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentLockerSavedsongBinding
import java.util.ArrayList


class SavedSongFragment : Fragment() {
    lateinit var binding: FragmentLockerSavedsongBinding
    private var songDatas = ArrayList<Song>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerSavedsongBinding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onStart() {
        super.onStart()
        initRecyclerview()
    }

    private fun initRecyclerview(){
        binding.lockerSavedSongRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        songDatas.apply {
            add(Song("Butter","방탄소년단 (BTS)",0,0,false,"", R.drawable.img_album_exp,false))
            add(Song("Lilac","아이유 (IU)", 0,0,false,"", R.drawable.img_album_exp2,false))
            add(Song("Next Level","에스파 (AESPA)", 0,0,false,"", R.drawable.img_album_exp3,false))
            add(Song("Boy with Luv","방탄소년단 (BTS)", 0,0,false,"", R.drawable.img_album_exp4,false))
            add(Song("BBoom BBoom","모모랜드 (MOMOLAND)", 0,0,false,"", R.drawable.img_album_exp5,false))
            add(Song("Weekend","태연 (Tae Yeon)", 0,0,false,"", R.drawable.img_album_exp6,false))
            add(Song("Butter","방탄소년단 (BTS)",0,0,false,"", R.drawable.img_album_exp,false))
            add(Song("Lilac","아이유 (IU)", 0,0,false,"", R.drawable.img_album_exp2,false))
            add(Song("Next Level","에스파 (AESPA)", 0,0,false,"", R.drawable.img_album_exp3,false))
            add(Song("Boy with Luv","방탄소년단 (BTS)", 0,0,false,"", R.drawable.img_album_exp4,false))
            add(Song("BBoom BBoom","모모랜드 (MOMOLAND)", 0,0,false,"", R.drawable.img_album_exp5,false))
            add(Song("Weekend","태연 (Tae Yeon)", 0,0,false,"", R.drawable.img_album_exp6,false))

        }

        val songRVAdapter = SavedSongRVAdapter(songDatas)
        binding.lockerSavedSongRecyclerView.adapter = songRVAdapter
        binding.lockerSavedSongRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

//        val songRVAdapter = SavedSongRVAdapter()
//
//        binding.lockerSavedSongRecyclerView.adapter = songRVAdapter

    }

}
