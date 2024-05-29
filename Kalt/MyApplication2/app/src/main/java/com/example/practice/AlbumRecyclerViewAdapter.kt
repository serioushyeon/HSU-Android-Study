
package com.example.practice

import android.content.Context
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.practice.databinding.ItemAlbumBinding

class AlbumRecyclerViewAdapter(private val albumList: ArrayList<Album>, listener:MyItemClickListener,private val context: Context) :
    RecyclerView.Adapter<AlbumRecyclerViewAdapter.ViewHolder>() {
    interface MyItemClickListener {
        fun onitemClick(album: Album)
        fun onRemoveAlbum(position: Int)
        fun onPlaySong(song: Song, position: Int)
    }

    private lateinit var mltemClickListener: MyItemClickListener
    fun setmyitemclickListener(itemClickListener: MyItemClickListener) {
        mltemClickListener = itemClickListener
    }

    fun addItem(album: Album) {
        albumList.add(album)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        albumList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = albumList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemAlbumBinding =
            ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(albumList[position])
    }

    inner class ViewHolder(val binding: ItemAlbumBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        private var mediaPlayer: MediaPlayer? = null

        fun bind(album: Album) {
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSingerTv.text = album.singer
            binding.itemAlbumCoverImgIv.setImageResource(album.coverimg!!)

            binding.itemAlbumPlayImgIv.setOnClickListener {
                /*album.songs?.get(0)?.let { song ->
                    mediaPlayer?.release()  // Release any existing media player instance
                    mediaPlayer = MediaPlayer.create(context, R.raw.lilac)
                    mediaPlayer?.start()
                }*/
            }
        }
    }
}