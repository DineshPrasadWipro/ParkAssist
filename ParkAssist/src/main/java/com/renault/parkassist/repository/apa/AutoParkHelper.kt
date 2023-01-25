package com.renault.parkassist.repository.apa

import alliance.car.autopark.AutoPark
import alliance.car.autopark.AutoPark.*

/**
 * Mirorring https://gitlabee.dt.renault.com/swlabs/mmc/aasp/media/autopark/-/blob/master/lib/src/alliance/car/autopark/internal/Helper.java
 */
object AutoParkHelper {
    /**
     * Get string description for AutoPark display state
     * @param displayState the object to be described
     * @return a string description
     */
    fun displayStateToString(@AutoPark.DisplayState displayState: Int): String {
        return when (displayState) {
            DISPLAY_NONE -> "DISPLAY_NONE"
            DISPLAY_SCANNING -> "DISPLAY_SCANNING"
            DISPLAY_PARKOUT_CONFIRMATION -> "DISPLAY_PARKOUT_CONFIRMATION"
            DISPLAY_GUIDANCE -> "DISPLAY_GUIDANCE"
            else -> throw NoSuchElementException("Unknown DisplayState: $displayState")
        }
    }

    /**
     * Get string description for AutoPark scan info
     * @param scanInfo the object to be described
     * @returna a string description
     */
    fun scanInfoToString(@ScanInfo scanInfo: Int): String {
        return when (scanInfo) {
            SCAN_INFO_NONE -> "SCAN_INFO_NONE"
            SCAN_INFO_SCAN -> "SCAN_INFO_SCAN"
            SCAN_INFO_SLOT_LEFT -> "SCAN_INFO_SLOT_LEFT"
            SCAN_INFO_SLOT_RIGHT -> "SCAN_INFO_SLOT_RIGHT"
            SCAN_INFO_SLOT_LEFT_RIGHT -> "SCAN_INFO_SLOT_LEFT_RIGHT"
            else -> throw NoSuchElementException("Unknown ScanInfo: $scanInfo")
        }
    }

    /**
     * Get string description for AutoPark parking slot left selected
     * @param parkingSlotLeftSelected the object to be described
     * @return a string description
     */
    fun parkingSlotLeftSelectedToString(
        @ParkingSlotLeftSelected parkingSlotLeftSelected: Int
    ): String {
        return when (parkingSlotLeftSelected) {
            SLOT_LEFT_NOT_SELECTED -> "SLOT_LEFT_NOT_SELECTED"
            SLOT_LEFT_SELECTED -> "SLOT_LEFT_SELECTED"
            else -> throw NoSuchElementException(
                "Unknown ParkingSlotLeftSelected: $parkingSlotLeftSelected"
            )
        }
    }

    /**
     * Get string description for AutoPark parking slot left suitable
     * @param parkingSlotLeftSuitable the object to be described
     * @return a string description
     */
    fun parkingSlotLeftSuitableToString(
        @ParkingSlotLeftSuitable parkingSlotLeftSuitable: Int
    ): String {
        return when (parkingSlotLeftSuitable) {
            SLOT_LEFT_NOT_SUITABLE -> "SLOT_LEFT_NOT_SUITABLE"
            SLOT_LEFT_SUITABLE -> "SLOT_LEFT_SUITABLE"
            else -> throw NoSuchElementException(
                "Unknown ParkingSlotLeftSuitable: $parkingSlotLeftSuitable"
            )
        }
    }

    /**
     * Get string description for AutoPark parking slot right selected
     * @param parkingSlotRightSelected the object to be described
     * @return a string description
     */
    fun parkingSlotRightSelectedToString(
        @ParkingSlotRightSelected parkingSlotRightSelected: Int
    ): String {
        return when (parkingSlotRightSelected) {
            SLOT_RIGHT_NOT_SELECTED -> "SLOT_RIGHT_NOT_SELECTED"
            SLOT_RIGHT_SELECTED -> "SLOT_RIGHT_SELECTED"
            else -> throw NoSuchElementException(
                "Unknown ParkingSlotRightSelected: $parkingSlotRightSelected"
            )
        }
    }

