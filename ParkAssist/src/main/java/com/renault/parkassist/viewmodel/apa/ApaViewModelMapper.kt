package com.renault.parkassist.viewmodel.apa

import androidx.annotation.StringRes
import com.renault.parkassist.R
import com.renault.parkassist.utility.mapNullable
import com.renault.parkassist.utility.unMap
import com.renault.parkassist.viewmodel.apa.ApaWarningMessage.*

class ApaViewModelMapper {
    private val warningHfpMessageMap = listOf(
        WARNING_MANEUVER_SUSPENDED_DOOR_OPEN to
            R.string.rlb_parkassist_apa_hfp_warning_maneuver_suspended_door_open,
        WARNING_MANEUVER_SUSPENDED_HAND_ON_WHEEL to
            R.string.rlb_parkassist_apa_hfp_warning_maneuver_suspended_hand_on_wheel,
        WARNING_MANEUVER_SUSPENDED_UNTIL_RESTART_ENGINE to
            R.string.rlb_parkassist_apa_hfp_warning_maneuver_suspended_until_restart_engine,
        WARNING_MANEUVER_SUSPENDED to R.string.rlb_parkassist_apa_hfp_warning_maneuver_suspended,
        WARNING_MANEUVER_SUSPENDED_OBSTACLE_ON_PATH to
            R.string.rlb_parkassist_apa_hfp_warning_maneuver_suspended_obstacle_on_path,
        WARNING_MANEUVER_SUSPENDED_BRAKE_TO_STOP to
            R.string.rlb_parkassist_apa_hfp_warning_maneuver_suspended_brake_to_stop,
        WARNING_MANEUVER_CANCELED to R.string.rlb_parkassist_apa_hfp_warning_maneuver_canceled,
        WARNING_MANEUVER_UNAVAILABLE_HAND_ON_WHEEL to
            R.string.rlb_parkassist_apa_hfp_warning_maneuver_unavailable_hand_on_wheel,
        WARNING_MANEUVER_UNAVAILABLE_DOOR_OPEN to
            R.string.rlb_parkassist_apa_hfp_warning_maneuver_unavailable_door_open,
        WARNING_MANEUVER_UNAVAILABLE_ENGINE_STOPPED to
            R.string.rlb_parkassist_apa_hfp_warning_maneuver_unavailable_engine_stopped,
        WARNING_FEATURE_UNAVAILABLE_REAR_GEAR_ENGAGED to
            R.string.rlb_parkassist_apa_hfp_warning_feature_unavailable_rear_gear_engaged,
        WARNING_FEATURE_UNAVAILABLE_SPEED_TOO_HIGH to
            R.string.rlb_parkassist_apa_hfp_warning_feature_unavailable_speed_too_high,
        WARNING_FEATURE_UNAVAILABLE to R.string.rlb_parkassist_apa_hfp_warning_feature_unavailable,
        WARNING_FEATURE_UNAVAILABLE_TRAILER_DETECTED to
            R.string.rlb_parkassist_apa_hfp_warning_feature_unavailable_trailer_detected,
        WARNING_FEATURE_UNAVAILABLE_CRUISE_CONTROL_ACTIVATED to
            R.string.rlb_parkassist_apa_hfp_warning_feature_unavailable_cruise_control_activated,
        WARNING_MANEUVER_CANCELED_PARKING_BRAKE to
            R.string.rlb_parkassist_apa_hfp_warning_maneuver_canceled_parking_brake,
        WARNING_MANEUVER_CANCELED_DRIVER_DOOR_OPEN to
            R.string.rlb_parkassist_apa_hfp_warning_maneuver_canceled_driver_door_open,
        WARNING_MANEUVER_CANCELED_SEAT_BELT_UNFASTEN to
            R.string.rlb_parkassist_apa_hfp_warning_maneuver_canceled_seat_belt_unfasten,
        WARNING_MANEUVER_UNAVAILABLE_PARKING_BRAKE to
            R.string.rlb_parkassist_apa_hfp_warning_maneuver_unavailable_parking_brake,
        WARNING_MANEUVER_CANCELED_SLOPE_TOO_HIGH to
            R.string.rlb_parkassist_apa_hfp_warning_maneuver_canceled_slope_too_high,
        WARNING_MANEUVER_UNAVAILABLE_SEAT_BELT_UNFASTEN to
            R.string.rlb_parkassist_apa_hfp_warning_maneuver_unavailable_seat_belt_unfasten,
        WARNING_MANEUVER_UNAVAILABLE_SLOPE_TOO_HIGH to
            R.string.rlb_parkassist_apa_hfp_warning_maneuver_unavailable_slope_too_high,
        WARNING_MANEUVER_CANCELED_BRAKING_FAILURE to
            R.string.rlb_parkassist_apa_hfp_warning_maneuver_canceled_braking_failure,
        WARNING_MANEUVER_CANCELED_TAKE_BACK_CONTROL to
            R.string.rlb_parkassist_apa_hfp_warning_maneuver_canceled_take_back_control,
        WARNING_SELECT_TURN_INDICATOR to
            R.string.rlb_parkassist_apa_hfp_warning_select_turn_indicator,
        WARNING_PRESS_OK_TO_START_MANEUVER to
            R.string.rlb_parkassist_apa_hfp_warning_press_ok_to_start_maneuver,
        WARNING_MANEUVER_UNAVAILABLE_H_152_ESC_OFF to
            R.string.rlb_parkassist_apa_hfp_warning_maneuver_unavailable_h_152_esc_off,
        WARNING_MANEUVER_CANCELED_ENGINE_STOPPED to
            R.string.rlb_parkassist_apa_hfp_warning_maneuver_canceled_engine_stopped,
        WARNING_MANEUVER_CANCELED_ESC_OFF to
            R.string.rlb_parkassist_apa_hfp_warning_maneuver_canceled_esc_off,
        WARNING_MANEUVER_FINISHED_NO_FRONT_OBSTACLE to
            R.string.rlb_parkassist_apa_hfp_warning_maneuver_finished_no_front_obstacle,
        WARNING_MANEUVER_UNAVAILABLE_SLOT_TOO_SMALL to
            R.string.rlb_parkassist_apa_hfp_warning_maneuver_unavailable_slot_too_small
    )

