package com.serioushyeon.floclone.data.local

import com.serioushyeon.floclone.data.remote.FloChartResult


interface LookView {
    fun onGetSongLoading()
    fun onGetSongSuccess(code: Int, result: FloChartResult)
    fun onGetSongFailure(code: Int, message: String)
}