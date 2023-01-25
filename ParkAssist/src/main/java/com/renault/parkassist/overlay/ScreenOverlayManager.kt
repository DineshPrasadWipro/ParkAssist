package com.renault.parkassist.overlay

import alliance.car.windowoverlay.AllianceCarWindowOverlay
import alliance.car.windowoverlay.AllianceCarWindowOverlay.LayoutParams.FLAG_UX_TYPE_WINDOW
import alliance.car.windowoverlay.AllianceCarWindowOverlay.LayoutParams.LAYER_CRITICAL_OVERLAY
import alliancex.arch.core.logger.logD
import android.content.Context
import android.content.Intent
import android.view.Gravity.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.renault.parkassist.R
import com.renault.parkassist.isLhd
import com.renault.parkassist.routing.RouteIdentifier
import com.renault.parkassist.routing.RouteIdentifier.*
import com.renault.parkassist.ui.FullscreenActivity
import com.renault.parkassist.ui.PopUpActivity
import com.renault.parkassist.utility.errorLog
import io.reactivex.rxjava3.core.Observable
import org.koin.core.inject

class ScreenOverlayManager(private val context: Context) : OverlayManagerBase() {

    private val fullscreenLayoutParams: AllianceCarWindowOverlay.LayoutParams by inject()
    private val popupLayoutParams: AllianceCarWindowOverlay.LayoutParams by inject()

    private val toTypeMapper = mapOf(
        SURROUND_AVM_MAIN to OverlayType.FULLSCREEN,
        SURROUND_MVC_MAIN to OverlayType.FULLSCREEN,
        SURROUND_AVM_TRAILER to OverlayType.FULLSCREEN,
        SURROUND_AVM_SETTINGS to OverlayType.FULLSCREEN,
        SURROUND_MVC_SETTINGS to OverlayType.FULLSCREEN,
        SURROUND_AVM_DEALER to OverlayType.FULLSCREEN,
        SURROUND_RVC_MAIN to OverlayType.FULLSCREEN,
        SURROUND_RVC_TRAILER to OverlayType.FULLSCREEN,
        SURROUND_RVC_SETTINGS to OverlayType.FULLSCREEN,
        SURROUND_RVC_DEALER to OverlayType.FULLSCREEN,
        SURROUND_AVM_POPUP to OverlayType.POPUP,
        SONAR_POPUP to OverlayType.POPUP,
        PARKING_AVM_HFP_SCANNING to OverlayType.FULLSCREEN,
        PARKING_AVM_HFP_GUIDANCE to OverlayType.FULLSCREEN,
        PARKING_AVM_HFP_PARK_OUT to OverlayType.FULLSCREEN,
        PARKING_RVC_HFP_SCANNING to OverlayType.FULLSCREEN,
        PARKING_RVC_HFP_GUIDANCE to OverlayType.FULLSCREEN,
        PARKING_RVC_HFP_PARK_OUT to OverlayType.FULLSCREEN,
        PARKING_FAPK_SCANNING to OverlayType.FULLSCREEN,
        PARKING_FAPK_GUIDANCE to OverlayType.FULLSCREEN,
        PARKING_FAPK_PARK_OUT to OverlayType.FULLSCREEN,
        NONE to OverlayType.NONE
    )

    init {
        layoutFlags = layoutFlags or FLAG_UX_TYPE_WINDOW

        with(fullscreenLayoutParams) {
            flags = layoutFlags
            gravity = FILL
            bottomMargin = 0
            leftMargin = 0
            rightMargin = 0
            topMargin = 0
            priority = OverlayPriorities.SCREEN.value
            height = MATCH_PARENT
            width = MATCH_PARENT
        }
        with(popupLayoutParams) {
            flags = layoutFlags
            gravity = TOP or if (context.isLhd) END else START
            bottomMargin = 0
            leftMargin =
                context.resources.getDimension(R.dimen.rd_dialog_horizontal_margin).toInt()
            rightMargin =
                context.resources.getDimension(R.dimen.rd_dialog_horizontal_margin).toInt()
            topMargin = 0
            priority = OverlayPriorities.SCREEN.value
            // FIXME: use WRAP_CONTENT when fixed in AWOS
            height = context.resources.getDimension(R.dimen.sonar_widget_height).toInt()
            width = context.resources.getDimension(R.dimen.sonar_widget_width).toInt()
        }
    }

    override fun setRoute(route: Observable<RouteIdentifier>) {
        activityType = OverlayType.NONE
        serialDisposable.set(route.map {
            toTypeMapper[it] ?: OverlayType.NONE
        }.subscribe(
            { overlayType ->
                onRouteChange(overlayType!!)
            },
            {
                errorLog("internal", "error while routing screen overlay ")
            }
        ))
    }

    private fun onRouteChange(type: OverlayType) {
        logD {
            "onRouteChange REQUESTED = (activityType = $type)" +
                "CURRENT = (activityType = $activityType)"
        }

        if (activityType != type) {
            // Mind the order
            close()
            activityType = type
            if (activityType != OverlayType.NONE) open()
        }
    }

    override val activityParams = mapOf<OverlayType, ActivityParams>(
        OverlayType.FULLSCREEN to ActivityParams(
            Intent(context, FullscreenActivity::class.java),
            LAYER_CRITICAL_OVERLAY,
            fullscreenLayoutParams
        ),
        OverlayType.POPUP to ActivityParams(
            // TODO : update
            Intent(context, PopUpActivity::class.java),
            LAYER_CRITICAL_OVERLAY,
            popupLayoutParams
        )
    )
}