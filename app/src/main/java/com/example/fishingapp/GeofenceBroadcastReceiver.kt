package com.example.fishingapp

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.location.Location
import android.util.Log
import androidx.activity.viewModels
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
import com.example.fishingapp.dao.ZonaDAO
import com.example.fishingapp.database.FishingRoomDatabase
import com.example.fishingapp.models.Zona
import com.example.fishingapp.repositorio.ZonaRepositorio
import com.example.fishingapp.viewModels.ZonaViewModel
import com.example.fishingapp.zonas.ZonaDetailActivity
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.time.Instant

class GeofenceBroadcastReceiver: BroadcastReceiver() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private fun isMarkerInsideCircle(
        centerLatLng: LatLng,
        draggedLatLng: LatLng,
        radius: Double
    ): Boolean {
        val distances = FloatArray(1)
        Location.distanceBetween(
            centerLatLng.latitude,
            centerLatLng.longitude,
            draggedLatLng.latitude,
            draggedLatLng.longitude, distances
        )
        return radius >= distances[0]
    }


    override fun onReceive(context: Context?, intent: Intent?) {
        Log.w("GEOTRANSITION", "Entre al broadcast")


        val geofencingEvent: GeofencingEvent = GeofencingEvent.fromIntent(intent!!)

        if (geofencingEvent.hasError()) {
            val errorMessage = GeofenceStatusCodes
                .getStatusCodeString(geofencingEvent.errorCode)
            Log.e(ContentValues.TAG, errorMessage)
            return
        }

        // Get the transition type.
        val geofenceTransition = geofencingEvent.geofenceTransition
        //geofencingEvent.triggeringGeofences



        Log.w("GEOTRANSITION", geofenceTransition.toString())

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->

                FirebaseFirestore.getInstance().collection("zonas").get().addOnSuccessListener { documents ->
                    for (document in documents) {
                        Log.w("GEOBRODZONA", document.get("nombre") as String)

                        var aZona = Zona (
                            0,
                            document.id,
                            document.get("nombre") as String,
                            document.get("descripcion") as String,
                            document.get("latitud") as Double,
                            document.get("longitud") as Double,
                            (document.get("radius") as Long).toDouble()
                        )

                        if (isMarkerInsideCircle(
                                LatLng(aZona.latitud, aZona.longitud),
                                LatLng(location!!.latitude, location!!.longitude),
                                aZona.radius
                        )){

                            var builder = createZonaNotification(aZona,context)

                            with(NotificationManagerCompat.from(context)) {
                                // notificationId is a unique int for each notification that you must define
                                notify(idStringToInt(aZona.id), builder.build())
                            }

                            val data = hashMapOf<String, Any>(
                                "userID" to Firebase.auth.currentUser!!.uid,
                                "zonaID" to aZona.id,
                                "timestamp" to Timestamp(System.currentTimeMillis() / 1000,0),
                            )

                            FirebaseFirestore.getInstance().collection("geofenceEvents").add(data)
                                .addOnCompleteListener { Log.w("geoUpload - exito", it.toString()) }
                                .addOnFailureListener {
                                    Log.w("geoUpload - fallo", it.toString())
                                }
                        }
                    }
                }
            }

    }

    private fun  idStringToInt(stringID: String): Int{
        var intReturn = 0;
        for (char in stringID) {
            intReturn += char.code
        }
        return intReturn
    }

    private fun createZonaNotification(aZona: Zona, context: Context): NotificationCompat.Builder {
        Log.w("GEOESTAS", aZona.nombre)

        val intent = Intent(context, LoginActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val snoozeIntent = Intent(context, ZonaDetailActivity::class.java).apply {
            putExtra("nombre", aZona.nombre);
            putExtra("descripcion", aZona.descripcion);
            putExtra("latitud", aZona.latitud);
            putExtra("longitud", aZona.longitud);
            putExtra("radius", aZona.radius);
        }

        val snoozePendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, snoozeIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)



        return NotificationCompat.Builder(context, "Avisos")
            .setSmallIcon(R.drawable.notification)
            .setContentTitle(aZona.nombre)
            .setContentText(aZona.descripcion)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.notification, "Ver mas",
                snoozePendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }
}
