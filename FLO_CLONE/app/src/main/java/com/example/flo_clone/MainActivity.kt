package com.example.flo_clone

import android.Manifest
import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.flo_clone.databinding.ActivityMainBinding
import com.example.flo_clone.room.entity.AlbumEntity
import com.example.flo_clone.ui.home.HomeFragment
import com.example.flo_clone.ui.locker.LockerFragment
import com.example.flo_clone.ui.look.LookFragment
import com.example.flo_clone.ui.search.SearchFragment
import com.example.flo_clone.room.database.SongDatabase
import com.example.flo_clone.room.entity.SongEntity
import com.example.flo_clone.ui.song.SongActivity
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding // 뷰 바인딩 함수

    private var song: SongEntity = SongEntity()
    private var gson:Gson = Gson()

    private val CHANNEL_ID = "testChannel01"
    private lateinit var notificationManager: NotificationManager

    private var abEntity = AlbumEntity()


    companion object {
        const val STRING_INTENT_KEY = "my_string_key"
        const val TAG = "MainActivity"
    }
    private val getResultText = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val returnString = result.data?.getStringExtra(MainActivity.STRING_INTENT_KEY)
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

        setStartFragment()      // 시작 프래그먼트 설정 (홈)
        setBottomNavigation()   // 바텀네비게이션 뷰 설정
        changeActivity()        // MainPlayer 클릭하면 액티비티 변경

        createNotificationChannel(CHANNEL_ID, "Test Channel", "Test Channel Description")

        binding.playerListBtn.setOnClickListener {
          displayNotification()
        }
    }

    // 토스트 메시지 만드는 함수
    private fun makeToastMsg(msg: String) {
        Toast.makeText(this, "${msg}", Toast.LENGTH_SHORT).show()
    }

    // mainPlayer 클릭하면 액티비티 변경하는 함수
    private fun changeActivity() {
//        val song = Song(binding.mainMiniPlayerTitleTv.text.toString(), binding.mainMiniPlayerSingerTv.text.toString(),
//            0, 214, false, "music_lilac")

        binding.mainPlayer.setOnClickListener {
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songID", song.id)
            editor.apply()

            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }
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
                    supportFragmentManager.beginTransaction().replace(R.id.frame_layout,
                        HomeFragment()).commit()
                    true
                }
                R.id.fragment_look -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame_layout,
                        LookFragment()).commit()
                    true
                }
                R.id.fragment_serach -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame_layout,
                        SearchFragment()).commit()
                    true
                }
                R.id.fragment_locker -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame_layout,
                        LockerFragment()).commit()
                    true
                }
                else -> false// when 문의 else
            }

        }
    }

    // 미니 플레이어의 제목, 가수명, seekBar progress를 Song의 데이터로 설정하는 함수
    private fun setMiniPlayer(song: SongEntity) {
        val title = intent.getStringExtra("album_title")
        val singer = intent.getStringExtra("album_singer")

        binding.mainMiniPlayerTitleTv.text = title ?: song.title
        binding.mainMiniPlayerSingerTv.text = singer ?: song.singer
        binding.mainSongProgressSb.progress = (song.second * 100000) / song.playTime
    }


    // Room Entity에 더미데이터 추가하는 함수
    private fun inputDummySongs() {
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()

        if (songs.isNotEmpty()) return

        songDB.songDao().insert(
            SongEntity(
                "Lilac",
                "아이유 (IU)",
                0,
                200,
                false,
                "music_lilac",
                false,
                R.drawable.img_album_exp2,
                false,
                0,
            )
        )

        songDB.songDao().insert(
            SongEntity(
                "Flu",
                "아이유 (IU)",
                0,
                200,
                false,
                "music_flu",
                false,
                R.drawable.img_album_exp2,
                false,
                1,
            )
        )

        songDB.songDao().insert(
            SongEntity(
                "Butter",
                "방탄소년단 (BTS)",
                0,
                190,
                false,
                "music_butter",
                false,
                R.drawable.img_album_exp,
                false,
                2,
            )
        )

        songDB.songDao().insert(
            SongEntity(
                "Next Level",
                "에스파 (AESPA)",
                0,
                210,
                false,
                "music_next",
                false,
                R.drawable.img_album_exp3,
                false,
                3,
            )
        )


        songDB.songDao().insert(
            SongEntity(
                "Boy with Luv",
                "music_boy",
                0,
                230,
                false,
                "music_lilac",
                false,
                R.drawable.img_album_exp4,
                false,
                4,
            )
        )


        songDB.songDao().insert(
            SongEntity(
                "BBoom BBoom",
                "모모랜드 (MOMOLAND)",
                0,
                240,
                false,
                "music_bboom",
                false,
                R.drawable.img_album_exp5,
                false,
                5,
            )
        )

        val _songs = songDB.songDao().getSongs()
        Log.d("DB data", _songs.toString())
    }

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

        val songDB = SongDatabase.getInstance(this)!!

        // 저장된 song 데이터 없으면 첫 번째 인덱스 가져옴
        song = if (songId == 0) {
            songDB.songDao().getSong(1)
        } else {
            songDB.songDao().getSong(songId)    // 있으면 songId로 저장된 song 가져옴
        }

        setMiniPlayer(song)
    }

}