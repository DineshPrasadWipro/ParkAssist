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
	ScanningSide.SCANNING_SIDE_NONE,
	ScanningSide.SCANNING_SIDE_RIGHT,
	ScanningSide.SCANNING_SIDE_LEFT,
	ScanningSide.SCANNING_SIDE_UNAVAILABLE
})
public @interface ScanningSide {
	int SCANNING_SIDE_NONE = 0;
	int SCANNING_SIDE_RIGHT = 1;
	int SCANNING_SIDE_LEFT = 2;
	int SCANNING_SIDE_UNAVAILABLE = 3;
}
