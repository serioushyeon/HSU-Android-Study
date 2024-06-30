package com.example.practice
//5:38
import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.practice.data.Song
import com.example.practice.databinding.ActivitySongBinding
import com.google.gson.Gson

class SongActivity : AppCompatActivity() {
    private var nowPos = 0
    private lateinit var binding: ActivitySongBinding
    lateinit var timer: Timer
    private var mediaPlayer: MediaPlayer? = null
    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onClickBackButton()
        initPlayList()
        bringIntentData()
    }



    private fun bringIntentData() {
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)
        nowPos = getPlayingSongPosition(songId)
//        with(intent) {
//            songex = Song(
//                singer = getStringExtra("singer").toString(),
//                title = getStringExtra("title").toString(),
//                isPlaying = getBooleanExtra("isPlaying", false),
//                playTime = getIntExtra("playTime", 0),
//                second = getIntExtra("second", 0),
//                music = getStringExtra("music")!!
//            )
//        }
        startTimer()
        setPlayer(songs[nowPos])
    }

    private fun getPlayingSongPosition(songId: Int): Int {
        for (i in 0 until songs.size) {
            if (songs[i].id == songId) {
                return i
            }
        }
        return 0
    }


    private fun setPlayer(song: Song) {
//        with(binding) {
//            songProfileMusicIv.setImageResource(intent.getIntExtra("albumImg",R.drawable.img_album_exp2))
//            with(songex) {
//                songTitleTv.text = title
//                songSingerTv.text = singer
//                songStartTomeTv.text =
//                    String.format("%02d:%02d", songex.second / 60, songex.second % 60)
//                songEndTimeTv.text =
//                    String.format("%02d:%02d", songex.playTime / 60, songex.playTime % 60)
//                songPlayLineMl.progress = (songex.second * 1000 / songex.playTime)
//                mediaPlayer = MediaPlayer.create(this@SongActivity,R.raw.lilac).apply {
//                    seekTo(songex.second * 1000)
//                    if (songex.isPlaying) {
//                        start()
//                    }
//                }
//                playSongState(songex.isPlaying)
//            }
//        }
        with(binding) {
            songProfileMusicIv.setImageResource(
                song.coverImg!!
            )
            songTitleTv.text = song.title
            songSingerTv.text = song.singer
            songStartTomeTv.text =
                String.format("%02d:%02d", song.second / 60, song.second % 60)
            songEndTimeTv.text =
                String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
            songPlayLineMl.progress = (song.second * 1000 / song.playTime)
            val music = resources.getIdentifier(song.music, "raw", this@SongActivity.packageName)
            Log.d("setPlayer", "Music Resource ID:  for ${song.music}")

            mediaPlayer = MediaPlayer.create(this@SongActivity, music)
//            mediaPlayer = MediaPlayer.create(this@SongActivity, music).apply {
//                seekTo(song.second * 1000)
//                if (song.isPlaying) {
//                    start()
//                }
//            }
            if (song.isLike) {
                binding.songLikeBtn.setImageResource(R.drawable.ic_my_like_on)

            } else {
                binding.songLikeBtn.setImageResource(R.drawable.ic_my_like_off)

            }
            playSongState(song.isPlaying)
        }
    }

    private fun initClickedListener() {

    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun initPlayList() {
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    private fun playSongState(state: Boolean) {
        songs[nowPos].isPlaying = state
        timer.isPlaying = state
        with(binding) {
            if (state) {
                songPauseBt.visibility = View.VISIBLE
                songPlayBt.visibility = View.GONE
                mediaPlayer?.start()
            } else {
                songPlayBt.visibility = View.VISIBLE
                songPauseBt.visibility = View.GONE
                if (mediaPlayer?.isPlaying == true) {
                    mediaPlayer?.pause()
                }
                else{

                }
            }
        }
    }

//    override fun onResume() {
//        super.onResume()
//        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
//        val songJson = sharedPreferences.getString("songData", null)
//        if (songJson != null) {
//            songex = gson.fromJson(songJson, Song::class.java)
//            setPlayer()
//        }
//    }

    override fun onPause() {
        super.onPause()

        songs[nowPos].second =
            ((binding.songPlayLineMl.progress * songs[nowPos].playTime) / 100) / 1000
        songs[nowPos].isPlaying = false
        playSongState(false)
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("songId", songs[nowPos].id)
        editor.apply()
    }

    private fun startTimer() {
        timer = Timer(songs[nowPos].playTime, songs[nowPos].isPlaying)
        timer.start()
    }

    private fun playLikeState(state: Boolean) {
        songs[nowPos].isLike = !state
        songDB.songDao().updateIsLikeById(!state, songs[nowPos].id)
        with(binding) {
            if (!state) {
                binding.songLikeBtn.setImageResource(R.drawable.ic_my_like_on)
            } else {
                binding.songLikeBtn.setImageResource(R.drawable.ic_my_like_off)
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
            songLikeBtn.setOnClickListener {
                playLikeState(songs[nowPos].isLike)
            }

            binding.songSkipNextIb.setOnClickListener {
                moveSong(+1)
            }
            binding.songSkipPreviewIb.setOnClickListener {
                moveSong(-1)
            }
        }
    }

    private fun moveSong(direct: Int) {
        if (nowPos + direct < 0) {
            Toast.makeText(this, "first Song", Toast.LENGTH_SHORT).show()
            return
        }
        if (nowPos + direct >= songs.size) {
            Toast.makeText(this, "Last Song", Toast.LENGTH_SHORT).show()
            return
        }
        nowPos += direct
        timer.interrupt()
        startTimer()
        mediaPlayer?.release()
        mediaPlayer = null

        setPlayer(songs[nowPos])


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
                                binding.songStartTomeTv.text =
                                    String.format("%02d:%02d", second / 60, second % 60)
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