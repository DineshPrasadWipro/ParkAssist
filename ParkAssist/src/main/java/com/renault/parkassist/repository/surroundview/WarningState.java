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
package com.renault.parkassist.repository.surroundview;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
	WarningState.WARNING_STATE_NONE,
	WarningState.WARNING_STATE_SPEED_NOK,
	WarningState.WARNING_STATE_CAMERA_MISALIGNED,
	WarningState.WARNING_STATE_CAMERA_SOILED,
	WarningState.WARNING_STATE_TRAILER_NOT_DETECTED,
	WarningState.WARNING_STATE_OBSTACLE_PRESENT,
	WarningState.WARNING_STATE_TRAILER_ACCESS_LIMITED
})
public @interface WarningState {
	int WARNING_STATE_NONE = 0;
	int WARNING_STATE_SPEED_NOK = 1;
	int WARNING_STATE_CAMERA_MISALIGNED = 2;
	int WARNING_STATE_CAMERA_SOILED = 3;
	int WARNING_STATE_TRAILER_NOT_DETECTED = 4;
	int WARNING_STATE_OBSTACLE_PRESENT = 5;
	int WARNING_STATE_TRAILER_ACCESS_LIMITED = 6;
}
