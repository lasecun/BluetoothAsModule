@file:OptIn(ExperimentalMaterial3Api::class)

package com.itram.bluetooth

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.itram.bluetooth.ui.TemperatureViewModel
import com.itram.bluetooth.ui.theme.BluetoothTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val permissions = arrayOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_CONNECT
    )
    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            // Si todos los permisos están concedidos, recarga el contenido Compose
            if (permissions.all { it.value }) {
                setComposeContent()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (hasAllPermissions()) {
            setComposeContent()
        } else {
            requestPermissionsLauncher.launch(permissions)
        }
    }

    private fun hasAllPermissions(): Boolean = permissions.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun setComposeContent() {
        setContent {
            BluetoothTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TemperatureScreen()
                }
            }
        }
    }
}

@Composable
fun TemperatureScreen(viewModel: TemperatureViewModel = hiltViewModel()) {
    val isConnected by viewModel.isConnected.collectAsState()
    val temperature by viewModel.temperature.collectAsState()
    val bleDevices by viewModel.bleDevices.collectAsState()
    var selectedDeviceAddress by remember { mutableStateOf<String?>(null) }
    var showNoDevicesMessage by remember { mutableStateOf(false) }

    LaunchedEffect(bleDevices) {
        if (bleDevices.isEmpty()) {
            showNoDevicesMessage = false
            delay(4000) // Espera 4 segundos antes de mostrar el mensaje de no encontrados
            if (bleDevices.isEmpty()) {
                showNoDevicesMessage = true
            }
        } else {
            showNoDevicesMessage = false
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Top, // Cambiado de Center a Top
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(title = { Text("Sensor de Temperatura BLE") })
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = if (isConnected) "Conectado" else "Desconectado")
        Spacer(modifier = Modifier.height(16.dp))
        if (isConnected) {
            Button(onClick = { viewModel.readTemperature() }) {
                Text("Leer temperatura")
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (temperature != null) {
                Text(text = "Temperatura: ${temperature}°C")
            } else {
                Text(text = "Temperatura no disponible")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { viewModel.disconnect() }) {
                Text("Desconectar")
            }
        } else {
            Text(text = "Dispositivos BLE encontrados:")
            Spacer(modifier = Modifier.height(8.dp))
            if (bleDevices.isEmpty()) {
                if (showNoDevicesMessage) {
                    Text(text = "No se han encontrado dispositivos BLE.")
                } else {
                    Text(text = "Buscando dispositivos BLE…")
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(bleDevices) { device ->
                        Button(
                            onClick = {
                                selectedDeviceAddress = device.address
                                viewModel.connect(device.address)
                            },
                            modifier = Modifier.padding(vertical = 2.dp)
                        ) {
                            Text(text = "${device.name ?: "Sin nombre"} (${device.address})")
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (selectedDeviceAddress != null) {
                Text(text = "Dispositivo seleccionado: $selectedDeviceAddress")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BluetoothTheme {
        TemperatureScreen()
    }
}