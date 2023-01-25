package com.renault.parkassist.overlay

import alliance.car.windowoverlay.AllianceCarWindowOverlay

class LayoutParamsProvider {

    fun makeLayoutParams(
        width: Int,
        height: Int,
        topMargin: Int,
        _rightMargin: Int,
        _bottomMargin: Int,
        _leftMargin: Int,
        _gravity: Int,
        _flags: Int,
        _priority: Int
    ): AllianceCarWindowOverlay.LayoutParams = AllianceCarWindowOverlay.LayoutParams(
        width,
        height,
        topMargin,
        _rightMargin,
        _bottomMargin,
        _leftMargin,
        _gravity,
        _flags,
        _priority
    )
}