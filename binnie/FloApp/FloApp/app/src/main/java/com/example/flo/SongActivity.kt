package com.example.flo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() { // 액티비티에서 안드로이드 기능을 사용할 수 있게 해주는 것
    lateinit var binding: ActivitySongBinding
    lateinit var song: Song
    lateinit var timer: Timer

    // 이미지 변경을 위한 boolean 변수 초기화
    var isPlay:Boolean = false
    var isRepeat:Int = 1
    var isShuffle:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSong()
        setPlayer(song)
        initClickListener()



//        // 플레이 버튼 클릭 이벤트
//        binding.songPlaybtnIv.setOnClickListener {
//            if(isPlay==false) {
//                binding.songPlaybtnIv.setImageResource(R.drawable.btn_miniplay_pause)
//                isPlay=true
//            }
//            else {
//                binding.songPlaybtnIv.setImageResource(R.drawable.btn_miniplayer_play)
//                isPlay=false
//            }
//        }
//
//        // 반복 버튼 클릭 이벤트
//        val layoutParams = binding.songRepeatbtnIv.layoutParams
//        val width = 40
//        val height = 40
//        binding.songRepeatbtnIv.setOnClickListener {
//            if(isRepeat == 1) {
//                binding.songRepeatbtnIv.setImageResource(R.drawable.nugu_btn_repeat_one)
//                Toast.makeText(this, "현재 음악을 반복합니다.", Toast.LENGTH_SHORT).show()
////                layoutParams.width = width
////                layoutParams.height = height
////                binding.songRepeatbtnIv.layoutParams = layoutParams
//                isRepeat = 2
//            }
//            else if(isRepeat == 2){
//                binding.songRepeatbtnIv.setImageResource(R.drawable.nugu_btn_repeat_all)
//                Toast.makeText(this, "전체음악을 반복합니다.", Toast.LENGTH_SHORT).show()
//                isRepeat = 3
//            }
//            else {
//                binding.songRepeatbtnIv.setImageResource(R.drawable.nugu_btn_repeat_inactive)
//                Toast.makeText(this, "반복을 사용하지 않습니다.", Toast.LENGTH_SHORT).show()
//                isRepeat = 1
//            }
//        }
//        // 셔플 버튼 클릭 이벤트
//        binding.songShufflebtnIv.setOnClickListener {
//            if(isShuffle == false) {
//                binding.songShufflebtnIv.setImageResource(R.drawable.nugu_btn_random_active)
//                Toast.makeText(this, "재생목록을 랜덤으로 재생합니다.", Toast.LENGTH_SHORT).show()
//                isShuffle = true
//            }
//            else {
//                binding.songShufflebtnIv.setImageResource(R.drawable.nugu_btn_repeat_inactive)
//                Toast.makeText(this, "재생목록을 순서대로 재생합니다.", Toast.LENGTH_SHORT).show()
//                isShuffle = false
//            }
//        }
    }
    private fun initClickListener(){
        binding.songDownbtnIv.setOnClickListener {
            finish()
        }

        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(true)
        }

        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
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
    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
    }
    private fun initSong() {
        if(intent.hasExtra("title") && intent.hasExtra("singer")) {
            song = Song(
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second", 0),
                intent.getIntExtra("playTime", 0),
                intent.getBooleanExtra("isPlaying", false),
                intent.getBooleanExtra("isLIke", false)

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

    private fun setPlayer(song: Song) {
        binding.songTitleTv.text = intent.getStringExtra("title")!!
        binding.songSingerTv.text = intent.getStringExtra("singer")!!
        binding.songStartTimeTv.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.songProgressSb.progress = (song.second * 1000 / song.playTime)

        setPlayerStatus(song.isPlaying)

    }
    private fun setPlayerStatus(isPlaying : Boolean) {
        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying) {
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
        } else {
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
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
                while(true) {
                    if(second >= playTime) {
                        break
                    }
                    if(isPlaying) {
                        sleep(50)
                        mills += 50

                        runOnUiThread {
                            binding.songProgressSb.progress = ((mills / playTime)+100).toInt()
                        }
                        if(mills % 1000 == 0f) {
                            runOnUiThread {
                                binding.songStartTimeTv.text = String.format("%02d:%02d", second / 60, second % 60)
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