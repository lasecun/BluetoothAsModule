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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.itram.bluetooth.ui.TemperatureViewModel
import com.itram.bluetooth.ui.theme.BluetoothTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.navigation.compose.hiltViewModel

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
    val deviceAddress = "00:11:22:33:44:55" // Simulado

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
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
            Button(onClick = { viewModel.connect(deviceAddress) }) {
                Text("Conectar")
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