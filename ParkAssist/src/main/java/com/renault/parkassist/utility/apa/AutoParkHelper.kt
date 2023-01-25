package com.renault.parkassist.utility.apa

import com.renault.parkassist.repository.apa.AutoParkHelper
import com.renault.parkassist.utility.catchStringMap

fun Int.displayStateToString() = catchStringMap {
    AutoParkHelper.displayStateToString(this)
}

fun Int.errorStatusToString() = catchStringMap {
    AutoParkHelper.errorStatusToString(this)
}

fun Int.extendedInstructionToString() = catchStringMap {
    AutoParkHelper.extendedInstructionToString(this)
}

fun Int.featureConfigurationToString() = catchStringMap {
    AutoParkHelper.featureConfigurationToString(this)
}

fun Int.maneuverMoveToString() = catchStringMap {
    AutoParkHelper.maneuverMoveToString(this)
}

fun Int.maneuverSelectionToString() = catchStringMap {
    AutoParkHelper.maneuverSelectionToString(this)
}

fun Int.maneuverTypeToString() = catchStringMap {
    AutoParkHelper.maneuverTypeToString(this)
}

fun Int.maneuverStartSwitchToString() = catchStringMap {
    AutoParkHelper.maneuverStartSwitchToString(this)
}

fun Int.parkingSlotLeftSelectedToString() = catchStringMap {
    AutoParkHelper.parkingSlotLeftSelectedToString(this)
}

fun Int.parkingSlotLeftSuitableToString() = catchStringMap {
    AutoParkHelper.parkingSlotLeftSuitableToString(this)
}

fun Int.parkingSlotRightSelectedToString() = catchStringMap {
    AutoParkHelper.parkingSlotRightSelectedToString(this)
}

fun Int.parkingSlotRightSuitableToString() = catchStringMap {
    AutoParkHelper.parkingSlotRightSuitableToString(this)
}

fun Int.statusDisplayToString() = catchStringMap {
    AutoParkHelper.statusDisplayToString(this)
}

fun Int.scanningSideToString() = catchStringMap {
    AutoParkHelper.scanningSideToString(this)
}

fun Int.warningMessageToString() = catchStringMap {
    AutoParkHelper.warningMessageToString(this)
}

fun Int.viewMaskToString() = catchStringMap {
    AutoParkHelper.viewMaskToString(this)
}