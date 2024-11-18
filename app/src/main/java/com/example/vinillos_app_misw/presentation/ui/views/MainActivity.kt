package com.example.vinillos_app_misw.presentation.ui.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGImageView
import com.example.vinillos_app_misw.R
import com.example.vinillos_app_misw.data.adapters.UserAdapter
import com.example.vinillos_app_misw.data.database.VinilosRoomDatabase
import com.example.vinillos_app_misw.data.model.TipoUsuario
import com.example.vinillos_app_misw.data.model.Usuario
import com.example.vinillos_app_misw.data.repositories.UserRepository
import com.example.vinillos_app_misw.presentation.ui.views.home.HomeActivity
import com.example.vinillos_app_misw.presentation.view_model.user.UserViewModel
import com.example.vinillos_app_misw.presentation.view_model.user.UserViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       userViewModel = startUserViewModel()

        // Cargar Toolbar superior
        onInitToolbar()

        // Cargar y asignar avatar para usuarios y collecionistas.
        onInitSvgButton(
            R.id.user_avatar,
            R.raw.avatar_icon,
            )
        onInitSvgButton(
            R.id.collection_avatar,
            R.raw.avatar_icon,
        )


        val userCardButton = findViewById<CardView>(R.id.user_card_button)
        userCardButton.setOnClickListener {
            userViewModel.saveUser(Usuario(TipoUsuario.USUARIO))
            goToHome()
        }

        val collectorCardButton = findViewById<CardView>(R.id.collector_card_button)
        collectorCardButton.setOnClickListener {
            userViewModel.saveUser(Usuario(TipoUsuario.COLECCIONISTA))
            goToHome()
        }
    }

    private fun startUserViewModel(): UserViewModel {
        val userAdapter = UserAdapter(applicationContext)
        val userRepository = UserRepository(userAdapter)
        val userFactory = UserViewModelFactory(application, userRepository)
        return ViewModelProvider(this, userFactory)[UserViewModel::class.java]
    }

    private fun goToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun onInitToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private  fun onInitSvgButton(componentID: Int, icon: Int ) {
        val avatarId = findViewById<SVGImageView>(componentID)
        val svgAvatar = SVG.getFromResource(resources, icon)
        avatarId.setSVG(svgAvatar)
    }

}