package com.example.floClone.feature.main.locker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.floClone.databinding.FragmentBottomSheetBinding
import com.example.floClone.core.data.model.local.database.SongDatabase
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment: BottomSheetDialogFragment() {
    lateinit var binding: FragmentBottomSheetBinding

    lateinit var songDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSheetBinding.inflate(inflater, container, false)

        setSongDataBase()
        setButton()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setSongDataBase() {
        songDB = SongDatabase.getInstance(requireContext())!!
    }

    private fun setButton() {
        binding.listenContainer.setOnClickListener{

        }

        binding.addPlayListContainer.setOnClickListener{

        }

        binding.addListContainer.setOnClickListener{

        }

        binding.deleteSongContainer.setOnClickListener{
            songDB.songDao().updateIsLikeToFalse()
            this.dismiss()
        }
    }
}