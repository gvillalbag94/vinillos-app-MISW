package com.example.vinillos_app_misw.presentation.ui.views.collector.collectorDetail

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vinillos_app_misw.R
import com.example.vinillos_app_misw.data.model.Comment




class CommentAdapter(private val commentList: List<Comment>, ) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        val descriptionComment: TextView = itemView.findViewById(R.id.description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.comment_card, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val collectorAlbum = commentList[position]

        holder.ratingBar.rating = collectorAlbum.rating.toFloat()
        holder.descriptionComment.text = collectorAlbum.description
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

}