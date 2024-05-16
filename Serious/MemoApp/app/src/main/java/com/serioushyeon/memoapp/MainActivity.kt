package com.serioushyeon.memoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.serioushyeon.memoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var memo: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnMain.setOnClickListener {
            var intent = Intent(this, SubActivity::class.java)
            intent.putExtra("data", binding.edtMain.text.toString())
            startActivity(intent)
        }

        val create = Toast.makeText(this.applicationContext, "onCreate", Toast.LENGTH_SHORT)
        create.show()
    }

    override fun onPause() {
        super.onPause()
        memo = binding.edtMain.text.toString()

        val pause = Toast.makeText(this.applicationContext, "onPause", Toast.LENGTH_SHORT)
        pause.show()
    }

    override fun onResume() {
        super.onResume()
        binding.edtMain.setText(memo)

        val resume = Toast.makeText(this.applicationContext, "onResume", Toast.LENGTH_SHORT)
        resume.show()
    }

    override fun onStop() {
        super.onStop()
        memo = binding.edtMain.text.toString()
        binding.edtMain.text.clear()

        val stop = Toast.makeText(this.applicationContext, "onStop", Toast.LENGTH_SHORT)
        stop.show()
    }

    override fun onRestart() {
        super.onRestart()
        AlertDialog.Builder(this)
            .setTitle("이어쓰기")
            .setMessage("이전에 작성하신 내용을 이어서 작성하시겠습니까?")
            .setPositiveButton("예") {dialog, which ->
                binding.edtMain.setText(memo)
            }
            .setNegativeButton("아니요") {dialog, which ->
                binding.edtMain.text.clear()
            }
            .create()
            .show()

        val restart = Toast.makeText(this.applicationContext, "onRestart", Toast.LENGTH_SHORT)
        restart.show()
    }
}