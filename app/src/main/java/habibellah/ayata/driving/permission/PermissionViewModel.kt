package habibellah.ayata.driving.permission

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class PermissionViewModel : ViewModel() {

   val visiblePermissionDialogs = mutableStateListOf<String>()

   fun dismissDialog(){
      visiblePermissionDialogs.removeFirst()
   }

   fun onPermissionResult(
      permission : String,
      isGranted : Boolean
   ){
      if(!isGranted && !visiblePermissionDialogs.contains(permission)){
         visiblePermissionDialogs.add(permission)
      }
   }
}