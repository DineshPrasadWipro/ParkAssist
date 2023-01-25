/*
 * Copyright (c) 2019
 * Developed by Renault SW Labs, an affiliate of RENAULT S.A.S. which holds all
 * intellectual property rights. Use of this software is subject to a specific
 * license granted by RENAULT S.A.S.
 */


/**
 * Handles Rear Cross Traffic Alert state.
 *
 * _Rear Cross Traffic Alert
 *
 * @model com/renault/parkassist/viewmodel/sonar
 */
interface SonarAlertsViewModel {
    /**
     * If left RCTA flag should be visible
     * @default false
     */
    rctaLeftVisible : boolean

    /**
     * If right RCTA flag should be visible
     * @default false
     */
    rctaRightVisible : boolean

    /**
     * If RAEB flags should be visible
     * @default false
     */
    raebVisible : boolean

}