    /**
     * Get string description for AutoPark parking slot right suitable
     * @param parkingSlotRightSuitable the object to be described
     * @return a string description
     */
    fun parkingSlotRightSuitableToString(
        @ParkingSlotRightSuitable parkingSlotRightSuitable: Int
    ): String {
        return when (parkingSlotRightSuitable) {
            SLOT_RIGHT_NOT_SUITABLE -> "SLOT_RIGHT_NOT_SUITABLE"
            SLOT_RIGHT_SUITABLE -> "SLOT_RIGHT_SUITABLE"
            else -> throw NoSuchElementException(
                "Unknown ParkingSlotRightSuitable: $parkingSlotRightSuitable"
            )
        }
    }

    /**
     * Get string description for AutoPark scanning side
     * @param scanningSide the object to be described
     * @return a string description
     */
    fun scanningSideToString(@AutoPark.ScanningSide scanningSide: Int): String {
        return when (scanningSide) {
            SCANNING_SIDE_UNAVAILABLE -> "SCANNING_SIDE_UNAVAILABLE"
            SCANNING_SIDE_NONE -> "SCANNING_SIDE_NONE"
            SCANNING_SIDE_LEFT -> "SCANNING_SIDE_LEFT"
            SCANNING_SIDE_RIGHT -> "SCANNING_SIDE_RIGHT"
            else -> throw NoSuchElementException("Unknown ScanningSide: $scanningSide")
        }
    }

    /**
     * Get string description for AutoPark status display
     * @param statusDisplay the object to be described
     * @return a string description
     */
    fun statusDisplayToString(@StatusDisplay statusDisplay: Int): String {
        return when (statusDisplay) {
            AUTOMATIC_MANEUVER_OFF -> "AUTOMATIC_MANEUVER_OFF"
            AUTOMATIC_MANEUVER_ON -> "AUTOMATIC_MANEUVER_ON"
            AUTOMATIC_MANEUVER_STANDBY -> "AUTOMATIC_MANEUVER_STANDBY"
            else -> throw NoSuchElementException("Unknown StatusDisplay: $statusDisplay")
        }
    }

    /**
     * Get string description for AutoPark maneuver move
     * @param maneuverMove the object to be described
     * @return a string description
     */
    fun maneuverMoveToString(@AutoPark.ManeuverMove maneuverMove: Int): String {
        return when (maneuverMove) {
            MANEUVER_MOVE_UNAVAILABLE -> "MANEUVER_MOVE_UNAVAILABLE"
            MANEUVER_MOVE_FIRST -> "MANEUVER_MOVE_FIRST"
            MANEUVER_MOVE_BACKWARD -> "MANEUVER_MOVE_BACKWARD"
            MANEUVER_MOVE_FORWARD -> "MANEUVER_MOVE_FORWARD"
            else -> throw NoSuchElementException("Unknown ManeuverMove: $maneuverMove")
        }
    }

    /**
     * Get string description for AutoPark maneuver type
     * @param maneuverType the object to be described
     * @return a string description
     */
    fun maneuverTypeToString(@AutoPark.ManeuverType maneuverType: Int): String {
        return when (maneuverType) {
            MANEUVER_TYPE_UNAVAILABLE -> "MANEUVER_TYPE_UNAVAILABLE"
            MANEUVER_TYPE_PARKIN_ANGLED -> "MANEUVER_TYPE_PARKIN_ANGLED"
            MANEUVER_TYPE_PARKIN_PARALLEL -> "MANEUVER_TYPE_PARKIN_PARALLEL"
            MANEUVER_TYPE_PARKIN_PERPENDICULAR -> "MANEUVER_TYPE_PARKIN_PERPENDICULAR"
            MANEUVER_TYPE_PARKOUT -> "MANEUVER_TYPE_PARKOUT"
            MANEUVER_TYPE_AUTO_MODE -> "MANEUVER_TYPE_AUTO_MODE"
            else -> throw NoSuchElementException("Unknown ManeuverType: $maneuverType")
        }
    }

