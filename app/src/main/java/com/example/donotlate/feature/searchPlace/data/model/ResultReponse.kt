package com.example.donotlate.feature.searchPlace.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResultReponse(

    @SerializedName("business_status")
    //영업상태
    val businessStatus: String,

    //지도에 표시될 아이콘(URL 포험)
    val icon: String,

    @SerializedName("icon_background_color")
    //장소 카테고리의 색깔 코드
    val iconBackgroundColor: String,

    @SerializedName("icon_mask_base_uri")
    //svg,png를 뺀 권장 아이콘
    val iconMaskBaseUri: String,

    //싱호명
    val name: String,

    @SerializedName("opening_hours")
    //오픈시간
    val openingHours: OpeningHours,

    //참조에 대한 사진 최대 10장
    val photos: List<Photo>,

    @SerializedName("place_id")
    //장소 고유 식별자
    val placeId: String,

    @SerializedName("price_level")
    //가격 수준 0 = 무료
    val priceLevel: Int,

    //사용자 리뷰
    val rating: Double,

    //장소 유형(카테고리) > bakery, bar, cafe, gym, zoo, university, shopping_mall, restaurant, park, museum, movie_theater
    val types: List<String>,

    @SerializedName("user_ratings_total")
    //리뷰 수
    val userRatingsTotal: Int,

    ): Parcelable