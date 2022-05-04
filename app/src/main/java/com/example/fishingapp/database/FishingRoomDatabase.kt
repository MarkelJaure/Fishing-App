package com.example.fishingapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.fishingapp.R
import com.example.fishingapp.dao.ConcursoDAO
import com.example.fishingapp.dao.ReglamentacionDAO
import com.example.fishingapp.dao.ReporteDAO
import com.example.fishingapp.models.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Reporte::class, Concurso::class, Reglamentacion::class], version = 1, exportSchema = false)
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
                    loadBaseDeDatos(database.reporteDao(),database.concursoDao(), database.reglamentacionDao())
                }
            }
        }

        suspend fun loadBaseDeDatos(reporteDAO: ReporteDAO,concursoDAO: ConcursoDAO,reglamentacionDAO: ReglamentacionDAO) {
            var newReporte = Reporte("El Deportivo", "Costa", "1/02/2022", R.drawable.pesca)
            var newReglamentacion = Reglamentacion("Prohibicion de pesca", "Pesca prohibida en Puerto Madryn", "Puerto Madryn")
            var newBaseOrCondicion = BaseOrCondicion("Pesca controlada","Solo se puede pescar en el balneario coral")
            var newConcurso = Concurso("Concurso de madryn", listOf(newBaseOrCondicion), "Cajon de mariscos",listOf(newReporte))
            reporteDAO.insert(newReporte)
            concursoDAO.insert(newConcurso)
            reglamentacionDAO.insert(newReglamentacion)
        }
    }
}