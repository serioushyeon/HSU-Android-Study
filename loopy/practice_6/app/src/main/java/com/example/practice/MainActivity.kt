package com.example.practice

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.practice.data.Album
import com.example.practice.data.SongEx
import com.example.practice.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var resultIntent: ActivityResultLauncher<Intent>
    private var songEx = SongEx()
    private var gson = Gson()
    private var ab = Album()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Practice)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavigation()
        onClickButton()
        setResultIntent()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            )
        ) {
            // 푸시 권한 없음, 사용자에게 권한 요청
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                REQ_PERMISSION_PUSH
            )
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQ_PERMISSION_PUSH) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 허용됨
            } else {
                // 권한이 거부됨
            }
        }
    }


    private fun setMiniPlayer(songEx: SongEx) {
        binding.mainMiniplayerTitleTv.text = songEx.title
        binding.mainMiniplayerSingerTv.text = songEx.singer
        binding.sbMain.progress = ((songEx.second * 100000) / songEx.playTime)

    }

    fun updateValue(album: Album) {
        ab = album
        binding.mainMiniplayerTitleTv.text = album.title.toString()
        binding.mainMiniplayerSingerTv.text = album.singer.toString()
    }

    override fun onStart() {
        super.onStart()
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val songJson = sharedPreferences.getString("songData", null)
        songEx = if (songJson == null) {
            SongEx("라일락", "아이유", 0, 60, false, "music_lilac")
        } else {
            gson.fromJson(songJson, SongEx::class.java)
        }
        setMiniPlayer(songEx)
    }

    private fun initNavigation() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fr_main, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fr_main, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fr_main, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fr_main, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fr_main, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                else -> {
                    return@setOnItemSelectedListener true
                }
            }
        }
    }


    private fun setResultIntent() {
        resultIntent = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val returnResult = it.data?.getStringExtra(STRING_INTENT_KEY)
                Toast.makeText(this, returnResult, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onClickButton() {
        val songex = SongEx("", "", 0, 60, false, "music_lilac")
        with(binding) {
            songex.singer = mainMiniplayerSingerTv.text.toString()
            songex.title = mainMiniplayerTitleTv.text.toString()
        }
        binding.mainPlayerCl.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java).apply {
                putExtra("title", songex.title)
                putExtra("singer", songex.singer)
                putExtra("second", songex.second)
                putExtra("isPlaying", songex.isPlaying)
                putExtra("playTime", songex.playTime)
                putExtra("music", songex.music)
                putExtra("albumImg", ab.coverImg)
            }
            resultIntent.launch(intent)
        }
    }

    override fun onPause() {
        super.onPause()
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val songJson = gson.toJson(songEx)
        editor.putString("songData", songJson)
        editor.apply()
    }

    companion object {
        const val STRING_INTENT_KEY = "intent_key"

        private const val REQ_PERMISSION_PUSH = 1001

    }
}

