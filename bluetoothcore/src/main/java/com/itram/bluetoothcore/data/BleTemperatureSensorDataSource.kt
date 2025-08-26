package com.itram.bluetoothcore.data

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.content.Context
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class BleTemperatureSensorDataSource(private val context: Context) : TemperatureSensorDataSource {
    private var bluetoothGatt: BluetoothGatt? = null
    private var connected = false
    private var lastTemperature: Float? = null

    // UUIDs de ejemplo, reemplaza por los de tu sensor
    private val TEMPERATURE_SERVICE_UUID = java.util.UUID.fromString("00001809-0000-1000-8000-00805f9b34fb")
    private val TEMPERATURE_CHARACTERISTIC_UUID = java.util.UUID.fromString("00002a1c-0000-1000-8000-00805f9b34fb")

    @SuppressLint("MissingPermission")
    override suspend fun connect(deviceAddress: String): Boolean = suspendCancellableCoroutine { cont ->
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter
        val device = bluetoothAdapter.getRemoteDevice(deviceAddress)
        bluetoothGatt = device.connectGatt(context, false, object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
                if (newState == android.bluetooth.BluetoothProfile.STATE_CONNECTED) {
                    connected = true
                    gatt.discoverServices()
                    cont.resume(true)
                } else if (newState == android.bluetooth.BluetoothProfile.STATE_DISCONNECTED) {
                    connected = false
                    cont.resume(false)
                }
            }
        })
    }

    @SuppressLint("MissingPermission")
    override suspend fun disconnect() {
        bluetoothGatt?.disconnect()
        bluetoothGatt?.close()
        bluetoothGatt = null
        connected = false
    }

    @SuppressLint("MissingPermission")
    override suspend fun readTemperature(): Float? = suspendCancellableCoroutine { cont ->
        val gatt = bluetoothGatt ?: run {
            cont.resume(null)
            return@suspendCancellableCoroutine
        }
        val service: BluetoothGattService? = gatt.getService(TEMPERATURE_SERVICE_UUID)
        val characteristic: BluetoothGattCharacteristic? = service?.getCharacteristic(TEMPERATURE_CHARACTERISTIC_UUID)
        if (characteristic == null) {
            cont.resume(null)
            return@suspendCancellableCoroutine
        }
        gatt.readCharacteristic(characteristic)
        // El resultado real se debe manejar en el callback, aquí es simplificado
        // En producción, deberías usar un canal/flow para recibir el valor leído
        cont.resume(lastTemperature)
    }

    override fun isConnected(): Boolean = connected
}

