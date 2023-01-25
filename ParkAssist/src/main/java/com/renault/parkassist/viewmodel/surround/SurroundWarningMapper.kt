package com.renault.parkassist.viewmodel.surround

import com.renault.parkassist.repository.surroundview.WarningState
import com.renault.parkassist.utility.map

class SurroundWarningMapper {

    private val warningTypeMap = listOf(
        WarningState.WARNING_STATE_NONE to SurroundWarningType.NONE,
        WarningState.WARNING_STATE_SPEED_NOK to SurroundWarningType.SPEED_NOK,
        WarningState.WARNING_STATE_CAMERA_MISALIGNED to SurroundWarningType.CAMERA_MISALIGNED,
        WarningState.WARNING_STATE_CAMERA_SOILED to SurroundWarningType.CAMERA_SOILED,
        WarningState.WARNING_STATE_OBSTACLE_PRESENT to SurroundWarningType.OBSTACLE_DETECTED,
        WarningState.WARNING_STATE_TRAILER_ACCESS_LIMITED
            to SurroundWarningType.TRAILER_ACCESS_LIMITED,
        WarningState.WARNING_STATE_TRAILER_NOT_DETECTED to SurroundWarningType.TRAILER_NOT_DETECTED
    )

    @SurroundWarningType
    fun mapSurroundWarningType(@WarningState input: Int) = warningTypeMap.map(input)
}