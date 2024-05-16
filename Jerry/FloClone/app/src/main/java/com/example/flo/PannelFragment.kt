package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentBannerBinding
import com.example.flo.databinding.FragmentPannelBinding

class PannelFragment(val imgRes : Int) : Fragment() {

    lateinit var binging : FragmentPannelBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binging = FragmentPannelBinding.inflate(inflater, container, false)

        binging.homePannelBackgroundIv.setImageResource(imgRes)
        return binging.root
    }
}