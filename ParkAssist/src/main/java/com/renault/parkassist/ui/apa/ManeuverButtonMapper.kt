package com.renault.parkassist.ui.apa

import com.renault.parkassist.repository.apa.DisplayState
import com.renault.parkassist.repository.apa.ManeuverSelection

class ManeuverButtonMapper {

    fun toEnabled(values: Pair<Int, Int>): Boolean = toEnabled(values.first, values.second)

    private fun toEnabled(maneuver: Int, displayState: Int): Boolean =
        when {
            maneuver == ManeuverSelection.SELECTED -> true
            maneuver == ManeuverSelection.NOT_SELECTED
                && displayState != DisplayState.DISPLAY_GUIDANCE -> true
            else -> false
        }

    fun toEnabled(maneuver: Int): Boolean =
        when (maneuver) {
            ManeuverSelection.SELECTED -> true
            ManeuverSelection.NOT_SELECTED -> true
            else -> false
        }

    fun toSelected(maneuver: Int): Boolean =
        when (maneuver) {
            ManeuverSelection.SELECTED -> true
            ManeuverSelection.NOT_SELECTED -> false
            else -> false
        }
}