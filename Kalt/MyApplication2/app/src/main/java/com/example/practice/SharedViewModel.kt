package com.example.practice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _imageResource = MutableLiveData<Int>()
    val imageResource: LiveData<Int> = _imageResource

    fun setImageResource(resourceId: Int) {
        _imageResource.value = resourceId
    }
}
