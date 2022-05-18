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
import com.example.fishingapp.dao.ConcursoDAO
import com.example.fishingapp.dao.ReglamentacionDAO
import com.example.fishingapp.dao.ReporteDAO
import com.example.fishingapp.models.*
import com.example.fishingapp.viewModels.ReporteViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Database(entities = [Reporte::class, Concurso::class, Reglamentacion::class], version = 3, exportSchema = false)
@TypeConverters(Converter::class)
abstract class FishingRoomDatabase : RoomDatabase() {

    abstract fun reporteDao(): ReporteDAO
    abstract fun concursoDao(): ConcursoDAO
    abstract fun reglamentacionDao(): ReglamentacionDAO

    companion object {
        @Volatile
        private var INSTANCIA: FishingRoomDatabase? = null
        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): FishingRoomDatabase {
            val instanciaTemporal = INSTANCIA
            if (instanciaTemporal != null) {
                Log.w("FIshingRoomDatabase","Si habia una instancia creada")
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
                    .addCallback(FishingDatabaseCallback(scope))
                    .build()
                INSTANCIA = instancia
                Log.w("FIshingRoomDatabase","No habia una instancia creada")
                return instancia
            }
        }
    }

    private class FishingDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCIA?.let { database ->
                scope.launch {
                    loadBaseDeDatos(database.reporteDao(),database.concursoDao(), database.reglamentacionDao())
                }
            }
        }

        suspend fun loadBaseDeDatos(reporteDAO: ReporteDAO,concursoDAO: ConcursoDAO,reglamentacionDAO: ReglamentacionDAO) {

            if (reporteDAO.getCount() == 0){
                for (newReporte in Reporte.data){
                    reporteDAO.insert(newReporte)
                }
                Log.w("FIshingRoomDatabase","Cargue la base de datos de reportes")

            }

            if (reglamentacionDAO.getCount() == 0){
                for (newReglamentacion in Reglamentacion.data){
                    reglamentacionDAO.insert(newReglamentacion)
                }
                Log.w("FishingRoomDatabase","Cargue la base de datos de reglamentaciones")
            }

            if (concursoDAO.getCount() == 0){
                for (newConcurso in Concurso.data){
                    concursoDAO.insert(newConcurso)
                }
                Log.w("FishingRoomDatabase","Cargue la base de datos de concursos")
            }


        }
    }
}