    /**
     * Get string description for a list of AutoPark maneuver types
     * @param maneuverTypes the list object to be described
     * @return a string description
     */
    fun maneuverTypesToString(maneuverTypes: List<Int>): String {
        var desc = ""
        for (maneuverType in maneuverTypes) {
            desc += maneuverTypeToString(maneuverType) + ", "
        }
        return desc.replace("[, ]+$".toRegex(), "")
    }

    /**
     * Get string description for AutoPark maneuver selection
     * @param maneuverSelection the object to be described
     * @return a string description
     */
    fun maneuverSelectionToString(@AutoPark.ManeuverSelection maneuverSelection: Int): String {
        return when (maneuverSelection) {
            MANEUVER_SELECTION_UNAVAILABLE -> "MANEUVER_SELECTION_UNAVAILABLE"
            MANEUVER_SELECTION_NOT_SELECTED -> "MANEUVER_SELECTION_NOT_SELECTED"
            MANEUVER_SELECTION_SELECTED -> "MANEUVER_SELECTION_SELECTED"
            else -> throw NoSuchElementException("Unknown ManeuverSelection: $maneuverSelection")
        }
    }

    /**
     * Get string description for AutoPark extended description
     * @param extendedInstruction the object to be described
     * @return a string description
     */
    fun extendedInstructionToString(@ExtendedInstruction extendedInstruction: Int): String {
        return when (extendedInstruction) {
            EXT_INSTRUCTION_UNAVAILABLE ->
                "EXT_INSTRUCTION_UNAVAILABLE"
            EXT_INSTRUCTION_SELECT_SIDE ->
                "EXT_INSTRUCTION_SELECT_SIDE"
            EXT_INSTRUCTION_DRIVE_FORWARD_TO_FIND_PARKING_SLOT ->
                "EXT_INSTRUCTION_DRIVE_FORWARD_TO_FIND_PARKING_SLOT"
            EXT_INSTRUCTION_DRIVE_FORWARD_TO_FIND_PARKING_SLOT2 ->
                "EXT_INSTRUCTION_DRIVE_FORWARD_TO_FIND_PARKING_SLOT2"
            EXT_INSTRUCTION_STOP -> "EXT_INSTRUCTION_STOP"
            EXT_INSTRUCTION_ENGAGE_REAR_GEAR_OR_PRESS_START ->
                "EXT_INSTRUCTION_ENGAGE_REAR_GEAR_OR_PRESS_START"
            EXT_INSTRUCTION_ENGAGE_FORWARD_GEAR ->
                "EXT_INSTRUCTION_ENGAGE_FORWARD_GEAR"
            EXT_INSTRUCTION_PUSH_BUTTON ->
                "EXT_INSTRUCTION_PUSH_BUTTON"
            EXT_INSTRUCTION_DRIVE_FORWARD -> "EXT_INSTRUCTION_DRIVE_FORWARD"
            EXT_INSTRUCTION_DRIVE_BACKWARD -> "EXT_INSTRUCTION_DRIVE_BACKWARD"
            EXT_INSTRUCTION_FINISHED -> "EXT_INSTRUCTION_FINISHED"
            EXT_INSTRUCTION_DRIVE_FORWARD_OR_BACKWARD ->
                "EXT_INSTRUCTION_DRIVE_FORWARD_OR_BACKWARD"
            EXT_INSTRUCTION_ENGAGE_REAR_GEAR -> "EXT_INSTRUCTION_ENGAGE_REAR_GEAR"
            EXT_INSTRUCTION_STOP_AFTER_REAR_GEAR_ENGAGED ->
                "EXT_INSTRUCTION_STOP_AFTER_REAR_GEAR_ENGAGED"
            EXT_INSTRUCTION_ACCELERATE_AND_HOLD_THE_PEDAL_PRESSED ->
                "EXT_INSTRUCTION_ACCELERATE_AND_HOLD_THE_PEDAL_PRESSED"
            EXT_INSTRUCTION_MANEUVER_FINISHED_RELEASE_OR_PARK ->
                "EXT_INSTRUCTION_MANEUVER_FINISHED_RELEASE_OR_PARK"
            EXT_INSTRUCTION_MANEUVER_FINISHED_TAKE_BACK_CONTROL ->
                "EXT_INSTRUCTION_MANEUVER_FINISHED_TAKE_BACK_CONTROL"
            EXT_INSTRUCTION_HOLD_ACCELERATOR_PRESSED ->
                "EXT_INSTRUCTION_HOLD_ACCELERATOR_PRESSED"
            EXT_INSTRUCTION_STANDBY ->
                "EXT_INSTRUCTION_STANDBY"
            EXT_INSTRUCTION_RELEASE_GAS_PEDAL ->
                "EXT_INSTRUCTION_RELEASE_GAS_PEDAL"
            else ->
                throw NoSuchElementException("Unknown ExtendedInstruction: $extendedInstruction")
        }
    }

