package com.example.vinillos_app_misw.presentation.ui.views.album.addNewTrack

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.vinillos_app_misw.R
import com.example.vinillos_app_misw.data.adapters.AlbumAdapter
import com.example.vinillos_app_misw.data.database.VinilosRoomDatabase
import com.example.vinillos_app_misw.data.repositories.AlbumRepository
import com.example.vinillos_app_misw.presentation.view_model.album.AlbumViewModel
import com.example.vinillos_app_misw.presentation.view_model.album.AlbumViewModelFactory
import com.google.android.material.snackbar.Snackbar

class AddNewTrackActivity : AppCompatActivity() {
    val regex = Regex("^(0[0-5][0-9]|[0-9][0-9]):([0-5][0-9])?$")

    private lateinit var albumViewModel: AlbumViewModel

    private lateinit var  addAlbumButton: Button

    private var albumID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_track)
        val backButton: ImageButton = findViewById(R.id.backIcon)
        val durationEditText: EditText = findViewById(R.id.trackDuration)
        val nameEditText: EditText = findViewById(R.id.trackName)
        addAlbumButton = findViewById(R.id.addAlbumButton)
        var durationTextSnapshot: String = "";


        durationEditText.addTextChangedListener(object : TextWatcher {
            private var isEditing = false


            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (isEditing) return

                    isEditing = true

                    val currentText = durationEditText.text.toString()

                    if (currentText.length == 1) {
                        val cleanedInput = currentText.last()
                        val numValidator: Int = cleanedInput.toString().toInt()

                        if (numValidator > 5) {
                            val newText = ""
                            durationEditText.setText(newText)
                            durationEditText.setSelection(newText
                                .length)
                            durationTextSnapshot = newText
                            isEditing = false
                            return
                        }
                    } else if (currentText.length == 4) {
                        val cleanedInput = currentText.last()
                        val numValidator: Int = cleanedInput.toString().toInt()

                        if (numValidator > 5) {
                            val newText = currentText.substring(0, currentText.length - 1)
                            durationEditText.setText(newText)
                            durationEditText.setSelection(newText
                                .length)
                            durationTextSnapshot = newText
                            isEditing = false
                            return
                        }
                    }

                    // Remove hte last character.
                    if (s == null) {
                        if (currentText.isEmpty()) {
                            isEditing = false
                            return
                        }
                    }
                    else if (currentText.length > durationTextSnapshot.length) {
                        if ( currentText.length == 2) {
                            val formattedInput = "$currentText:"
                            durationEditText.setText(formattedInput)
                            durationEditText.setSelection(formattedInput.length)
                            durationTextSnapshot = formattedInput
                            isEditing = false
                            return
                        }
                        durationTextSnapshot = currentText
                        isEditing = false
                        return
                    }
                    else {
                        val remove: Int = if (durationTextSnapshot.last() == ':') 2 else 1

                        if (durationTextSnapshot.isEmpty()) {
                            isEditing = false
                            return
                        }

                        val newText = durationTextSnapshot.substring(0, durationTextSnapshot.length - remove)
                        durationEditText.setText(newText)
                        durationEditText.setSelection(newText
                            .length)
                        durationTextSnapshot = newText
                        isEditing = false
                        return
                    }

                }
        })




        addAlbumButton.setOnClickListener {

            addAlbumButton.isEnabled = false

            if (validateFields(
                durationEditText,
                nameEditText,
            )) {
                albumViewModel.addTrack(
                    albumID,
                    nameEditText.text.toString(),
                    durationEditText.text.toString(),
                )
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
        loadAlbumID()
        observerAddAlbum()
    }

    private fun observerAddAlbum() {
        albumViewModel.addTrack.observe(
            this,
            Observer { addAlbum ->
                if (addAlbum) {
                    finish()
                } else {
                    showSnackbarFail("El track no se creo correctamente, vuelve a intentarlo.")
                    addAlbumButton.isEnabled = true
                }
            })

    }

    private  fun startAlbumViewModel(): AlbumViewModel {
        val database = VinilosRoomDatabase.getDatabase(applicationContext)
        val albumAdapter = AlbumAdapter(applicationContext)
        val albumDao = database.albumDao()
        val repository = AlbumRepository(applicationContext, albumAdapter,albumDao)
        val factory = AlbumViewModelFactory(repository)
        return ViewModelProvider(this, factory)[AlbumViewModel::class.java]
    }

    private fun loadAlbumID() {
        albumViewModel.albumId.observe(
            this,
            Observer { currentAlbumID ->
                currentAlbumID?.let {
                    albumID = it

                }
            })
        albumViewModel.getAlbumId()
    }

    private fun validateFields(
        durationEditText: EditText,
        nameEditText: EditText
    ) : Boolean {
        val firstValue = nameEditText.text.toString().trim()
        val secondValue = durationEditText.text.toString().trim()

        when {
            firstValue.isEmpty() -> {
                nameEditText.error = "El campo no debe estar vacio"
                return false
            }
            secondValue.isEmpty() -> {

                durationEditText.error = "El campo no debe estar vacio"
                return false
            }
            !regex.matches(secondValue) -> {
                durationEditText.error = "El campo no cumple con el formato requerido."
                return false
            }
            else -> {
                return true
            }
        }
    }

    private fun showSnackbarFail(message: String) {
        val rootLayout = window.decorView.findViewById<View>(android.R.id.content)
        Snackbar.make(rootLayout, message, Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(applicationContext, R.color.error))
            .setTextColor(Color.WHITE)
            .show()
    }

}