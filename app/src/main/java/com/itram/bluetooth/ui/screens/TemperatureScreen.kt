package com.itram.bluetooth.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.itram.bluetooth.ui.TemperatureViewModel
import com.itram.bluetooth.ui.components.BleDeviceButton
import com.itram.bluetooth.ui.components.ConnectionStatus
import com.itram.bluetooth.ui.components.TemperatureValue
import com.itram.bluetooth.ui.components.BleDevicesMessage
import kotlinx.coroutines.delay
import androidx.compose.material3.SnackbarHostState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemperatureScreen(
    viewModel: TemperatureViewModel = hiltViewModel()
) {
    val isConnected by viewModel.isConnected.collectAsState()
    val temperature by viewModel.temperature.collectAsState()
    val bleDevices by viewModel.bleDevices.collectAsState()
    val isConnecting by viewModel.isConnecting.collectAsState()
    var selectedDeviceAddress by remember { mutableStateOf<String?>(null) }
    var showNoDevicesMessage by remember { mutableStateOf(false) }

    LaunchedEffect(bleDevices) {
        if (bleDevices.isEmpty()) {
            showNoDevicesMessage = false
            delay(4000)
            if (bleDevices.isEmpty()) {
                showNoDevicesMessage = true
            }
        } else {
            showNoDevicesMessage = false
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(title = { Text("Sensor de Temperatura BLE") })
        Spacer(modifier = Modifier.height(32.dp))
        ConnectionStatus(isConnected)
        Spacer(modifier = Modifier.height(16.dp))
        if (isConnected) {
            Button(
                onClick = { viewModel.readTemperature() },
                enabled = !isConnecting
            ) {
                Text("Leer temperatura")
            }
            Spacer(modifier = Modifier.height(16.dp))
            TemperatureValue(temperature)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.disconnect() },
                enabled = !isConnecting
            ) {
                Text("Desconectar")
            }
        } else {
            Text(text = "Dispositivos BLE encontrados:")
            Spacer(modifier = Modifier.height(8.dp))
            if (bleDevices.isEmpty()) {
                BleDevicesMessage(isSearching = !showNoDevicesMessage)
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(bleDevices) { device ->
                        BleDeviceButton(
                            name = device.name,
                            address = device.address,
                            onClick = {
                                selectedDeviceAddress = device.address
                                viewModel.connect(device.address)
                            },
                            enabled = !isConnecting
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (selectedDeviceAddress != null) {
                Text(text = "Dispositivo seleccionado: $selectedDeviceAddress")
            }
        }
        if (isConnecting) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.CircularProgressIndicator()
            }
        }
    }
}
