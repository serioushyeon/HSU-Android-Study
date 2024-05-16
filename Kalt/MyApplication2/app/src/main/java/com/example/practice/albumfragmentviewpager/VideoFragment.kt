package com.example.practice.albumfragmentviewpager

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.fragment.app.Fragment
import com.example.practice.R
import com.example.practice.databinding.FragmentDetailBinding
import com.example.practice.databinding.FragmentVideoBinding

class VideoFragment : Fragment() {
    lateinit var binding : FragmentVideoBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideoBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeVideoPlayer()
    }

    private fun initializeVideoPlayer() {
        val videoPath = "android.resource://" + requireActivity().packageName + "/" + R.raw.ballad_video // 로컬 리소스 사용
        val uri = Uri.parse(videoPath)
        binding.videoView.setVideoURI(uri)

        // 미디어 컨트롤러 추가
        val mediaController = MediaController(context)
        mediaController.setAnchorView(binding.videoView)
        binding.videoView.setMediaController(mediaController)

        binding.videoView.start() // 비디오 재생 시작
    }
}