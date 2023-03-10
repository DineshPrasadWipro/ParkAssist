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
package com.renault.parkassist.viewmodel.sonar;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
	SensorLevel.INVISIBLE,
	SensorLevel.GREYED,
	SensorLevel.VERY_FAR,
	SensorLevel.FAR,
	SensorLevel.MEDIUM,
	SensorLevel.CLOSE,
	SensorLevel.VERY_CLOSE
})
/** Sensor level enumeration. */
public @interface SensorLevel {
	int INVISIBLE = 0;
	int GREYED = 1;
	int VERY_FAR = 2;
	int FAR = 3;
	int MEDIUM = 4;
	int CLOSE = 5;
	int VERY_CLOSE = 6;
}
