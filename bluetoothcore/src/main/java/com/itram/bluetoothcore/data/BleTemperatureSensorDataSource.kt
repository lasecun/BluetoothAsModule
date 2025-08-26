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
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class BleTemperatureSensorDataSource(private val context: Context) : TemperatureSensorDataSource {
    private var bluetoothGatt: BluetoothGatt? = null
    private var connected = false
    private var lastTemperature: Float? = null

    // UUIDs de ejemplo, reemplaza por los de tu sensor
    private val TEMPERATURE_SERVICE_UUID = java.util.UUID.fromString("FC543622-236C-4C94-8FA9-944A3E5353FA")
    private val TEMPERATURE_CHARACTERISTIC_UUID = java.util.UUID.fromString("00001530-1212-EFDE-1523-785FEABCD123")

    private var gattCallback: BluetoothGattCallback? = null

    @SuppressLint("MissingPermission")
    override suspend fun connect(deviceAddress: String): Boolean = suspendCancellableCoroutine { cont ->
        android.util.Log.i("BLE_DS", "connect() llamado con $deviceAddress")
        // Comprobación de permisos en tiempo de ejecución
        val hasConnectPerm = ContextCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
        val hasScanPerm = ContextCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED
        if (!hasConnectPerm || !hasScanPerm) {
            android.util.Log.e("BLE_DS", "Faltan permisos BLUETOOTH_CONNECT o BLUETOOTH_SCAN")
            cont.resume(false)
            return@suspendCancellableCoroutine
        }
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter
        val device = bluetoothAdapter.getRemoteDevice(deviceAddress)
        android.util.Log.i("BLE_DS", "Permisos OK, llamando a connectGatt...")
        gattCallback = object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
                android.util.Log.i("BLE_DS", "onConnectionStateChange: status=$status, newState=$newState")
                if (status != BluetoothGatt.GATT_SUCCESS) {
                    android.util.Log.e("BLE_DS", "Connection failed with status: $status")
                    connected = false
                    cont.resume(false)
                    return
                }
                if (newState == android.bluetooth.BluetoothProfile.STATE_CONNECTED) {
                    android.util.Log.i("BLE_DS", "STATE_CONNECTED, discovering services...")
                    connected = true
                    gatt.discoverServices()
                    // No hacemos resume aquí, esperamos a onServicesDiscovered
                } else if (newState == android.bluetooth.BluetoothProfile.STATE_DISCONNECTED) {
                    android.util.Log.i("BLE_DS", "STATE_DISCONNECTED")
                    connected = false
                    cont.resume(false)
                }
            }
            override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
                android.util.Log.i("BLE_DS", "onServicesDiscovered: status=$status")
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    val services = gatt.services
                    for (service in services) {
                        android.util.Log.i("BLE_DS", "Service UUID: ${service.uuid}")
                        for (characteristic in service.characteristics) {
                            android.util.Log.i("BLE_DS", "  Characteristic UUID: ${characteristic.uuid}, properties: ${characteristic.properties}")
                        }
                    }
                    cont.resume(true)
                } else {
                    android.util.Log.e("BLE_DS", "Service discovery failed: $status")
                    cont.resume(false)
                }
            }
        }
        bluetoothGatt = device.connectGatt(context, false, gattCallback)
        android.util.Log.i("BLE_DS", "connectGatt llamado")
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
            android.util.Log.e("BLE", "No GATT connection available")
            cont.resume(null)
            return@suspendCancellableCoroutine
        }
        val service: BluetoothGattService? = gatt.getService(TEMPERATURE_SERVICE_UUID)
        if (service == null) {
            android.util.Log.e("BLE", "Temperature service not found: $TEMPERATURE_SERVICE_UUID")
            cont.resume(null)
            return@suspendCancellableCoroutine
        }
        val characteristic: BluetoothGattCharacteristic? = service.getCharacteristic(TEMPERATURE_CHARACTERISTIC_UUID)
        if (characteristic == null) {
            android.util.Log.e("BLE", "Temperature characteristic not found: $TEMPERATURE_CHARACTERISTIC_UUID")
            cont.resume(null)
            return@suspendCancellableCoroutine
        }
        val callback = object : BluetoothGattCallback() {
            override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    val value = characteristic.value
                    // Suponiendo que la temperatura es un float en los primeros 4 bytes
                    val temp = if (value.size >= 4) java.nio.ByteBuffer.wrap(value).order(java.nio.ByteOrder.LITTLE_ENDIAN).float else null
                    android.util.Log.i("BLE", "Read temperature value: $temp")
                    cont.resume(temp)
                } else {
                    android.util.Log.e("BLE", "Failed to read temperature characteristic: $status")
                    cont.resume(null)
                }
            }
        }
        // Registrar temporalmente el callback para la lectura
        gatt.setCharacteristicNotification(characteristic, true)
        gatt.readCharacteristic(characteristic)
        // NOTA: En producción, deberías gestionar el callback a nivel de clase para evitar fugas
    }

    override fun isConnected(): Boolean = connected
}
