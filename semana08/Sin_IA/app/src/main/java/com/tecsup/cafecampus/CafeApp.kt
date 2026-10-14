package com.tecsup.cafecampus

import android.app.Application
import com.tecsup.cafecampus.data.local.CafeDatabase
import com.tecsup.cafecampus.data.local.DatosIniciales
import com.tecsup.cafecampus.data.repository.CafeRepositoryRoom
import com.tecsup.cafecampus.viewmodel.CafeViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CafeApp : Application() {
    val database by lazy { CafeDatabase.obtener(this) }
    val repository by lazy { CafeRepositoryRoom(database) }
    val factory by lazy { CafeViewModelFactory(repository) }

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            DatosIniciales.sembrar(database)
        }
    }
}
