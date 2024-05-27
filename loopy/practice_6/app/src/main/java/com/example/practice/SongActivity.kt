package com.example.practice

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.practice.data.SongEx
import com.example.practice.databinding.ActivitySongBinding
import com.google.gson.Gson

class SongActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySongBinding
    lateinit var songex: SongEx
    lateinit var timer: Timer
    private var mediaPlayer : MediaPlayer? = null
    private var gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onClickBackButton()
        bringIntentData()
        setPlayer()
    }


    private fun bringIntentData() {
        with(intent) {
            songex = SongEx(
                singer = getStringExtra("singer").toString(),
                title = getStringExtra("title").toString(),
                isPlaying = getBooleanExtra("isPlaying", false),
                playTime = getIntExtra("playTime", 0),
                second = getIntExtra("second", 0),
                music = getStringExtra("music")!!
            )
        }
        startTimer()
    }

    private fun setPlayer() {
        with(binding) {
            songProfileMusicIv.setImageResource(intent.getIntExtra("albumImg",R.drawable.img_album_exp2))
            with(songex) {
                songTitleTv.text = title
                songSingerTv.text = singer
                songStartTomeTv.text =
                    String.format("%02d:%02d", songex.second / 60, songex.second % 60)
                songEndTimeTv.text =
                    String.format("%02d:%02d", songex.playTime / 60, songex.playTime % 60)
                songPlayLineMl.progress = (songex.second * 1000 / songex.playTime)
                mediaPlayer = MediaPlayer.create(this@SongActivity,R.raw.lilac).apply {
                    seekTo(songex.second * 1000)
                    if (songex.isPlaying) {
                        start()
                    }
                }
                playSongState(songex.isPlaying)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun playSongState(state: Boolean) {
        songex.isPlaying = state
        timer.isPlaying = state
        with(binding) {
            if (state) {
                songPauseBt.visibility = View.VISIBLE
                songPlayBt.visibility = View.GONE
                mediaPlayer?.start()
            } else {
                songPlayBt.visibility = View.VISIBLE
                songPauseBt.visibility = View.GONE
                if(mediaPlayer?.isPlaying == true)
                    mediaPlayer?.pause()
                else{}

            }
        }
    }
    override fun onResume() {
        super.onResume()
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val songJson = sharedPreferences.getString("songData", null)
        if (songJson != null) {
            songex = gson.fromJson(songJson, SongEx::class.java)
            setPlayer()
        }
    }
    override fun onPause() {
        super.onPause()
        playSongState(  false)
        songex.second = ((binding.songPlayLineMl.progress * songex.playTime)/100)/1000
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val songJson = gson.toJson(songex)
        editor.putString("songData",songJson)
        editor.apply()

    }
    private fun startTimer() {
        timer = Timer(songex.playTime, songex.isPlaying)
        timer.start()
    }

    private fun playLikeState(state: Boolean) {
        with(binding) {
            if (state) {
                songLikeOnBtn.visibility = View.VISIBLE
                songLikeBtn.visibility = View.GONE
            } else {
                songLikeBtn.visibility = View.VISIBLE
                songLikeOnBtn.visibility = View.GONE
            }
        }
    }

    private fun playUnLikeState(state: Boolean) {
        with(binding) {
            if (state) {
                songUnlikeOnBtn.visibility = View.VISIBLE
                songUnlikeBtn.visibility = View.GONE
            } else {
                songUnlikeBtn.visibility = View.VISIBLE
                songUnlikeOnBtn.visibility = View.GONE
            }
        }
    }

    private fun onClickBackButton() {
        with(binding) {
            songDropButtonIbt.setOnClickListener {
                val intent = Intent(this@SongActivity, MainActivity::class.java).apply {
                    putExtra(MainActivity.STRING_INTENT_KEY, binding.songTitleTv.text.toString())
                }
                setResult(Activity.RESULT_OK, intent)
                if (!isFinishing)
                    finish()
            }

            songPlayBt.setOnClickListener {
                playSongState(true)
            }
            songPauseBt.setOnClickListener {
                playSongState(false)
            }
            songUnlikeBtn.setOnClickListener {
                playUnLikeState(true)
            }
            songUnlikeOnBtn.setOnClickListener {
                playUnLikeState(false)
            }
            songLikeBtn.setOnClickListener {
                playLikeState(true)
            }
            songLikeOnBtn.setOnClickListener {
                playLikeState(false)
            }


        }
    }

    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true) : Thread() {
        private var second: Int = 0
        private var mills: Float = 0f

        override fun run() {
            super.run()
            try {
                while (true) {
                    if (second >= playTime)
                        break

                    if (isPlaying) {
                        sleep(50)
                        mills += 50
                        runOnUiThread {
                            binding.songPlayLineMl.progress = ((mills / playTime) * 100).toInt()
                        }
                        if (mills % 1000 == 0f) {
                            runOnUiThread {
                                binding.songStartTomeTv.text = String.format("%02d:%02d", second / 60, second % 60)
                            }
                            second++
                        }
                    }
                }
            } catch (e: InterruptedException) {
                Log.d("Song", "쓰레드가 죽었습니다. + ${e.message}")
            }
        }
    }

}