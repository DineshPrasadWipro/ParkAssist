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
package com.renault.parkassist.repository.apa;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
	DisplayState.DISPLAY_NONE,
	DisplayState.DISPLAY_SCANNING,
	DisplayState.DISPLAY_PARKOUT_CONFIRMATION,
	DisplayState.DISPLAY_GUIDANCE
})
/**
 * Auto Park Assist display states
 * - `display none` no display
 * - `display scanning` scanning display
 * - `display parkout confirmation` parkout display
 * - `display guidance` scanning display
 */
public @interface DisplayState {
	int DISPLAY_NONE = 0;
	int DISPLAY_SCANNING = 1;
	int DISPLAY_PARKOUT_CONFIRMATION = 2;
	int DISPLAY_GUIDANCE = 3;
}
