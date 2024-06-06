package com.example.practice


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.practice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var songs = arrayListOf<Song>()
    //private var albums = arrayListOf<Album>()
    private var nowPos = 0
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var sharedPreferences: SharedPreferences
    private var currentSong: Song? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)

        clearAndInputDummySongs()
        loadSongsFromDatabase()
       // inputDummyAlbums()
        initClickListeners()
        initBottomNavigation()
        loadCurrentSong()
    }

    override fun onStart() {
        super.onStart()
        loadCurrentSong()
    }

    private fun loadSongsFromDatabase() {
        val songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }


    private fun initClickListeners() {
        binding.mainNext.setOnClickListener {
            moveSong(1)
        }
        binding.mainPrevios.setOnClickListener {
            moveSong(-1)
        }
        binding.mainMiniplayerBtn.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initBottomNavigation() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        binding.bottomNavigationview.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    true
                }

                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    true
                }

                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    true
                }

                else -> false
            }
        }
    }

    private fun loadCurrentSong() {
        val songId = sharedPreferences.getInt("songId", 1)
        currentSong = songs.find { it.id == songId }
        currentSong?.let {
            setPlayer(it)
            setMiniplayer(it)
        } ?: run {
            Toast.makeText(this, "Song not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun moveSong(direction: Int) {
        val newPos = (nowPos + direction).coerceIn(0, songs.size - 1)
        nowPos = newPos
        val newSong = songs[nowPos]
        setPlayer(newSong)
        updateSharedPreferences(newSong.id)
    }

    private fun setPlayer(song: Song) {
        mediaPlayer?.release()
        mediaPlayer =
            MediaPlayer.create(this, resources.getIdentifier(song.music, "raw", packageName))
        mediaPlayer?.start()
        setMiniplayer(song)
    }

    private fun setMiniplayer(song: Song) {
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
    }

    private fun updateSharedPreferences(songId: Int) {
        sharedPreferences.edit().putInt("songId", songId).apply()
    }

    private fun clearAndInputDummySongs() {
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()

        if (songs.isNotEmpty()) return
        songDB.songDao()
            .insert(Song("Lilac", "IU", 0, 210, false, "lilac", R.drawable.img_album_exp2, false))
        songDB.songDao().insert(
            Song(
                "butter", "Celebrity", 0, 200, false, "lilac",
                R.drawable.img_album_exp, false
            )
        )
        songDB.songDao().insert(
            Song(
                "flue",
                "Blueming",
                0,
                195,
                false,
                "lilac",
                R.drawable.img_album_exp2,
                false
            )
        )
    }
}
    /*private fun inputDummyAlbums() {
        val songDB = SongDatabase.getInstance(this)!!
        val albums = songDB.albumDao().getAlbums()

        //if (albums.isNotEmpty()) return

        songDB.albumDao().insert(
            Album(
                0,
                "IU 5th Album 'LILAC'", "아이유 (IU)", R.drawable.img_album_exp2
            )
        )

        songDB.albumDao().insert(
            Album(
                1,
                "Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp
            )
        )

        songDB.albumDao().insert(
            Album(
                2,
                "iScreaM Vol.10 : Next Level Remixes", "에스파 (AESPA)", R.drawable.ballad_image1
            )
        )

        songDB.albumDao().insert(
            Album(
                3,
                "MAP OF THE SOUL : PERSONA", "방탄소년단 (BTS)", R.drawable.hiphop_iv1
            )
        )

        songDB.albumDao().insert(
            Album(
                4,
                "GREAT!", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp
            )
        )

    }

}
*/