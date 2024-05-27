package com.serioushyeon.floclone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.serioushyeon.floclone.databinding.FragmentMusicFileBinding

class MusicFileFragment : Fragment() {

    lateinit var binding : FragmentMusicFileBinding
    private var musicFileDatas = ArrayList<MusicFile>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMusicFileBinding.inflate(inflater, container, false)

        musicFileDatas.apply {
            add(MusicFile("숲", "최유리", R.drawable.img_pannel_album1))
            add(MusicFile("Lucky Girl Syndrome", "아일릿(ILLIT)", R.drawable.img_pannel_album2))
            add(MusicFile("bad idea right?", "Olivia Rodrigo", R.drawable.img_pannel_album3))
            add(MusicFile("These Tears", "Andy Grammer", R.drawable.img_pannel_album4))
            add(MusicFile("ETA", "NewJeans", R.drawable.img_pannel_album5))
            add(MusicFile("네가 좋은 사람일 수는 없을까", "윤지영", R.drawable.img_pannel_album6))
        }

        val musicFileRVAdapter = MusicFileRVAdapter(musicFileDatas)
        binding.musicfileRv.adapter = musicFileRVAdapter

        musicFileRVAdapter.setMyItemClickListener(object : MusicFileRVAdapter.MyItemClickListener{
            override fun onRemoveMusicFile(position: Int) {
                musicFileRVAdapter.removeItem(position)
            }
        })

        return binding.root
    }
}