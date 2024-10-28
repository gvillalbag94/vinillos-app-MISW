package com.example.vinillos_app_misw.presentation.ui.views.album.album_detail

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vinillos_app_misw.R
import com.example.vinillos_app_misw.data.adapters.AlbumAdapter
import com.example.vinillos_app_misw.data.adapters.UserAdapter
import com.example.vinillos_app_misw.data.model.Usuario
import com.example.vinillos_app_misw.data.repositories.AlbumRepository
import com.example.vinillos_app_misw.data.repositories.UserRepository
import com.example.vinillos_app_misw.databinding.ActivityAlbumDetailBinding
import com.example.vinillos_app_misw.presentation.view_model.album.AlbumViewModel
import com.example.vinillos_app_misw.presentation.view_model.album.AlbumViewModelFactory
import com.example.vinillos_app_misw.presentation.view_model.user.UserViewModel
import com.example.vinillos_app_misw.presentation.view_model.user.UserViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class AlbumDetailActivity(): AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var albumViewModel: AlbumViewModel
    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var user: Usuario
    private var albumID: Int = 0

    private lateinit var binding: ActivityAlbumDetailBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlbumDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }

    override fun onStart() {
        super.onStart()
        userViewModel = startUserViewModel()
        albumViewModel = startAlbumViewModel()
        loadUser()
        loadAlbumID()
        onInitNavBar()

    }

    private fun loadAlbumID() {
        albumViewModel.albumId.observe(
            this,
            Observer { currentAlbumID ->
                currentAlbumID?.let {
                    albumID = it

                    getAlbum()
                }
            })
        albumViewModel.getAlbumId()
    }

    private fun getAlbum() {
        albumViewModel.album.observe(
            this
        ) { currentAlbum ->
            currentAlbum?.let {
                binding.album = currentAlbum
                loadImageFromUrl(
                    binding.albumCoverImageView,
                    currentAlbum.cover,
                )

                binding.songsRecyclerView.layoutManager = LinearLayoutManager(this)
                val songAdapter = SongAdapter(currentAlbum.tracks, currentAlbum)
                binding.songsRecyclerView.adapter = songAdapter

                binding.artistsRecyclerView.layoutManager = LinearLayoutManager(this)
                val artistAdapter = ArtistAdapter(currentAlbum.performers)
                binding.artistsRecyclerView.adapter = artistAdapter


            }
        }

        albumViewModel.getAlbum(
            albumID
        )
    }


    private  fun startAlbumViewModel(): AlbumViewModel {
        val albumAdapter = AlbumAdapter(applicationContext)
        val repository = AlbumRepository(applicationContext, albumAdapter)
        val factory = AlbumViewModelFactory(repository)
        return ViewModelProvider(this, factory)[AlbumViewModel::class.java]
    }

    private fun startUserViewModel(): UserViewModel {
        val userAdapter = UserAdapter(applicationContext)
        val userRepository = UserRepository(userAdapter)
        val userFactory = UserViewModelFactory(application, userRepository)
        return ViewModelProvider(this, userFactory)[UserViewModel::class.java]
    }

    private fun loadUser() {
        userViewModel.user.observe(
            this,
            Observer { usuario ->
                usuario?.let {
                    user = it
                }
            })
        userViewModel.loadUser()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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