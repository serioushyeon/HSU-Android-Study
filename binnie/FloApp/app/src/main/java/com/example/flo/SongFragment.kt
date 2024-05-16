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
        return binding.root

    }

}