package com.example.vinillos_app_misw

import android.app.Application
import com.example.vinillos_app_misw.data.database.VinilosRoomDatabase

class VinilosApplication : Application()  {
    val database by lazy { VinilosRoomDatabase.getDatabase(this) }
}