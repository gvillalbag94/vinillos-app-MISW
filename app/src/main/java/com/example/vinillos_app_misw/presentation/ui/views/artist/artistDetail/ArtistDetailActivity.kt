package com.example.vinillos_app_misw.presentation.ui.views.artist.artistDetail

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vinillos_app_misw.R
import com.example.vinillos_app_misw.data.adapters.ArtistAdapter
import com.example.vinillos_app_misw.data.database.VinilosRoomDatabase
import com.example.vinillos_app_misw.data.repositories.ArtistRepository
import com.example.vinillos_app_misw.databinding.ActivityArtistDetailBinding
import com.example.vinillos_app_misw.presentation.ui.views.album.albumDetail.SongAdapter
import com.example.vinillos_app_misw.presentation.view_model.artist.ArtistViewModel
import com.example.vinillos_app_misw.presentation.view_model.artist.ArtistViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class ArtistDetailActivity : AppCompatActivity() {

    private lateinit var artistViewModel: ArtistViewModel
    private lateinit var bottomNavigationView: BottomNavigationView
    private var artistID: Int = 0
    private lateinit var binding: ActivityArtistDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtistDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        artistViewModel = startArtistViewModel()
        loadArtistID()
        onInitNavBar()

    }

    private fun startArtistViewModel(): ArtistViewModel {
        val database = VinilosRoomDatabase.getDatabase(applicationContext)
        val artistAdapter = ArtistAdapter(applicationContext)
        val repository = ArtistRepository(applicationContext, artistAdapter)
        val factory = ArtistViewModelFactory(repository)
        return ViewModelProvider(this, factory)[ArtistViewModel::class.java]
    }

    private fun loadArtistID() {
        artistViewModel.artistId.observe(
            this,
            Observer { artistId ->
                artistId?.let {
                    artistID = it

                    getArtist()
                }
            })
        artistViewModel.getArtistId()
    }

    private fun onInitNavBar() {
        bottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.menu.setGroupCheckable(0, true, false)
        for (i in 0 until bottomNavigationView.menu.size()) {
            bottomNavigationView.menu.getItem(i).isChecked = false
        }
        bottomNavigationView.menu.setGroupCheckable(0, true, true)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.back_nav -> {
                    finish()
                }
            }
            false
        }
    }

    private fun getArtist() {
        artistViewModel.artist.observe(
            this
        ) { currentArtist ->
            currentArtist?.let {
                binding.artist = currentArtist

                loadImageFromUrl(
                    binding.artistImageView,
                    currentArtist.image,
                )

                // binding.songsRecyclerView.layoutManager = LinearLayoutManager(this)
                // val songAdapter = SongAdapter(currentAlbum.tracks, currentAlbum)
                // binding.songsRecyclerView.adapter = songAdapter

                // binding.artistsRecyclerView.layoutManager = LinearLayoutManager(this)
                //val artistAdapter =
                 //   com.example.vinillos_app_misw.presentation.ui.views.album.albumDetail.ArtistAdapter(
                 //       currentAlbum.performers
                 //   )

                //binding.artistsRecyclerView.adapter = artistAdapter


            }
        }

        artistViewModel.getArtist(
            artistID
        )
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