package habibellah.ayata.driving.screens.mapsScreen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import habibellah.ayata.driving.R

@Composable
fun MapScreen() {
   MapScreenContent()
}

@SuppressLint("MissingPermission")
@Composable
fun MapScreenContent() {
   val uiSettings = remember {
      MapUiSettings(myLocationButtonEnabled = true)
   }
   val properties by remember {
      mutableStateOf(MapProperties(isMyLocationEnabled = true))
   }
   val currentLocationMarker = remember{
      mutableStateOf(LatLng(0.0,0.0))
   }

   LocationServices.getFusedLocationProviderClient(LocalContext.current).lastLocation.addOnSuccessListener {
      val current = LatLng(it.latitude,it.longitude)
      currentLocationMarker.value = current

   }
   val pointsState = remember{
      mutableStateOf(mutableListOf(currentLocationMarker.value,currentLocationMarker.value))
   }

   var destinationState by remember {
      mutableStateOf(false)
   }

   val marker = rememberMarkerState()


   Box(modifier = Modifier.fillMaxSize()) {
      if(pointsState.value[1] != LatLng(0.0,0.0)){
         val cameraPosition = rememberCameraPositionState(null){
            position =  CameraPosition(LatLng(pointsState.value[1].latitude, pointsState.value[1].longitude), 5f, 0f, 0f)
         }
         GoogleMap(
            modifier = Modifier
               .fillMaxSize()
               .padding(bottom = 20.dp) ,
            properties = properties ,
            uiSettings = uiSettings ,
            onMapClick = {
                marker.position = it
            },
            cameraPositionState = cameraPosition
         ) {
            Polyline(
               points = pointsState.value ,
               color = Color.Red
            )
            AddMarker(currentLocationMarker = marker)

          }
      }else{
         GoogleMap(
            modifier = Modifier
               .fillMaxSize()
               .padding(bottom = 20.dp) ,
            properties = properties ,
            uiSettings = uiSettings
         )
      }

      Box(
         modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .padding(PaddingValues(20.dp))
      ) {

         Column(
            modifier = Modifier
               .fillMaxWidth()
               .background(color = Color.White , shape = RoundedCornerShape(10)) ,
            horizontalAlignment = Alignment.CenterHorizontally ,
            verticalArrangement = Arrangement.Center
         ) {
            AnimatedVisibility(visible = destinationState) {
               Column(
                  horizontalAlignment = Alignment.CenterHorizontally ,
                  verticalArrangement = Arrangement.Center ,
                  modifier = Modifier.padding(PaddingValues(10.dp))
               ) {
                  DestinationsChips{
                     pointsState.value[1] = it
                  }
                  Button(
                     onClick = {
                        pointsState.value[0] = currentLocationMarker.value
                        destinationState = false
                                },
                     modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(15))
                        .background(color = Color.Black),
                     colors  = ButtonDefaults.buttonColors(Color.Black)
                  ) {
                     Text(text = stringResource(id = R.string.validate) ,
                        textAlign = TextAlign.Center,
                        color = Color.White)
                  }
               }
            }
            Row(modifier = Modifier.clickable {
               destinationState = !destinationState
            }) {
               Text(text = "Choose Available destination" , color = Color.Black)
               Image(
                  painter = painterResource(id = R.drawable.destination_icon) ,
                  contentDescription = ""
               )
            }
         }
      }
   }

}


@Composable
fun AddMarker(currentLocationMarker : MarkerState) {
   Marker(
      state = currentLocationMarker,
      draggable = true
   )
}

