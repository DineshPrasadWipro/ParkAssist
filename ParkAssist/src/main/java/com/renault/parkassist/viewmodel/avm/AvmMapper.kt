package com.renault.parkassist.viewmodel.avm

import com.renault.parkassist.repository.surroundview.Action
import com.renault.parkassist.repository.surroundview.View
import com.renault.parkassist.utility.mapNullable
import com.renault.parkassist.utility.unMap

class AvmMapper {

    private val avmButtonSelectedMap = listOf(
        View.FRONT_VIEW to AvmModeSelected.STANDARD,
        View.REAR_VIEW to AvmModeSelected.STANDARD,
        View.PANORAMIC_REAR_VIEW to AvmModeSelected.PANORAMIC,
        View.PANORAMIC_FRONT_VIEW to AvmModeSelected.PANORAMIC,
        View.SIDES_VIEW to AvmModeSelected.SIDES,
        View.THREE_DIMENSION_VIEW to AvmModeSelected.THREE_D
    )

    private val requestModeMap = listOf(
        Action.SELECT_STANDARD_VIEW to AvmModeRequest.STANDARD,
        Action.SELECT_PANORAMIC_VIEW to AvmModeRequest.PANORAMIC,
        Action.SELECT_SIDES_VIEW to AvmModeRequest.SIDES,
        Action.SELECT_THREE_DIMENSION_VIEW to AvmModeRequest.VIEW_3_D,
        Action.ACTIVATE_MANEUVER_VIEW to AvmModeRequest.MANEUVER,
        Action.CLOSE_VIEW to AvmModeRequest.CLOSE,
        Action.ACTIVATE_SETTINGS_VIEW to AvmModeRequest.SETTINGS,
        Action.BACK_FROM_SETTINGS_VIEW to AvmModeRequest.BACK_FROM_SETTINGS
    )

    @Action
    fun unMapModeRequest(@AvmModeRequest input: Int) = requestModeMap.unMap(input)

    @AvmModeSelected
    fun mapAvmButtonSelected(@View input: Int): Int = avmButtonSelectedMap.mapNullable(input)
        ?: AvmModeSelected.STANDARD
}