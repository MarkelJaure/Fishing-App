package com.example.fishingapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.fishingapp.R
import com.example.fishingapp.dao.ReporteDAO
import com.example.fishingapp.models.Reporte
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Reporte::class], version = 1, exportSchema = false)
abstract class FishingRoomDatabase : RoomDatabase() {
    abstract fun reporteDao(): ReporteDAO

    companion object {
        @Volatile
        private var INSTANCIA: FishingRoomDatabase? = null
        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): FishingRoomDatabase {
            val instanciaTemporal = INSTANCIA
            if (instanciaTemporal != null) {
                return instanciaTemporal
            }
            synchronized(this) {
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    FishingRoomDatabase::class.java,
                    "fishing_database"
                )
                    .addCallback(FishingDatabaseCallback(scope))
                    .build()
                INSTANCIA = instancia
                return instancia
            }
        }
    }

    private class FishingDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCIA?.let { database ->
                scope.launch {
                    loadBaseDeDatos(database.reporteDao())
                }
            }
        }

        suspend fun loadBaseDeDatos(reporteDAO: ReporteDAO) {
            var newReporte = Reporte(1, "El Deportivo", "Costa", "1/02/2022", R.drawable.pesca)
            reporteDAO.insert(newReporte)
        }
    }
}