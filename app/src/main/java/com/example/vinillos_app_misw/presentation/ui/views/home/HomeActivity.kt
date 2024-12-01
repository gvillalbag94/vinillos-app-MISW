package com.example.vinillos_app_misw.presentation.ui.views.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.vinillos_app_misw.R
import com.example.vinillos_app_misw.data.adapters.UserAdapter
import com.example.vinillos_app_misw.data.model.TipoUsuario
import com.example.vinillos_app_misw.data.model.Usuario
import com.example.vinillos_app_misw.data.repositories.UserRepository
import com.example.vinillos_app_misw.presentation.ui.views.album.addNewAlbum.AddNewAlbumActivity
import com.example.vinillos_app_misw.presentation.ui.views.album.albumList.AlbumListFragment
import com.example.vinillos_app_misw.presentation.ui.views.artist.artistList.ArtistListFragment
import com.example.vinillos_app_misw.presentation.ui.views.collector.collectorDetail.CollectorDetailActivity
import com.example.vinillos_app_misw.presentation.ui.views.collector.collectorList.CollectorListFragment
import com.example.vinillos_app_misw.presentation.view_model.user.UserViewModel
import com.example.vinillos_app_misw.presentation.view_model.user.UserViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout

class HomeActivity : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var user: Usuario
    private lateinit var tabLayout: TabLayout
    private lateinit var bottomNavigationView: BottomNavigationView
    private val albumListFragment = AlbumListFragment()
    private val artistListFragment = ArtistListFragment()
    private val collectorListFragment = CollectorListFragment()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        userViewModel = startUserViewModel()

        onInitToolbar()

        loadUser()

        tabLayout = findViewById(R.id.tab_layout)

        setupTabs()

        loadFragment(albumListFragment)
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
                    Toast.makeText(
                        this,
                        "Tipo de Usuario: ${it.tipoUsuario}",
                        Toast.LENGTH_SHORT,
                    ).show()
                    onInitNavBar()
                }
            })

        userViewModel.loadUser()
    }

    private fun onInitToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

    }

    private fun onInitNavBar() {
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.menu.clear()


        if(user.tipoUsuario == TipoUsuario.USUARIO) {
            menuInflater.inflate(R.menu.user_menu, bottomNavigationView.menu)
        } else{
            menuInflater.inflate(R.menu.collector_menu, bottomNavigationView.menu)
        }

        bottomNavigationView.menu.setGroupCheckable(0, true, false)
        for (i in 0 until bottomNavigationView.menu.size()) {
            bottomNavigationView.menu.getItem(i).isChecked = false
        }
        bottomNavigationView.menu.setGroupCheckable(0, true, true)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_nav -> {
                    finish()
                }
                R.id.add_nav -> {
                    val intent = Intent(
                        applicationContext,
                        AddNewAlbumActivity::class.java,
                        )
                    startActivity(intent)
                }
            }
            false
        }
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

    private fun setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("Albumes"))
        tabLayout.addTab(tabLayout.newTab().setText("Artistas"))
        tabLayout.addTab(tabLayout.newTab().setText("Collecionistas"))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> loadFragment(albumListFragment)
                    1 -> loadFragment(artistListFragment)
                    2 -> loadFragment(collectorListFragment)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    internal fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onDestroy() {
        userViewModel.clearUser()
        super.onDestroy()
    }

}