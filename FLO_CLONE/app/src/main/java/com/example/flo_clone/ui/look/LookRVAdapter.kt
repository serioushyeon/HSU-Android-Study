package com.example.flo_clone.ui.look

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo_clone.databinding.ItemLockerAlbumBinding
import com.example.flo_clone.room.entity.AlbumEntity
import com.example.flo_clone.room.entity.SongEntity

class LookRVAdapter : RecyclerView.Adapter<LookRVAdapter.ViewHolder>() {

    private var songs = ArrayList<SongEntity>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): LookRVAdapter.ViewHolder {
        val binding: ItemLockerAlbumBinding = ItemLockerAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LookRVAdapter.ViewHolder, position: Int) {
        val song = songs[position]
        holder.bind(song)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    fun addSongs(songs: ArrayList<SongEntity>) {
        this.songs.clear()
        this.songs.addAll(songs)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding:ItemLockerAlbumBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(song: SongEntity) {
            song.coverImg?.let { binding.itemAlbumImgIv.setImageResource(it) }
            binding.itemAlbumTitleTv.text = song.title
            binding.itemAlbumSingerTv.text = song.singer
        }
    }
}
