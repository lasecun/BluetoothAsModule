package com.itram.bluetooth.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BleDeviceButton(name: String?, address: String, onClick: () -> Unit, enabled: Boolean = true) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Text(text = "${name ?: "Sin nombre"} ($address)")
    }
}

@Composable
fun ConnectionStatus(isConnected: Boolean) {
    Text(text = if (isConnected) "Conectado" else "Desconectado")
}

@Composable
fun TemperatureValue(temperature: Float?) {
    if (temperature != null) {
        Text(text = "Temperatura: ${temperature}°C")
    } else {
        Text(text = "Temperatura no disponible")
    }
}

@Composable
fun BleDevicesMessage(isSearching: Boolean) {
    if (isSearching) {
        Text(text = "Buscando dispositivos BLE…")
    } else {
        Text(text = "No se han encontrado dispositivos BLE.")
    }
}
