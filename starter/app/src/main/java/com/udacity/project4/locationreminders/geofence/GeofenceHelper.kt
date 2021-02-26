package com.udacity.project4.locationreminders.geofence

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.maps.model.LatLng

class GeofenceHelper(val context: Context) : ContextCompat() {
    private var pendingIntent: PendingIntent? = null

    fun geoFencingRequest(geofence: Geofence): GeofencingRequest {
        return GeofencingRequest.Builder()
            .addGeofence(geofence)
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .build()
    }

    fun getGeofence(id: String, latLng: LatLng, radius: Float, transitionTypes: Int): Geofence {
        return Geofence.Builder()
            .setCircularRegion(latLng.latitude, latLng.longitude, radius)
            .setRequestId(id)
            .setTransitionTypes(transitionTypes)
            .setLoiteringDelay(5000) // require if alert needs to be send after 5 seconds of enter station in case of dwell
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .build()
    }

    fun getPendingIntent(): PendingIntent? {
        if (pendingIntent != null) {
            return pendingIntent
        }

        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(
            context, 1201,
            intent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        return pendingIntent
    }

    fun getErrorString(e: Exception): String {
        if (e is ApiException) {
            when (e.statusCode) {
                GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE ->
                    return "GEOFNECE NOT AVAILABLE"
                GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES ->
                    return "GEOFENCE TOO MANY GEOFENCES"
                GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS ->
                    return "GEOFENCE TOO MANY PENDING INTENTS"
            }
        }
        if (e.localizedMessage != null)
            return e.localizedMessage!!
        else
            return "NO MESSAGE FOUND"

    }

    companion object {
        private val TAG = GeofenceHelper::class.java.simpleName
    }
}