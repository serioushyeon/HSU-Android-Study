package com.serioushyeon.floclone

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.annotation.SuppressLint
import com.serioushyeon.floclone.databinding.ItemSavedsongBinding

class SavedSongRVAdapter() :
    RecyclerView.Adapter<SavedSongRVAdapter.ViewHolder>() {
    private val songs = ArrayList<Song>()
    interface MyItemClickListener{
        fun onRemoveSong(songId: Int)
    }
    private lateinit var mItemClickListener : MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SavedSongRVAdapter.ViewHolder {
        val binding: ItemSavedsongBinding = ItemSavedsongBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedSongRVAdapter.ViewHolder, position: Int) {
        holder.bind(songs[position])
        holder.binding.saveSongMore.setOnClickListener {
            removeSong(position)
            mItemClickListener.onRemoveSong(songs[position].id)
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

    inner class ViewHolder(val binding: ItemSavedsongBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(song: Song){
            binding.saveSongImg.setImageResource(song.img!!)
            binding.saveSongTitle.text = song.title
            binding.saveSongSinger.text = song.singer
        }
    }
}