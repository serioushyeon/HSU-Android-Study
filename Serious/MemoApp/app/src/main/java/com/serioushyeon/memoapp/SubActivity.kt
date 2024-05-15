package com.serioushyeon.memoapp
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.serioushyeon.memoapp.databinding.ActivitySubBinding

class SubActivity : AppCompatActivity() {

    lateinit var binding: ActivitySubBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textSub.text = intent.getStringExtra("data")

        binding.btnGoBack.setOnClickListener {
            finish()
        }

    }
}