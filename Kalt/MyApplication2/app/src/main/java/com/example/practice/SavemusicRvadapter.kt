package com.example.practice

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.practice.databinding.ItemSavemusicalbumBinding

class SavemusicRvadapter()
    : RecyclerView.Adapter<SavemusicRvadapter.ViewHolder>() {
    private val songs = ArrayList<Song>()
    interface MyItemClickListener {
        fun onRemoveAlbum(songId: Int)
    }

    private var myItemClickListener: MyItemClickListener? = null

    fun setMyItemClickListener(listener: MyItemClickListener) {
        myItemClickListener = listener
    }

    /*fun addItem(album: Album) {
        songs.add(album)
        notifyDataSetChanged()
    }*/

    fun removeItem(position: Int) {
        songs.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemSavemusicalbumBinding = ItemSavemusicalbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(songs[position])
        holder.binding.savemusicMore.setOnClickListener {
            myItemClickListener?.onRemoveAlbum(songs[position].id)
            removeSong(position)
        }
    }

    override fun getItemCount(): Int = songs.size
    @SuppressLint("NotifyDataSetChanged")
    fun addSongs(songs: ArrayList<Song>) {
        this.songs.clear()
        this.songs.addAll(songs)

        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun removeSong(position: Int){
        songs.removeAt(position)
        notifyDataSetChanged()
    }
    inner class ViewHolder(val binding: ItemSavemusicalbumBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(song: Song) {
            binding.itemAlbumTitleTv.text = song.title
            binding.itemAlbumSingerTv.text = song.singer
            binding.itemAlbumCoverImgIv.setImageResource(song.coverImg!!)
        }
    }
}
