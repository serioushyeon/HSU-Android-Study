package com.example.flo_clone.core.domain.repository

import com.example.flo_clone.core.data.model.remote.FloChartResult

interface LookView {
    fun onGetSongLoading()
    fun onGetSongSuccess(code: Int, result: FloChartResult)
    fun onGetSongFailure(code: Int, message: String)
}