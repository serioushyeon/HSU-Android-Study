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
import com.example.practice.databinding.ActivityHomeFragmentBinding
import com.example.practice.databinding.ActivityLookFragmentBinding

class LookFragment : Fragment() {
    lateinit var binding : ActivityLookFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityLookFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }
}