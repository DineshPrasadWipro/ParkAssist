/**
 * This is a generated file: do not modify.
 *
 * Generated by midl v1.5.1.
 *
 * Copyright (c) 2019 Renault SW Labs
 *
 * Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all
 * intellectual property rights. Use of this software is subject to a specific
 * license granted by RENAULT S.A.S.
 */
package com.renault.parkassist.viewmodel.sonar;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
	RctaLevel.NONE,
	RctaLevel.HIGH,
	RctaLevel.LOW
})
/** Collision risk level */
public @interface RctaLevel {
	int NONE = 0;
	int HIGH = 1;
	int LOW = 2;
}
