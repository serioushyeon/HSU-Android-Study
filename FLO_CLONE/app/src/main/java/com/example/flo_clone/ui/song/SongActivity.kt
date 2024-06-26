package com.example.flo_clone.ui.song

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.flo_clone.MainActivity
import com.example.flo_clone.R
import com.example.flo_clone.databinding.ActivitySongBinding
import com.example.flo_clone.room.database.SongDatabase
import com.example.flo_clone.room.entity.SongEntity
import com.google.gson.Gson

class SongActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySongBinding
    private lateinit var songEntity: SongEntity
    private lateinit var timer: Timer
    private var mediaPlayer: MediaPlayer? = null
    private var gson: Gson = Gson()

    private val songs = arrayListOf<SongEntity>()
    private lateinit var songDB: SongDatabase
    private var nowPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setButton()
        initPlayList()
        initSong()
        //setPlayer(songs[nowPos])

        setPlayerStatus(isPlaying = false)
    }

    // Toast 메시지 만드는 함수
    private fun makeToastMsg(context: Context, msg: String) {
        val toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    // 노래 리스트에 추가
    private fun initPlayList() {
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())

    }

    // 노래 초기화하는 함수
    private fun initSong() {
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        nowPos = getPlayingSongPosition(songId)

        Log.d("now Song ID", songs[nowPos].id.toString())

        startTimer()
        setPlayer(songs[nowPos])
    }

    // 이전, 다음 곡으로 노래 이동
    private fun moveSong(direct: Int) {
        if (nowPos + direct < 0) {
            Toast.makeText(this, "first song", Toast.LENGTH_LONG).show()
            return
        }

        if (nowPos + direct >= songs.size) {
            Toast.makeText(this, "last song", Toast.LENGTH_LONG).show()
            return
        }

        nowPos += direct

        timer.interrupt()
        startTimer()

        mediaPlayer?.release()
        mediaPlayer = null

        setPlayer(songs[nowPos])
    }

    // 리스트에서 현재 노래 위치 확인후 반환하는 함수
    private fun getPlayingSongPosition(songId: Int): Int {
        for (i in 0 until songs.size) {
            if (songs[i].id == songId) {
                return i
            }
        }
        return 0
    }

    private fun setPlayer(songEntity: SongEntity) {
        binding.songTitleTv.text = songEntity.title
        binding.singerTitleTv.text = songEntity.singer
        binding.startTimerTv.text = String.format("%02d:%02d", songEntity.second / 60, songEntity.second % 60)
        binding.endTimerTv.text = String.format("%02d:%02d", songEntity.playTime / 60, songEntity.playTime % 60)
        binding.albumImgIv.setImageResource(songEntity.coverImg!!)
        binding.songProgressSb.progress = (songEntity.second * 1000 / songEntity.playTime)

        val music = resources.getIdentifier(songEntity.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)

        if (songEntity.isLike) {
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        } else {
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }

        setPlayerStatus(songEntity.isPlaying)
    }

    // 좋아요 (하트) 클릭 Room DB 연동 및 뷰 렌더링하는 함수
    private fun setLike(isLike: Boolean) {
        songs[nowPos].isLike = !isLike
        songDB.songDao().updateIsLikeById(!isLike, songs[nowPos].id) // DB 업데이트

        // 뷰 렌더링
        if (!isLike) {
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
            makeToastMsg(this@SongActivity, "좋아요 한 곡에 담겼습니다.")
        } else {
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
            makeToastMsg(this@SongActivity, "좋아요 한 곡이 취소되었습니다.")
        }
    }

    // 뒤로가기 버튼 쓰면 호출되는 함수
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra(MainActivity.STRING_INTENT_KEY, binding.songTitleTv.text.toString())
        }
        setResult(Activity.RESULT_OK, intent)
    }

    private fun setButton() {

        // 재생/정지
        var isPlaying = true // 초기 상태 설정
        binding.nuguBtnDownIb.setOnClickListener{
            finish()
        }
        binding.nuguBtnPlayIb.setOnClickListener {
            isPlaying = !isPlaying // 클릭할 때마다 상태 변경
            setPlayerStatus(isPlaying) // 변경된 상태에 따라 이미지 변경
        }

        // 좋아요 버튼
        binding.songLikeIv.setOnClickListener {
            setLike(songs[nowPos].isLike)
        }

        // 이전 곡 재생
        binding.songPreviousIv.setOnClickListener{
            moveSong(+1)
        }

        // 다음 곡 재생
        binding.songNextIv.setOnClickListener{
            moveSong(-1)
        }

        // 반복 재생
        var isRepeat = false
        binding.nuguBtnRepeatInactiveIb.setOnClickListener{
            isRepeat = !isRepeat
            if (!isRepeat) {
                val color = ContextCompat.getColor(this, R.color.gray_color)
                binding.nuguBtnRepeatInactiveIb.setColorFilter(color)
                songs[nowPos].isRepeating = false
            } else {
                val color = ContextCompat.getColor(this, R.color.blue)
                binding.nuguBtnRepeatInactiveIb.setColorFilter(color)
                songs[nowPos].isRepeating = true
            }
        }

        // 셔플
        var isRandom = false
        binding.nuguBtnRandomInactiveIb.setOnClickListener{
            isRandom = !isRandom
            if (!isRandom) {
                val color = ContextCompat.getColor(this, R.color.gray_color)
                binding.nuguBtnRandomInactiveIb.setColorFilter(color)
            } else {
                val color = ContextCompat.getColor(this, R.color.blue)
                binding.nuguBtnRandomInactiveIb.setColorFilter(color)
            }
        }

        // 블랙리스트?
        var isUnLike = true
        binding.songUnlikeIv.setOnClickListener{
            isUnLike = !isUnLike
            if (isUnLike) {
                binding.songUnlikeIv.setImageResource(R.drawable.btn_player_unlike_on)
            } else {
                binding.songUnlikeIv.setImageResource(R.drawable.btn_player_unlike_off)
            }
        }
    }

    // 재생/정지 버튼 클릭시 호출되는 함수 -> 클릭시 이미지 변경, 음악 재생 및 정지
    private fun setPlayerStatus(isPlaying: Boolean) {
        songs[nowPos].isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if (!isPlaying) {
            binding.nuguBtnPlayIb.setImageResource(R.drawable.nugu_btn_play_32)
            if(mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
        } else {
            binding.nuguBtnPlayIb.setImageResource(R.drawable.nugu_btn_pause_32)
            mediaPlayer?.start()
        }
    }

    private fun startTimer() {
        timer = Timer(songs[nowPos].playTime, songs[nowPos].isPlaying)
        timer.start()
    }

    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true): Thread() {

        private var second: Int = 0
        private var mills: Float = 0f

        override fun run() {
            super.run()

            try {
                while (true) {

                    if (second >= playTime) {
                        if (songEntity.isRepeating) {
                            mills = 0f
                            second = 0
                            mediaPlayer?.seekTo(0)
                            mediaPlayer?.start()
                        } else {
                            mediaPlayer?.pause()
                            break
                        }
                    }

                    if (isPlaying) {
                        sleep(50)
                        mills += 50

                        runOnUiThread {
                            binding.songProgressSb.progress = ((mills / playTime)*100).toInt()
                        }
                        if (mills % 1000 == 0f) {
                            runOnUiThread {
                                binding.startTimerTv.text = String.format("%02d:%02d", songs[nowPos].second / 60, songs[nowPos].second % 60)
                            }
                            second++
                        }
                    }
                }
            } catch (e:InterruptedException) {
                Log.d("Song", "스레드가 죽었습니다. ${e.message}")
            }

        }
    }

    // 사용자가 포커스를 잃었을 때 음악이 중지
    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)
        songs[nowPos].second = ((binding.songProgressSb.progress * songs[nowPos].playTime)/100)/1000

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit() // 에디터

        editor.putInt("songId", songs[nowPos].id)
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release() // 미디어 플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null // 미디어 플레이어 해제
    }
}