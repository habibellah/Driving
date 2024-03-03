package habibellah.ayata.driving.screens.mapsScreen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.LatLng

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinationsChips(chosenDestination:(selectedChipLatLng : LatLng)->Unit) {
   var selectedChip by remember{
      mutableStateOf("")
   }
   getAllDestinations().forEach {
      AssistChip(onClick = {
                           selectedChip = it.destination
         chosenDestination(it.latLng)
      } ,
         label = { Text(text = it.destination , color =
         if(selectedChip == it.destination){
            Color.Red
         }else{
            Color.Black
         }
         ) },
         modifier = Modifier.fillMaxWidth())
   }
}

enum class SearchCategory(val destination : String,val latLng : LatLng) {
   ALGER(destination = "Alger", latLng = LatLng(36.6738130571063,2.821106333814487))
   , BBA(destination = "Bba", latLng = LatLng(36.07597513939479,4.773955637770331)) ,
   SETIF(destination = "Setif", latLng = LatLng(36.19898695455914,5.414956362393018))
}

fun getAllDestinations() : List<SearchCategory> {
   return listOf(
      SearchCategory.ALGER ,
      SearchCategory.SETIF ,
      SearchCategory.BBA
   )
}