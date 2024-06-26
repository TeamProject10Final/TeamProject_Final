package com.nomorelateness.donotlate.core.util

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

fun Timestamp.toFormattedString(): String {
    val sdf = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
    val date = this.toDate()
    return sdf.format(date)
}

fun Timestamp.toFormattedHMString(): String {
    val sdf = SimpleDateFormat("a hh:mm", Locale.KOREAN)
    val hours = this.toDate()
    return sdf.format(hours)
}

fun parseTime(timeStr: String): LocalTime? {
    return try {
        val trimmedTimeStr = timeStr.trim()
        // 오전 또는 오후를 분리하여 amPm에 저장
        val amPm = trimmedTimeStr.substring(0, 2) // "오전" 또는 "오후"
        // 나머지 시간 부분을 분리하여 time에 저장
        val time = trimmedTimeStr.substring(3).trim() // "01 : 00"
        // 시간 문자열을 파싱하기 위한 포맷터 정의
        val formatter = DateTimeFormatter.ofPattern("HH : mm")
        // time 문자열을 LocalTime 객체로 변환
        var localTime = LocalTime.parse(time, formatter)

        // 오전/오후를 처리하여 24시간 형식으로 변환
        // 오후 상태이며, 시간이 12가 아니면 12시간을 더한다.
        if (amPm == "오후" || amPm == "PM" && localTime.hour != 12) {
            localTime = localTime.plusHours(12)
        }
        // 오전 상태이며, 12이면 시간을 0으로 설정(자정을 나타냄)
        else if (amPm == "오전" || amPm == "AM" && localTime.hour == 12) {
            localTime = localTime.minusHours(12)
        }

        localTime
    } catch (e: DateTimeParseException) {
        e.printStackTrace()
        null
    }
}