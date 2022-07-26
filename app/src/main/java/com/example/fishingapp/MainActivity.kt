package com.example.fishingapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.*
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.fishingapp.databinding.ActivityMainBinding
import com.example.fishingapp.models.*
import com.example.fishingapp.viewModels.*
import com.google.android.gms.location.*
import com.google.firebase.firestore.FirebaseFirestore
import android.content.Intent
import com.example.fishingapp.GeofenceBroadcastReceiver
import com.example.fishingapp.viewModels.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout

    private val reporteModel: ReporteViewModel by viewModels()
    private val eventoModel: EventoViewModel by viewModels()
    private val reglamentacionesModel: ReglamentacionViewModel by viewModels()
    private val concursosModel: ConcursoViewModel by viewModels()
    private val zonasModel: ZonaViewModel by viewModels()
    private val geoEventModel: GeoEventViewModel by viewModels()

    lateinit var geofencingClient: GeofencingClient

    var aGeofenceList: List<Geofence> = listOf()


    private val geofencePendingIntent: PendingIntent by lazy {
        val aIntent = Intent(this, GeofenceBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(this, 0, aIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
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


        FirebaseFirestore.getInstance().collection("concursos").addSnapshotListener{ data, error ->
            geoEventModel.borrarTodos()
            loadGeoEventsFirebase()
        }

        geofencingClient = LocationServices.getGeofencingClient(this)

        val prefs = PreferenceManager.getDefaultSharedPreferences(
            baseContext
        )
        val previouslyStarted = prefs.getBoolean("previously_started", false)
        Log.w("Prev_Start", previouslyStarted.toString())
        if (!previouslyStarted || true) { //TODO: sacar true, se ejecutaria solo la primera vez que abris la aplicacion
            val edit = prefs.edit()
            edit.putBoolean("previously_started", java.lang.Boolean.TRUE)
            edit.apply()

            FirebaseFirestore.getInstance().collection("zonas").get().addOnSuccessListener { documents ->

                if (documents != null) {
                    zonasModel.borrarTodos()
                    loadZonasFirebase()

                    for (document in documents) {
                        aGeofenceList = aGeofenceList.plus(
                            Geofence.Builder()
                                .setRequestId(document.id)
                                .setCircularRegion(document.get("latitud") as Double, document.get("longitud") as Double, (document.get("radius") as Long).toFloat())
                                .setExpirationDuration( Geofence.NEVER_EXPIRE)
                                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER )
                                .build(),

                            )
                    }

                    geofencingClient.addGeofences(getGeofencingRequest(), geofencePendingIntent).run {
                        addOnSuccessListener {
                            Log.w("GEOFENCE","Se Agregaron los geofence")
                        }
                        addOnFailureListener {
                            Log.w("GEOFENCE","No se agregaron los geofence")
                            Log.w("ErrorGEOFENCE",it.toString())
                        }
                    }
                }
            }
        }



        createNotificationChannel()

    }

    private fun getGeofencingRequest(): GeofencingRequest {
        Log.w("GEOLENGTH",aGeofenceList.size.toString())
        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER or GeofencingRequest.INITIAL_TRIGGER_DWELL)
            addGeofences(aGeofenceList)
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

    private fun loadZonasFirebase() {
        FirebaseFirestore.getInstance().collection("zonas").get().addOnSuccessListener { documents ->
            for (document in documents) {
                zonasModel.insert(
                    Zona(
                        0,
                        document.id,
                        document.get("nombre") as String,
                        document.get("descripcion") as String,
                        document.get("latitud") as Double,
                        document.get("longitud") as Double,
                        (document.get("radius") as Long).toDouble()
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

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Avisos"
            val descriptionText = "Avisos de fishing app dados por Geofence"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("Avisos", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
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

    private fun loadGeoEventsFirebase() {
        FirebaseFirestore.getInstance().collection("geofenceEvents")
            .whereEqualTo("userID", Firebase.auth.currentUser?.uid).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    FirebaseFirestore.getInstance().collection("zonas")
                        .document(document.data.get("zonaID").toString()).get()
                        .addOnSuccessListener { data ->
                            geoEventModel.insert(
                                GeoEvent(
                                    document.id,
                                    Firebase.auth.currentUser?.uid!!,
                                    (document.data.get("timestamp")).toString(),
                                    data.get("nombre") as String,
                                    data.get("descripcion") as String,
                                    data.get("latitud") as Double,
                                    data.get("longitud") as Double,
                                    (data.get("radius") as Long).toDouble()
                                )
                            )
                        }
                }
            }
    }
}


