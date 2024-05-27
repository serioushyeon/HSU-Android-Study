package com.serioushyeon.floclone

data class SaveSong(
    var title: String? = "",
    var singer: String? = "",
    var coverImg: Int? = null,
    var isChecked: Boolean = false
)
