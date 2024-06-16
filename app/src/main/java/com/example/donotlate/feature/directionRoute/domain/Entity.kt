package com.example.donotlate.feature.directionRoute.domain

data class DirectionsEntity(
    val routes: List<DirectionsRouteEntity>,
    val directionsStatus: String,
    val availableTravelModes: List<String>,
    val geocodedWaypoints: List<DirectionsGeocodedWaypointEntity>
)

data class DirectionsGeocodedWaypointEntity(
    val geocoderStatus: String,
    val partialMatch: Boolean,
    val placeId: String,
    val types: List<String>
)

data class DirectionsRouteEntity(
    val bounds: BoundsEntity,
    val copyrights: String,
    val legs: List<DirectionsLegEntity>,
    val overviewPolyline: DirectionsPolylineEntity,
    val summary: String,
    val warnings: List<String>,
    val waypointOrder: List<Int>,
    val fare: FareEntity
)

data class BoundsEntity(
    val northeast: LatLngEntity,
    val southwest: LatLngEntity
)

data class LatLngEntity(
    val lat: Double,
    val lng: Double
)

data class DirectionsLegEntity(
    val totalEndAddress: String,
    val totalEndLocation: LatLngEntity,
    val totalStartAddress: String,
    val totalStartLocation: LatLngEntity,
    val steps: List<DirectionsStepEntity>,
    val trafficSpeedEntry: List<DirectionsTrafficSpeedEntryEntity>,
    val viaWaypoint: List<DirectionsViaWaypointEntity>,
    val totalArrivalTime: TimeZoneTextValueObjectEntity,
    val totalDepartureTime: TimeZoneTextValueObjectEntity,
    val totalDistance: TextValueObjectEntity,
    val totalDuration: TextValueObjectEntity,
    val durationInTraffic: TextValueObjectEntity
)

data class DirectionsStepEntity(
    val stepDuration: TextValueObjectEntity,
    val endLocation: LatLngEntity,
    val htmlInstructions: String,
    val polyline: DirectionsPolylineEntity,
    val startLocation: LatLngEntity,
    val travelMode: String,
    val distance: TextValueObjectEntity,
    val stepInSteps: List<DirectionsStepEntity>,
    val transitDetails: DirectionsTransitDetailsEntity
)

data class DirectionsTransitDetailsEntity(
    val arrivalStop: DirectionsTransitStopEntity,
    val arrivalTime: TimeZoneTextValueObjectEntity,
    val departureStop: DirectionsTransitStopEntity,
    val departureTime: TimeZoneTextValueObjectEntity,
    val headSign: String,
    val headWay: Int,
    val line: DirectionsTransitLineEntity,
    val numStops: Int,
    val tripShortName: String
)

data class DirectionsPolylineEntity(
    val points: String
)

data class DirectionsTransitStopEntity(
    val location: LatLngEntity,
    val name: String
)

data class DirectionsTransitLineEntity(
    val agencies: List<DirectionsTransitAgencyEntity>,
    val name: String,
    val color: String,
    val icon: String,
    val shortName: String,
    val textColor: String,
    val url: String,
    val vehicle: DirectionsTransitVehicleEntity
)

data class DirectionsTransitAgencyEntity(
    val name: String,
    val phone: String,
    val url: String
)

data class DirectionsTransitVehicleEntity(
    val name: String,
    val type: String,
    val icon: String,
    val localIcon: String
)

data class DirectionsTrafficSpeedEntryEntity(
    val offsetMeters: Double,
    val speedCategory: String
)

data class DirectionsViaWaypointEntity(
    val location: LatLngEntity,
    val stepIndex: Int,
    val stepInterpolation: Number
)

data class TimeZoneTextValueObjectEntity(
    val text: String,
    val timeZone: String,
    val value: Double
)

data class TextValueObjectEntity(
    val text: String,
    val value: Double
)

data class FareEntity(
    val currency: String,
    val text: String,
    val value: Double
)
