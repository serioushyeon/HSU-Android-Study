package com.example.practice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.practice.databinding.ActivityLockerMusicfileFragmentBinding
import com.example.practice.databinding.ActivityLockerSavemusicFragmentBinding

class locker_musicfile_fragment : Fragment() {
    lateinit var binding: ActivityLockerMusicfileFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityLockerMusicfileFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }
}