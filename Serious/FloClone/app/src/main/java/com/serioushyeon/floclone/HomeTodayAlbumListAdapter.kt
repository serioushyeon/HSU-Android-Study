package com.serioushyeon.floclone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HomeTodayAlbumListAdapter(private val items: List<Song>, private val listener: OnItemClickListener) : RecyclerView.Adapter<HomeTodayAlbumListAdapter.HomeTodayAlbumListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeTodayAlbumListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.album_rv_item, parent, false)
        return HomeTodayAlbumListViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeTodayAlbumListViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            listener.onItemClick(item)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class HomeTodayAlbumListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.item_today_music_title1_tv)
        private val singer: TextView = itemView.findViewById(R.id.item_today_music_singer1_tv)
        private val albumImg: ImageView = itemView.findViewById(R.id.item_today_music_img1_iv)

        fun bind(item: Song) {
            title.text = item.title
            singer.text = item.singer
            albumImg.setImageResource(item.img)
        }
    }
    interface OnItemClickListener {
        fun onItemClick(item: Song)
    }
}