package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentPannel1Binding
import com.example.flo.databinding.FragmentSavedfileBinding

class PannelFragment1 : Fragment() {
    lateinit var binding : FragmentPannel1Binding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPannel1Binding.inflate(inflater,container,false)
        return binding.root
    }
}
class PannelFragment2 :Fragment(R.layout.fragment_pannel2) {

}
