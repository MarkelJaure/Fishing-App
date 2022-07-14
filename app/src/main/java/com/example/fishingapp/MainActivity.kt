package com.example.fishingapp

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings.Global.getString
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.fishingapp.databinding.ActivityMainBinding
import com.example.fishingapp.models.*
import com.example.fishingapp.viewModels.ConcursoViewModel
import com.example.fishingapp.viewModels.EventoViewModel
import com.example.fishingapp.viewModels.ReglamentacionViewModel
import com.example.fishingapp.viewModels.ReporteViewModel
import com.google.android.gms.location.*
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout

    private val reporteModel: ReporteViewModel by viewModels()
    private val eventoModel: EventoViewModel by viewModels()
    private val reglamentacionesModel: ReglamentacionViewModel by viewModels()
    private val concursosModel: ConcursoViewModel by viewModels()

    lateinit var geofencingClient: GeofencingClient

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i("Main Activity", "OnCreate")

        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        val view = binding.root

        drawerLayout = binding.drawerLayout
        setContentView(view)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.navView, navController)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)


        FirebaseFirestore.getInstance().collection("reportes").addSnapshotListener{ data, error ->
            reporteModel.borrarTodos()
            loadReportesFirebase()
        }

        FirebaseFirestore.getInstance().collection("eventos").addSnapshotListener{ data, error ->
            eventoModel.borrarTodos()
            loadEventosFirebase()
        }

        FirebaseFirestore.getInstance().collection("reglamentaciones").addSnapshotListener{ data, error ->
            reglamentacionesModel.borrarTodos()
            loadReglamentacionesFirebase()
        }

        FirebaseFirestore.getInstance().collection("concursos").addSnapshotListener{ data, error ->
            concursosModel.borrarTodos()
            loadConcursosFirebase()

        }


        geofencingClient = LocationServices.getGeofencingClient(this)


        var mGeofenceList = ArrayList<Geofence>()
        var geeo= Geofence.Builder()
        .setRequestId("Place1")
        .setCircularRegion(-42.752771, -65.043896, 10F) // defining fence region
        .setNotificationResponsiveness(1000)
        .setExpirationDuration( Geofence.NEVER_EXPIRE)
        .setTransitionTypes( Geofence.GEOFENCE_TRANSITION_ENTER)
        .build()

        mGeofenceList.add(geeo)


        val geofenceRequest = with(GeofencingRequest.Builder()){
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(geeo).build()
        }
        val intent = Intent(this,GeofenceBroadcastReceiver::class.java)
        val pendingIntent =  PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_MUTABLE)
        val geofencingClient= LocationServices.getGeofencingClient(this)
        geofencingClient.addGeofences(geofenceRequest,pendingIntent)





    }

    private fun getGeofencingRequest(geofenceList:List<Geofence>): GeofencingRequest {
        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            addGeofences(geofenceList)
        }.build()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    private fun loadReportesFirebase() {
        FirebaseFirestore.getInstance().collection("reportes").get().addOnSuccessListener { documents ->
            for (document in documents) {
                reporteModel.insert(
                    Reporte(
                        0,
                        document.id,
                        document.get("userID") as String,
                        document.get("nombre") as String,
                        document.get("tipoPesca") as String,
                        document.get("tipoEspecie") as String,
                        document.get("date") as String,
                        document.get("imagen") as String,
                        document.get("latitud") as Double,
                        document.get("longitud") as Double
                    )
                )
            }
        }
    }

    private fun loadReglamentacionesFirebase() {
        FirebaseFirestore.getInstance().collection("reglamentaciones").get().addOnSuccessListener { documents ->
            for (document in documents) {
                reglamentacionesModel.insert(
                    Reglamentacion(
                        0,
                        document.id,
                        document.get("nombre") as String,
                        document.get("descripcion") as String,
                        document.get("latitud") as Double,
                        document.get("longitud") as Double,
                        (document.get("radius") as Long).toDouble(),
                        document.get("ubicacion") as String,
                    )
                )
            }
        }
    }

    private fun loadConcursosFirebase() {
        FirebaseFirestore.getInstance().collection("concursos").get().addOnSuccessListener { documents ->
            for (document in documents) {
                var idsBasesAndCondiciones = document.get("basesYCondiciones") as List<String>;
                var idsReportes = document.get("ranking") as List<String>;


                var basesAndCondiciones: List<BaseOrCondicion> = listOf()
                var ranking: List<Reporte> = listOf()

                FirebaseFirestore.getInstance().collection("basesOrCondiciones").get().addOnSuccessListener { documents ->
                    for (document in documents) {
                        if (idsBasesAndCondiciones.contains(document.id)){
                            var aBaseOrCondicion = BaseOrCondicion(
                                document.get("articulo") as String,
                                document.get("descripcion") as String
                            )
                            basesAndCondiciones = basesAndCondiciones.plus(aBaseOrCondicion)
                        }
                    }
                }.addOnCompleteListener {

                    FirebaseFirestore.getInstance().collection("reportes").get().addOnSuccessListener { documents ->
                        for (document in documents) {
                            if (idsReportes.contains(document.id)){
                                var aReporte = Reporte(
                                    0,
                                    document.id,
                                    document.get("userID") as String,
                                    document.get("nombre") as String,
                                    document.get("tipoPesca") as String,
                                    document.get("tipoEspecie") as String,
                                    document.get("date") as String,
                                    document.get("imagen") as String,
                                    document.get("latitud") as Double,
                                    document.get("longitud") as Double
                                )
                                ranking = ranking.plus(aReporte)
                            }
                        }
                    }.addOnCompleteListener {

                        var aConcurso =  Concurso(
                            0,
                            document.id,
                            document.get("nombre") as String,
                            BasesAndCondiciones(basesAndCondiciones),
                            document.get("premio") as String,
                            Ranking(ranking),
                        )

                        concursosModel.insert(aConcurso)
                    }
                }
            }
        }
    }

    private fun loadEventosFirebase() {
        FirebaseFirestore.getInstance().collection("eventos").get().addOnSuccessListener { documents ->
            for (document in documents) {
                eventoModel.insert(
                    Evento(
                        0,
                        document.id,
                        document.get("nombre") as String,
                        document.get("tipoEvento") as String,
                        document.get("date") as String,
                        document.get("imagenes") as List<String>,
                        document.get("latitud") as Double,
                        document.get("longitud") as Double
                    )
                )
            }
        }
    }
}

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    // ...
    override fun onReceive(context: Context?, intent: Intent?) {
        val geofencingEvent: GeofencingEvent = GeofencingEvent.fromIntent(intent!!)
        if (geofencingEvent.hasError()) {
            val errorMessage = GeofenceStatusCodes
                .getStatusCodeString(geofencingEvent.errorCode)
            Log.e(TAG, errorMessage)
            return
        }

        // Get the transition type.
        val geofenceTransition = geofencingEvent.geofenceTransition
        val transitions = mapOf(
                Geofence.GEOFENCE_TRANSITION_DWELL to "Habita",
                Geofence.GEOFENCE_TRANSITION_ENTER to "Entra",
                Geofence.GEOFENCE_TRANSITION_EXIT to "DSale"
        )
        Log.w("GEOFENCE", transitions[geofenceTransition].toString())

    }
}