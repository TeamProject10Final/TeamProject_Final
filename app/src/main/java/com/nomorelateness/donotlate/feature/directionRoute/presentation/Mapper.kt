package com.nomorelateness.donotlate.feature.directionRoute.presentation

import com.nomorelateness.donotlate.feature.directionRoute.domain.BoundsEntity
import com.nomorelateness.donotlate.feature.directionRoute.domain.DirectionsEntity
import com.nomorelateness.donotlate.feature.directionRoute.domain.DirectionsGeocodedWaypointEntity
import com.nomorelateness.donotlate.feature.directionRoute.domain.DirectionsLegEntity
import com.nomorelateness.donotlate.feature.directionRoute.domain.DirectionsPolylineEntity
import com.nomorelateness.donotlate.feature.directionRoute.domain.DirectionsRouteEntity
import com.nomorelateness.donotlate.feature.directionRoute.domain.DirectionsStepEntity
import com.nomorelateness.donotlate.feature.directionRoute.domain.DirectionsTrafficSpeedEntryEntity
import com.nomorelateness.donotlate.feature.directionRoute.domain.DirectionsTransitAgencyEntity
import com.nomorelateness.donotlate.feature.directionRoute.domain.DirectionsTransitDetailsEntity
import com.nomorelateness.donotlate.feature.directionRoute.domain.DirectionsTransitLineEntity
import com.nomorelateness.donotlate.feature.directionRoute.domain.DirectionsTransitStopEntity
import com.nomorelateness.donotlate.feature.directionRoute.domain.DirectionsTransitVehicleEntity
import com.nomorelateness.donotlate.feature.directionRoute.domain.DirectionsViaWaypointEntity
import com.nomorelateness.donotlate.feature.directionRoute.domain.FareEntity
import com.nomorelateness.donotlate.feature.directionRoute.domain.LatLngEntity
import com.nomorelateness.donotlate.feature.directionRoute.domain.TextValueObjectEntity
import com.nomorelateness.donotlate.feature.directionRoute.domain.TimeZoneTextValueObjectEntity

fun DirectionsEntity.toModel() = DirectionsModel(
    routes = routes.map {
        it.toModel()
    }.orEmpty(),
    directionsStatus = directionsStatus ?: "",
    availableTravelModes = availableTravelModes.orEmpty(),
    geocodedWaypoints = geocodedWaypoints.map {
        it.toModel()
    }.orEmpty()
)

fun DirectionsGeocodedWaypointEntity.toModel() = DirectionsGeocodedWaypointModel(
    geocoderStatus = geocoderStatus ?: "",
    partialMatch = partialMatch ?: false,
    placeId = placeId ?: "",
    types = types.orEmpty()
)

fun DirectionsRouteEntity.toModel() = DirectionsRouteModel(
    bounds = bounds.toModel() ?: BoundsModel(
        LatLngModel(0.0, 0.0), LatLngModel(0.0, 0.0)
    ),
    copyrights = copyrights ?: "",
    legs = legs.map {
        it.toModel()
    }.orEmpty(),
    overviewPolyline = overviewPolyline.toModel() ?: DirectionsPolylineModel(points = ""),
    summary = summary ?: "",
    warnings = warnings ?: emptyList(),
    waypointOrder = waypointOrder ?: emptyList(),
    fare = fare.toModel() ?: FareModel(currency = "", text = "", value = 0.0)

)

fun BoundsEntity.toModel() = BoundsModel(
    northeast = northeast.toModel() ?: LatLngModel(0.0, 0.0),
    southwest = southwest.toModel() ?: LatLngModel(0.0, 0.0)
)

fun LatLngEntity.toModel() = LatLngModel(
    lat = lat ?: 0.0,
    lng = lng ?: 0.0
)

fun DirectionsLegEntity.toModel() = DirectionsLegModel(
    totalEndAddress = totalEndAddress ?: "",
    totalEndLocation = totalEndLocation.toModel(),
    totalStartAddress = totalStartAddress ?: "",
    totalStartLocation = totalStartLocation.toModel(),
    steps = steps.map {
        it.toModel()
    }.orEmpty(),
    trafficSpeedEntry = trafficSpeedEntry.map {
        it.toModel()
    }.orEmpty(),
    viaWaypoint = viaWaypoint.map {
        it.toModel()
    }.orEmpty(),
    totalArrivalTime = totalArrivalTime.toModel() ?: TimeZoneTextValueObjectModel(
        text = "",
        timeZone = "",
        value = 0.0
    ),
    totalDepartureTime = totalDepartureTime.toModel() ?: TimeZoneTextValueObjectModel(
        text = "",
        timeZone = "",
        value = 0.0
    ),
    totalDistance = totalDistance.toModel() ?: TextValueObjectModel(text = "", value = 0.0),
    totalDuration = totalDuration.toModel() ?: TextValueObjectModel(text = "", value = 0.0),
    durationInTraffic = durationInTraffic.toModel() ?: TextValueObjectModel(
        text = "",
        value = 0.0
    )
)


