package com.tecsup.publicaciones

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tecsup.publicaciones.data.auth.AuthRepository
import com.tecsup.publicaciones.data.auth.FirebaseAuthRepository
import com.tecsup.publicaciones.data.local.AppDatabase
import com.tecsup.publicaciones.data.remote.ApiService
import com.tecsup.publicaciones.data.remote.RetrofitClient
import com.tecsup.publicaciones.data.repository.ItemRepository
import com.tecsup.publicaciones.data.repository.ItemRepositoryImpl
import com.tecsup.publicaciones.notification.Notificador
import com.tecsup.publicaciones.notification.NotificadorAndroid
import com.tecsup.publicaciones.ui.admin.AdminViewModel
import com.tecsup.publicaciones.ui.detalle.DetalleViewModel
import com.tecsup.publicaciones.ui.favoritos.FavoritosViewModel
import com.tecsup.publicaciones.ui.lista.ListaViewModel
import com.tecsup.publicaciones.ui.login.LoginViewModel

class AppContainer(context: Context) {

    private val appContext = context.applicationContext

    val apiService: ApiService by lazy {
        RetrofitClient.apiService
    }

    val database: AppDatabase by lazy {
        AppDatabase.obtenerBaseDatos(appContext)
    }

    val itemRepository: ItemRepository by lazy {
        ItemRepositoryImpl(
            api = apiService,
            dao = database.itemDao()
        )
    }

    val authRepository: AuthRepository by lazy {
        FirebaseAuthRepository()
    }

    val notificador: Notificador by lazy {
        NotificadorAndroid(appContext)
    }

    fun provideLoginFactory(): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LoginViewModel(authRepository) as T
        }
    }

    fun provideListaFactory(): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ListaViewModel(itemRepository, notificador) as T
        }
    }

    fun provideDetalleFactory(itemId: Int): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DetalleViewModel(itemRepository, itemId) as T
        }
    }

    fun provideFavoritosFactory(): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FavoritosViewModel(itemRepository) as T
        }
    }

    fun provideAdminFactory(): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AdminViewModel(itemRepository, notificador) as T
        }
    }
}
