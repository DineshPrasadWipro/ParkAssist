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
	Finger.FIRST,
	Finger.SECOND
})
/**
 * Finger identification:
 * - `first` 1st finger identifier
 * - `second` 2nd finger identifier
 */
public @interface Finger {
	int FIRST = 0;
	int SECOND = 1;
}