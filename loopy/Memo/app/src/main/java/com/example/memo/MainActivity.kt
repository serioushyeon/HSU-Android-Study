package com.example.memo

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    lateinit var et : EditText
    var st = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)// onCreate : Layout XML 파일을 Activity에서 ContentView로 사용할 수 있도록 하기 (즉, 화면 설정)
        et = findViewById(R.id.et)
    }

    override fun onStart() {
        super.onStart()
        val bt = findViewById<Button>(R.id.button)
        bt.setOnClickListener {
            Intent(this, ok::class.java).apply {
                putExtra("text", et.text.toString())
                startActivity(this)
            }
        }
    }
    override fun onResume() {
        super.onResume()
        et.setText(st)
    }

    override fun onRestart() {
        super.onRestart()
        val builder = AlertDialog.Builder(this).apply {
            setTitle("다시 작성?")
            setPositiveButton("예스", DialogInterface.OnClickListener { dialog, which ->
                et.setText("")
            })
            setNegativeButton("노", DialogInterface.OnClickListener() { dialog, which ->
                st = ""
            })
            create()
            show()
        }

    }
    override fun onPause() {
        super.onPause()
        st = et.text.toString()
    }

}