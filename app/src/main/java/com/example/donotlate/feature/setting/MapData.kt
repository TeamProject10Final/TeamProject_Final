package com.example.donotlate.feature.setting

data class MapData(
    val routes: List<Routes>
)


data class Routes(
    val legs: List<Legs>
)

data class Legs(
    var distance: Distance,
    var duration: Duration,
    var end_address: String,
    var start_address: String,
    var end_location: Location,
    var start_location: Location,
    var steps: List<Steps>
)

data class Steps(
    var distance: Distance,
    var duration: Duration,
    var end_address: String,
    var start_address: String,
    var end_location: Location,
    var start_location: Location,
    var polyline: PolyLine,
    var travel_mode: String,
    var maneuver: String
)

data class Duration (
    val text:String,
    val value:Double
)


data class Distance (
    val text:String,
    val value:Double
)


data class PolyLine (
    val points:String
)
data class Location (
    var lat :String,
    var lng :String
)

