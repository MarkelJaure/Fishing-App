package com.example.fishingapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.*
import android.content.ContentValues.TAG
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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
import android.content.Intent





class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout

    private val reporteModel: ReporteViewModel by viewModels()
    private val eventoModel: EventoViewModel by viewModels()
    private val reglamentacionesModel: ReglamentacionViewModel by viewModels()
    private val concursosModel: ConcursoViewModel by viewModels()

    lateinit var geofencingClient: GeofencingClient
    var aGeofenceList: List<Geofence> = listOf(
        Geofence.Builder()
            .setRequestId("Casa Markel")
            .setCircularRegion(-42.752789, -65.043793, 200F) // defining fence region
            .setExpirationDuration( Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .build(),
        Geofence.Builder()
            .setRequestId("Escuela 7707")
            .setCircularRegion(-42.759337, -65.061087, 200F) // defining fence region
            .setExpirationDuration( Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .build(),
        Geofence.Builder()
            .setRequestId("Casa Martin")
            .setCircularRegion(-42.7788101, -65.0537488, 200F) // defining fence region
            .setExpirationDuration( Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .build()
    )


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



        geofencingClient = LocationServices.getGeofencingClient(this)


        geofencingClient.addGeofences(getGeofencingRequest(), geofencePendingIntent).run {
            addOnSuccessListener {
                Log.w("GEOFENCE","Se Agregaron los geofence")
            }
            addOnFailureListener {
                Log.w("GEOFENCE","No se agregaron los geofence")
                Log.w("ErrorGEOFENCE",it.toString())
            }
        }

        createNotificationChannel()

        var builder = NotificationCompat.Builder(this, "1")
            .setSmallIcon(R.drawable.pesca)
            .setContentTitle("Fishing App")
            .setContentText("Notificacion de prueba al ingrersar a la app")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(1, builder.build())
        }


    }

    private fun getGeofencingRequest(): GeofencingRequest {
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
            val name = "getString(R.string.channel_name)"
            val descriptionText = "getString(R.string.channel_description)"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(1.toString(), name, importance).apply {
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
}

class GeofenceBroadcastReceiver: BroadcastReceiver() {
    var broadcastIntent: Intent? = Intent()

    override fun onReceive(context: Context?, intent: Intent?) {
        broadcastIntent.addCategory(GeofenceUtils.CATEGORY_LOCATION_SERVICE);
        Log.w("GEOTRANSITION", "Entre al broadcast")

        var  action: String? = intent.getAction()
        Log.i("ACTION", "" + action)
        val geofencingEvent: GeofencingEvent = GeofencingEvent.fromIntent(intent!!)
        if (geofencingEvent.hasError()) {
            val errorMessage = GeofenceStatusCodes
                .getStatusCodeString(geofencingEvent.errorCode)
            Log.e(TAG, errorMessage)
            return
        }

        // Get the transition type.
        val geofenceTransition = geofencingEvent.geofenceTransition
        //geofencingEvent.triggeringGeofences

        val transitions = mapOf(
                Geofence.GEOFENCE_TRANSITION_DWELL to "Habita",
                Geofence.GEOFENCE_TRANSITION_ENTER to "Entra",
                Geofence.GEOFENCE_TRANSITION_EXIT to "Sale"
        )

       // Log.w("GEOTRIGGERED", geofencingEvent.triggeringGeofences?.toString())
        Log.w("GEOTRANSITION", geofenceTransition.toString())
        Log.w("GEOSTATE", transitions[geofenceTransition].toString())

        var builder = NotificationCompat.Builder(context!!, "1")
            .setSmallIcon(R.drawable.pesca)
            .setContentTitle("Geofence transition")
            .setContentText("Int value: " + geofenceTransition.toString() + ", Transition: " + transitions[geofenceTransition].toString())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(2, builder.build())
        }

    }
}

