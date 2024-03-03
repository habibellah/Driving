package habibellah.ayata.driving

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import habibellah.ayata.driving.permission.PermissionViewModel
import habibellah.ayata.driving.permission.dialog.AccessFineTextProvider
import habibellah.ayata.driving.permission.dialog.LocationAccessTextProvider
import habibellah.ayata.driving.permission.dialog.PermissionDialog
import habibellah.ayata.driving.screens.mapsScreen.MapScreen
import habibellah.ayata.driving.ui.theme.DrivingTheme

class MainActivity : ComponentActivity() {
   private val permissionToRequest = arrayOf(
      Manifest.permission.ACCESS_FINE_LOCATION,
      Manifest.permission.ACCESS_COARSE_LOCATION
   )
   override fun onCreate(savedInstanceState : Bundle?) {
      super.onCreate(savedInstanceState)
      setContent {
         DrivingTheme {
            val viewModel = viewModel<PermissionViewModel>()
            val dialogQueue = viewModel.visiblePermissionDialogs

            val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
               contract = ActivityResultContracts.RequestMultiplePermissions() ,
               onResult = { perms ->
                  permissionToRequest.forEach { permission ->
                     viewModel.onPermissionResult(
                        permission =permission,
                        isGranted = perms[permission] == true
                     )
                  }

               }
            )

            Surface(
               modifier = Modifier.fillMaxSize() ,
               color = MaterialTheme.colorScheme.background
            ) {
               MapScreen()
//               Column {
//                  Button(onClick = {
//                     multiplePermissionResultLauncher.launch(
//                        arrayOf(
//                           Manifest.permission.ACCESS_FINE_LOCATION,
//                           Manifest.permission.ACCESS_COARSE_LOCATION
//                        )
//                     )
//                  }) {
//
//                  }
//
//               }

            }

            dialogQueue
               .reversed()
               .forEach { permission ->
                  PermissionDialog(
                     permissionTextProvider = when (permission) {
                        Manifest.permission.ACCESS_FINE_LOCATION -> {
                           AccessFineTextProvider()
                        }
                        Manifest.permission.ACCESS_COARSE_LOCATION -> {
                           LocationAccessTextProvider()
                        }
                        else->{
                           return@forEach
                        }
                     } ,
                     isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                        permission
                     ),
                     onDismiss = { viewModel::dismissDialog } ,
                     onOkClick = {
                                 viewModel.dismissDialog()
                          multiplePermissionResultLauncher.launch(
                             arrayOf(
                                permission
                             )
                          )
                                 } ,
                     onGoToAppSettingsClick = {
                            openAppSettings()
                     })
               }
         }
      }
   }
}

fun Activity.openAppSettings(){
 Intent(
    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
    Uri.fromParts("package",packageName,null)
 ).also(::startActivity)
}
