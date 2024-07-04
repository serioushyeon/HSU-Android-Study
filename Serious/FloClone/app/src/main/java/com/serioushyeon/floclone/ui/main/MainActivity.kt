package com.serioushyeon.floclone.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.serioushyeon.floclone.R
import com.serioushyeon.floclone.data.entities.Album
import com.serioushyeon.floclone.data.entities.Song
import com.serioushyeon.floclone.data.local.SongDatabase
import com.serioushyeon.floclone.databinding.ActivityMainBinding
import com.serioushyeon.floclone.ui.main.home.HomeFragment
import com.serioushyeon.floclone.ui.main.locker.LockerFragment
import com.serioushyeon.floclone.ui.main.look.LookFragment
import com.serioushyeon.floclone.ui.main.search.SearchFragment
import com.serioushyeon.floclone.ui.song.SongActivity

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    lateinit var timer: Timer
    private var mediaPlayer : MediaPlayer? = null

    private var song: Song = Song()
    private var gson: Gson = Gson()

    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setTheme(R.style.Theme_FloClone)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputDummyAlbums()
        inputDummySongs()
        initBottomNavigation()
        initPlayList()
        initSong()
        initClickListener()
        //val song = Song(binding.mainMiniplayerTitleTv.text.toString(), binding.mainMiniplayerSingerTv.text.toString(), 0, 60, false, "music_lilac")

        binding.mainPlayerCl.setOnClickListener {
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId", song.id)
            editor.apply()

            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }


        Log.d("MAIN/JWT_TO_SERVER", getJwt().toString())

    }

    private fun getJwt(): String? {
        val spf = this.getSharedPreferences("auth2" , AppCompatActivity.MODE_PRIVATE)
        Log.d("Song", song.title + song.singer)
        return spf!!.getString("jwt", "")
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

    fun setMiniPlayer(song: Song){
        binding.mainMinipalyerTitleTv.text = song.title
        binding.mainMinipalyerSingerTv.text = song.singer
        binding.mainMiniplayerProgressSb.progress = (song.second*100000)/song.playTime

        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)

        setPlayerStatus(song.isPlaying)
    }

    private fun inputDummySongs(){
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()
        val song1 = Song("숲", "최유리",
            R.drawable.img_pannel_album1, 0, 60, false, "forest", false, false)
        val song2 = Song("Lucky Girl Syndrome", "아일릿(ILLIT)",
            R.drawable.img_pannel_album2, 0, 60, false, "luckygirlsyndrome", false, false)
        val song3 = Song("bad idea right?", "Olivia Rodrigo",
            R.drawable.img_pannel_album3, 0, 60, false, "badidearight", false, false)
        val song4 = Song("These Tears", "Andy Grammer",
            R.drawable.img_pannel_album4, 0, 60, false, "thesetears", false, false)
        val song5 = Song("ETA", "NewJeans",
            R.drawable.img_pannel_album5, 0, 60, false, "eta", false, false)
        val song6 = Song("네가 좋은 사람일 수는 없을까", "윤지영",
            R.drawable.img_pannel_album6, 0, 60, false, "goodperson", false, false)
        if (songs.isNotEmpty()) return

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


        songDB.songDao().insert(
            song6
        )

        val _songs = songDB.songDao().getSongs()
        Log.d("DB data", _songs.toString())
    }
    //ROOM_DB
    private fun inputDummyAlbums() {
        val songDB = SongDatabase.getInstance(this)!!
        val albums = songDB.albumDao().getAlbums()

        if (albums.isNotEmpty()) return

        songDB.albumDao().insert(
            Album(
                0,
                "유영", "최유리", R.drawable.img_pannel_album1
            )
        )

        songDB.albumDao().insert(
            Album(
                1,
                "SUPER REAL ME", "아일릿(ILLIT)", R.drawable.img_pannel_album2
            )
        )

        songDB.albumDao().insert(
            Album(
                2,
                "GUTS", "Olivia Rodrigo", R.drawable.img_pannel_album3
            )
        )

        songDB.albumDao().insert(
            Album(
                3,
                "These Tears", "Andy Grammer", R.drawable.img_pannel_album4
            )
        )

        songDB.albumDao().insert(
            Album(
                4,
                "NewJeans 2nd EP 'Get Up'", "NewJeans", R.drawable.img_pannel_album5
            )
        )
        songDB.albumDao().insert(
            Album(
                5,
                "Blue bird", "윤지영", R.drawable.img_pannel_album6
            )
        )
        val _albums = songDB.albumDao().getAlbums()
        Log.d("DB data", _albums.toString())

    }
    override fun onStart() {
        super.onStart()
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId",0)

        val songDB = SongDatabase.getInstance(this)!!

        song = if (songId == 0){
            songDB.songDao().getSong(1)
        } else{
            songDB.songDao().getSong(songId)
        }

        Log.d("song ID", song.id.toString())

        setMiniPlayer(song)
    }
    // 메인 미니바
    fun setPlayerStatus(isPlaying : Boolean){
        songs[nowPos].isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying){
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE
            mediaPlayer?.start()
        }
        else {
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE
            if(mediaPlayer?.isPlaying == true){
                mediaPlayer?.pause()
            }
        }
    }

    private fun startTimer(){
        timer = Timer(songs[nowPos].playTime, songs[nowPos].isPlaying)
        timer.start()
    }
    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun initPlayList(){
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    private fun initClickListener(){
        binding.mainMiniplayerBtn.setOnClickListener {
            setPlayerStatus(true)
        }

        binding.mainPauseBtn.setOnClickListener {
            setPlayerStatus(false)
        }

        binding.songNext.setOnClickListener {
            moveSong(+1)
        }

        binding.songPrevious.setOnClickListener {
            moveSong(-1)
        }
    }

    // 메인 미니바
    private fun initSong(){
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId",0)

        nowPos = getPlayingSongPosition(songId)

        Log.d("now Song ID",songs[nowPos].id.toString())

        startTimer()
        setMiniPlayer(songs[nowPos])
    }

    private fun getPlayingSongPosition(songId: Int): Int{
        for (i in 0 until songs.size){
            if (songs[i].id == songId){
                return i
            }
        }
        return 0
    }


    private fun moveSong(direct: Int){
        if (nowPos + direct < 0){
            Toast.makeText(this,"first song", Toast.LENGTH_SHORT).show()
            return
        }

        if (nowPos + direct >= songs.size){
            Toast.makeText(this,"last song", Toast.LENGTH_SHORT).show()
            return
        }

        nowPos += direct

        timer.interrupt()
        startTimer()

        mediaPlayer?.release()
        mediaPlayer = null

        setMiniPlayer(songs[nowPos])
    }
    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true): Thread() {
        private var second : Int = 0
        private var mills: Float = 0f

        override fun run() {
            super.run()
            try {
                while(true){

                    if(second >= playTime){
                        mediaPlayer?.pause()
                        break
                    }

                    if(isPlaying){
                        sleep(50)
                        mills += 50

                        runOnUiThread{
                            binding.mainMiniplayerProgressSb.progress = ((mills / playTime) * 100).toInt()
                        }

                        if (mills % 1000 == 0f){
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