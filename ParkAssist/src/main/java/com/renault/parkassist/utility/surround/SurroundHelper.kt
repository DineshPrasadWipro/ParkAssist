package com.renault.parkassist.utility.surround

import alliance.car.surroundview.SurroundViewHelper
import com.renault.parkassist.utility.catchStringMap

fun Int.actionToString() = catchStringMap {
    SurroundViewHelper.actionToString(this)
}

fun Int.displayModeToString() = catchStringMap {
    SurroundViewHelper.displayModeToString(this)
}

fun Int.viewToString() = catchStringMap {
    SurroundViewHelper.viewToString(this)
}

fun Int.warningStateToString() = catchStringMap {
    SurroundViewHelper.warningStateToString(this)
}

fun Int.errorStateToString() = catchStringMap {
    SurroundViewHelper.errorStateToString(this)
}

fun Int.trailerPresenceToString() = catchStringMap {
    SurroundViewHelper.trailerPresenceToString(this)
}

fun Int.trunkStateToString() = catchStringMap {
    SurroundViewHelper.trunkStateToString(this)
}

fun Int.userAcknowledgementToString() = catchStringMap {
    SurroundViewHelper.userAcknowledgementToString(this)
}

fun Int.fingerTouchToString() = catchStringMap {
    SurroundViewHelper.fingerTouchToString(this)
}

fun Int.availabilityStateToString() = catchStringMap {
    SurroundViewHelper.availabilityStateToString(this)
}