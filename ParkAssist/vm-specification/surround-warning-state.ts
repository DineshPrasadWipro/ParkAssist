/*
 * Copyright (c) 2020
 * Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all
 * intellectual property rights. Use of this software is subject to a specific
 * license granted by RENAULT S.A.S.
 */

/**
 * Type of warning to display to the user.
 */
type SurroundWarningType =
    | 'speed_nok'
    | 'camera_soiled'
    | 'camera_misaligned'
    | 'obstacle_detected'
    | 'trailer_access_limited'
    | 'trailer_not_detected'
    | 'none'

/**
 * Surround view warnings state
 * @model com/renault/parkassist/viewmodel/surround
 */
interface SurroundWarningStateViewModel {

    /**
     * The warning type sent to the view
     */
    warning : SurroundWarningType;

    /**
     * Ack the previously received warning
     */
    acknowledge();
}
