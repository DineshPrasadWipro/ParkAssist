package com.renault.parkassist.repository.apa

import alliance.car.autopark.AutoPark
import com.renault.parkassist.utility.map
import com.renault.parkassist.utility.unMap
import com.renault.parkassist.viewmodel.apa.ApaWarningAcknowledgment
import com.renault.parkassist.viewmodel.apa.ApaWarningMessage

class ApaMapper {

    private val warningMessageMap = listOf(
        AutoPark.MESSAGE_NONE
            to ApaWarningMessage.WARNING_NONE,
        AutoPark.MESSAGE_MANEUVER_SUSPENDED_DOOR_OPEN
            to ApaWarningMessage.WARNING_MANEUVER_SUSPENDED_DOOR_OPEN,
        AutoPark.MESSAGE_MANEUVER_SUSPENDED_HAND_ON_WHEEL
            to ApaWarningMessage.WARNING_MANEUVER_SUSPENDED_HAND_ON_WHEEL,
        AutoPark.MESSAGE_MANEUVER_SUSPENDED_UNTIL_RESTART_ENGINE
            to ApaWarningMessage.WARNING_MANEUVER_SUSPENDED_UNTIL_RESTART_ENGINE,
        AutoPark.MESSAGE_MANEUVER_SUSPENDED
            to ApaWarningMessage.WARNING_MANEUVER_SUSPENDED,
        AutoPark.MESSAGE_ASK_RESUME_MANEUVER
            to ApaWarningMessage.WARNING_ASK_RESUME_MANEUVER,
        AutoPark.MESSAGE_MANEUVER_SUSPENDED_OBSTACLE_ON_PATH
            to ApaWarningMessage.WARNING_MANEUVER_SUSPENDED_OBSTACLE_ON_PATH,
        AutoPark.MESSAGE_MANEUVER_SUSPENDED_BRAKE_TO_STOP
            to ApaWarningMessage.WARNING_MANEUVER_SUSPENDED_BRAKE_TO_STOP,
        AutoPark.MESSAGE_MANEUVER_CANCELED
            to ApaWarningMessage.WARNING_MANEUVER_CANCELED,
        AutoPark.MESSAGE_MANEUVER_UNAVAILABLE_HAND_ON_WHEEL
            to ApaWarningMessage.WARNING_MANEUVER_UNAVAILABLE_HAND_ON_WHEEL,
        AutoPark.MESSAGE_MANEUVER_UNAVAILABLE_DOOR_OPEN
            to ApaWarningMessage.WARNING_MANEUVER_UNAVAILABLE_DOOR_OPEN,
        AutoPark.MESSAGE_MANEUVER_UNAVAILABLE_ENGINE_STOPPED
            to ApaWarningMessage.WARNING_MANEUVER_UNAVAILABLE_ENGINE_STOPPED,
        AutoPark.MESSAGE_FEATURE_UNAVAILABLE_REAR_GEAR_ENGAGED
            to ApaWarningMessage.WARNING_FEATURE_UNAVAILABLE_REAR_GEAR_ENGAGED,
        AutoPark.MESSAGE_FEATURE_UNAVAILABLE_SPEED_TOO_HIGH
            to ApaWarningMessage.WARNING_FEATURE_UNAVAILABLE_SPEED_TOO_HIGH,
        AutoPark.MESSAGE_FEATURE_UNAVAILABLE
            to ApaWarningMessage.WARNING_FEATURE_UNAVAILABLE,
        AutoPark.MESSAGE_FEATURE_UNAVAILABLE_TRAILER_DETECTED
            to ApaWarningMessage.WARNING_FEATURE_UNAVAILABLE_TRAILER_DETECTED,
        AutoPark.MESSAGE_FEATURE_UNAVAILABLE_CRUISE_CONTROL_ACTIVATED
            to ApaWarningMessage.WARNING_FEATURE_UNAVAILABLE_CRUISE_CONTROL_ACTIVATED,
        AutoPark.MESSAGE_MANEUVER_CANCELED_PARKING_BRAKE
            to ApaWarningMessage.WARNING_MANEUVER_CANCELED_PARKING_BRAKE,
        AutoPark.MESSAGE_MANEUVER_CANCELED_DRIVER_DOOR_OPEN
            to ApaWarningMessage.WARNING_MANEUVER_CANCELED_DRIVER_DOOR_OPEN,
        AutoPark.MESSAGE_MANEUVER_CANCELED_SEAT_BELT_UNFASTEN
            to ApaWarningMessage.WARNING_MANEUVER_CANCELED_SEAT_BELT_UNFASTEN,
        AutoPark.MESSAGE_MANEUVER_UNAVAILABLE_PARKING_BRAKE
            to ApaWarningMessage.WARNING_MANEUVER_UNAVAILABLE_PARKING_BRAKE,
        AutoPark.MESSAGE_MANEUVER_CANCELED_SLOPE_TOO_HIGH
            to ApaWarningMessage.WARNING_MANEUVER_CANCELED_SLOPE_TOO_HIGH,
        AutoPark.MESSAGE_MANEUVER_UNAVAILABLE_SEAT_BELT_UNFASTEN
            to ApaWarningMessage.WARNING_MANEUVER_UNAVAILABLE_SEAT_BELT_UNFASTEN,
        AutoPark.MESSAGE_MANEUVER_UNAVAILABLE_SLOPE_TOO_HIGH
            to ApaWarningMessage.WARNING_MANEUVER_UNAVAILABLE_SLOPE_TOO_HIGH,
        AutoPark.MESSAGE_MANEUVER_CANCELED_BRAKING_FAILURE
            to ApaWarningMessage.WARNING_MANEUVER_CANCELED_BRAKING_FAILURE,
        AutoPark.MESSAGE_BRAKE_TO_RESUME_MANEUVER
            to ApaWarningMessage.WARNING_BRAKE_TO_RESUME_MANEUVER,
        AutoPark.MESSAGE_FEATURE_UNAVAILABLE_VEHICLE_NOT_STOPPED
            to ApaWarningMessage.WARNING_FEATURE_UNAVAILABLE_VEHICLE_NOT_STOPPED,
        AutoPark.MESSAGE_MANEUVER_CANCELED_TAKE_BACK_CONTROL
            to ApaWarningMessage.WARNING_MANEUVER_CANCELED_TAKE_BACK_CONTROL,
        AutoPark.MESSAGE_MANEUVER_SUSPENDED_RELEASE_ACCELERATOR_PEDAL
            to ApaWarningMessage.WARNING_MANEUVER_SUSPENDED_RELEASE_ACCELERATOR_PEDAL,
        AutoPark.MESSAGE_MANEUVER_SUSPENDED_GEAR_SHIFT_ACTIVATED
            to ApaWarningMessage.WARNING_MANEUVER_SUSPENDED_GEAR_SHIFT_ACTIVATED,
        AutoPark.MESSAGE_SELECT_TURN_INDICATOR
            to ApaWarningMessage.WARNING_SELECT_TURN_INDICATOR,
        AutoPark.MESSAGE_MANEUVER_CANCELED_RELEASE_ACCELERATOR_PEDAL
            to ApaWarningMessage.WARNING_MANEUVER_CANCELED_RELEASE_ACCELERATOR_PEDAL,
        AutoPark.MESSAGE_PRESS_OK_TO_START_MANEUVER
            to ApaWarningMessage.WARNING_PRESS_OK_TO_START_MANEUVER,
        AutoPark.MESSAGE_MANEUVER_UNAVAILABLE_H152_ESC_OFF
            to ApaWarningMessage.WARNING_MANEUVER_UNAVAILABLE_H_152_ESC_OFF,
        AutoPark.MESSAGE_MANEUVER_CANCELED_ENGINE_STOPPED
            to ApaWarningMessage.WARNING_MANEUVER_CANCELED_ENGINE_STOPPED,
        AutoPark.MESSAGE_MANEUVER_CANCELED_GEAR_SHIFT_ACTIVATED
            to ApaWarningMessage.WARNING_MANEUVER_CANCELED_GEAR_SHIFT_ACTIVATED,
        AutoPark.MESSAGE_MANEUVER_CANCELED_ESC_OFF
            to ApaWarningMessage.WARNING_MANEUVER_CANCELED_ESC_OFF,
        AutoPark.MESSAGE_FEATURE_UNAVAILABLE_MIRRORS_FOLDED
            to ApaWarningMessage.WARNING_FEATURE_UNAVAILABLE_MIRRORS_FOLDED,
        AutoPark.MESSAGE_MANEUVER_CANCELED_MIRRORS_FOLDED
            to ApaWarningMessage.WARNING_MANEUVER_CANCELED_MIRRORS_FOLDED,
        AutoPark.MESSAGE_MANEUVER_FINISHED_NO_FRONT_OBSTACLE
            to ApaWarningMessage.WARNING_MANEUVER_FINISHED_NO_FRONT_OBSTACLE,
        AutoPark.MESSAGE_MANEUVER_UNAVAILABLE_SLOT_TOO_SMALL
            to ApaWarningMessage.WARNING_MANEUVER_UNAVAILABLE_SLOT_TOO_SMALL
    )

