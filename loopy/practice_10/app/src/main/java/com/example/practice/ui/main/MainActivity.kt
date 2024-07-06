package com.example.practice.ui.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.practice.R
import com.example.practice.ui.song.SongActivity
import com.example.practice.data.local.SongDatabase
import com.example.practice.data.entity.Album
import com.example.practice.data.entity.Song
import com.example.practice.databinding.ActivityMainBinding
import com.example.practice.ui.home.HomeFragment
import com.example.practice.ui.locker.LockerFragment
import com.example.practice.ui.look.LookFragment
import com.example.practice.ui.search.SearchFragment
import com.kakao.sdk.common.util.Utility

class MainActivity : AppCompatActivity() {
    private var nowPos = 0
    private lateinit var binding: ActivityMainBinding
    private lateinit var resultIntent: ActivityResultLauncher<Intent>
    private var song: Song = Song()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Practice)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var keyHash = Utility.getKeyHash(this)
        Log.d("해시","$keyHash")


        inputDummySongs()
        inputDummyAlbums()
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

    private fun onClickButton() {
//        val songex = Song("", "", 0, 60, false, "music_lilac")
//        with(binding) {
//            songex.singer = mainMiniplayerSingerTv.text.toString()
//            songex.title = mainMiniplayerTitleTv.text.toString()
//        }
//            val intent = Intent(this, SongActivity::class.java).apply {
//                putExtra("title", songex.title)
//                putExtra("singer", songex.singer)
//                putExtra("second", songex.second)
//                putExtra("isPlaying", songex.isPlaying)
//                putExtra("playTime", songex.playTime)
//                putExtra("music", songex.music)
//                putExtra("albumImg", ab.coverImg)
//            }
//            resultIntent.launch(intent)
        binding.mainPlayerCl.setOnClickListener {
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId", song.id)
            editor.apply()
            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }

        Log.d("MAIN/JWT_TO_SERVER",getJwt2().toString())
    }
    override fun onStart() {
        super.onStart()
//        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
//        val songJson = sharedPreferences.getString("songData", null)
//        song = if (songJson == null) {
//            Song("라일락", "아이유", 0, 60, false, "music_lilac")
//        } else {
//            gson.fromJson(songJson, Song::class.java)
//        }
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)
        val songDB = SongDatabase.getInstance(this)!!
        Log.d("얄리","$song, $songId")
        song = if (songId == 0) {
            Log.d("얄리","$song, $songId")
            songDB.songDao().getSong(1)
        } else {
            songDB.songDao().getSong(songId)
        }
        setMiniPlayer(song)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQ_PERMISSION_PUSH) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 허용됨
            } else {
                // 권한이 거부됨
            }
        }
    }



    override fun onPause() {
        super.onPause()

//        songs[nowPos].second =
//            ((binding.songPlayLineMl.progress * songs[nowPos].playTime) / 100) / 1000
//        songs[nowPos].isPlaying = false
//        playSongState(false)
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("songId", nowPos)
        editor.apply()
    }

    fun updateValue(position: Int) {
        val songDB = SongDatabase.getInstance(this)!!
        val album = songDB.songDao().getSong(position+1)
        nowPos = position + 1

        Log.d("포지션","$position  ${album}")
        setMiniPlayer(album)
    }


    private fun setMiniPlayer(song: Song) {
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.sbMain.progress = (song.second * 100000) / song.playTime

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



    companion object {
        const val STRING_INTENT_KEY = "intent_key"

        private const val REQ_PERMISSION_PUSH = 1001

    }

    private fun inputDummySongs() {
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()
        Log.d("포포","$songs")
        if (songs.isNotEmpty())
            return
        Log.d("포포2","$songs")



        songDB.songDao().insert(
            Song(
                "LILAC",
                "아이유",
                0,
                60,
                false,
                "lilac",
                R.drawable.img_album_exp2,
                false
            )
        )

        songDB.songDao().insert(
            Song(
                "Butter",
                "방탄소년단",
                0,
                60,
                false,
                "butter",
                R.drawable.img_album_exp,
                false
            )
        )
        songDB.songDao().insert(
            Song(
                "Next_Level",
                "여자 아이들",
                0,
                60,
                false,
                "nextlevel",
                R.drawable.img_album_exp3,
                false
            )
        )
        songDB.songDao().insert(
            Song(
                "Boy With Luv",
                "BTS",
                0,
                60,
                false,
                "boywithluv",
                R.drawable.img_album_exp4,
                false
            )
        )
        songDB.songDao().insert(
            Song(
                "BBoom BBoom",
                "모모랜드",
                0,
                60,
                false,
                "momoland",
                R.drawable.img_album_exp5,
                false
            )
        )
        songDB.songDao().insert(
            Song(
                "Weekend",
                "태연",
                0,
                60,
                false,
                "weekend",
                R.drawable.img_album_exp6,
                false
            )
        )


        val _songs = songDB.songDao().getSongs()
        Log.d("얄리", _songs.toString())

    }


    private fun inputDummyAlbums() {
        val songDB = SongDatabase.getInstance(this)!!
        val albums = songDB.albumDao().getAlbums()

        if (albums.isNotEmpty())
            return

        songDB.albumDao().insert(
            Album(
                0,
                "IU 5th Album 'LILAC'", "아이유 (IU)", R.drawable.img_album_exp2,false
            )
        )

        songDB.albumDao().insert(
            Album(
                1,
                "Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp,false
            )
        )

        songDB.albumDao().insert(
            Album(
                2,
                "iScreaM Vol.10 : Next Level Remixes", "에스파 (AESPA)",
                R.drawable.img_album_exp3,false
            )
        )

        songDB.albumDao().insert(
            Album(
                3,
                "MAP OF THE SOUL : PERSONA", "방탄소년단 (BTS)", R.drawable.img_album_exp4,false
            )
        )

        songDB.albumDao().insert(
            Album(
                4,
                "GREAT!", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5,false
            )
        )

        songDB.albumDao().insert(
            Album(
                5,
                "태연!", "태연", R.drawable.img_album_exp6,false
            )
        )

    }

    private fun getJwt2():String?{
        val spf = this.getSharedPreferences("auth",AppCompatActivity.MODE_PRIVATE)
        return spf!!.getString("jwt","")
    }


}

