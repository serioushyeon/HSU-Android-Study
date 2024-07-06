package com.example.flo.util

import com.example.flo.ui.main.album.Album

interface CommunicationInterface {
    fun sendData(album: Album)
}