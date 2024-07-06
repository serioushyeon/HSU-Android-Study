package com.example.floClone.core.ui.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.floClone.databinding.ItemLockerAlbumBinding
import com.example.floClone.core.data.model.local.entities.SongEntity

class LookRVAdapter : RecyclerView.Adapter<LookRVAdapter.ViewHolder>() {

    private var songs = ArrayList<SongEntity>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemLockerAlbumBinding = ItemLockerAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
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
