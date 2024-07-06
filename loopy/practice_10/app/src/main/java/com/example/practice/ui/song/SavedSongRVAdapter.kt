package com.example.practice.ui.song

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.practice.data.entity.Song
import com.example.practice.databinding.ItemLockerBinding

class SavedSongRVAdapter(private var albumList: ArrayList<Song>) :
    RecyclerView.Adapter<SavedSongRVAdapter.ViewHolder>() {

    interface MyItemClickListener {
        fun onItemClick(item : Song)
        fun onRemoveAlbum(position: Int)
        fun onToggle(position: Int)
        fun onRemoveSong(position: Int)
    }

    private lateinit var mItemClickListener : MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }



    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: ItemLockerBinding =
            ItemLockerBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(albumList[position])
//        holder.itemView.setOnClickListener {
//            mItemClickListener.onItemClick(albumList[position])
//        }
        holder.binding.lockerMenu.setOnClickListener { removeSong(position)
        mItemClickListener.onRemoveSong(albumList[position].id)
        }

    }

    override fun getItemCount() = albumList.size

    @SuppressLint("NotifyDataSetChanged")
    fun addSongs(songs : ArrayList<Song>){
        this.albumList.clear()
        this.albumList.addAll(songs)
        notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun removeSong(position: Int){
        this.albumList.removeAt(position)
        notifyDataSetChanged()
    }




    inner class ViewHolder(val binding: ItemLockerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Song) {
            binding.ivLocker.setImageResource(album.coverImg!!)
            binding.tvLockerSinger.text = album.singer.toString()
            binding.tvLockerTitle.text = album.title.toString()

        }
    }

}