package com.renault.parkassist.overlay

import alliance.car.windowoverlay.AllianceCarWindowOverlay
import android.content.Intent

interface IAllianceCarWindowOverlayManager {
    fun createOverlay(intent: Intent, layer: Int): AllianceCarWindowOverlay?
}