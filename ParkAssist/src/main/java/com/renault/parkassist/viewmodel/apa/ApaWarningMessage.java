/**
 * This is a generated file: do not modify.
 *
 * Generated by midl v2.1.0.
 *
 * Copyright (c) 2019 Renault SW Labs
 *
 * Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all
 * intellectual property rights. Use of this software is subject to a specific
 * license granted by RENAULT S.A.S.
 */
package com.renault.parkassist.viewmodel.apa;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
	ApaWarningMessage.WARNING_NONE,
	ApaWarningMessage.WARNING_MANEUVER_SUSPENDED_DOOR_OPEN,
	ApaWarningMessage.WARNING_MANEUVER_SUSPENDED_HAND_ON_WHEEL,
	ApaWarningMessage.WARNING_MANEUVER_SUSPENDED_UNTIL_RESTART_ENGINE,
	ApaWarningMessage.WARNING_MANEUVER_SUSPENDED,
	ApaWarningMessage.WARNING_ASK_RESUME_MANEUVER,
	ApaWarningMessage.WARNING_MANEUVER_SUSPENDED_OBSTACLE_ON_PATH,
	ApaWarningMessage.WARNING_MANEUVER_SUSPENDED_BRAKE_TO_STOP,
	ApaWarningMessage.WARNING_MANEUVER_CANCELED,
	ApaWarningMessage.WARNING_MANEUVER_UNAVAILABLE_HAND_ON_WHEEL,
	ApaWarningMessage.WARNING_MANEUVER_UNAVAILABLE_DOOR_OPEN,
	ApaWarningMessage.WARNING_MANEUVER_UNAVAILABLE_ENGINE_STOPPED,
	ApaWarningMessage.WARNING_FEATURE_UNAVAILABLE_REAR_GEAR_ENGAGED,
	ApaWarningMessage.WARNING_FEATURE_UNAVAILABLE_SPEED_TOO_HIGH,
	ApaWarningMessage.WARNING_FEATURE_UNAVAILABLE,
	ApaWarningMessage.WARNING_FEATURE_UNAVAILABLE_TRAILER_DETECTED,
	ApaWarningMessage.WARNING_FEATURE_UNAVAILABLE_CRUISE_CONTROL_ACTIVATED,
	ApaWarningMessage.WARNING_MANEUVER_CANCELED_PARKING_BRAKE,
	ApaWarningMessage.WARNING_MANEUVER_CANCELED_DRIVER_DOOR_OPEN,
	ApaWarningMessage.WARNING_MANEUVER_CANCELED_SEAT_BELT_UNFASTEN,
	ApaWarningMessage.WARNING_MANEUVER_UNAVAILABLE_PARKING_BRAKE,
	ApaWarningMessage.WARNING_MANEUVER_CANCELED_SLOPE_TOO_HIGH,
	ApaWarningMessage.WARNING_MANEUVER_UNAVAILABLE_SEAT_BELT_UNFASTEN,
	ApaWarningMessage.WARNING_MANEUVER_UNAVAILABLE_SLOPE_TOO_HIGH,
	ApaWarningMessage.WARNING_MANEUVER_CANCELED_BRAKING_FAILURE,
	ApaWarningMessage.WARNING_BRAKE_TO_RESUME_MANEUVER,
	ApaWarningMessage.WARNING_FEATURE_UNAVAILABLE_VEHICLE_NOT_STOPPED,
	ApaWarningMessage.WARNING_MANEUVER_CANCELED_TAKE_BACK_CONTROL,
	ApaWarningMessage.WARNING_MANEUVER_SUSPENDED_RELEASE_ACCELERATOR_PEDAL,
	ApaWarningMessage.WARNING_MANEUVER_SUSPENDED_GEAR_SHIFT_ACTIVATED,
	ApaWarningMessage.WARNING_SELECT_TURN_INDICATOR,
	ApaWarningMessage.WARNING_MANEUVER_CANCELED_RELEASE_ACCELERATOR_PEDAL,
	ApaWarningMessage.WARNING_PRESS_OK_TO_START_MANEUVER,
	ApaWarningMessage.WARNING_MANEUVER_UNAVAILABLE_H_152_ESC_OFF,
	ApaWarningMessage.WARNING_MANEUVER_CANCELED_ENGINE_STOPPED,
	ApaWarningMessage.WARNING_MANEUVER_CANCELED_GEAR_SHIFT_ACTIVATED,
	ApaWarningMessage.WARNING_MANEUVER_CANCELED_ESC_OFF,
	ApaWarningMessage.WARNING_FEATURE_UNAVAILABLE_MIRRORS_FOLDED,
	ApaWarningMessage.WARNING_MANEUVER_CANCELED_MIRRORS_FOLDED,
	ApaWarningMessage.WARNING_MANEUVER_FINISHED_NO_FRONT_OBSTACLE,
	ApaWarningMessage.WARNING_MANEUVER_UNAVAILABLE_SLOT_TOO_SMALL
})
/** APA Warning Messages */
public @interface ApaWarningMessage {
	int WARNING_NONE = 0;
	int WARNING_MANEUVER_SUSPENDED_DOOR_OPEN = 1;
	int WARNING_MANEUVER_SUSPENDED_HAND_ON_WHEEL = 2;
	int WARNING_MANEUVER_SUSPENDED_UNTIL_RESTART_ENGINE = 3;
	int WARNING_MANEUVER_SUSPENDED = 4;
	int WARNING_ASK_RESUME_MANEUVER = 5;
	int WARNING_MANEUVER_SUSPENDED_OBSTACLE_ON_PATH = 6;
	int WARNING_MANEUVER_SUSPENDED_BRAKE_TO_STOP = 7;
	int WARNING_MANEUVER_CANCELED = 8;
	int WARNING_MANEUVER_UNAVAILABLE_HAND_ON_WHEEL = 9;
	int WARNING_MANEUVER_UNAVAILABLE_DOOR_OPEN = 10;
	int WARNING_MANEUVER_UNAVAILABLE_ENGINE_STOPPED = 11;
	int WARNING_FEATURE_UNAVAILABLE_REAR_GEAR_ENGAGED = 12;
	int WARNING_FEATURE_UNAVAILABLE_SPEED_TOO_HIGH = 13;
	int WARNING_FEATURE_UNAVAILABLE = 14;
	int WARNING_FEATURE_UNAVAILABLE_TRAILER_DETECTED = 15;
	int WARNING_FEATURE_UNAVAILABLE_CRUISE_CONTROL_ACTIVATED = 16;
	int WARNING_MANEUVER_CANCELED_PARKING_BRAKE = 17;
	int WARNING_MANEUVER_CANCELED_DRIVER_DOOR_OPEN = 18;
	int WARNING_MANEUVER_CANCELED_SEAT_BELT_UNFASTEN = 19;
	int WARNING_MANEUVER_UNAVAILABLE_PARKING_BRAKE = 20;
	int WARNING_MANEUVER_CANCELED_SLOPE_TOO_HIGH = 21;
	int WARNING_MANEUVER_UNAVAILABLE_SEAT_BELT_UNFASTEN = 22;
	int WARNING_MANEUVER_UNAVAILABLE_SLOPE_TOO_HIGH = 23;
	int WARNING_MANEUVER_CANCELED_BRAKING_FAILURE = 24;
	int WARNING_BRAKE_TO_RESUME_MANEUVER = 25;
	int WARNING_FEATURE_UNAVAILABLE_VEHICLE_NOT_STOPPED = 26;
	int WARNING_MANEUVER_CANCELED_TAKE_BACK_CONTROL = 27;
	int WARNING_MANEUVER_SUSPENDED_RELEASE_ACCELERATOR_PEDAL = 28;
	int WARNING_MANEUVER_SUSPENDED_GEAR_SHIFT_ACTIVATED = 29;
	int WARNING_SELECT_TURN_INDICATOR = 30;
	int WARNING_MANEUVER_CANCELED_RELEASE_ACCELERATOR_PEDAL = 31;
	int WARNING_PRESS_OK_TO_START_MANEUVER = 32;
	int WARNING_MANEUVER_UNAVAILABLE_H_152_ESC_OFF = 33;
	int WARNING_MANEUVER_CANCELED_ENGINE_STOPPED = 34;
	int WARNING_MANEUVER_CANCELED_GEAR_SHIFT_ACTIVATED = 35;
	int WARNING_MANEUVER_CANCELED_ESC_OFF = 36;
	int WARNING_FEATURE_UNAVAILABLE_MIRRORS_FOLDED = 37;
	int WARNING_MANEUVER_CANCELED_MIRRORS_FOLDED = 38;
	int WARNING_MANEUVER_FINISHED_NO_FRONT_OBSTACLE = 39;
	int WARNING_MANEUVER_UNAVAILABLE_SLOT_TOO_SMALL = 40;
}