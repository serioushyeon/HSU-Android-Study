package com.example.practice

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.practice.databinding.ItemSavemusicalbumBinding

class savemusicalbum : AppCompatActivity() {
    lateinit var binding : ItemSavemusicalbumBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ItemSavemusicalbumBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
    class Item(// 아이템 제목 등 기타 필드
        private val title: String, // 스위치 상태
        var isChecked: Boolean
    )


}