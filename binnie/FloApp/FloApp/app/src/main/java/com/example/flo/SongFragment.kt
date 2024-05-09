package com.example.flo

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentDetailBinding
import com.example.flo.databinding.FragmentSongBinding

class SongFragment : Fragment() {
    lateinit var binding : FragmentSongBinding
    var isToggle:Boolean = false
    val imgViews = mutableListOf<ImageView>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongBinding.inflate(inflater,container,false)
        binding.songMagneticLayout.setOnClickListener {
            Toast.makeText(activity, "Magnetic", Toast.LENGTH_SHORT).show()
        }

        binding.songMixtoggleIv.setOnClickListener {
            if(isToggle==false) {
                binding.songMixtoggleIv.setImageResource(R.drawable.btn_toggle_on)
                Log.d("toggle","누름")
                isToggle = true
            }
            else {
                binding.songMixtoggleIv.setImageResource(R.drawable.btn_toggle_off)
                isToggle = false
            }
        }
        binding.songMiniplaybtnIv2.setOnClickListener {


        }
//        val rootView = binding.root
//        findImageViewsWithPath(rootView)
//        // 이미지 경로가 같은 이미지뷰들을 처리
//        for (imageView in imgViews) {
//            // 처리할 작업 수행
//            imageView.setOnClickListener {
//                Log.d("플레이버튼", "누름")
//            }
//        }
        return binding.root

    }
    // 원하는 이미지 경로를 가진 이미지뷰를 찾는 함수
//    fun findImageViewsWithPath(view: View) {
//        if(view is ImageView) {
//            if(view.drawable is BitmapDrawable) {
//                val bitmap = (view.drawable as BitmapDrawable).bitmap
//                val imageViewPath = getImagePathFromBitmap(bitmap)
//// 원하는 이미지 경로를 가진 이미지뷰를 찾은 경우
//                val targetImagePath = "@drawable/btn_miniplayer_play" // 원하는 이미지 경로
//                if (imageViewPath == targetImagePath) {
//                    // 이미지뷰를 리스트에 추가
//                    imgViews.add(view)
//                }
//            }
//        }
//    }
//    // 비트맵으로부터 이미지 경로를 가져오는 함수 (이 부분은 이미지 경로를 가져오는 방법에 따라 변경될 수 있습니다.)
//    fun getImagePathFromBitmap(bitmap: Bitmap): String {
//        return "@drawable/btn_miniplayer_play"
//    }
}