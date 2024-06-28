package com.example.floclone

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.floclone.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    //lateinit은 나중에 초기화 될것을 나타냄
    // ActivityMainBinding은 레이아웃 파일이름에 Binding을 추가한이름
    lateinit var binding: ActivityMainBinding // 뷰 바인딩 함수
    //전역변수
    private var song: Song = Song()
    private var gson: Gson = Gson()
    private var mediaPlayer: MediaPlayer? = null //미디어 플레이어
    lateinit var timer : SongActivity.Timer //타이머 변수 초기화

    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //앱이 로드되는 동안에는 splash화면이 나오지만 -> onCreate가 호출되면 >>
        setTheme(R.style.Theme_FloClone) // 원래 처음 실행 화면으로 돌려줌
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        songDB = SongDatabase.getInstance(this)!!

        initBottomNavigation()
        inputDummySongs()
        inputDummyAlbums()

        //이제 필요없음 -> sharedPreferences로 값을 가져오기 때문
        //val song = Song(binding.mainPlayTitleTv.text.toString(),binding.mainPlaySingerTv.text.toString(),0,60,false,"music_tomorow")

        binding.mainPlayBar.setOnClickListener {
            //어디로 갈지 설정
            //mainPlayBar를 눌렀을 때 SongActivity로 전환
            //startActivity(Intent(this,SongActivity::class.java))
            //Song인스턴스의 title과 singer을 넣기위한 방법 putExtrax
            //val intent = Intent(this,SongActivity::class.java)
            //intent.putExtra("title",song.title)
            //intent.putExtra("singer",song.singer)
            //intent.putExtra("second",song.second)
            //intent.putExtra("playTime",song.playTime)
            //intent.putExtra("isPlaying",song.isPlaying)
            //intent.putExtra("music",song.music)

            //이제 데이터를 넘길 때 인텐트 말고 song에 데이터를 저장
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId", song.id)
            editor.apply()

            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }

        Log.d("Song", song.title + song.singer)
    }

    private fun initBottomNavigation() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    private fun setMiniPlayer(song: Song) {
        val title = intent.getStringExtra("album_title") //[오늘 발매음악]에서 재생 버튼 클릭시 miniplayer에 제목, 가수명 변경
        val singer = intent.getStringExtra("album_singer")

        binding.mainPlayTitleTv.text = title ?: song.title
        binding.mainPlaySingerTv.text = singer ?: song.singer
        binding.mainMiniplayerProgressSb.progress = (song.second * 100000) / song.playTime

        binding.mainPrevMusicBtn.setOnClickListener {
            moveSong(-1)
        }
        binding.mainStartMusicBtn.setOnClickListener {
            setPlayerStatus(true)
        }
        binding.mainNextMusicBtn.setOnClickListener {
            moveSong(1)
        }
        // MediaPlayer 초기화 및 설정
        if (mediaPlayer == null) {
            val musicResId = resources.getIdentifier(song.music, "raw", packageName)
            mediaPlayer = MediaPlayer.create(this, musicResId)
            mediaPlayer?.setOnCompletionListener {
                moveSong(1) // 자동으로 다음 곡으로 이동
            }
        }

    }

    //재생 버튼 변경
    private fun setPlayerStatus(isPlaying :Boolean){
        if (songs.isEmpty()) { // 리스트가 비어 있는지 확인
            Toast.makeText(this, "No songs available", Toast.LENGTH_SHORT).show()
            return
        }

        songs[nowPos].isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying){
            binding.mainStartMusicBtn.visibility= View.GONE
            binding.mainPauseMusicBtn.visibility = View.VISIBLE
            mediaPlayer?.start()
        }
        else{
            binding.mainStartMusicBtn.visibility= View.VISIBLE
            binding.mainPauseMusicBtn.visibility = View.GONE
            if(mediaPlayer?.isPlaying == true){ //mediaPlayer에서는 재생중이 아닐 때 pause하면 오류 => if문으로
                mediaPlayer?.pause()
            }
        }
    }

    private fun moveSong(direct: Int) {
        val songs = songDB.songDao().getSongs()

        if (songs.isEmpty()) { // 리스트가 비어 있는지 확인
            Toast.makeText(this, "No songs available", Toast.LENGTH_SHORT).show()
            return
        }

        nowPos = songs.indexOfFirst { it.id == song.id }

        if (nowPos + direct < 0) {
            Toast.makeText(this, "first song", Toast.LENGTH_SHORT).show()
            return
        }
        if (nowPos + direct >= songs.size) {
            Toast.makeText(this, "last song", Toast.LENGTH_SHORT).show()
            return
        }

        nowPos += direct
        song = songs[nowPos]

        mediaPlayer?.release() //미디어 플레이어가 갖고있던 리소스 해제
        mediaPlayer = null //미디어 플레이어 해제

        setMiniPlayer(song)

        val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
        editor.putInt("songId", song.id)
        editor.apply()
    }

    private fun inputDummySongs() {
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()
        if (songs.isNotEmpty()) return

        songDB.songDao().insert(
            Song(
                "내일의 우리",
                "카더가든",
                0,
                230,
                false,
                "music_tomorow",
                R.drawable.img_album_exp3,
                false
            )
        )

        songDB.songDao().insert(
            Song(
                "Shake It Off",
                "Taylor Swift",
                0,
                240,
                false,
                "music_shakeitoff",
                R.drawable.img_album_exp,
                false
            )
        )

        songDB.songDao().insert(
            Song(
                "우리의 사랑은",
                "찰리빈웍스",
                0,
                230,
                false,
                "music_welove",
                R.drawable.img_album_exp4,
                false
            )
        )

        songDB.songDao().insert(
            Song(
                "Surf Boy",
                "혁오",
                0,
                230,
                false,
                "music_surfboy",
                R.drawable.img_album_exp6,
                false
            )
        )

        songDB.songDao().insert(
            Song(
                "Ling Ling",
                "검정치마",
                0,
                230,
                false,
                "music_lingling",
                R.drawable.img_album_exp5,
                false
            )
        )

        val _songs = songDB.songDao().getSongs()
        Log.d("DBDB", _songs.toString()) //데이터가 잘 들어왔는지 확인
    }

    private fun inputDummyAlbums() {
        val songDB = SongDatabase.getInstance(this)!!
        val albums = songDB.albumDao().getAlbums()
        if (albums.isNotEmpty()) return

        songDB.albumDao().insert(
            Album(
                0,
                "내일의 우리 앨범", "카더가든", R.drawable.img_album_exp3
            )
        )
        songDB.albumDao().insert(
            Album(
                1,
                "Shake It Off 앨범",
                "Taylor Swift",
                R.drawable.img_album_exp
            )
        )
        songDB.albumDao().insert(
            Album(
                2,
                "우리의 사랑은 앨범",
                "찰리빈웍스",
                R.drawable.img_album_exp4
            )
        )
        songDB.albumDao().insert(
            Album(
                3,
                "Surf Boy 앨범",
                "혁오",
                R.drawable.img_album_exp6
            )
        )
        songDB.albumDao().insert(
            Album(
                4,
                "Ling Ling앨범",
                "검정치마",
                R.drawable.img_album_exp6
            )
        )



        val _albums = songDB.songDao().getSongs()
        Log.d("DBDB", _albums.toString()) //데이터가 잘 들어왔는지 확인
    }

    override fun onStart() {
        super.onStart()
        initSong()
    }

    private fun initSong() {
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        song = if (songId == 0) {
            songDB.songDao().getSong(1)
        } else {
            songDB.songDao().getSong(songId) //아니라면 저장된 거 가져오기
        }

        Log.d("Song ID", song.id.toString())

        setMiniPlayer(song)
    }
}
