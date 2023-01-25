package com.renault.parkassist.viewmodel.mvc

import com.renault.parkassist.repository.surroundview.Action
import com.renault.parkassist.repository.surroundview.View
import com.renault.parkassist.utility.mapNullable
import com.renault.parkassist.utility.unMap
import com.renault.parkassist.viewmodel.avm.AvmModeSelected

class MvcMapper {
    private val mvcButtonSelectedMap = listOf(
    View.FRONT_VIEW to MvcModeSelected.FRONT,
    View.REAR_VIEW to MvcModeSelected.REAR,
    View.LEFT_VIEW to MvcModeSelected.LEFT,
    View.RIGHT_VIEW to MvcModeSelected.RIGHT,
)

private val requestModeMap = listOf(
    Action.SELECT_FRONT_CAMERA to MvcModeRequest.FRONT,
    Action.SELECT_REAR_CAMERA to MvcModeRequest.REAR,
    Action.SELECT_LEFT_CAMERA to MvcModeRequest.LEFT,
    Action.SELECT_RIGHT_CAMERA to MvcModeRequest.RIGHT,
    Action.CLOSE_VIEW to MvcModeRequest.CLOSE,
    Action.ACTIVATE_SETTINGS_VIEW to MvcModeRequest.SETTINGS,
    Action.BACK_FROM_SETTINGS_VIEW to MvcModeRequest.BACK_FROM_SETTINGS
)

@Action
fun unMapModeRequest(@MvcModeRequest input: Int) = requestModeMap.unMap(input)

@AvmModeSelected
fun mapAvmButtonSelected(@View input: Int): Int = mvcButtonSelectedMap.mapNullable(input)
    ?: MvcModeSelected.FRONT
}