package com.example.fishingapp.database

import android.content.Context
import android.util.Log
import androidx.navigation.navGraphViewModels
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.fishingapp.R
import com.example.fishingapp.dao.*
import com.example.fishingapp.models.*
import com.example.fishingapp.viewModels.ReporteViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Reporte::class, Concurso::class, Reglamentacion::class, Evento::class,Zona::class], version = 13, exportSchema = false)
@TypeConverters(Converter::class)
abstract class FishingRoomDatabase : RoomDatabase() {

    abstract fun reporteDao(): ReporteDAO
    abstract fun concursoDao(): ConcursoDAO
    abstract fun reglamentacionDao(): ReglamentacionDAO
    abstract fun eventoDao(): EventoDAO
    abstract fun zonaDao(): ZonaDAO

    companion object {
        @Volatile
        private var INSTANCIA: FishingRoomDatabase? = null
        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): FishingRoomDatabase {
            val instanciaTemporal = INSTANCIA
            if (instanciaTemporal != null) {
                Log.w("FIshingRoomDatabase", "Si habia una instancia creada")
                return instanciaTemporal
            }
            synchronized(this) {
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    FishingRoomDatabase::class.java,
                    "fishing_database20"
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                INSTANCIA = instancia
                Log.w("FishingRoomDatabase", "No habia una instancia creada")
                return instancia
            }
        }
    }
}