    private val warningFapkMessageMap = listOf(
        WARNING_MANEUVER_SUSPENDED_DOOR_OPEN to
            R.string.rlb_parkassist_apa_fapk_warning_maneuver_suspended_door_open,
        WARNING_MANEUVER_SUSPENDED_HAND_ON_WHEEL to
            R.string.rlb_parkassist_apa_fapk_warning_maneuver_suspended_hand_on_wheel,
        WARNING_MANEUVER_SUSPENDED to R.string.rlb_parkassist_apa_fapk_warning_maneuver_suspended,
        WARNING_MANEUVER_SUSPENDED_OBSTACLE_ON_PATH to
            R.string.rlb_parkassist_apa_fapk_warning_maneuver_suspended_obstacle_on_path,
        WARNING_MANEUVER_CANCELED to R.string.rlb_parkassist_apa_fapk_warning_maneuver_canceled,
        WARNING_MANEUVER_UNAVAILABLE_HAND_ON_WHEEL to
            R.string.rlb_parkassist_apa_fapk_warning_maneuver_unavailable_hand_on_wheel,
        WARNING_MANEUVER_UNAVAILABLE_DOOR_OPEN to
            R.string.rlb_parkassist_apa_fapk_warning_maneuver_unavailable_door_open,
        WARNING_MANEUVER_UNAVAILABLE_ENGINE_STOPPED to
            R.string.rlb_parkassist_apa_fapk_warning_maneuver_unavailable_engine_stopped,
        WARNING_FEATURE_UNAVAILABLE_REAR_GEAR_ENGAGED to
            R.string.rlb_parkassist_apa_fapk_warning_feature_unavailable_rear_gear_engaged,
        WARNING_FEATURE_UNAVAILABLE_SPEED_TOO_HIGH to
            R.string.rlb_parkassist_apa_fapk_warning_feature_unavailable_speed_too_high,
        WARNING_FEATURE_UNAVAILABLE to R.string.rlb_parkassist_apa_fapk_warning_feature_unavailable,
        WARNING_FEATURE_UNAVAILABLE_TRAILER_DETECTED to
            R.string.rlb_parkassist_apa_fapk_warning_feature_unavailable_trailer_detected,
        WARNING_FEATURE_UNAVAILABLE_CRUISE_CONTROL_ACTIVATED to
            R.string.rlb_parkassist_apa_fapk_warning_feature_unavailable_cruise_control_activated,
        WARNING_MANEUVER_CANCELED_PARKING_BRAKE to
            R.string.rlb_parkassist_apa_fapk_warning_maneuver_canceled_parking_brake,
        WARNING_MANEUVER_CANCELED_DRIVER_DOOR_OPEN to
            R.string.rlb_parkassist_apa_fapk_warning_maneuver_canceled_driver_door_open,
        WARNING_MANEUVER_CANCELED_SEAT_BELT_UNFASTEN to
            R.string.rlb_parkassist_apa_fapk_warning_maneuver_canceled_seat_belt_unfasten,
        WARNING_MANEUVER_UNAVAILABLE_PARKING_BRAKE to
            R.string.rlb_parkassist_apa_fapk_warning_maneuver_unavailable_parking_brake,
        WARNING_MANEUVER_CANCELED_SLOPE_TOO_HIGH to
            R.string.rlb_parkassist_apa_fapk_warning_maneuver_canceled_slope_too_high,
        WARNING_MANEUVER_UNAVAILABLE_SEAT_BELT_UNFASTEN to
            R.string.rlb_parkassist_apa_fapk_warning_maneuver_unavailable_seat_belt_unfasten,
        WARNING_MANEUVER_UNAVAILABLE_SLOPE_TOO_HIGH to
            R.string.rlb_parkassist_apa_fapk_warning_maneuver_unavailable_slope_too_high,
        WARNING_MANEUVER_CANCELED_BRAKING_FAILURE to
            R.string.rlb_parkassist_apa_fapk_warning_maneuver_canceled_braking_failure,
        WARNING_BRAKE_TO_RESUME_MANEUVER to
            R.string.rlb_parkassist_apa_fapk_warning_brake_to_resume_maneuver,
        WARNING_FEATURE_UNAVAILABLE_VEHICLE_NOT_STOPPED to
            R.string.rlb_parkassist_apa_fapk_warning_feature_unavailable_vehicle_not_stopped,
        WARNING_MANEUVER_CANCELED_TAKE_BACK_CONTROL to
            R.string.rlb_parkassist_apa_fapk_warning_maneuver_canceled_take_back_control,
        WARNING_MANEUVER_SUSPENDED_RELEASE_ACCELERATOR_PEDAL to
            R.string.rlb_parkassist_apa_fapk_warning_maneuver_suspended_release_accelerator_pedal,
        WARNING_MANEUVER_SUSPENDED_GEAR_SHIFT_ACTIVATED to
            R.string.rlb_parkassist_apa_fapk_warning_maneuver_suspended_gear_shift_activated,
        WARNING_SELECT_TURN_INDICATOR to
            R.string.rlb_parkassist_apa_fapk_warning_select_turn_indicator,
        WARNING_MANEUVER_CANCELED_RELEASE_ACCELERATOR_PEDAL to
            R.string.rlb_parkassist_apa_fapk_warning_maneuver_canceled_release_accelerator_pedal,
        WARNING_PRESS_OK_TO_START_MANEUVER to
            R.string.rlb_parkassist_apa_fapk_warning_press_ok_to_start_maneuver,
        WARNING_MANEUVER_UNAVAILABLE_H_152_ESC_OFF to
            R.string.rlb_parkassist_apa_fapk_warning_maneuver_unavailable_h_152_esc_off,
        WARNING_MANEUVER_CANCELED_ENGINE_STOPPED to
            R.string.rlb_parkassist_apa_fapk_warning_maneuver_canceled_engine_stopped,
        WARNING_MANEUVER_CANCELED_GEAR_SHIFT_ACTIVATED to
            R.string.rlb_parkassist_apa_fapk_warning_maneuver_canceled_gear_shift_activated,
        WARNING_MANEUVER_CANCELED_ESC_OFF to
            R.string.rlb_parkassist_apa_fapk_warning_maneuver_canceled_esc_off,
        WARNING_FEATURE_UNAVAILABLE_MIRRORS_FOLDED to
            R.string.rlb_parkassist_apa_fapk_warning_feature_unavailable_mirrors_folded,
        WARNING_MANEUVER_CANCELED_MIRRORS_FOLDED to
            R.string.rlb_parkassist_apa_fabk_warning_maneuver_canceled_mirrors_folded,
        WARNING_MANEUVER_FINISHED_NO_FRONT_OBSTACLE to
            R.string.rlb_parkassist_apa_fapk_warning_maneuver_finished_no_front_obstacle,
        WARNING_MANEUVER_UNAVAILABLE_SLOT_TOO_SMALL to
            R.string.rlb_parkassist_apa_fapk_warning_maneuver_unavailable_slot_too_small
    )

    @StringRes
    fun toHfpLabelMessage(viewModelWarningId: Int): Int? =
        warningHfpMessageMap.mapNullable(viewModelWarningId)

    @StringRes
    fun toFapkLabelMessage(viewModelWarningId: Int): Int? =
        warningFapkMessageMap.mapNullable(viewModelWarningId)

    fun toApaViewModelWarningId(@StringRes labelId: Int): Int =
        warningHfpMessageMap.unMap(labelId)
}