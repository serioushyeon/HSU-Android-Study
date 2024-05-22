package com.serioushyeon.floclone

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.gson.Gson
import com.serioushyeon.floclone.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private var song: Song = Song()
    private var gson: Gson = Gson()
    lateinit var timer: Timer
    private var mediaPlayer : MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.Theme_FloClone)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainPauseBtn.setOnClickListener {
            setPlayerStatus(false)
        }
        binding.mainMiniplayerBtn.setOnClickListener {
            setPlayerStatus(true)
        }
        //val song = Song(binding.mainMinipalyerTitleTv.text.toString(), binding.mainMinipalyerSingerTv.text.toString(), 0, 0, 10, false, "music_lilac")
        timer = Timer(song.playTime, song.isPlaying)

        binding.mainMinipalyerTitleTv.setOnClickListener{
            //startActivity(Intent(this, SongActivity::class.java))
            timer.interrupt()
            mediaPlayer?.release()  // 리소스
            mediaPlayer = null
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("title", song.title)
            intent.putExtra("singer", song.singer)
            intent.putExtra("img", song.img)
            intent.putExtra("second", song.second)
            intent.putExtra("playTime", song.playTime)
            intent.putExtra("isPlaying", song.isPlaying)
            intent.putExtra("music", song.music)
            intent.putExtra("isRepeating", song.isRepeating)
            startActivity(intent)
        }
        initBottomNavigation()
    }
    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release()  // 리소스
        mediaPlayer = null
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

    fun setMiniPlayer(song: Song){
        timer.interrupt()
        mediaPlayer?.release()  // 리소스
        mediaPlayer = null
        this.song = song
        binding.mainMinipalyerTitleTv.text = song.title
        binding.mainMinipalyerSingerTv.text = song.singer
        if (song.playTime != 0) {
            binding.mainMiniplayerProgressSb.progress = (song.second * 100000) / song.playTime
        } else {
            binding.mainMiniplayerProgressSb.progress = 0
        }
        Log.d("", song.music)
        val musicResId = resources.getIdentifier(song.music, "raw", this.packageName)
        if (musicResId != 0) {
            mediaPlayer = MediaPlayer.create(this, musicResId)
        } else {
            Log.e("MainActivity", "Music resource not found for: ${song.music}")
            // 리소스가 없을 경우에 대한 처리 (예: 디폴트 음악 설정 또는 에러 메시지)
            return
        }
        startTimer()
        setPlayerStatus(song.isPlaying)
    }

    private fun startTimer(){
        timer = Timer(song.playTime, song.isPlaying)
        timer.start()
    }

    fun setPlayerStatus(isPlaying : Boolean){
        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying){
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE
            mediaPlayer?.start()
            Log.d("","start")
        }
        else {
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE
            if(mediaPlayer?.isPlaying == true){
                mediaPlayer?.pause()
            }
            Log.d("","pause")
        }
    }

    override fun onStart() {
        super.onStart()
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val songJson = sharedPreferences.getString("songData", null)

        song = if(songJson == null){
            Song("라일락", "아이유(IU)", 0, 0, 60 , false, "music_lilac", false)
        } else{
            gson.fromJson(songJson, Song::class.java)
        }
        setMiniPlayer(song)
    }

    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true): Thread() {
        private var second : Int = 0
        private var mills: Float = 0f

        override fun run() {
            super.run()
            try {
                while(true){

                    if(second >= playTime){
                        if (song.isRepeating) {
                            mills = 0f
                            second = 0
                            mediaPlayer?.seekTo(0)
                            mediaPlayer?.start()
                        } else {
                            mediaPlayer?.pause()
                            break
                        }
                    }

                    if(isPlaying){
                        sleep(50)
                        mills += 50

                        runOnUiThread{
                            binding.mainMiniplayerProgressSb.progress = ((mills / playTime) * 100).toInt()
                        }

                        if (mills % 1000 == 0f){
                            second++
                            song.second = second
                        }
                    }
                }
            }catch (e: InterruptedException){
                Log.d("Song", "쓰레드가 죽었습니다. ${e.message}")
            }

        }
    }
}