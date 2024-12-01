package com.example.vinillos_app_misw.presentation.ui.views.collector.collectorDetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vinillos_app_misw.R
import com.example.vinillos_app_misw.data.adapters.AlbumAdapter
import com.example.vinillos_app_misw.data.adapters.CollectorAdapter
import com.example.vinillos_app_misw.data.database.VinilosRoomDatabase
import com.example.vinillos_app_misw.data.model.AlbumWithDetails
import com.example.vinillos_app_misw.data.repositories.AlbumRepository
import com.example.vinillos_app_misw.data.repositories.CollectorRepository
import com.example.vinillos_app_misw.databinding.ActivityCollectorDetailBinding
import com.example.vinillos_app_misw.presentation.view_model.album.AlbumViewModel
import com.example.vinillos_app_misw.presentation.view_model.album.AlbumViewModelFactory
import com.example.vinillos_app_misw.presentation.view_model.collector.CollectorViewModel
import com.example.vinillos_app_misw.presentation.view_model.collector.CollectorViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class CollectorDetailActivity : AppCompatActivity() {

    private lateinit var collectorViewModel: CollectorViewModel
    private lateinit var albumViewModel: AlbumViewModel
    private lateinit var bottomNavigationView: BottomNavigationView
    private var collectorID: Int = 0
    private lateinit var binding: ActivityCollectorDetailBinding
    private var albums = emptyList<AlbumWithDetails>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCollectorDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        collectorViewModel = startCollectorViewModel()
        albumViewModel = startAlbumViewModel()
        getAlbums()
        loadCollectorID()
        onInitNavBar()

    }

    private  fun startAlbumViewModel(): AlbumViewModel {
        val database = VinilosRoomDatabase.getDatabase(applicationContext)
        val albumAdapter = AlbumAdapter(applicationContext)
        val albumDao = database.albumDao()
        val repository = AlbumRepository(applicationContext, albumAdapter,albumDao)
        val factory = AlbumViewModelFactory(repository)
        return ViewModelProvider(this, factory)[AlbumViewModel::class.java]
    }

    private fun startCollectorViewModel(): CollectorViewModel {
        val database = VinilosRoomDatabase.getDatabase(applicationContext)
        val collectorAdapter = CollectorAdapter(applicationContext)
        val repository = CollectorRepository(
            applicationContext,
            collectorAdapter,
            database.collectorDao(),
            )
        val factory = CollectorViewModelFactory(repository)
        return ViewModelProvider(this, factory)[CollectorViewModel::class.java]
    }

    private fun loadCollectorID() {
        collectorViewModel.collectorId.observe(
            this,
            Observer { collectorId ->
                collectorId?.let {
                    collectorID = it

                    getCollector()
                }
            })
        collectorViewModel.getCollectorId()
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

    private fun getAlbums() {
        albumViewModel.albums.observe(this) { albums ->

            this.albums = albums

        }

        albumViewModel.getAlbums()
    }

    private fun getCollector() {
        collectorViewModel.collector.observe(
            this
        ) { currentCollector ->
            currentCollector?.let {
                binding.collector = currentCollector

                binding.albumesRecyclerView.layoutManager = LinearLayoutManager(this)
                val albumAdapter = CollectorAlbumAdapter(currentCollector.collectorAlbums, this.albums)
                binding.albumesRecyclerView.adapter = albumAdapter



                 binding.commentsRecyclerView.layoutManager = LinearLayoutManager(this)
                 val commentAdapter = CommentAdapter(currentCollector.comments)
                 binding.commentsRecyclerView.adapter = commentAdapter

                binding.favoritePerformersRecyclerView.layoutManager = LinearLayoutManager(this)
                val performerAdapter = FavoritesPerformerAdapter(currentCollector.favoritePerformers)
                binding.favoritePerformersRecyclerView.adapter = performerAdapter


            }
        }
        collectorViewModel.getCollector(
            collectorID
        )
    }

}