package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentBannerBinding

class BannerFragment(val imgRes : Int) : Fragment() {

    lateinit var binging : FragmentBannerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binging = FragmentBannerBinding.inflate(inflater, container, false)

        binging.bannerImageIv.setImageResource(imgRes)
        return binging.root
    }
}