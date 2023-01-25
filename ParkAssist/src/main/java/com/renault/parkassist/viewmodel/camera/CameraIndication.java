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
package com.renault.parkassist.viewmodel.camera;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
	CameraIndication.NONE,
	CameraIndication.FRONT,
	CameraIndication.REAR,
	CameraIndication.TRAILER,
	CameraIndication.THREE_D
})
/**
 * Camera direction indication:
 * - `none` no camera direction indication
 * - `front` front camera indication
 * - `rear` rear camera indication
 */
public @interface CameraIndication {
	int NONE = 0;
	int FRONT = 1;
	int REAR = 2;
	int TRAILER = 3;
	int THREE_D = 4;
}