    private val acknowledgementMap = listOf(
        AutoPark.USER_ACKNOWLEDGEMENT_1 to ApaWarningAcknowledgment.ACK_1,
        AutoPark.USER_ACKNOWLEDGEMENT_2 to ApaWarningAcknowledgment.ACK_2
    )

    private val scanningSideMap = listOf(
        AutoPark.SCANNING_SIDE_NONE to ScanningSide.SCANNING_SIDE_NONE,
        AutoPark.SCANNING_SIDE_LEFT to ScanningSide.SCANNING_SIDE_LEFT,
        AutoPark.SCANNING_SIDE_RIGHT to ScanningSide.SCANNING_SIDE_RIGHT,
        AutoPark.SCANNING_SIDE_UNAVAILABLE to ScanningSide.SCANNING_SIDE_UNAVAILABLE
    )

    private val instructionMap = listOf(
        AutoPark.EXT_INSTRUCTION_SELECT_SIDE to Instruction.SELECT_SIDE,
        AutoPark.EXT_INSTRUCTION_DRIVE_FORWARD_TO_FIND_PARKING_SLOT to
            Instruction.DRIVE_FORWARD_TO_FIND_PARKING_SLOT,
        AutoPark.EXT_INSTRUCTION_DRIVE_FORWARD_TO_FIND_PARKING_SLOT2 to
            Instruction.DRIVE_FORWARD_SLOT_SUITABLE,
        AutoPark.EXT_INSTRUCTION_STOP to Instruction.STOP,
        AutoPark.EXT_INSTRUCTION_ENGAGE_REAR_GEAR_OR_PRESS_START to
            Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON,
        AutoPark.EXT_INSTRUCTION_ENGAGE_FORWARD_GEAR to Instruction.SELECT_OR_ENGAGE_FORWARD_GEAR,
        AutoPark.EXT_INSTRUCTION_DRIVE_FORWARD to Instruction.DRIVE_FORWARD,
        AutoPark.EXT_INSTRUCTION_DRIVE_BACKWARD to Instruction.REVERSE,
        AutoPark.EXT_INSTRUCTION_FINISHED to Instruction.MANEUVER_COMPLETE_OR_FINISHED,
        AutoPark.EXT_INSTRUCTION_DRIVE_FORWARD_OR_BACKWARD to Instruction.GO_FORWARD_OR_REVERSE,
        AutoPark.EXT_INSTRUCTION_MANEUVER_FINISHED_TAKE_BACK_CONTROL to
            Instruction.MANEUVER_FINISHED_TAKE_BACK_CONTROL,
        AutoPark.EXT_INSTRUCTION_ENGAGE_REAR_GEAR to Instruction.ENGAGE_REAR_GEAR,
        AutoPark.EXT_INSTRUCTION_STOP_AFTER_REAR_GEAR_ENGAGED to
            Instruction.STOP_AFTER_REAR_GEAR_ENGAGED,
        AutoPark.EXT_INSTRUCTION_ACCELERATE_AND_HOLD_THE_PEDAL_PRESSED to
            Instruction.ACCELERATE_AND_HOLD_THE_PEDAL_PRESSED,
        AutoPark.EXT_INSTRUCTION_MANEUVER_FINISHED_RELEASE_OR_PARK to
            Instruction.MANEUVER_FINISHED_RELEASE_THE_ACCELERATOR_PEDAL,
        AutoPark.EXT_INSTRUCTION_HOLD_ACCELERATOR_PRESSED to
            Instruction.HOLD_THE_ACCELERATOR_PEDAL_PRESSED,
        AutoPark.EXT_INSTRUCTION_STANDBY to
            Instruction.STAND_BY_NO_TEXT,
        AutoPark.EXT_INSTRUCTION_UNAVAILABLE to Instruction.UNAVAILABLE
    )

