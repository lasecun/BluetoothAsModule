package com.itram.bluetooth.permissions

import android.app.Application
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PermissionViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val permissions = arrayOf(
        android.Manifest.permission.BLUETOOTH,
        android.Manifest.permission.BLUETOOTH_ADMIN,
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.BLUETOOTH_SCAN,
        android.Manifest.permission.BLUETOOTH_CONNECT
    )

    private val _state = MutableStateFlow<PermissionState>(PermissionState.Explanation)
    val state: StateFlow<PermissionState> = _state

    init {
        if (checkPermissions()) {
            _state.value = PermissionState.Granted
        } else {
            _state.value = PermissionState.Explanation
        }
    }

    fun checkPermissions(): Boolean = permissions.all {
        ContextCompat.checkSelfPermission(getApplication(), it) == PackageManager.PERMISSION_GRANTED
    }

    fun onAcceptExplanation(requestPermissionsLauncher: ActivityResultLauncher<Array<String>>) {
        _state.value = PermissionState.Request
        requestPermissionsLauncher.launch(permissions)
    }

    fun onDeclineExplanation() {
        _state.value = PermissionState.Warning
    }

    fun onPermissionsResult(granted: Boolean) {
        _state.value = if (granted) PermissionState.Granted else PermissionState.Warning
    }

    fun onRetry() {
        _state.value = PermissionState.Explanation
    }
}
