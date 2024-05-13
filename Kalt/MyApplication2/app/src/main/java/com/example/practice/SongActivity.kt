package com.example.practice

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.practice.databinding.ActivitySongBinding
import com.google.gson.Gson

class SongActivity : AppCompatActivity() {
    lateinit var binding : ActivitySongBinding
    lateinit var song : Song
    lateinit var timer : Timer
    private var mediaPlayer : MediaPlayer? = null
    private var gson: Gson = Gson()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initsong()
        setPlayer(song)
        binding.songDown.setOnClickListener {
            finish()
        }
        binding.songPlayIv.setOnClickListener {
            setplayerstatus(true)
        }
        binding.songPauseIv.setOnClickListener {
            setplayerstatus(false)
        }
        binding.repeatInactiveIv.setOnClickListener {
            binding.repeatInactiveIv.visibility = View.GONE
            binding.repeatInactiveIv2.visibility = View.VISIBLE
        }
        binding.repeatInactiveIv2.setOnClickListener {
            binding.repeatInactiveIv.visibility = View.VISIBLE
            binding.repeatInactiveIv2.visibility = View.GONE
        }
        binding.randdomInactiveIv.setOnClickListener {
            binding.randdomInactiveIv.visibility = View.GONE
            binding.randdomInactiveIv2.visibility = View.VISIBLE
        }
        binding.randdomInactiveIv2.setOnClickListener {
            binding.randdomInactiveIv.visibility = View.VISIBLE
            binding.randdomInactiveIv2.visibility = View.GONE
        }


    }
    // 사용자가 포커스를 잃었을 때 음악이 중지
    //json - 자바 객체로 -> json 으로 변환을 간단하게 해준다.
    override fun onPause() {
        super.onPause()
        setplayerstatus(false)
        song.second = ((binding.songprogressSB.progress *song.playTime)/100)/1000
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)// 앱이 종료되도 데이터를 다시 사용할 수 있다.
        val editor = sharedPreferences.edit() //에디터
        val songgson = gson.toJson(song)
        editor.putString("songdata",songgson)

        editor.apply() //꼭 해줘야 한다.
    }
    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release() //미디어플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null
    }

    private fun initsong(){
        if(intent.hasExtra("title") && intent.hasExtra("singer")){
            song = Song(
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second",0),
                intent.getIntExtra("playTime",0),
                intent.getBooleanExtra("isplaying",false),
                intent.getStringExtra("music")!!
            )
        }
        startTimer()
    }
    private fun setPlayer(song : Song){
        binding.songMusicTitleTv.text = intent.getStringExtra("title")
        binding.songMusicSingerTv.text = intent.getStringExtra("singer")
        binding.songStarttimeIv.text = String.format("%02d:%02d",song.second/60,song.second%60)
        binding.songEndtimeIv.text = String.format("%02d:%02d",song.playTime/60,song.playTime%60)
        binding.songprogressSB.progress = ((song.second * 3000.0 / song.playTime).toInt())
        val music = resources.getIdentifier(song.music,"raw",this.packageName)
        mediaPlayer = MediaPlayer.create(this,music)
        setplayerstatus(song.isplaying)
    }
    private fun setplayerstatus(isplaying:Boolean){
        song.isplaying = isplaying
        timer.isplaying = isplaying


        if(isplaying){
            binding.songPlayIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
            mediaPlayer?.start()
        }
        else{
            binding.songPlayIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
            if(mediaPlayer?.isPlaying ==true){
                mediaPlayer?.pause()
            }

        }
    }
    private fun startTimer(){
        timer = Timer(song.playTime,song.isplaying)
        timer.start()
    }

    inner class Timer (private val playTime: Int, var isplaying : Boolean = true) : Thread(){
        private var second : Int =0
        private var pills : Float =0f


        override fun run() {
            super.run()
            try {
                while(true){
                    if(second >=playTime){
                        break
                    }
                    if(isplaying){
                        sleep(50)
                        pills +=50
                        runOnUiThread {
                            binding.songprogressSB.progress = ((pills/ playTime)*100).toInt()
                        }
                        if(pills % 1000 ==0f){
                            runOnUiThread {
                                binding.songStarttimeIv.text = String.format("%02d:%02d",second/60,second%60)
                            }
                            second++
                        }
                    }
                }
            }catch (e : InterruptedException){
                Log.d("Song","쓰레드가 죽었습니다.")
            }

        }
    }


}