fun DirectionsStepEntity.toModel(): DirectionsStepModel {
    return DirectionsStepModel(
        stepDuration = stepDuration.toModel() ?: TextValueObjectModel(text = "", value = 0.0),
        endLocation = endLocation.toModel() ?: LatLngModel(0.0, 0.0),
        htmlInstructions = htmlInstructions ?: "",
        polyline = polyline.toModel() ?: DirectionsPolylineModel(points = ""),
        startLocation = startLocation.toModel() ?: LatLngModel(0.0, 0.0),
        travelMode = travelMode ?: "",
        distance = distance.toModel() ?: TextValueObjectModel(text = "", value = 0.0),
        stepInSteps = stepInSteps.map { it.toModel() } ?: emptyList(),
        transitDetails = transitDetails.toModel() ?: DirectionsTransitDetailsModel(
            arrivalStop = DirectionsTransitStopModel(
                location = LatLngModel(0.0, 0.0),
                name = ""
            ),
            arrivalTime = TimeZoneTextValueObjectModel(text = "", timeZone = "", value = 0.0),
            departureStop = DirectionsTransitStopModel(
                location = LatLngModel(0.0, 0.0),
                name = ""
            ),
            departureTime = TimeZoneTextValueObjectModel(text = "", timeZone = "", value = 0.0),
            headSign = "",
            headWay = 0,
            line = DirectionsTransitLineModel(
                agencies = emptyList(),
                name = "",
                color = "",
                icon = "",
                shortName = "",
                textColor = "",
                url = "",
                vehicle = DirectionsTransitVehicleModel(
                    name = "",
                    type = "",
                    icon = "",
                    localIcon = ""
                )
            ),
            numStops = 0,
            tripShortName = ""
        )
    )
}

fun DirectionsTransitDetailsEntity.toModel() = DirectionsTransitDetailsModel(
    arrivalStop = arrivalStop.toModel() ?: DirectionsTransitStopModel(
        location = LatLngModel(0.0, 0.0),
        name = ""
    ),
    arrivalTime = arrivalTime.toModel() ?: TimeZoneTextValueObjectModel(
        text = "",
        timeZone = "",
        value = 0.0
    ),
    departureStop = departureStop.toModel() ?: DirectionsTransitStopModel(
        location = LatLngModel(0.0, 0.0),
        name = ""
    ),
    departureTime = departureTime.toModel() ?: TimeZoneTextValueObjectModel(
        text = "",
        timeZone = "",
        value = 0.0
    ),
    headSign = headSign ?: "",
    headWay = headWay ?: 0,
    line = line.toModel() ?: DirectionsTransitLineModel(
        agencies = emptyList(),
        color = "",
        icon = "",
        name = "",
        shortName = "",
        textColor = "",
        url = "",
        vehicle = DirectionsTransitVehicleModel(name = "", type = "", icon = "", localIcon = "")
    ),
    numStops = numStops ?: 0,
    tripShortName = tripShortName ?: ""
)

fun DirectionsPolylineEntity.toModel() = DirectionsPolylineModel(
    points
)

fun DirectionsTransitStopEntity.toModel() = DirectionsTransitStopModel(
    location = location.toModel(), name
)

fun DirectionsTransitLineEntity.toModel() = DirectionsTransitLineModel(
    agencies = agencies.map { it.toModel() } ?: emptyList(),
    name,
    color,
    icon,
    shortName,
    textColor,
    url,
    vehicle = vehicle.toModel() ?: DirectionsTransitVehicleModel(
        name = "", type = "", icon = "", localIcon = ""
    )

)

fun DirectionsTransitAgencyEntity.toModel() = DirectionsTransitAgencyModel(
    name, phone, url
)

fun DirectionsTransitVehicleEntity.toModel() = DirectionsTransitVehicleModel(
    name, type, icon, localIcon
)

fun DirectionsTrafficSpeedEntryEntity.toModel() = DirectionsTrafficSpeedEntryModel(
    offsetMeters, speedCategory
)

fun DirectionsViaWaypointEntity.toModel() = DirectionsViaWaypointModel(
    location = location.toModel(), stepIndex, stepInterpolation
)

fun TimeZoneTextValueObjectEntity.toModel() = TimeZoneTextValueObjectModel(
    text, timeZone, value
)

fun TextValueObjectEntity.toModel() = TextValueObjectModel(
    text, value
)

fun FareEntity.toModel() = FareModel(
    currency, text, value
)

