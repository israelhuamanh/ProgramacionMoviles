package com.tecsup.cafecampus.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tecsup.cafecampus.data.local.dao.ProductoDao
import com.tecsup.cafecampus.data.local.entity.Producto

@Database(
    entities = [Producto::class /* TODO: agregar las otras 3 */],
    version = 1,
    exportSchema = false
)
abstract class CafeDatabase : RoomDatabase() {
    abstract fun productoDao(): ProductoDao
    // TODO: usuarioDao() y pedidoDao()

    companion object {
        @Volatile
        private var INSTANCE: CafeDatabase? = null

        fun obtener(context: Context): CafeDatabase = INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                CafeDatabase::class.java,
                "cafecampus.db"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}
