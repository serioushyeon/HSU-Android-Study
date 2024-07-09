package com.example.practice.ui.look

import com.example.practice.data.entity.iTunesResponse

interface LookView {
    fun onGetSongLoading()
    fun onGetSongSuccess(code: Int, result: iTunesResponse)
    fun onGetSongFailure(code: Int, message: String)
}