package com.serioushyeon.floclone

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.serioushyeon.floclone.databinding.ActivitySongBinding
import android.media.MediaPlayer
import com.google.gson.Gson

class SongActivity : AppCompatActivity() {

    lateinit var binding : ActivitySongBinding
    lateinit var song: Song
    lateinit var timer: Timer
    private var mediaPlayer : MediaPlayer? = null
    private var gson: Gson = Gson()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSong()
        setPlayer(song)

        binding.songDownIb.setOnClickListener{
            finish()
        }
        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(true)
        }
        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
        }
        binding.songRepeatIv.setOnClickListener {
            setRepeatStatus(true)
        }
        binding.songRepeatOnIv.setOnClickListener {
            setRepeatStatus(false)
        }
        binding.songRandomIv.setOnClickListener {
            setRandomStatus(true)
        }
        binding.songRandomOnIv.setOnClickListener {
            setRandomStatus(false)
        }
        binding.songLikeOnIv.setOnClickListener {
            setLikeStatus(false)
        }
        binding.songLikeIv.setOnClickListener {
            setLikeStatus(true)
        }
        binding.songUnlikeOnIv.setOnClickListener {
            setUnlikeStatus(false)
        }
        binding.songUnlikeIv.setOnClickListener {
            setUnlikeStatus(true)
        }

        if (intent.hasExtra("title") && intent.hasExtra("singer")){
            binding.songMusicTitleTv.text = intent.getStringExtra("title")
            binding.songSingerNameTv.text = intent.getStringExtra("singer")
        }
    }
    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)
        song.second = ((binding.songProgressSb.progress * song.playTime)/100)/1000
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()   //에디터
        val songJson = gson.toJson(song)
        editor.putString("songData", songJson)

        editor.apply()
    }
    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release()  // 리소스
        mediaPlayer = null
    }
    fun setLikeStatus(isLike: Boolean) {
        if (isLike) {
            binding.songLikeOnIv.visibility = View.VISIBLE
            binding.songLikeIv.visibility = View.GONE
        } else {
            binding.songLikeIv.visibility = View.VISIBLE
            binding.songLikeOnIv.visibility = View.GONE
        }
    }
    fun setUnlikeStatus(isUnlike : Boolean){
        if(isUnlike){
            binding.songUnlikeOnIv.visibility = View.VISIBLE
            binding.songUnlikeIv.visibility = View.GONE
        }
        else {
            binding.songUnlikeIv.visibility = View.VISIBLE
            binding.songUnlikeOnIv.visibility = View.GONE
        }
    }

    fun setRepeatStatus(isRepeating : Boolean){
        song.isRepeating = isRepeating
        if(isRepeating){
            binding.songRepeatOnIv.visibility = View.VISIBLE
            binding.songRepeatIv.visibility = View.GONE
        }
        else {
            binding.songRepeatOnIv.visibility = View.GONE
            binding.songRepeatIv.visibility = View.VISIBLE
        }
    }

    fun setRandomStatus(isRandoming : Boolean){
        if(isRandoming){
            binding.songRandomOnIv.visibility = View.VISIBLE
            binding.songRandomIv.visibility = View.GONE
        }
        else {
            binding.songRandomOnIv.visibility = View.GONE
            binding.songRandomIv.visibility = View.VISIBLE
        }
    }

    private fun initSong(){
        if(intent.hasExtra("title") && intent.hasExtra("singer")){
            song = Song(
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("img", 0),
                intent.getIntExtra("second", 0),
                intent.getIntExtra("playTime", 0),
                intent.getBooleanExtra("isPlaying", false),
                intent.getStringExtra("music")!!,
                intent.getBooleanExtra("isRepeating", false)
            )
        }
        startTimer()
    }

    private fun setPlayer(song: Song){
        binding.songMusicTitleTv.text = intent.getStringExtra("title")!!
        binding.songSingerNameTv.text = intent.getStringExtra("singer")!!
        binding.songStartTimeTv.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        Log.d("Song", binding.songEndTimeTv.text.toString())
        Log.d("Song", song.playTime.toString())
        binding.songProgressSb.progress = (song.second * 1000 / song.playTime)
        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)
        setPlayerStatus(song.isPlaying)
    }

    fun setPlayerStatus(isPlaying : Boolean){
        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying){
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
            mediaPlayer?.start()
        }
        else {
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
            if(mediaPlayer?.isPlaying == true){
                mediaPlayer?.pause()
            }
        }
    }

    private fun startTimer(){
        timer = Timer(song.playTime, song.isPlaying)
        timer.start()
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
                            binding.songProgressSb.progress = ((mills / playTime) * 100).toInt()
                        }

                        if (mills % 1000 == 0f){
                            runOnUiThread{
                                binding.songStartTimeTv.text = String.format("%02d:%02d", second / 60, second % 60)
                            }
                            second++
                        }
                    }
                }
            }catch (e: InterruptedException){
                Log.d("Song", "쓰레드가 죽었습니다. ${e.message}")
            }

        }
    }
}