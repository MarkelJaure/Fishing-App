package com.example.fishingapp

import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.location.Location
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.location.*

class GeofenceBroadcastReceiver: BroadcastReceiver() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient



    override fun onReceive(context: Context?, intent: Intent?) {
        Log.w("GEOTRANSITION", "Entre al broadcast")



        var  action: String? = intent!!.action

        Log.w("GEOACTION", "" + action)

        val geofencingEvent: GeofencingEvent = GeofencingEvent.fromIntent(intent)

        Log.w("GEOTRIGGER","" + geofencingEvent.triggeringGeofences?.toString())
        Log.w("GEOLOCATION","" + geofencingEvent.triggeringLocation?.toString())

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

        // Log.w("GEOTRIGGERED", geofencingEvent.triggeringGeofences?.toString())
        Log.w("GEOTRANSITION", geofenceTransition.toString())
        Log.w("GEOSTATE", transitions[geofenceTransition].toString())

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                Log.w("GEOLOC", "" + location!!.toString())

                var builder = NotificationCompat.Builder(context, "1")
                    .setSmallIcon(R.drawable.pesca)
                    .setContentTitle("Geofence transition")
                    .setContentText("Int value: " + geofenceTransition.toString() + " on lat: " + location.latitude + ", lng: " + location.longitude)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                with(NotificationManagerCompat.from(context)) {
                    // notificationId is a unique int for each notification that you must define
                    notify(2, builder.build())
                }
            }



    }
}
