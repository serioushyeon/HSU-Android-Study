package com.example.flo

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding
import java.util.Timer

class SongActivity : AppCompatActivity() {

    lateinit var binding : ActivitySongBinding
    lateinit var song: Song
    lateinit var timer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding 초기화
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSong()
        setPlayer(song)

        binding.songDownIb.setOnClickListener {
            finish()
        }

        binding.songPlayIv.setOnClickListener {
            setPlayerStatus(true)
        }
        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
        }

        binding.songLikeIv.setOnClickListener {
            setLikeStatus(false)
        }
        binding.songColorLikeIv.setOnClickListener {
            setLikeStatus(true)
        }

        binding.songUnlikeIv.setOnClickListener {
            setUnlikeStatus(false)
        }
        binding.songColorUnlikeIv.setOnClickListener {
            setUnlikeStatus(true)
        }

        var isRepeatEnabled = false // 초기에는 false로 설정합니다.

        binding.songRepeatIv.setOnClickListener {
            isRepeatEnabled = !isRepeatEnabled // 현재 상태를 반전시킵니다.

            if (isRepeatEnabled) {
                // Repeat가 활성화되었을 때 파란색으로 설정합니다.
                binding.songRepeatIv.setColorFilter(Color.parseColor("#000069"))
            } else {
                // Repeat가 비활성화되었을 때 회색으로 설정합니다.
                binding.songRepeatIv.setColorFilter(Color.parseColor("#323232"))
            }
        }
        binding.songRandomIv.setOnClickListener {
            isRepeatEnabled = !isRepeatEnabled // 현재 상태를 반전시킵니다.

            if (isRepeatEnabled) {
                // Repeat가 활성화되었을 때 파란색으로 설정합니다.
                binding.songRandomIv.setColorFilter(Color.parseColor("#000069"))
            } else {
                // Repeat가 비활성화되었을 때 회색으로 설정합니다.
                binding.songRandomIv.setColorFilter(Color.parseColor("#323232"))
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
    }

    private fun initSong(){
        if (intent.hasExtra("title")&&intent.hasExtra("singer")) {
            song = Song(
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second",0),
                intent.getIntExtra("playTime", 0),
                intent.getBooleanExtra("isPlaying", false)
            )
        }
        startTimer()
    }

    private fun setPlayer(song: Song){
        binding.songMusicTitleTv.text=intent.getStringExtra("title")
        binding.songSingerNameTv.text=intent.getStringExtra("singer")
        binding.songStartTimeTv.text=String.format("%02d:%02d",song.second/60,song.second%60)
        binding.songEndTimeTv.text=String.format("%02d:%02d",song.playTime/60,song.playTime%60)
        binding.songProgressSb.progress=(song.second * 1000 / song.playTime)

        setPlayerStatus(song.isPlaying)
    }

    fun setPlayerStatus(isPlaying : Boolean){
        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying){
            binding.songPlayIv.visibility= View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
        }
        else{
            binding.songPlayIv.visibility= View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
        }
    }

    fun setLikeStatus(isPlaying : Boolean){
        if(isPlaying){
            binding.songLikeIv.visibility = View.VISIBLE
            binding.songColorLikeIv.visibility = View.GONE
        }
        else{
            binding.songLikeIv.visibility = View.GONE
            binding.songColorLikeIv.visibility = View.VISIBLE
        }
    }

    fun setUnlikeStatus(isPlaying : Boolean){
        if(isPlaying){
            binding.songUnlikeIv.visibility = View.VISIBLE
            binding.songColorUnlikeIv.visibility = View.GONE
        }
        else{
            binding.songUnlikeIv.visibility = View.GONE
            binding.songColorUnlikeIv.visibility = View.VISIBLE
        }
    }

    private fun startTimer(){
        timer = Timer(song.playTime,song.isPlaying)
        timer.start()
    }

    inner class Timer(private val playTime: Int, var isPlaying: Boolean= true):Thread(){
        private var second : Int = 0
        private var mills : Float = 0f

        override fun run() {
            super.run()
            try{
                while (true){

                    if(second >= playTime){
                        break
                    }

                    if(isPlaying){
                        sleep(50)
                        mills += 50

                        runOnUiThread{
                            binding.songProgressSb.progress = ((mills / playTime)*100).toInt()
                        }
                        if(mills % 1000 == 0f){
                            runOnUiThread {
                                binding.songStartTimeTv.text=String.format("%02d:%02d",second/60,second%60)
                            }
                            second++
                        }
                    }
                }

            }catch (e: InterruptedException){
                Log.d("Song","쓰레드가 죽었습니다. ${e.message}")
            }

        }
    }
}