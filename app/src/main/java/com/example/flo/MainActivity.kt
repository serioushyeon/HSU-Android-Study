package com.example.flo

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.flo.databinding.ActivityMainBinding
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var song: Song = Song()
    lateinit var timer: SongActivity.Timer
    private var mediaPlayer: MediaPlayer? = null
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private var gson : Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomNavigation()
        inputDummyAlbums()
        inputDummySongs() // 노래 더미데이터 넣기


//        song = Song(binding.mainMiniplayerTitleTv.text.toString(), binding.mainMiniplayerSingerTv.text.toString(), 0, 60, false, false, "music_magnetic")
        binding.mainMiniplayerBtn.setOnClickListener {
            setPlayerStatus(true)
            setMiniPlayer(song)
        }
        binding.mainPlayerCl.setOnClickListener {
            //startActivity(Intent(this, SongActivity::class.java))
/*            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("title", song.title)
            intent.putExtra("singer", song.singer)
            intent.putExtra("second", song.second)
            intent.putExtra("playTime", song.playTime)
            intent.putExtra("isPlaying", song.isPlaying)
            intent.putExtra("isLike", song.isLike)
            intent.putExtra("music", song.music)
            startActivity(intent)*/

            val editor = this.getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId", song.id) // 현재 노래의 id를 저장
            editor.apply()

            val intent = Intent(this, SongActivity::class.java)
            //activityResultLauncher.launch(intent)
            startActivity(intent)

        }

/*        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                if (data != null) {
                    val message = data.getStringExtra("message")
                    Log.d("message", message!!)
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }*/
    }
    override fun onStart() {
        super.onStart()
/*        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val jsonToSong = sharedPreferences.getString("songData", null)
        Log.d("jsonToSong", jsonToSong.toString())
        song = if(jsonToSong == null) { // 최초 실행 시
            Song("Magnetic", "아일릿(ILLIT)", 0, 60, false, false, "music_magnetic")
        } else { // SongActivity에서 노래가 한번이라도 pause 된 경우
            gson.fromJson(jsonToSong, Song::class.java)
        }*/

        CoroutineScope(Dispatchers.IO).launch {
            val spf = getSharedPreferences("song", MODE_PRIVATE)
            val songId = spf.getInt("songId", 0)

            val songDB = SongDatabase.getInstance(this@MainActivity)!!

/*            song = if (songId == 0) {
                songDB.songDao().getSong(1)
            } else {
                songDB.songDao().getSong(songId)
            }*/

            withContext(Dispatchers.Main) {
                Log.d("song ID", song.id.toString())
                setMiniPlayer(song)
            }
        }
    }
    private fun setMiniPlayer(song : Song){
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.mainMiniplayerProgressSb.progress = (song.second*100000)/song.playTime

//        val music = resources.getIdentifier(song.music, "raw", this.packageName)
//        mediaPlayer = MediaPlayer.create(this, music)
    }
    private fun setPlayerStatus(isPlaying : Boolean) {
        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying) {
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE
            //mediaPlayer?.start()
        } else {
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE
//            if(mediaPlayer?.isPlaying == true) {
//                mediaPlayer?.pause()
//            }
        }
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
    fun updateMainPlayerCl(album : Album) {
        binding.mainMiniplayerTitleTv.text = album.title
        binding.mainMiniplayerSingerTv.text = album.singer
        binding.mainMiniplayerProgressSb.progress = 0

        Log.d("변경", binding.mainMiniplayerSingerTv.text.toString())
    }

    private fun inputDummyAlbums() {
        val songDB = SongDatabase.getInstance(this)!!
        val albums = songDB.albumDao().getAlbums()

        if(albums.isNotEmpty()) return

        songDB.albumDao().insert(
            Album(
                0,
                "Magnetic",
                "아일릿(ILLIT)",
                R.drawable.img_album_exp3
            )
        )
        songDB.albumDao().insert(
            Album(
                1,
                "Supernova",
                "에스파(aespa)",
                R.drawable.song_supernova
            )
        )
        songDB.albumDao().insert(
            Album(
                2,
                "Next Level",
                "에스파(aespa)",
                R.drawable.img_album_next
            )
        )
        songDB.albumDao().insert(
            Album(
                3,
                "소나기",
                "이클립스(Eclipse)",
                R.drawable.song_eclipse
            )
        )
        songDB.albumDao().insert(
            Album(
                4,
                "Run Run",
                "이클립스(Eclipse)",
                R.drawable.song_eclipse
            )
        )
        val _albums = songDB.albumDao().getAlbums()
        Log.d("DB data", _albums.toString())
    }

    private fun inputDummySongs() {
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()

        val song1 = Song("Magnetic", "아일릿(ILLIT)", 0, 200, false, false, "music_magnetic", R.drawable.img_album_exp3)
        val song2 = Song("Supernova", "에스파(aespa)", 0, 200, false, false, "music_supernova", R.drawable.song_supernova)
        val song3 = Song("Next Level", "에스파(aespa)", 0, 200, false, false, "music_next", R.drawable.img_album_next)
        val song4 = Song("소나기", "이클립스(Eclipse)", 0, 200, false, false, "music_sudden", R.drawable.song_eclipse)
        val song5 = Song("Run Run", "이클립스(Eclipse)", 0, 200, false, false, "music_magnetic", R.drawable.song_eclipse)

        if(songs.isNotEmpty()) return

        // songs에 데이터가 없을 때에는 더미 데이터를 삽입해주어야 함

        try {
            // songs에 데이터가 없을 때에는 더미 데이터를 삽입해주어야 함
            songDB.songDao().insert(
                song1
            )
            songDB.songDao().insert(
                song2
            )
            songDB.songDao().insert(
                song3
            )
            songDB.songDao().insert(
                song4
            )
            songDB.songDao().insert(
                song5
            )

            val _songs = songDB.songDao().getSongs()
            Log.d("DB data", _songs.toString())
        } catch (e: Exception) {
            Log.e("메인에러", "Error inserting dummy songs", e)
        }
    }



}