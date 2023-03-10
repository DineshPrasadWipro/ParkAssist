/**
 * This is a generated file: do not modify.
 *
 * Generated by midl v2.1.0 from 'ParkAssist/vm-specification/camera.ts'.
 *
 * Copyright (c) 2019 Renault SW Labs
 *
 * Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all
 * intellectual property rights. Use of this software is subject to a specific
 * license granted by RENAULT S.A.S.
 */
package com.renault.parkassist.viewmodel.camera;

import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;
import androidx.lifecycle.LiveData;
import java.util.List;

/** Camera component reused in various views (RVC, AVM, APA...) */
public interface ICameraViewModel {
	/** Whether tailgate opened warning message is displayed or not */
	@NonNull
	LiveData<Boolean> getShowTailgateOpenedWarning();

	/** Camera indication */
	@NonNull
	LiveData<Integer> getCameraIndication();

	/** EasyPark indication */
	@NonNull
	LiveData<Boolean> getEasyParkIndication();

	/** raeb Off Visible */
	@NonNull
	LiveData<Boolean> getRaebOffVisible();

	/** Camera preview size in pixels */
	@NonNull
	LiveData<AvmCameraSize> getCameraSize();

	/** Camera overlay */
	@NonNull
	LiveData<Integer> getCameraOverlay();

	/** Camera gravity */
	@NonNull
	LiveData<Integer> getCameraGravity();

	/** Show camera stream if true, else hide it */
	@NonNull
	LiveData<Boolean> getCameraVisible();

	/** Whether or not the camera is in error state */
	@NonNull
	LiveData<Boolean> getCameraError();

	/** Whether or not the camera view position has to be updated */
	@NonNull
	LiveData<Boolean> getUpdateCameraViewPosition();

	/** Whether we display camera mask or not */
	@NonNull
	LiveData<Boolean> getCameraMaskVisible();

	/**
	 * To be called when we have new coordinates for a finger on the screen.
	 *
	 * @param finger identifier of the finger
	 * @param x position of the finger on the x-axis
	 * @param y position of the finger on the y-axis
	 */
	public void screenPress(
		@Finger int finger,
		float x,
		float y
	);

	/**
	 * To be called when a finger left the screen.
	 *
	 * @param finger identifier of the finger
	 */
	public void screenRelease(@Finger int finger);

	/**
	 * To be called to notify the service that the camera view position has changed on screen.
	 *
	 * @param x0 left side x position
	 * @param y0 top side y position
	 * @param x1 right side x position
	 * @param y1 bottom side y position
	 */
	public void setCameraViewPosition(
		int x0,
		int y0,
		int x1,
		int y1
	);
}