    private val maneuverTypeMap = listOf(
        AutoPark.MANEUVER_TYPE_PARKIN_PARALLEL to ManeuverType.PARALLEL,
        AutoPark.MANEUVER_TYPE_PARKIN_PERPENDICULAR to ManeuverType.PERPENDICULAR,
        AutoPark.MANEUVER_TYPE_PARKIN_ANGLED to ManeuverType.ANGLED,
        AutoPark.MANEUVER_TYPE_PARKOUT to ManeuverType.PARKOUT,
        AutoPark.MANEUVER_TYPE_AUTO_MODE to ManeuverType.AUTO_MODE,
        AutoPark.MANEUVER_TYPE_UNAVAILABLE to ManeuverType.UNAVAILABLE
    )

    private val maneuverMoveMap = listOf(
        AutoPark.MANEUVER_MOVE_FIRST to ManeuverMove.FIRST,
        AutoPark.MANEUVER_MOVE_FORWARD to ManeuverMove.FORWARD,
        AutoPark.MANEUVER_MOVE_BACKWARD to ManeuverMove.BACKWARD,
        AutoPark.MANEUVER_MOVE_UNAVAILABLE to ManeuverMove.UNAVAILABLE
    )

    private val maneuverStartSwitchMap = listOf(
        AutoPark.MANEUVER_START_SWITCH_NONE to ManeuverStartSwitch.NONE,
        AutoPark.MANEUVER_START_SWITCH_UNUSABLE_START to ManeuverStartSwitch.UNUSABLE_START,
        AutoPark.MANEUVER_START_SWITCH_DISPLAY_START to ManeuverStartSwitch.DISPLAY_START,
        AutoPark.MANEUVER_START_SWITCH_DISPLAY_START_AUTO_MODE to
            ManeuverStartSwitch.DISPLAY_START_AUTO_MODE,
        AutoPark.MANEUVER_START_SWITCH_DISPLAY_START_PARALLEL to
            ManeuverStartSwitch.DISPLAY_START_PARALLEL,
        AutoPark.MANEUVER_START_SWITCH_DISPLAY_START_PERPENDICULAR to
            ManeuverStartSwitch.DISPLAY_START_PERPENDICULAR,
        AutoPark.MANEUVER_START_SWITCH_DISPLAY_START_PARKOUT to
            ManeuverStartSwitch.DISPLAY_START_PARKOUT,
        AutoPark.MANEUVER_START_SWITCH_DISPLAY_CANCEL to ManeuverStartSwitch.DISPLAY_CANCEL
    )

