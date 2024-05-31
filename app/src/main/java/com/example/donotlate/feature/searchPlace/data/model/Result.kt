package com.example.donotlate.feature.searchPlace.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Result(

    @SerializedName("businessStatus")
    //영업상태
    val business_status: String,
    //위치와 위체에 대한 viewPort
    val geometry: Geometry,
    //지도에 표시될 아이콘(URL 포험)
    val icon: String,

    @SerializedName("iconBackgroundColor")
    //장소 카테고리의 색깔 코드
    val icon_background_color: String,

    @SerializedName("iconMaskBaseUri")
    //svg,png를 뺀 권장 아이콘
    val icon_mask_base_uri: String,
    //싱호명
    val name: String,

    @SerializedName("openingHours")
    //오픈시간
    val opening_hours: OpeningHours,
    //참조에 대한 사진 최대 10장
    val photos: List<Photo>,

    @SerializedName("placeId")
    //장소 고유 식별자
    val place_id: String,

    @SerializedName("plusCode")
    //위도, 경도로 위치 참조
    val plus_code: PlusCode,

    @SerializedName("priceLevel")
    //가격 수준 0 = 무료
    val price_level: Int,
    //사용자 리뷰
    val rating: Double,
    //지원끊김
    val reference: String,
    //지원끊김
    val scope: String,
    //장소 유형(카테고리) > bakery, bar, cafe, gym, zoo, university, shopping_mall, restaurant, park, museum, movie_theater
    val types: List<String>,

    @SerializedName("userRatingsTotal")
    //리뷰 수
    val user_ratings_total: Int,
    //거리 이름, 거리 번호, 지역을 포함하여 장소에 대한 단순화된 주소
    val vicinity: String
): Parcelable