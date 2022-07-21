package com.example.fishingapp

import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.location.Location
import android.util.Log
import androidx.activity.viewModels
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.viewModelScope
import com.example.fishingapp.dao.ZonaDAO
import com.example.fishingapp.database.FishingRoomDatabase
import com.example.fishingapp.models.Zona
import com.example.fishingapp.repositorio.ZonaRepositorio
import com.example.fishingapp.viewModels.ZonaViewModel
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore

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
        Log.w("Distancia entre puntos", distances[0].toString())
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

        val transitions = mapOf(
            Geofence.GEOFENCE_TRANSITION_DWELL to "Habita",
            Geofence.GEOFENCE_TRANSITION_ENTER to "Entra",
            Geofence.GEOFENCE_TRANSITION_EXIT to "Sale"
        )

        Log.w("GEOTRANSITION", geofenceTransition.toString())
        Log.w("GEOSTATE", transitions[geofenceTransition].toString())

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->

                FirebaseFirestore.getInstance().collection("zonas").get().addOnSuccessListener { documents ->
                    for (document in documents) {
                        Log.w("GEOBRODZONA", document.get("nombre") as String)

                        if (isMarkerInsideCircle(
                                LatLng(document.get("latitud") as Double, document.get("longitud") as Double),
                                LatLng(location!!.latitude, location!!.longitude),
                                (document.get("radius") as Long).toDouble()
                        )){

                            Log.w("GEOESTAS", document.get("nombre") as String)
                            var builder = NotificationCompat.Builder(context, "1")
                                .setSmallIcon(R.drawable.pesca)
                                .setContentTitle(document.get("nombre") as String)
                                .setContentText("Int value: " + geofenceTransition.toString() + " on lat: " + location.latitude + ", lng: " + location.longitude)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                            with(NotificationManagerCompat.from(context)) {
                                // notificationId is a unique int for each notification that you must define
                                notify(2, builder.build())
                            }
                        }
                    }
                }
                Log.w("GEOLOC", "" + location!!.toString())


            }



    }
}
