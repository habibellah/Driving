package habibellah.ayata.driving.screens.mapsScreen

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

lateinit var locationCallback : LocationCallback
lateinit var locationProvider : FusedLocationProviderClient

@SuppressLint("MissingPermission")
@Composable
fun getUserLocation(context : Context) : LatandLong {
   locationProvider = LocationServices.getFusedLocationProviderClient(context)
   var currentUserLocation by remember { mutableStateOf(LatandLong()) }

   DisposableEffect(key1 = locationProvider) {
      locationCallback = object : LocationCallback() {
         //1
         override fun onLocationResult(result : LocationResult) {
            for (location in result.locations) {
               currentUserLocation = LatandLong(location.latitude , location.longitude)

            }

         }
      }

      onDispose {
         stopLocationUpdate()
      }
   }

   return currentUserLocation
}

fun stopLocationUpdate() {
   try {
      val removeTask = locationProvider.removeLocationUpdates(locationCallback)
      removeTask.addOnCompleteListener { task ->
         if (task.isSuccessful) {
         } else {
         }
      }
   } catch (se : SecurityException) {
   }
}

data class LatandLong(
   val latitude : Double = 0.0 ,
   val longitude : Double = 0.0
)