    /**
     * Get string description for AutoPark warning message
     * @param warningMessage the object to be described
     * @return a string description
     */
    fun warningMessageToString(@WarningMessage warningMessage: Int): String {
        return when (warningMessage) {
            MESSAGE_NONE -> "MESSAGE_NONE"
            MESSAGE_MANEUVER_SUSPENDED_DOOR_OPEN ->
                "MESSAGE_MANEUVER_SUSPENDED_DOOR_OPEN"
            MESSAGE_MANEUVER_SUSPENDED_HAND_ON_WHEEL ->
                "MESSAGE_MANEUVER_SUSPENDED_HAND_ON_WHEEL"
            MESSAGE_MANEUVER_SUSPENDED_UNTIL_RESTART_ENGINE ->
                "MESSAGE_MANEUVER_SUSPENDED_UNTIL_RESTART_ENGINE"
            MESSAGE_MANEUVER_SUSPENDED -> "MESSAGE_MANEUVER_SUSPENDED"
            MESSAGE_ASK_RESUME_MANEUVER -> "MESSAGE_ASK_RESUME_MANEUVER"
            MESSAGE_MANEUVER_SUSPENDED_OBSTACLE_ON_PATH ->
                "MESSAGE_MANEUVER_SUSPENDED_OBSTACLE_ON_PATH"
            MESSAGE_MANEUVER_SUSPENDED_BRAKE_TO_STOP ->
                "MESSAGE_MANEUVER_SUSPENDED_BRAKE_TO_STOP"
            MESSAGE_MANEUVER_CANCELED -> "MESSAGE_MANEUVER_CANCELED"
            MESSAGE_MANEUVER_UNAVAILABLE_HAND_ON_WHEEL ->
                "MESSAGE_MANEUVER_UNAVAILABLE_HAND_ON_WHEEL"
            MESSAGE_MANEUVER_UNAVAILABLE_DOOR_OPEN ->
                "MESSAGE_MANEUVER_UNAVAILABLE_DOOR_OPEN"
            MESSAGE_MANEUVER_UNAVAILABLE_ENGINE_STOPPED ->
                "MESSAGE_MANEUVER_UNAVAILABLE_ENGINE_STOPPED"
            MESSAGE_FEATURE_UNAVAILABLE_REAR_GEAR_ENGAGED ->
                "MESSAGE_FEATURE_UNAVAILABLE_REAR_GEAR_ENGAGED"
            MESSAGE_FEATURE_UNAVAILABLE_SPEED_TOO_HIGH ->
                "MESSAGE_FEATURE_UNAVAILABLE_SPEED_TOO_HIGH"
            MESSAGE_FEATURE_UNAVAILABLE -> "MESSAGE_FEATURE_UNAVAILABLE"
            MESSAGE_FEATURE_UNAVAILABLE_TRAILER_DETECTED ->
                "MESSAGE_FEATURE_UNAVAILABLE_TRAILER_DETECTED"
            MESSAGE_FEATURE_UNAVAILABLE_CRUISE_CONTROL_ACTIVATED ->
                "MESSAGE_FEATURE_UNAVAILABLE_CRUISE_CONTROL_ACTIVATED"
            MESSAGE_MANEUVER_CANCELED_PARKING_BRAKE -> "MESSAGE_MANEUVER_CANCELED_PARKING_BRAKE"
            MESSAGE_MANEUVER_CANCELED_DRIVER_DOOR_OPEN ->
                "MESSAGE_MANEUVER_CANCELED_DRIVER_DOOR_OPEN"
            MESSAGE_MANEUVER_CANCELED_SEAT_BELT_UNFASTEN ->
                "MESSAGE_MANEUVER_CANCELED_SEAT_BELT_UNFASTEN"
            MESSAGE_MANEUVER_UNAVAILABLE_PARKING_BRAKE ->
                "MESSAGE_MANEUVER_UNAVAILABLE_PARKING_BRAKE"
            MESSAGE_MANEUVER_CANCELED_SLOPE_TOO_HIGH -> "MESSAGE_MANEUVER_CANCELED_SLOPE_TOO_HIGH"
            MESSAGE_MANEUVER_UNAVAILABLE_SEAT_BELT_UNFASTEN ->
                "MESSAGE_MANEUVER_UNAVAILABLE_SEAT_BELT_UNFASTEN"
            MESSAGE_MANEUVER_UNAVAILABLE_SLOPE_TOO_HIGH ->
                "MESSAGE_MANEUVER_UNAVAILABLE_SLOPE_TOO_HIGH"
            MESSAGE_MANEUVER_CANCELED_BRAKING_FAILURE ->
                "MESSAGE_MANEUVER_CANCELED_BRAKING_FAILURE"
            MESSAGE_BRAKE_TO_RESUME_MANEUVER ->
                "MESSAGE_BRAKE_TO_RESUME_MANEUVER"
            MESSAGE_FEATURE_UNAVAILABLE_VEHICLE_NOT_STOPPED ->
                "MESSAGE_FEATURE_UNAVAILABLE_VEHICLE_NOT_STOPPED"
            MESSAGE_MANEUVER_CANCELED_TAKE_BACK_CONTROL ->
                "MESSAGE_MANEUVER_CANCELED_TAKE_BACK_CONTROL"
            MESSAGE_MANEUVER_SUSPENDED_RELEASE_ACCELERATOR_PEDAL ->
                "MESSAGE_MANEUVER_SUSPENDED_RELEASE_ACCELERATOR_PEDAL"
            MESSAGE_MANEUVER_SUSPENDED_GEAR_SHIFT_ACTIVATED ->
                "MESSAGE_MANEUVER_SUSPENDED_GEAR_SHIFT_ACTIVATED"
            MESSAGE_SELECT_TURN_INDICATOR -> "MESSAGE_SELECT_TURN_INDICATOR"
            MESSAGE_MANEUVER_CANCELED_RELEASE_ACCELERATOR_PEDAL ->
                "MESSAGE_MANEUVER_CANCELED_RELEASE_ACCELERATOR_PEDAL"
            MESSAGE_PRESS_OK_TO_START_MANEUVER ->
                "MESSAGE_PRESS_OK_TO_START_MANEUVER"
            MESSAGE_MANEUVER_UNAVAILABLE_H152_ESC_OFF ->
                "MESSAGE_MANEUVER_UNAVAILABLE_H152_ESC_OFF"
            MESSAGE_MANEUVER_CANCELED_ENGINE_STOPPED ->
                "MESSAGE_MANEUVER_CANCELED_ENGINE_STOPPED"
            MESSAGE_MANEUVER_CANCELED_GEAR_SHIFT_ACTIVATED ->
                "MESSAGE_MANEUVER_CANCELED_GEAR_SHIFT_ACTIVATED"
            MESSAGE_MANEUVER_CANCELED_ESC_OFF ->
                "MESSAGE_MANEUVER_CANCELED_ESC_OFF"
            MESSAGE_FEATURE_UNAVAILABLE_MIRRORS_FOLDED ->
                "MESSAGE_FEATURE_UNAVAILABLE_MIRRORS_FOLDED"
            MESSAGE_MANEUVER_CANCELED_MIRRORS_FOLDED ->
                "MESSAGE_MANEUVER_CANCELED_MIRRORS_FOLDED"
            MESSAGE_MANEUVER_FINISHED_NO_FRONT_OBSTACLE ->
                "MESSAGE_MANEUVER_FINISHED_NO_FRONT_OBSTACLE"
            MESSAGE_MANEUVER_UNAVAILABLE_SLOT_TOO_SMALL ->
                "MESSAGE_MANEUVER_UNAVAILABLE_SLOT_TOO_SMALL"
            else -> throw NoSuchElementException("Unknown WarningMessage: $warningMessage")
        }
    }

