package com.example.flo

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.flo.databinding.ActivityMainBinding
import com.google.gson.Gson
import java.util.Timer

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private var song: Song = Song()
    private var gson: Gson = Gson()
    private var mediaPlayer: MediaPlayer? = null
    private var timer: Timer? = null
    private var isPlaying: Boolean = false
    val songs = arrayListOf<Song>()
    var nowPos = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_FLO)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainMiniplayerBtn.setOnClickListener {
            setPlayerStatus(true)
        }

        binding.mainPauseBtn.setOnClickListener {
            setPlayerStatus(false)
        }

        binding.mainMiniplayerNext.setOnClickListener {
            moveSong(+1)
        }

        binding.mainMiniplayerPrevious.setOnClickListener {
            moveSong(-1)
        }


        binding.mainPlayerCl.setOnClickListener {
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId",song.id)
            editor.apply()

            val intent = Intent(this,SongActivity::class.java)
            startActivity(intent)
        }
        inputDummySongs()
        inputDummyAlbums()
        initBottomNavigation()

        Log.d("Song",song.title+song.singer)
    }

    override fun onStart() {
        super.onStart()
//        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
//        val songJson = sharedPreferences.getString("songData", null)
//
//        song = if(songJson == null) {
//            Song("라일락", "아이유(IU)", 0, 60, false, "music_lilac")
//        } else {
//            gson.fromJson(songJson, Song::class.java)
//        }
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId",0)
        val savedProgress = spf.getInt("progress", 0)
        val songDB = SongDatabase.getInstance(this)!!

        song = if (songId == 0){
            songDB.songDao().getSong(1)
        } else{
            songDB.songDao().getSong(songId)
        }

        songs.addAll(songDB.songDao().getSongs())
        nowPos = getPlayingSongPosition(song.id)

        Log.d("song ID", song.id.toString())

        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)
        mediaPlayer?.seekTo(savedProgress)
        mediaPlayer?.setOnCompletionListener {
            setPlayerStatus(false)
        }

        song.second = savedProgress / 1000
        setMiniPlayer(song)
    }

