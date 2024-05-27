package com.serioushyeon.floclone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HomeTodayAlbumListAdapter(private val items: ArrayList<Song>) : RecyclerView.Adapter<HomeTodayAlbumListAdapter.HomeTodayAlbumListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeTodayAlbumListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.album_rv_item, parent, false)
        return HomeTodayAlbumListViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeTodayAlbumListViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { mItemClickListener.onItemClick(items[position]) }
        holder.playBtn.setOnClickListener { mItemClickListener.onPlayButtonClick(items[position]) }
    }

    override fun getItemCount(): Int {
        return items.size
    }
    fun addItem(album: Song){
        items.add(album)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int){
        items.removeAt(position)
        notifyDataSetChanged()
    }

    inner class HomeTodayAlbumListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.item_today_music_title1_tv)
        private val singer: TextView = itemView.findViewById(R.id.item_today_music_singer1_tv)
        private val albumImg: ImageView = itemView.findViewById(R.id.item_today_music_img1_iv)
        val playBtn : ImageView = itemView.findViewById(R.id.item_play_02_iv)
        fun bind(item: Song) {
            title.text = item.title
            singer.text = item.singer
            albumImg.setImageResource(item.img)
        }
    }
    // 클릭 인터페이스 정의
    interface MyItemClickListener{
        fun onItemClick(album: Song)
        fun onRemoveAlbum(position: Int)
        fun onPlayButtonClick(album: Song)
    }

    // 리스너 객체를 전달받는 함수랑 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }


}