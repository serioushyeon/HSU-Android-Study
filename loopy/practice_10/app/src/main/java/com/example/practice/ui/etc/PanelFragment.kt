package com.example.practice.ui.etc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.practice.databinding.FragmentHomePanelBinding


data class PanelItem(
    val title: String,
    val musicTitle: String,
    val singer: String,
    val albumResource: Int,
    val backgroundResource: Int
)

class PanelFragment(private val panelItem: PanelItem) : Fragment() {

    private lateinit var binding: FragmentHomePanelBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomePanelBinding.inflate(inflater, container, false)

        with(binding) {
            homePanelTitleTv.text = panelItem.title
            homePanelAlbumTitleTv.text = panelItem.musicTitle
            homePanelAlbumTitleTv2.text = panelItem.musicTitle
            homePanelAlbumSingerTv.text = panelItem.singer
            homePanelAlbumSingerTv2.text = panelItem.singer
            homePanelBackgroundIv.setImageResource(panelItem.backgroundResource)
            homePanelAlbumImgIv.setImageResource(panelItem.albumResource)
            homePanelAlbumImgIv1.setImageResource(panelItem.albumResource)
        }

        return binding.root
    }

}
