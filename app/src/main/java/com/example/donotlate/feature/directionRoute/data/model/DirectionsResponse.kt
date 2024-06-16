package com.example.donotlate.feature.directionRoute.data.model

import com.google.gson.annotations.SerializedName

data class DirectionsResponse(
    @SerializedName("routes") val routes: List<DirectionsRoute>?,
    @SerializedName("status") val directionsStatus: String?,
    @SerializedName("available_travel_modes") val availableTravelModes: List<String>?,
    @SerializedName("geocoded_waypoints") val geocodedWaypoints: List<DirectionsGeocodedWaypoint>?
)

data class DirectionsGeocodedWaypoint(
    @SerializedName("geocoder_status") val geocoderStatus: String?,
    @SerializedName("partial_match") val partialMatch: Boolean?,
    @SerializedName("place_id") val placeId: String?,
    @SerializedName("types") val types: List<String>?
)

data class DirectionsRoute(
    @SerializedName("bounds") val bounds: Bounds?,
    @SerializedName("copyrights") val copyrights: String?,
    @SerializedName("legs") val legs: List<DirectionsLeg>?,
    @SerializedName("overview_polyline") val overviewPolyline: DirectionsPolyline?,
    @SerializedName("summary") val summary: String?,
    @SerializedName("warnings") val warnings: List<String>?,
    @SerializedName("waypoint_order") val waypointOrder: List<Int>?,
    @SerializedName("fare") val fare: Fare?

)

data class Bounds(
    @SerializedName("northeast") val northeast: LatLngLiteral?,
    @SerializedName("southwest") val southwest: LatLngLiteral?
)

data class LatLngLiteral(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double

)

data class DirectionsLeg(
    @SerializedName("end_address") val totalEndAddress: String?,
    @SerializedName("end_location") val totalEndLocation: LatLngLiteral?,
    @SerializedName("start_address") val totalStartAddress: String?,
    @SerializedName("start_location") val totalStartLocation: LatLngLiteral?,
    @SerializedName("steps") val steps: List<DirectionsStep>?,
    @SerializedName("traffic_speed_entry") val trafficSpeedEntry: List<DirectionsTrafficSpeedEntry>?,
    @SerializedName("via_waypoint") val viaWaypoint: List<DirectionsViaWaypoint>?,
    @SerializedName("arrival_time") val totalArrivalTime: TimeZoneTextValueObject?,
    @SerializedName("departure_time") val totalDepartureTime: TimeZoneTextValueObject?,
    @SerializedName("distance") val totalDistance: TextValueObject?,
    @SerializedName("duration") val totalDuration: TextValueObject?,
    @SerializedName("duration_in_traffic") val durationInTraffic: TextValueObject?
)

data class DirectionsStep(
    @SerializedName("duration") val stepDuration: TextValueObject?,
    @SerializedName("end_location") val endLocation:LatLngLiteral?,
    @SerializedName("html_instructions") val htmlInstructions: String?,
    @SerializedName("polyline") val polyline: DirectionsPolyline?,
    @SerializedName("start_location") val startLocation: LatLngLiteral?,
    @SerializedName("travel_mode") val travelMode: String?,
    @SerializedName("distance") val distance: TextValueObject?,
    @SerializedName("steps") val stepInSteps: List<DirectionsStep>?,//
    @SerializedName("transit_details") val transitDetails: DirectionsTransitDetails?
)


data class DirectionsTransitDetails(
    @SerializedName("arrival_stop") val arrivalStop: DirectionsTransitStop?,
    @SerializedName("arrival_time") val arrivalTime: TimeZoneTextValueObject?,
    @SerializedName("departure_stop") val departureStop: DirectionsTransitStop?,
    @SerializedName("departure_time") val departureTime: TimeZoneTextValueObject?,
    @SerializedName("headsign") val headSign: String?,
    @SerializedName("headway") val headWay: Int?,
    @SerializedName("line") val line: DirectionsTransitLine?,
    @SerializedName("num_stops") val numStops: Int?,
    @SerializedName("trip_short_name") val tripShortName: String?
)

data class DirectionsPolyline(
    @SerializedName("points") val points: String?
)

data class DirectionsTransitStop(
    @SerializedName("location") val location: LatLngLiteral?,
    @SerializedName("name") val name: String?
)

data class DirectionsTransitLine(
    @SerializedName("agencies") val agencies: List<DirectionsTransitAgency>?,
    @SerializedName("name") val name: String?,
    @SerializedName("color") val color: String?,
    @SerializedName("icon") val icon: String?,
    @SerializedName("short_name") val shortName: String?,
    @SerializedName("text_color") val textColor: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("vehicle") val vehicle: DirectionsTransitVehicle?
)

data class DirectionsTransitAgency(
    @SerializedName("name") val name: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("url") val url: String?
)

data class DirectionsTransitVehicle(
    @SerializedName("name") val name: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("icon") val icon: String?,
    @SerializedName("local_icon") val localIcon: String?
)

data class DirectionsTrafficSpeedEntry(
    @SerializedName("offset_meters") val offsetMeters: Double?,
    @SerializedName("speed_category") val speedCategory: String?
)

data class DirectionsViaWaypoint(
    @SerializedName("location") val location: LatLngLiteral?,
    @SerializedName("step_index") val stepIndex: Int?,
    @SerializedName("step_interpolation") val stepInterpolation: Double?
)
data class TimeZoneTextValueObject(
    @SerializedName("text") val text: String?,
    @SerializedName("time_zone") val timeZone: String?,
    @SerializedName("value") val value: Double?
)

data class TextValueObject(
    @SerializedName("text") val text: String?,
    @SerializedName("value") val value: Double?
)

data class Fare(
    @SerializedName("currency") val currency: String?,
    @SerializedName("text") val text: String?,
    @SerializedName("value") val value: Double?
)

