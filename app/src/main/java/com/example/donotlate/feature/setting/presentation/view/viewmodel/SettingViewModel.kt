package com.example.donotlate.feature.setting.presentation.view.viewmodel

import androidx.lifecycle.ViewModel
import com.example.donotlate.feature.setting.presentation.view.model.LicenseModel

class SettingViewModel : ViewModel() {


    var date: String = ""

    fun showDate(data: String) {
        date = data
    }

    fun licenseList(): List<LicenseModel> {
        return listOf(
            LicenseModel(
                "Pager Dots Indicator", "https://github.com/tommybuonomo/dotsindicator"
            ),
            LicenseModel(
                "Android Developer ViewPager2", "https://developer.android.com/reference/androidx/viewpager2/widget/ViewPager2"
            ),
            LicenseModel(
                "Android Developer MVVM", "https://developer.android.com/topic/architecture?hl=ko"
            ),
            LicenseModel(
                "Android Developer Clean Architecture", "https://developer.android.com/topic/architecture?hl=ko"
            ),
            LicenseModel(
                "Google Places API", "https://developers.google.com/maps/documentation/places/web-service?hl=ko"
            ),
            LicenseModel(
                "Google Directions API", "https://developers.google.com/maps/documentation/directions/get-directions?hl=ko"
            ),
            )
    }

}
