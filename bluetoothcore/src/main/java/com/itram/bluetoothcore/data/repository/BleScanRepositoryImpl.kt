package com.itram.bluetoothcore.data.repository

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import com.itram.bluetoothcore.domain.model.BleDevice
import com.itram.bluetoothcore.domain.repository.BleScanRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class BleScanRepositoryImpl @Inject constructor() : BleScanRepository {
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    @SuppressLint("MissingPermission")
    override fun scanDevices(): Flow<List<BleDevice>> = callbackFlow {
        val scanner: BluetoothLeScanner? = bluetoothAdapter?.bluetoothLeScanner
        val devices = mutableMapOf<String, BleDevice>()
        val callback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                val device = result.device
                val bleDevice = BleDevice(device.name, device.address)
                devices[device.address] = bleDevice
                trySend(devices.values.toList())
            }

            override fun onBatchScanResults(results: List<ScanResult>) {
                results.forEach { onScanResult(ScanSettings.CALLBACK_TYPE_ALL_MATCHES, it) }
            }

            override fun onScanFailed(errorCode: Int) {
                close(Exception("Scan failed: $errorCode"))
            }
        }
        scanner?.startScan(callback)
        awaitClose { scanner?.stopScan(callback) }
    }
}
