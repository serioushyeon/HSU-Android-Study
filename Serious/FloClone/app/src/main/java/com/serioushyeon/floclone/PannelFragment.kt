package com.serioushyeon.floclone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.serioushyeon.floclone.databinding.FragmentPannelBinding

class PannelFragment(val pannelData : PannelData) : Fragment() {
    lateinit var binding: FragmentPannelBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPannelBinding.inflate(inflater, container, false)

        with(binding) {
            homePannelBackgroundIv.setImageResource(pannelData.backgroundImg)
            homePannelTitleTv.text = pannelData.title
            homePannelAlbumInfoTv.text = pannelData.totalSongs

            with(pannelData.firstSong) {
                homePannelAlbumImg1Iv.setImageResource(img)
                homePannelAlbumSinger1Tv.text = singer
                homePannelAlbumTitle1Tv.text = title
            }

            with(pannelData.secondSong) {
                homePannelAlbumImg2Iv.setImageResource(img)
                homePannelAlbumSinger2Tv.text = singer
                homePannelAlbumTitle2Tv.text = title
            }
        }

        return binding.root
    }

}