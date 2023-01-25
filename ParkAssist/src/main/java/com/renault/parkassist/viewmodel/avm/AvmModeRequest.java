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
package com.renault.parkassist.viewmodel.avm;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
	AvmModeRequest.STANDARD,
	AvmModeRequest.PANORAMIC,
	AvmModeRequest.SIDES,
	AvmModeRequest.VIEW_3_D,
	AvmModeRequest.MANEUVER,
	AvmModeRequest.CLOSE,
	AvmModeRequest.SETTINGS,
	AvmModeRequest.BACK_FROM_SETTINGS
})
/**
 * View mode request, asked by a user when he clicks on a bottom navigation view button in the AVM app:
 * - `standard` requests for a standard view (bird + rear/front view)
 * - `panoramic` requests for a panoramic view (rear or front panoramic view)
 * - `sides` requests for a sides view
 * - `view-3d` request for a 3D view
 * - 'settings' requests a settings view
 * - 'back-from-settings' requests a back from settings view
 */
public @interface AvmModeRequest {
	int STANDARD = 0;
	int PANORAMIC = 1;
	int SIDES = 2;
	int VIEW_3_D = 3;
	int MANEUVER = 4;
	int CLOSE = 5;
	int SETTINGS = 6;
	int BACK_FROM_SETTINGS = 7;
}