    private val featureConfigMap = listOf(
        AutoPark.FEATURE_NONE to FeatureConfig.NONE,
        AutoPark.FEATURE_HFP to FeatureConfig.HFP,
        AutoPark.FEATURE_HFPB to FeatureConfig.HFPB,
        AutoPark.FEATURE_FPK to FeatureConfig.FPK,
        AutoPark.FEATURE_FAPK to FeatureConfig.FAPK
    )

    private val maneuverSelectionMap = listOf(
        AutoPark.MANEUVER_SELECTION_SELECTED to ManeuverSelection.SELECTED,
        AutoPark.MANEUVER_SELECTION_NOT_SELECTED to ManeuverSelection.NOT_SELECTED,
        AutoPark.MANEUVER_SELECTION_UNAVAILABLE to ManeuverSelection.UNAVAILABLE
    )

    private val displayStateMap = listOf(
        AutoPark.DISPLAY_SCANNING to DisplayState.DISPLAY_SCANNING,
        AutoPark.DISPLAY_GUIDANCE to DisplayState.DISPLAY_GUIDANCE,
        AutoPark.DISPLAY_PARKOUT_CONFIRMATION to DisplayState.DISPLAY_PARKOUT_CONFIRMATION,
        AutoPark.DISPLAY_NONE to DisplayState.DISPLAY_NONE
    )

    private val viewMaskMap = listOf(
        AutoPark.VIEW_MASK_UNAVAILABLE to ViewMask.UNAVAILABLE,
        AutoPark.VIEW_MASK_REQUESTED to ViewMask.REQUESTED
    )

    @ScanningSide
    fun mapScanningSide(input: Int): Int = scanningSideMap.map(input)

    @Instruction
    fun mapInstruction(input: Int): Int = instructionMap.map(input)

    @ManeuverType
    fun mapManeuverType(input: Int): Int = maneuverTypeMap.map(input)

    @ManeuverMove
    fun mapManeuverMove(input: Int): Int = maneuverMoveMap.map(input)

    @ManeuverStartSwitch
    fun mapManeuverStartSwitch(input: Int): Int = maneuverStartSwitchMap.map(input)

    @FeatureConfig
    fun mapFeatureConfig(input: Int): Int = featureConfigMap.map(input)

    @ManeuverSelection
    fun mapManeuverSelection(input: Int): Int = maneuverSelectionMap.map(input)

    @ManeuverType
    fun unmapManeuverType(input: Int): Int = maneuverTypeMap.unMap(input)

    @DisplayState
    fun mapDisplayState(input: Int): Int = displayStateMap.map(input)

    @ApaWarningMessage
    fun mapApaViewModelWarningId(input: Int): Int = warningMessageMap.map(input)

    @ViewMask
    fun mapViewMask(input: Int): Int = viewMaskMap.map(input)

    fun unMapApaViewModelAck(@ApaWarningAcknowledgment input: Int): Int =
        acknowledgementMap.unMap(input)
}