package com.example.practice
interface LookView {
    fun onGetSongLoading()
    fun onGetSongSuccess(code: Int, result: iTunesResponse)
    fun onGetSongFailure(code: Int, message: String)
}