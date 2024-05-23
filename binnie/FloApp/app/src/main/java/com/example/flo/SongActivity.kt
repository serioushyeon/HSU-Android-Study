package com.example.flo

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding
import com.google.gson.Gson

class SongActivity : AppCompatActivity() { // 액티비티에서 안드로이드 기능을 사용할 수 있게 해주는 것
    lateinit var binding: ActivitySongBinding
    lateinit var song: Song
    lateinit var timer: Timer
    private var mediaPlayer: MediaPlayer? = null
    var isRepeatEnabled = 1
    private var gson : Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSong()
        setPlayer(song)
        initClickListener()
    }
    // 사용자가 포커스를 잃었을 때 음악이 중지
    // 생명주기를 지정하지 않으면 홈버튼을 눌렀을 때 음악이 중지되지 않음
    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)

        song.second = (song.playTime * binding.songProgressSb.progress) / 100000
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE) // 앱의 SharedPreferences 객체를 가져온다
        val editor = sharedPreferences.edit()
        val songToJson = gson.toJson(song)
        editor.putString("songData", songToJson)
        Log.d("songData", songToJson.toString())
        editor.apply()
    }
    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release() // 미디어 플레이어가 가지고 있던 리소스 해제
        mediaPlayer = null // 미디어 플레이어 해제
    }

    private fun initClickListener(){
        binding.songDownbtnIv.setOnClickListener {
            finish()
        }

        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(true)
            startStopService()
        }

        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
            startStopService()
        }
        binding.songRepeatbtnIv.setOnClickListener {
            setRepeat()
        }

//        binding.songNextIv.setOnClickListener {
//            moveSong(+1)
//        }
//
//        binding.songPreviousIv.setOnClickListener {
//            moveSong(-1)
//        }
//
        binding.songLikeIv.setOnClickListener {
            setLike(song.isLike)
        }
    }
    // 서비스
    private fun startStopService() {
        if (isServiceRunning(ForegroundService::class.java)) {
            Toast.makeText(this, "Foreground Service Stopped", Toast.LENGTH_SHORT).show()
            stopService(Intent(this, ForegroundService::class.java))
        }
        else {
            Toast.makeText(this, "Foreground Service Started", Toast.LENGTH_SHORT).show()
            startService(Intent(this, ForegroundService::class.java))
        }
    }

    private fun isServiceRunning(inputClass : Class<ForegroundService>) : Boolean {
        val manager : ActivityManager = getSystemService(
            Context.ACTIVITY_SERVICE
        ) as ActivityManager

        for (service : ActivityManager.RunningServiceInfo in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (inputClass.name.equals(service.service.className)) {
                return true
            }

        }
        return false
    }
    private fun initSong() {
        if(intent.hasExtra("title") && intent.hasExtra("singer")) {
            song = Song(
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second", 0),
                intent.getIntExtra("playTime", 0),
                intent.getBooleanExtra("isPlaying", false),
                intent.getBooleanExtra("isLIke", false),
                intent.getStringExtra("music")!!
            )
        }
        startTimer()
    }
    private fun setLike(isLike : Boolean) {
        if(!isLike) {
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
            song.isLike = true
        } else {
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
            song.isLike = false
        }
    }
    private fun setRepeat() {
        if(isRepeatEnabled == 1) {
            binding.songRepeatbtnIv.setImageResource(R.drawable.nugu_btn_repeat_all)
            Toast.makeText(this, "전체 음악을 반복합니다.", Toast.LENGTH_SHORT).show()
            isRepeatEnabled = 2
        } else if(isRepeatEnabled == 2) {
            binding.songRepeatbtnIv.setImageResource(R.drawable.nugu_btn_repeat_one)
            Toast.makeText(this, "현재 음악을 반복합니다.", Toast.LENGTH_SHORT).show()
            isRepeatEnabled = 3

        } else {
            binding.songRepeatbtnIv.setImageResource(R.drawable.nugu_btn_repeat_inactive)
            Toast.makeText(this, "반복을 사용하지 않습니다.", Toast.LENGTH_SHORT).show()
            isRepeatEnabled = 1
        }

    }

    private fun setPlayer(song: Song) {
        binding.songTitleTv.text=intent.getStringExtra("title")
        binding.songSingerTv.text=intent.getStringExtra("singer")
        binding.songStartTimeTv.text=String.format("%02d:%02d",song.second/60,song.second%60)
        binding.songEndTimeTv.text=String.format("%02d:%02d",song.playTime/60,song.playTime%60)
        binding.songProgressSb.progress=(song.second * 1000 / song.playTime)
        //binding.songProgressSb.progress = ((song.second / 1000f) / song.playTime * 100).toInt()

        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)
        // MediaPlayer 재생이 끝났을 때 호출되는 리스너 설정
        mediaPlayer?.setOnCompletionListener {
            if (isRepeatEnabled == 2 || isRepeatEnabled == 3) { // 한 곡 반복 재생인 경우
                mediaPlayer?.seekTo(0) // 재생 위치를 처음으로 되돌림
                mediaPlayer?.start() // 재생 시작
            }
        }
        setPlayerStatus(song.isPlaying)


    }
    private fun setPlayerStatus(isPlaying : Boolean) {
        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying) {
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
            mediaPlayer?.start()
        } else {
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
            if(mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
        }
    }

    private fun startTimer() {
        timer = Timer(song.playTime, song.isPlaying)
        timer.start()

    }
    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true): Thread() {
        private var second : Int = 0
        private var mills: Float = 0f

        override fun run() {
            super.run()
            try {
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
                                binding.songStartTimeTv.text = String.format("%02d:%02d",second/60,second%60)
                            }
                            second++
                        }
                    }
                }

            } catch(e: InterruptedException) {
                Log.d("song", "스레드가 죽었습니다. ${e.message}")
            }
        }

    }


}