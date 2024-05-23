package com.example.floclone

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.floclone.databinding.ItemAlbumBinding
import java.util.ArrayList

class AlbumRVAdapter (private val albumList: ArrayList<Album>): RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>(){

    interface MyItemClickLitener{
        fun onItemClick(album: Album) //데이터도 같이
        fun onRemoveAlbum(position: Int)
    }

    private lateinit var  mItemClickListener: MyItemClickLitener
    fun setMyItemClickListener(itemClickLitener: MyItemClickLitener){
        mItemClickListener = itemClickLitener
    }

    fun addItem(album: Album){
        albumList.add(album)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int){
        albumList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AlbumRVAdapter.ViewHolder {
        //뷰 홀더 생성될 때 호출되는 함수
        val binding: ItemAlbumBinding = ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)

        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(albumList[position])
        //클릭 이벤트 처리
        //클릭 인터페이스 처리
        holder.itemView.setOnClickListener{ mItemClickListener.onItemClick(albumList[position])} // 아이템 클릭 시데이터 넘겨줌
        //holder.binding.itemAlbumPlayImgIv.setOnClickListener {mItemClickListener.onItemClick(albumList[position]) } // 앨범에 재생 버튼 클릭하면
        //holder.binding.itemAlbumTitleTv.setOnClickListener{mItemClickListener.onRemoveAlbum(position)}//클릭 시 리사이클러뷰 삭제 확인하는거
    }

    //마지막이 언젠지 알 수 있음
    override fun getItemCount(): Int = albumList.size

    inner class ViewHolder(val binding: ItemAlbumBinding): RecyclerView.ViewHolder(binding.root){
        init{
            sendToMiniPlayer()
        }
        //플레이 버튼 클릭하면 메인 엑티비티 제목, 가수명 전달(인텐트사용)[미니]
        private fun sendToMiniPlayer() {
            binding.itemAlbumPlayImgIv.setOnClickListener {
                val intent = Intent(binding.root.context, MainActivity::class.java).apply {
                    putExtra("album_title", albumList[adapterPosition].title)
                    putExtra("album_singer", albumList[adapterPosition].singer)
                    // 다른 필요한 정보들도 필요한 경우 추가
                }
                binding.root.context.startActivity(intent)
            }
        }

        fun bind(album: Album) {
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSingerTv.text = album.singer
            binding.itemAlbumCoverImgIv.setImageResource(album.coverImg!!)
        }

    }

}