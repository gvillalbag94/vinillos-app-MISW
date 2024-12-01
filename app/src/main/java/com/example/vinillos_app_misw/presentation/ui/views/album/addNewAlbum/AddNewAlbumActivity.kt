package com.example.vinillos_app_misw.presentation.ui.views.album.addNewAlbum

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.vinillos_app_misw.R
import com.example.vinillos_app_misw.data.adapters.AlbumAdapter
import com.example.vinillos_app_misw.data.database.VinilosRoomDatabase
import com.example.vinillos_app_misw.data.model.Album
import com.example.vinillos_app_misw.data.repositories.AlbumRepository
import com.example.vinillos_app_misw.presentation.view_model.album.AlbumViewModel
import com.example.vinillos_app_misw.presentation.view_model.album.AlbumViewModelFactory
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddNewAlbumActivity : AppCompatActivity() {

    private lateinit var albumViewModel: AlbumViewModel

    private lateinit var  addAlbumButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_album)
        val albumName: EditText = findViewById(R.id.albumName)
        val albumCover: EditText = findViewById(R.id.albumCover)
        val releaseDate: EditText = findViewById(R.id.releaseDate)
        val description: EditText = findViewById(R.id.description)
        val genreSpinner: Spinner = findViewById(R.id.genreSpinner)
        val recordLabel: Spinner = findViewById(R.id.recordSpinner)
        addAlbumButton = findViewById(R.id.addAlbumButton)
        val backButton: ImageButton = findViewById(R.id.backIcon)

        // Spinner setup
        val genres = arrayOf("Salsa", "Classical", "Rock", "Folk")
        val adapter = ArrayAdapter(this, R.layout.custom_spinner_text, genres)
        genreSpinner.adapter = adapter

        val recordLabels = arrayOf("Sony Music", "EMI", "Discos Fuentes", "Elektra", "Fania Records")
        val adapterRecordLabel = ArrayAdapter(this, R.layout.custom_spinner_text, recordLabels)
        recordLabel.adapter = adapterRecordLabel


        // Date Picker dialog
        releaseDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(this, { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                releaseDate.setText(dateFormat.format(selectedDate.time))
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        }

        addAlbumButton.setOnClickListener {
            addAlbumButton.isEnabled = false
            if (validateFields(
                albumName,
                albumCover,
                releaseDate,
                description,
            )) {
                val releaseDateValue = convertDateToIso(releaseDate.text.trim().toString())

                val album = Album(
                    id = 0,
                    name = albumName.text.toString(),
                    cover = albumCover.text.toString(),
                    releaseDate = releaseDateValue,
                    description = description.text.toString(),
                    genre = genreSpinner.selectedItem.toString(),
                    recordLabel = recordLabel.selectedItem.toString(),
                )

                albumViewModel.addAlbum(album)

            } else {
                addAlbumButton.isEnabled = true
            }
        }

        backButton.setOnClickListener {
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        albumViewModel = startAlbumViewModel()
        observerAddAlbum()
    }

    private  fun startAlbumViewModel(): AlbumViewModel {
        val database = VinilosRoomDatabase.getDatabase(applicationContext)
        val albumAdapter = AlbumAdapter(applicationContext)
        val albumDao = database.albumDao()
        val repository = AlbumRepository(applicationContext, albumAdapter,albumDao)
        val factory = AlbumViewModelFactory(repository)
        return ViewModelProvider(this, factory)[AlbumViewModel::class.java]
    }


    private fun validateFields(
        albumName: EditText,
        albumCover: EditText,
        releaseDate: EditText,
        description: EditText,
    ) : Boolean {
        val firstValue = albumName.text.toString().trim()
        val secondValue = albumCover.text.toString().trim()
        val thirdValue = releaseDate.text.toString().trim()
        val fourthValue = description.text.toString().trim()

        when {
            firstValue.isEmpty() -> {
                albumName.error = "El campo no debe estar vacio"
                return false
            }
            secondValue.isEmpty() -> {
                albumCover.error = "El campo no debe estar vacio"
                return false
            }
            thirdValue.isEmpty() -> {
                releaseDate.error = "El campo no debe estar vacio"
                return false
            }
            fourthValue.isEmpty() -> {
                description.error = "El campo no debe estar vacio"
                return false
            }


            else -> {
                return true
            }
        }
    }

    private fun convertDateToIso(dateString: String): String {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date: Date = inputFormat.parse(dateString) ?: throw IllegalArgumentException("Invalid date format")
        val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        return outputFormat.format(date)
    }

    private fun observerAddAlbum() {
        albumViewModel.addAlbum.observe(
            this,
            Observer { addAlbum ->
                if (addAlbum) {
                    albumViewModel.fetchAlbums()
                    albumViewModel.saveUpdateValue(addAlbum)
                    finish()
                } else {
                    showSnackbarFail("El álbum no se creo correctamente, vuelve a intentarlo.")
                    addAlbumButton.isEnabled = true
                }
            })

    }

    private fun showSnackbarFail(message: String) {
        val rootLayout = window.decorView.findViewById<View>(android.R.id.content)
        Snackbar.make(rootLayout, message, Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(applicationContext, R.color.error))
            .setTextColor(Color.WHITE)
            .show()
    }

}