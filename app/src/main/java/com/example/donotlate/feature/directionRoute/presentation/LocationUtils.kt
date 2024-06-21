package com.example.donotlate.feature.directionRoute.presentation

import android.content.Context
import android.location.Geocoder
import java.util.Locale

class LocationUtils {
    fun getCountryFromLatLng(context: Context, lat: Double, lng: Double): String? {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(lat, lng, 1)
        return if (addresses?.isNotEmpty() == true) {
            val address = addresses[0]
            address.countryName
        } else {
            null
        }
    }
}