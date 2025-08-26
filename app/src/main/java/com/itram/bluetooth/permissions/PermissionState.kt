package com.itram.bluetooth.permissions

sealed class PermissionState {
    object Explanation : PermissionState()
    object Request : PermissionState()
    object Warning : PermissionState()
    object Granted : PermissionState()
}

