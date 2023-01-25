package com.renault.parkassist.repository.surroundview

import alliance.car.surroundview.SurroundView
import alliance.car.surroundview.SurroundViewCapabilities
import com.renault.parkassist.utility.map
import com.renault.parkassist.utility.unMap
import com.renault.parkassist.viewmodel.avm.Finger

class SurroundMapper {

    private val viewMap = listOf(
        SurroundView.REAR_VIEW to View.REAR_VIEW,
        SurroundView.FRONT_VIEW to View.FRONT_VIEW,
        SurroundView.PANORAMIC_REAR_VIEW to View.PANORAMIC_REAR_VIEW,
        SurroundView.PANORAMIC_FRONT_VIEW to View.PANORAMIC_FRONT_VIEW,
        SurroundView.SIDES_VIEW to View.SIDES_VIEW,
        SurroundView.THREE_DIMENSION_VIEW to View.THREE_DIMENSION_VIEW,
        SurroundView.NO_DISPLAY to View.NO_DISPLAY,
        SurroundView.POP_UP_VIEW to View.POP_UP_VIEW,
        SurroundView.TRAILER_VIEW to View.TRAILER_VIEW,
        SurroundView.SETTINGS_REAR_VIEW to View.SETTINGS_REAR_VIEW,
        SurroundView.SETTINGS_FRONT_VIEW to View.SETTINGS_FRONT_VIEW,
        SurroundView.APA_FRONT_VIEW to View.APA_FRONT_VIEW,
        SurroundView.APA_REAR_VIEW to View.APA_REAR_VIEW,
        SurroundView.DEALER_VIEW to View.DEALER_VIEW,
        SurroundView.AUTO_ZOOM_REAR_VIEW to View.AUTO_ZOOM_REAR_VIEW
    )

    private val trailerPresenceMap = listOf(
        SurroundView.TRAILER_PRESENCE_DETECTED to TrailerPresence.TRAILER_PRESENCE_DETECTED,
        SurroundView.TRAILER_PRESENCE_NOT_DETECTED to TrailerPresence.TRAILER_PRESENCE_NOT_DETECTED,
        SurroundView.TRAILER_PRESENCE_UNAVAILABLE to TrailerPresence.TRAILER_PRESENCE_UNAVAILABLE
    )

    private val actionMap = listOf(
        SurroundView.CLOSE_VIEW to Action.CLOSE_VIEW,
        SurroundView.ACTIVATE_MANEUVER_VIEW to Action.ACTIVATE_MANEUVER_VIEW,
        SurroundView.ACTIVATE_SETTINGS_VIEW to Action.ACTIVATE_SETTINGS_VIEW,
        SurroundView.ACTIVATE_TRAILER_VIEW to Action.ACTIVATE_TRAILER_VIEW,
        SurroundView.SELECT_PANORAMIC_VIEW to Action.SELECT_PANORAMIC_VIEW,
        SurroundView.SELECT_STANDARD_VIEW to Action.SELECT_STANDARD_VIEW,
        SurroundView.SELECT_SIDES_VIEW to Action.SELECT_SIDES_VIEW,
        SurroundView.SELECT_THREE_DIMENSION_VIEW to Action.SELECT_THREE_DIMENSION_VIEW,
        SurroundView.SELECT_FRONT_CAMERA to Action.SELECT_FRONT_CAMERA,
        SurroundView.SELECT_REAR_CAMERA to Action.SELECT_REAR_CAMERA,
        SurroundView.BACK_FROM_SETTINGS_VIEW to Action.BACK_FROM_SETTINGS_VIEW
    )

    private val trunkStateMap = listOf(
        SurroundView.TRUNK_DOOR_STATE_UNAVAILABLE to TrunkState.TRUNK_DOOR_STATE_UNAVAILABLE,
        SurroundView.TRUNK_DOOR_CLOSED to TrunkState.TRUNK_DOOR_CLOSED,
        SurroundView.TRUNK_DOOR_OPENED to TrunkState.TRUNK_DOOR_OPENED
    )

