package com.letianpai.robot.wificonnet.manager

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.letianpai.robot.wificonnet.system.SystemUtil

/**
 * A class that scans for available Wi-Fi networks using the Android WifiManager.
 *
 * @param context The application context used to access system services.
 */
class WifiScanner(private val context: Context) {

    // Lazily initialize the WifiManager when needed
    private val wifiManager: WifiManager by lazy {
        context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }


    val availableWifiList: List<ScanResult>
        @SuppressLint("MissingPermission")
        get() {
            if (SystemUtil.wifiPermissionsGranted){
                if (!wifiManager.isWifiEnabled) {
                    wifiManager.setWifiEnabled(true)
                }
            }
            Log.e("Connected", "El wifi esta habilitado ${wifiManager.isWifiEnabled}")
            return wifiManager.scanResults
        }

    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Checks if location services are enabled on the device.
     *
     * @return True if location services are enabled, false otherwise.
     */
    private fun isLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
}
