package com.example.flo_clone.feature.main

import android.Manifest
import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.flo_clone.R
import com.example.flo_clone.databinding.ActivityMainBinding
import com.example.flo_clone.core.data.model.local.entities.AlbumEntity
import com.example.flo_clone.feature.home.HomeFragment
import com.example.flo_clone.feature.locker.LockerFragment
import com.example.flo_clone.feature.look.LookFragment
import com.example.flo_clone.feature.search.SearchFragment
import com.example.flo_clone.core.data.model.local.database.SongDatabase
import com.example.flo_clone.core.data.model.local.entities.SongEntity
import com.example.flo_clone.feature.song.SongActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding // 뷰 바인딩 함수

    private val CHANNEL_ID = "testChannel01"
    private lateinit var notificationManager: NotificationManager

    private var song: SongEntity = SongEntity()
    private var abEntity = AlbumEntity()

    private lateinit var songDB: SongDatabase
    private val songsList = arrayListOf<SongEntity>()
    private  var nowPos = 0

    private lateinit var timer: Timer
    private var mediaPlayer : MediaPlayer? = null


    companion object {
        const val STRING_INTENT_KEY = "my_string_key"
        const val TAG = "MainActivity"
    }
    private val getResultText = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val returnString = result.data?.getStringExtra(STRING_INTENT_KEY)
            returnString?.let { makeToastMsg(it) }
        }
    }

    fun updateValue(albumEntity: AlbumEntity) {
        abEntity = albumEntity
        binding.mainMiniPlayerTitleTv.text = albumEntity.title.toString()
        binding.mainMiniPlayerSingerTv.text = albumEntity.singer.toString()
    }

    private fun displayNotification() {

        // 클릭시 실행할 액비티비티 설정
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notifyId = 0
        // 채널 생성
        val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat
                .Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.img_album_exp2)
                .setContentTitle("Flo")
                .setContentText("아이유 Lilac")
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)
                .setProgress(100, 0,true)

        } else {
            TODO("VERSION.SDK_INT < O")
        }

        // 백그라운드 스코프를 사용하여 코루틴 실행
        CoroutineScope(Dispatchers.IO).launch {
            for (i in 1..1000) {
                notification.setProgress(300, i, true)
                if (ActivityCompat.checkSelfPermission(
                        this@MainActivity,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return@launch
                }
                NotificationManagerCompat.from(this@MainActivity).notify(notifyId, notification.build())
                delay(100)
            }

        }
        NotificationManagerCompat.from(this).notify(notifyId, notification.build())

    }

    private fun createNotificationChannel(channelId: String, name: String, channelDescription: String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = channelDescription
            }

            // 알림 매니저 생성 및 채널에 등록
            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager?.createNotificationChannel(channel)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputDummySongs()
        inputDummyAlbums()
        initPlayList()          // songEntity 정보들 배열에 저장 (초기화)
        setBottomNavigation()  // 바텀네비게이션 뷰 설정
        setStartFragment()    // 시작 프래그먼트 설정 (홈)
        setButton()          // MainPlayer 클릭하면 액티비티 변경
        initSong()          // 노래 초기화

        createNotificationChannel(CHANNEL_ID, "Test Channel", "Test Channel Description")

        binding.playerListBtn.setOnClickListener {
          displayNotification()
        }

        Log.d("MAIN/JWT_TO_SERVER", getJwt().toString())
    }

    // 토스트 메시지 만드는 함수
    private fun makeToastMsg(msg: String) {
        Toast.makeText(this, "${msg}", Toast.LENGTH_SHORT).show()
    }


    private fun getJwt(): String? {
        val spf = this?.getSharedPreferences("auth2", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getString("jwt", "")
    }

    // 처음 시작하는 프래그먼트 설정하는 함수
    private fun setStartFragment() {
        val homeFragment = HomeFragment() // 홈 프래그먼트 생성

        // 시작 프래그먼트 생성
        supportFragmentManager.beginTransaction()
            .add(R.id.frame_layout, homeFragment).commit()
    }

    // 바텀네비게이션 뷰를 설정하는 함수 -> menu 파일의 home_navigation_menu 파일 사용
    private fun setBottomNavigation() {
        val bottomNavigationView = binding.bottomNav
        bottomNavigationView.itemIconTintList = null

        // 바텀 네비게이션 뷰의 아이템 선택 리스너
        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.fragment_home -> { // 홈 프래그먼트가 선택되면 현재 프래그먼트를 홈 프래그먼트로 변경 (이하 동일)
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frame_layout,
                        HomeFragment()).commit()
                    true
                }
                R.id.fragment_look -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frame_layout,
                        LookFragment()).commit()
                    true
                }
                R.id.fragment_serach -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frame_layout,
                        SearchFragment()).commit()
                    true
                }
                R.id.fragment_locker -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frame_layout,
                        LockerFragment()).commit()
                    true
                }
                else -> false// when 문의 else
            }

        }
    }

    // 미니 플레이어의 제목, 가수명, seekBar progress를 Song의 데이터로 설정하는 함수
    private fun setMiniPlayer(song: SongEntity) {
        binding.mainMiniPlayerTitleTv.text = song.title
        binding.mainMiniPlayerSingerTv.text = song.singer
        binding.mainSongProgressSb.progress = (song.second * 100000) / song.playTime
        mediaPlayer?.seekTo(song.second * 1000) // 불러온 재생 위치로 이동

        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)

        setPlayerStatus(song.isPlaying)
    }

    private fun setButton() {

        // mainPlayer 클릭하면 로 SongActivity로이동 (SongActivity로 데이터 연동)
        binding.mainPlayer.setOnClickListener {
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songID", song.id)
            editor.apply()

            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }

        // 재생/정지 버튼 클릭
        binding.mainMiniPlayerStartBtn.setOnClickListener{
            setPlayerStatus(true)
        }
        binding.mainPlayerPauseBtn.setOnClickListener{
            setPlayerStatus(false)
        }

        // 다음/이전 곡 버튼 클릭
        binding.mainMiniNextMusicBtn.setOnClickListener {
            moveSong(+1)
        }

        binding.mainMiniPlayerPrevBtn.setOnClickListener {
            moveSong(-1)
        }
    }

    // 이전, 다음 곡으로 노래 이동
    private fun moveSong(direct: Int) {
        if (nowPos + direct < 0) {
            Toast.makeText(this, "first song", Toast.LENGTH_LONG).show()
            return
        }

        if (nowPos + direct >= songsList.size) {
            Toast.makeText(this, "last song", Toast.LENGTH_LONG).show()
            return
        }

        nowPos += direct

        timer.interrupt()
        startTimer()

        mediaPlayer?.release()
        mediaPlayer = null

        setMiniPlayer(songsList[nowPos])
    }


    // 현재 위치의 음악 반환
    private fun getPlayingSongPosition(songId: Int): Int {
        for (i in 0 until songsList.size) {
            if (songsList[i].id == songId) {
                return i
            }
        }
        return 0
    }

    // SongActivity와 같이 리스트에 엔티티 추가
    private fun initPlayList() {
        songDB = SongDatabase.getInstance(this)!!
        songsList.addAll(songDB.songDao().getSongs())
    }

    // 노래 초기화하는 함수
    private fun initSong() {
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        nowPos = getPlayingSongPosition(songId)

        Log.d("now Song ID", songsList[nowPos].id.toString())

        startTimer()
        setMiniPlayer(songsList[nowPos])
    }

    // 재생/정지 버튼 클릭시 호출되는 함수 -> 클릭시 이미지 변경, 음악 재생 및 정지
    private fun setPlayerStatus(isPlaying: Boolean) {
        songsList[nowPos].isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if (isPlaying) {
            binding.mainMiniPlayerStartBtn.visibility = View.GONE
            binding.mainPlayerPauseBtn.visibility = View.VISIBLE
            mediaPlayer?.start()
        } else {
            binding.mainMiniPlayerStartBtn.visibility = View.VISIBLE
            binding.mainPlayerPauseBtn.visibility = View.GONE
            if(mediaPlayer?.isPlaying == true){
                mediaPlayer?.pause()
            }
        }
    }

    // Room Entity에 더미데이터 추가하는 함수
    private fun inputDummySongs() {
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()

        val song1 = SongEntity("Lilac", "아이유 (IU)", 0, 200, false, "music_lilac", false,
            R.drawable.img_album_exp2, false, 0,)
        val song2 =  SongEntity("Flu", "아이유 (IU)", 0, 200, false, "music_flu", false,
            R.drawable.img_album_exp2, false, 1,)
        val song3 = SongEntity("Butter", "방탄소년단 (BTS)", 0, 190, false, "music_butter", false,
            R.drawable.img_album_exp, false, 2,)
        val song4 = SongEntity("Next Level", "에스파 (AESPA)", 0, 210, false, "music_next", false,
            R.drawable.img_album_exp3, false, 3,)
        val song5 = SongEntity("Boy with Luv", "방탄소년단 (BTS)", 0, 11, false, "music_boy", false,
            R.drawable.img_album_exp4, false, 4,)
        val song6 = SongEntity("BBoom BBoom", "모모랜드 (MOMOLAND)", 0, 240, false, "music_bboom", false,
            R.drawable.img_album_exp5, false, 5,)

        if (songs.isNotEmpty()) return

        songDB.songDao().insert(song1)
        songDB.songDao().insert(song2)
        songDB.songDao().insert(song3)
        songDB.songDao().insert(song4)
        songDB.songDao().insert(song5)
        songDB.songDao().insert(song6)

        val _songs = songDB.songDao().getSongs()
        Log.d("DB data", _songs.toString())
    }

    // Album Entity에 더미데이터 추가하는 함수
    private fun inputDummyAlbums() {
        val songDB = SongDatabase.getInstance(this)!!
        val albums = songDB.albumDao().getAlbums()

        if (albums.isNotEmpty()) return

        songDB.albumDao().insert(
            AlbumEntity(
                0,
                "IU 5th Album 'LILAC'", "아이유 (IU)", R.drawable.img_album_exp2
            )
        )

        songDB.albumDao().insert(
            AlbumEntity(
                1,
                "Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp
            )
        )

        songDB.albumDao().insert(
            AlbumEntity(
                2,
                "iScreaM Vol.10 : Next Level Remixes", "에스파 (AESPA)", R.drawable.img_album_exp3
            )
        )

        songDB.albumDao().insert(
            AlbumEntity(
                3,
                "MAP OF THE SOUL : PERSONA", "방탄소년단 (BTS)", R.drawable.img_album_exp4
            )
        )

        songDB.albumDao().insert(
            AlbumEntity(
                4,
                "GREAT!", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5
            )
        )

    }

    override fun onStart() {
        super.onStart()

        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)
        val songSecond = spf.getInt("songSecond", 0) // 저장된 재생 위치 불러오기

        val songDB = SongDatabase.getInstance(this)!!

        // 저장된 song 데이터 없으면 첫 번째 인덱스 가져옴
        song = if (songId == 0) {
            songDB.songDao().getSong(1)
        } else {
            songDB.songDao().getSong(songId)    // 있으면 songId로 저장된 song 가져옴
        }

        song.second = songSecond // 불러온 재생 위치 설정
        setMiniPlayer(song)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release() // 미디어 플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null // 미디어 플레이어 해제
    }

    private fun startTimer() {
        timer = Timer(songsList[nowPos].playTime, songsList[nowPos].isPlaying)
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
                        mediaPlayer?.pause()
                        break
                    }

                    if (isPlaying) {
                        sleep(50)
                        mills += 50

                        runOnUiThread {
                            binding.mainSongProgressSb.progress = ((mills / playTime) * 100).toInt()
                        }
                        if (mills % 1000 == 0f) {
                            second++
                        }
                    }
                }
            } catch (e:InterruptedException) {
                Log.d("Song", "스레드가 죽었습니다. ${e.message}")
            }
        }
    }

}