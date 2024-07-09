package com.example.practice.ui.song

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.practice.data.entity.Track
import com.example.practice.databinding.ItemSongBinding

class SingerRVAdapter(val context: Context, private val result : List<Track>) :
    RecyclerView.Adapter<SingerRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemSongBinding = ItemSongBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //holder.bind(result.songs[position])

        if(result[position].artworkUrl100 == "" || result[position].artworkUrl100 == null){

        } else {
            Log.d("image",result[position].artworkUrl100 )
            Glide.with(context).load(result[position].artworkUrl100).into(holder.binding.itemSongImgIv)
        }
        holder.binding.itemSongTitleTv.text = result[position].collectionName
        holder.binding.itemSongSingerTv.text = result[position].artistName

    }

    override fun getItemCount(): Int = result.size


    inner class ViewHolder(val binding: ItemSongBinding) : RecyclerView.ViewHolder(binding.root){

        val coverImg : ImageView = binding.itemSongImgIv
        val title : TextView = binding.itemSongTitleTv
        val singer : TextView = binding.itemSongSingerTv

//        fun bind(song: FloChartSongs){
//            if(song.coverImgUrl == "" || song.coverImgUrl == null) {
//            } else {
//                Glide.with(context).load(song.coverImgUrl).into(binding.itemSongImgIv)
//            }
//
//            binding.itemSongTitleTv.text = song.title
//            binding.itemSongSingerTv.text = song.singer
//        }
    }

    interface MyItemClickListener{
        fun onRemoveSong(songId: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }
}