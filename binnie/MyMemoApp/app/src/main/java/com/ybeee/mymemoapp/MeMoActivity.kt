package com.ybeee.mymemoapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ybeee.mymemoapp.databinding.ActivityMemoBinding

var data : String?= ""
class MeMoActivity : AppCompatActivity() {
    lateinit var binding: ActivityMemoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.memoSaveBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("memo", binding.memoEditEt.text.toString())
            startActivity(intent)
        }
        Log.d("생명주기", "onCreate")
    }

    override fun onPause() {
        super.onPause()
        data = binding.memoEditEt.text.toString()

        Log.d("생명주기", "onPause")
    }

    override fun onResume() {
        super.onResume()
        if(data != null) {
            binding.memoEditEt.setText(data)
        }
        Log.d("생명주기", "onResume")
    }
    override fun onRestart() {
        super.onRestart()
        AlertDialog.Builder(this)
            .setTitle("잠깐!!")
            .setMessage("종료하지 않고, 다시 작성하겠습니까?")
            .setPositiveButton("예") {dialog, which ->
                binding.memoEditEt.setText(data)
            }
            .setNegativeButton("아니요") {dialog, which ->
                binding.memoEditEt.text?.clear()
            }
            .create()
            .show()

        Log.d("생명주기", "onRestart")
    }
}