//    private fun setMiniPlayer(song: Song){
//        val title = intent.getStringExtra("album_title")
//        val singer = intent.getStringExtra("album_singer")
//
//        binding.mainMiniplayerTitleTv.text = title ?: song.title
//        binding.mainMiniplayerSingerTv.text = singer ?: song.singer
//        binding.mainProgressSb.progress = (song.second*100000)/song.playTime
//    }

    private fun getPlayingSongPosition(songId: Int): Int {
        for (i in 0 until songs.size) {
            if (songs[i].id == songId) {
                return i
            }
        }
        return 0
    }

    private fun moveSong(direction: Int) {
        val newPos = nowPos + direction

        if (newPos < 0) {
            Toast.makeText(this, "First song", Toast.LENGTH_SHORT).show()
            return
        }

        if (newPos >= songs.size) {
            Toast.makeText(this, "Last song", Toast.LENGTH_SHORT).show()
            return
        }

        nowPos = newPos
        val newSong = songs[nowPos]

        timer?.interrupt()
        timer = null

        mediaPlayer?.release()
        mediaPlayer = null

        val music = resources.getIdentifier(newSong.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)
        mediaPlayer?.setOnCompletionListener {
            setPlayerStatus(false)
        }

        setMiniPlayer(newSong)
        setPlayerStatus(true)
    }


    private fun setMiniPlayer(song: Song){
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.mainProgressSb.progress = (song.second * 1000 / song.playTime)

        if (isPlaying) {
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE
        } else {
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE
        }
    }

    private fun setPlayerStatus(isPlaying: Boolean) {
        Log.d("MainActivity", "setPlayerStatus called with isPlaying: $isPlaying")
        this.isPlaying = isPlaying

        if (isPlaying) {
            Log.d("MainActivity", "Starting mediaPlayer")
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE
            mediaPlayer?.start()
            startTimer()
        } else {
            Log.d("MainActivity", "Pausing mediaPlayer")
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
                Log.d("MainActivity", "mediaPlayer paused")
            } else {
                Log.d("MainActivity", "mediaPlayer was not playing")
            }
            timer?.interrupt()
        }
    }

    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)
    }


    private fun initBottomNavigation(){

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener{ item ->
            when (item.itemId) {

                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    private fun startTimer() {
        timer = Timer(song.playTime, isPlaying)
        timer?.start()
    }

    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true) : Thread() {
        private var second: Int = 0
        private var mills: Float = 0f

        override fun run() {
            super.run()
            try {
                while (true) {
                    if (second >= playTime) {
                        break
                    }

                    if (isPlaying) {
                        sleep(50)
                        mills += 50

                        runOnUiThread {
                            binding.mainProgressSb.progress = ((mills / playTime) * 100).toInt()
                        }
                        if (mills % 1000 == 0f) {
                            runOnUiThread {
                            }
                            second++
                        }
                    }
                }

            } catch (e: InterruptedException) {
                Log.d("Song","쓰레드가 죽었습니다. ${e.message}")
            }
        }
    }


    private fun inputDummySongs(){
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()

        if (songs.isNotEmpty()) return
        songDB.songDao().insert(
            Song(
                "Lilac",
                "아이유 (IU)",
                0,
                200,
                false,
                "music_lilac",
                R.drawable.img_album_exp2,
                false,
            )
        )

        songDB.songDao().insert(
            Song(
                "Flu",
                "아이유 (IU)",
                0,
                200,
                false,
                "music_flu",
                R.drawable.img_album_exp2,
                false,
            )
        )

        songDB.songDao().insert(
            Song(
                "Butter",
                "방탄소년단 (BTS)",
                0,
                190,
                false,
                "music_butter",
                R.drawable.img_album_exp,
                false,
            )
        )

        songDB.songDao().insert(
            Song(
                "Next Level",
                "에스파 (AESPA)",
                0,
                210,
                false,
                "music_next",
                R.drawable.img_album_exp3,
                false,
            )
        )


        songDB.songDao().insert(
            Song(
                "Boy with Luv",
                "방탄소년단 (BTS)",
                0,
                230,
                false,
                "music_boy",
                R.drawable.img_album_exp4,
                false,
            )
        )


        songDB.songDao().insert(
            Song(
                "BBoom BBoom",
                "모모랜드 (MOMOLAND)",
                0,
                240,
                false,
                "music_bboom",
                R.drawable.img_album_exp5,
                false,
            )
        )

        val _songs = songDB.songDao().getSongs()
        Log.d("DB data", _songs.toString())
    }

    private fun inputDummyAlbums(){
        val songDB = SongDatabase.getInstance(this)!!
        val albums = songDB.albumDao().getAlbums()

        if (albums.isNotEmpty()) return

        songDB.albumDao().insert(
            Album(
                0,
                "IU 5th Album 'LILAC'", "아이유 (IU)", R.drawable.img_album_exp2
            )
        )

        songDB.albumDao().insert(
            Album(
                1,
                "Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp
            )
        )

        songDB.albumDao().insert(
            Album(
                2,
                "iScreaM Vol.10 : Next Level Remixes", "에스파 (AESPA)", R.drawable.img_album_exp3
            )
        )

        songDB.albumDao().insert(
            Album(
                3,
                "MAP OF THE SOUL : PERSONA", "방탄소년단 (BTS)", R.drawable.img_album_exp4
            )
        )

        songDB.albumDao().insert(
            Album(
                4,
                "GREAT!", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5
            )
        )

        val _songs = songDB.songDao().getSongs()
        Log.d("DB data", _songs.toString())
    }


    override fun onDestroy() {
        super.onDestroy()
        timer?.interrupt()
        mediaPlayer?.release()
        mediaPlayer = null
    }


}