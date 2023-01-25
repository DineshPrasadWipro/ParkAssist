/**
 * This is a generated file: do not modify.
 *
 * Generated by midl v2.1.0 from 'ParkAssist/vm-specification/hfp-guidance.ts'.
 *
 * Copyright (c) 2019 Renault SW Labs
 *
 * Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all
 * intellectual property rights. Use of this software is subject to a specific
 * license granted by RENAULT S.A.S.
 */
package com.renault.parkassist.viewmodel.apa.hfp;

import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;
import androidx.lifecycle.LiveData;
import java.util.List;

/** Easy Park Assist (APA) guidance state */
public interface IHfpGuidanceViewModel {
	/** Whether the background Parallel Left is visible */
	@NonNull
	LiveData<Boolean> getBackgroundParallelLeftVisible();

	/** Whether the background Parallel Right is visible */
	@NonNull
	LiveData<Boolean> getBackgroundParallelRightVisible();

	/** Whether the vehicle center Cut and Parking Large is visible with parallel left background */
	@NonNull
	LiveData<Boolean> getParallelLeftVehicleCenterCutVisible();

	/** Whether the vehicle center Cut and Parking Large is visible with parallel right background */
	@NonNull
	LiveData<Boolean> getParallelRightVehicleCenterCutVisible();

	/** Whether the vehicle center Front is visible with parallel left background */
	@NonNull
	LiveData<Boolean> getParallelLeftVehicleCenterFrontVisible();

	/** Whether the vehicle center Front is visible with parallel right background */
	@NonNull
	LiveData<Boolean> getParallelRightVehicleCenterFrontVisible();

	/** Whether the vehicle center Back is visible with parallel left background */
	@NonNull
	LiveData<Boolean> getParallelLeftVehicleCenterBackVisible();

	/** Whether the vehicle center Back is visible with parallel right background */
	@NonNull
	LiveData<Boolean> getParallelRightVehicleCenterBackVisible();

	/** Whether the vehicle center is visible with parallel left background */
	@NonNull
	LiveData<Boolean> getParallelLeftVehicleCenterVisible();

	/** Whether the vehicle center is visible with parallel right background */
	@NonNull
	LiveData<Boolean> getParallelRightVehicleCenterVisible();

	/** Whether the Park finished icon is visible with parallel left background */
	@NonNull
	LiveData<Boolean> getParallelLeftParkVisible();

	/** Whether the Park finished icon is visible with parallel right background */
	@NonNull
	LiveData<Boolean> getParallelRightParkVisible();

	/** Whether the vehicle left is visible */
	@NonNull
	LiveData<Boolean> getParkoutLeftVehicleLeftVisible();

	/** Whether the vehicle right is visible */
	@NonNull
	LiveData<Boolean> getParkoutRightVehicleRightVisible();

	/** Whether the background Perpendicular Center is visible */
	@NonNull
	LiveData<Boolean> getBackgroundPerpendicularCenterVisible();

	/** Whether the vehicle center park is visible */
	@NonNull
	LiveData<Boolean> getPerpendicularVehicleCenterParkVisible();

	/** Whether the vehicle center front is visible in perpendicular background */
	@NonNull
	LiveData<Boolean> getPerpendicularVehicleCenterFrontVisible();

	/** Whether the vehicle center back is visible in perpendicular background */
	@NonNull
	LiveData<Boolean> getPerpendicularVehicleCenterBackVisible();

	/** Whether the stop back is active in vehicle center back view with perpendicular background */
	@NonNull
	LiveData<Boolean> getPerpendicularVehicleCenterBackStopBackVisible();

	/** Whether the stop front is active in vehicle center front view with perpendicular background */
	@NonNull
	LiveData<Boolean> getPerpendicularVehicleCenterFrontStopFrontVisible();

	/** Whether the vehicle center Cut and Parking Large is visible with perp left background */
	@NonNull
	LiveData<Boolean> getPerpendicularLeftVehicleCenterCutVisible();

	/** Whether the vehicle center Cut and Parking Large is visible with perp right background */
	@NonNull
	LiveData<Boolean> getPerpendicularRightVehicleCenterCutVisible();

	/** Current text resource id for apa guidance instruction */
	@NonNull
	LiveData<Integer> getExtendedInstruction();

	/** Current apa guidance maneuver completion */
	@NonNull
	LiveData<Integer> getManeuverCompletion();

	/** Gauge shall take forward shape if true, backward shape else */
	@NonNull
	LiveData<Boolean> getIsForwardGauge();

	/** Whether apa guidance maneuver progress (gauge) is Visible */
	@NonNull
	LiveData<Boolean> getGaugeVisible();

	/** The color of the maneuver progress bar (gauge) */
	@NonNull
	LiveData<Integer> getGaugeColor();

	/** raeb Sonar Off Visible */
	@NonNull
	LiveData<Boolean> getRaebSonarOffVisible();

	/** whether avm display is visible */
	@NonNull
	LiveData<Boolean> getIsCameraVisible();

	/**
	 * Requests the camera switch On/Off
	 *
	 * @param cameraOn true/false
	 */
	public void requestCameraSwitch(boolean cameraOn);
}
