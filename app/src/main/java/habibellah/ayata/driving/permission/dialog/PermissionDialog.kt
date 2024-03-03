package habibellah.ayata.driving.permission.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PermissionDialog(
   permissionTextProvider : PermissionTextProvider,
   isPermanentlyDeclined : Boolean,
   onDismiss:()-> Unit,
   onOkClick:()->Unit,
   onGoToAppSettingsClick:()->Unit,
   modifier : Modifier = Modifier
){
   AlertDialog(
      onDismissRequest = { /*TODO*/ } ,
      title = {
              Text(text = "Permission required")
      },
      text = {
         Text(text = permissionTextProvider.getDescription(isPermanentlyDeclined)
         )
      },
      modifier = modifier,
      confirmButton = {
         Text(
            text = if (isPermanentlyDeclined) {
               "Grant Permission"
            }else{
               "OK"
            },
            modifier = Modifier
               .fillMaxWidth()
               .clickable {
                  if (isPermanentlyDeclined) {
                     onGoToAppSettingsClick()
                  } else {
                     onOkClick()
                  }
               }
         )
      })
}

interface PermissionTextProvider{
   fun getDescription(isPermanentlyDeclined : Boolean): String
}

class LocationAccessTextProvider : PermissionTextProvider{
   override fun getDescription(isPermanentlyDeclined : Boolean) : String {
      return if(isPermanentlyDeclined){
         "it seems you permanently declined location access permission"
      }else{
         "this app needs access to your location"
      }
   }
}

class AccessFineTextProvider : PermissionTextProvider{
   override fun getDescription(isPermanentlyDeclined : Boolean) : String {
      return if(isPermanentlyDeclined){
         "it seems you permanently declined ...."
      }else{
         "this app needs access ...."
      }
   }
}