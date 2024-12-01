package com.example.vinillos_app_misw.presentation.ui.views.collector.collectorDetail

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vinillos_app_misw.R
import com.example.vinillos_app_misw.data.model.AlbumWithDetails
import com.example.vinillos_app_misw.data.model.CollectorAlbum
import com.example.vinillos_app_misw.data.model.Performer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class FavoritesPerformerAdapter(private val artists: List<Performer>) : RecyclerView.Adapter<FavoritesPerformerAdapter.FavoritesPerformerViewHolder>() {

    class FavoritesPerformerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val albumTitleTextView: TextView = itemView.findViewById(R.id.albumTitle)
        val albumGenreTextView: TextView = itemView.findViewById(R.id.albumGenre)
        val albumImageView: ImageView = itemView.findViewById(R.id.albumImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesPerformerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.artist_card, parent, false)
        return FavoritesPerformerViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoritesPerformerViewHolder, position: Int) {
        val collectorAlbum = artists[position]

        holder.albumTitleTextView.text = collectorAlbum.name
        holder.albumGenreTextView.text = collectorAlbum.name
        loadImageFromUrl(holder.albumImageView,collectorAlbum.image)
    }

    override fun getItemCount(): Int {
        return artists.size
    }

    fun loadImageFromUrl(imageView: ImageView, url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input: InputStream = connection.inputStream
                val bitmap = BitmapFactory.decodeStream(input)
                withContext(Dispatchers.Main) {
                    imageView.setImageBitmap(bitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Opcional: establece una imagen de error en caso de fallo

            }
        }
    }
}