package com.serioushyeon.floclone.ui.main.album

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.serioushyeon.floclone.data.remote.AlbumChartResult
import com.serioushyeon.floclone.data.remote.AlbumChartSongs
import com.serioushyeon.floclone.databinding.AlbumRvItemBinding

class AlbumRVAdapter(val context: Context, val result : AlbumChartResult) : RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>(){

    // 클릭 인터페이스 정의
    interface MyItemClickListener{
        fun onItemClick(AlbumChartSongs: AlbumChartSongs)
        fun onRemoveAlbum(position: Int)
    }

    // 리스너 객체를 전달받는 함수랑 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    // 뷰홀더를 생성해줘야 할 때 호출되는 함수 => 아이템 뷰 객체를 만들어서 뷰홀더에 던져줍니다.
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: AlbumRvItemBinding = AlbumRvItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    //    fun addItem(album: Album){
//        albumList.add(album)
//        notifyDataSetChanged()
//    }
//
    fun removeItem(position: Int){
    }

    // 뷰홀더에 데이터를 바인딩해줘야 할 때마다 호출되는 함수 => 엄청나게 많이 호출
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(albumList[position])
//        holder.itemView.setOnClickListener { mItemClickListener.onItemClick(albumList[position]) }
////        holder.binding.itemAlbumTitleTv.setOnClickListener { mItemClickListener.onRemoveAlbum(position) } //삭제됐을 때


        if(result.albums[position].coverImgUrl == "" || result.albums[position].coverImgUrl == null){

        } else {
            Log.d("image",result.albums[position].coverImgUrl )
            Glide.with(context).load(result.albums[position].coverImgUrl).into(holder.coverImg)
        }
        holder.title.text = result.albums[position].title
        holder.singer.text = result.albums[position].singer

        holder.itemView.setOnClickListener {
            mItemClickListener.onItemClick(result.albums[position])
        }

    }

    // 데이터 세트 크기를 알려주는 함수 => 리사이클러뷰가 마지막이 언제인지를 알게 된다.
    override fun getItemCount(): Int = result.albums.size

    // 뷰홀더
    inner class ViewHolder(val binding: AlbumRvItemBinding): RecyclerView.ViewHolder(binding.root){

        val title : TextView =  binding.itemTodayMusicTitle1Tv
        val singer : TextView = binding.itemTodayMusicSinger1Tv
        val coverImg : ImageView = binding.itemTodayMusicImg1Iv

    }
}