    private val warningMap = listOf(
        SurroundView.WARNING_STATE_NONE
            to WarningState.WARNING_STATE_NONE,
        SurroundView.WARNING_STATE_SPEED_NOK
            to WarningState.WARNING_STATE_SPEED_NOK,
        SurroundView.WARNING_STATE_CAMERA_MISALIGNED
            to WarningState.WARNING_STATE_CAMERA_MISALIGNED,
        SurroundView.WARNING_STATE_CAMERA_SOILED
            to WarningState.WARNING_STATE_CAMERA_SOILED,
        SurroundView.WARNING_STATE_TRAILER_NOT_DETECTED
            to WarningState.WARNING_STATE_TRAILER_NOT_DETECTED,
        SurroundView.WARNING_STATE_OBSTACLE_PRESENT
            to WarningState.WARNING_STATE_OBSTACLE_PRESENT,
        SurroundView.WARNING_STATE_TRAILER_ACCESS_LIMITED
            to WarningState.WARNING_STATE_TRAILER_ACCESS_LIMITED
    )

    private val ackMap = listOf(
        SurroundView.ACK_OK to UserAcknowledgement.ACK_OK,
        SurroundView.ACK_CANCEL to UserAcknowledgement.ACK_CANCEL
    )

    private val errorStateMap = listOf(
        SurroundView.ERROR_STATE_NO_ERROR to ErrorState.ERROR_STATE_NO_ERROR,
        SurroundView.ERROR_STATE_CAMERA_FAILURE to ErrorState.ERROR_STATE_CAMERA_FAILURE
    )

    private val originMap = listOf(
        SurroundView.REQUEST_FROM_VEHICLE to Origin.REQUEST_FROM_VEHICLE,
        SurroundView.REQUEST_FROM_CLIENT to Origin.REQUEST_FROM_CLIENT,
        SurroundView.REQUEST_NONE to Origin.NO_REQUEST
    )

    private val fingerMap = listOf(
        SurroundView.FINGER_TOUCH_FIRST to Finger.FIRST,
        SurroundView.FINGER_TOUCH_SECOND to Finger.SECOND
    )

    private val configMap = listOf(
        SurroundViewCapabilities.FEATURE_AROUND_VIEW_MONITORING to FeatureConfig.AVM,
        SurroundViewCapabilities.FEATURE_REAR_VIEW_CAMERA to FeatureConfig.RVC,
        SurroundViewCapabilities.FEATURE_NOT_SUPPORTED to FeatureConfig.NONE
    )

    private val maneuverAvailabilityMap = listOf(
        SurroundView.AVAILABILITY_STATE_NOT_READY to ManeuverAvailability.NOT_READY,
        SurroundView.AVAILABILITY_STATE_READY to ManeuverAvailability.READY,
        SurroundView.AVAILABILITY_STATE_RESTRICTED to ManeuverAvailability.RESTRICTED
    )

    fun mapSurroundState(state: Int, isRequest: Boolean) =
        SurroundState(
            mapView(state),
            isRequest
        )

    @View
    fun mapView(input: Int): Int = viewMap.map(input)

    fun unmapView(@View input: Int): Int = viewMap.unMap(input)

    @TrailerPresence
    fun mapTrailerPresence(input: Int): Int = trailerPresenceMap.map(input)

    @Action
    fun mapAction(input: Int): Int = actionMap.map(input)

    fun unmapAction(@Action input: Int): Int = actionMap.unMap(input)

    @TrunkState
    fun mapTrunkState(input: Int): Int = trunkStateMap.map(input)

    fun unmapFinger(@Finger input: Int): Int = fingerMap.unMap(input)

    @FeatureConfig
    fun mapConfig(input: Int): Int = configMap.map(input)

    @ManeuverAvailability
    fun mapManeuverAvailability(input: Int): Int = maneuverAvailabilityMap.map(input)

    @WarningState
    fun mapWarningState(input: Int): Int = warningMap.map(input)

    @ErrorState
    fun mapErrorState(input: Int): Int = errorStateMap.map(input)

    fun unmapAck(@UserAcknowledgement input: Int): Int = ackMap.unMap(input)
}