    /**
     * Get string description for AutoPark feature
     * @param feature the object to be described
     * @return a string description
     */
    fun featureConfigurationToString(@FeatureConfiguration feature: Int): String {
        return when (feature) {
            FEATURE_NONE -> "FEATURE_NONE"
            FEATURE_HFP -> "FEATURE_HFP"
            FEATURE_HFPB -> "FEATURE_HFPB"
            FEATURE_FPK -> "FEATURE_FPK"
            FEATURE_FAPK -> "FEATURE_FAPK"
            else -> throw NoSuchElementException("Unknown FeatureConfiguration: $feature")
        }
    }

    /**
     * Get string description for AutoPark view mask
     * @param viewMask the object to be described
     * @return a string description
     */
    fun viewMaskToString(@AutoPark.ViewMask viewMask: Int): String {
        return when (viewMask) {
            VIEW_MASK_UNAVAILABLE -> "VIEW_MASK_UNAVAILABLE"
            VIEW_MASK_REQUESTED -> "VIEW_MASK_REQUESTED"
            else -> throw NoSuchElementException("Unknown ViewMask: $viewMask")
        }
    }

    /**
     * Get string description for AutoPark maneuver start switch
     * @param maneuverStartSwitch the object to be described
     * @return a string description
     */
    fun maneuverStartSwitchToString(
        @AutoPark.ManeuverStartSwitch maneuverStartSwitch: Int
    ): String {
        return when (maneuverStartSwitch) {
            MANEUVER_START_SWITCH_NONE -> "MANEUVER_START_SWITCH_NONE"
            MANEUVER_START_SWITCH_UNUSABLE_START -> "MANEUVER_START_SWITCH_UNUSABLE_START"
            MANEUVER_START_SWITCH_DISPLAY_START -> "MANEUVER_START_SWITCH_DISPLAY_START"
            MANEUVER_START_SWITCH_DISPLAY_START_AUTO_MODE ->
                "MANEUVER_START_SWITCH_DISPLAY_START_AUTO_MODE"
            MANEUVER_START_SWITCH_DISPLAY_START_PARALLEL ->
                "MANEUVER_START_SWITCH_DISPLAY_START_PARALLEL"
            MANEUVER_START_SWITCH_DISPLAY_START_PERPENDICULAR ->
                "MANEUVER_START_SWITCH_DISPLAY_START_PERPENDICULAR"
            MANEUVER_START_SWITCH_DISPLAY_START_PARKOUT ->
                "MANEUVER_START_SWITCH_DISPLAY_START_PARKOUT"
            MANEUVER_START_SWITCH_DISPLAY_CANCEL -> "MANEUVER_START_SWITCH_DISPLAY_CANCEL"
            else -> throw NoSuchElementException(
                "Unknown ManeuverStartSwitch: $maneuverStartSwitch"
            )
        }
    }

    /**
     * Get string description for AutoPark error status code
     * @param errorStatus the object to be described
     * @return a string description
     */
    fun errorStatusToString(@StatusCode errorStatus: Int): String {
        return when (errorStatus) {
            ERROR_NONE -> "ERROR_NONE"
            ERROR_INVALID_ARGUMENT -> "ERROR_INVALID_ARGUMENT"
            ERROR_INVALID_STATE -> "ERROR_INVALID_STATE"
            ERROR_NOT_READY -> "ERROR_NOT_READY"
            ERROR_INTERNAL -> "ERROR_INTERNAL"
            ERROR_INIT_FAILED -> "ERROR_INIT_FAILED"
            else -> throw NoSuchElementException("Unknown StatusCode: $errorStatus")
        }
    }
}