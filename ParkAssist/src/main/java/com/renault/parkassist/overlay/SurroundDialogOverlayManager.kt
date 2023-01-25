package com.renault.parkassist.overlay

import alliance.car.windowoverlay.AllianceCarWindowOverlay.*
import alliance.car.windowoverlay.AllianceCarWindowOverlay.LayoutParams.*
import android.content.Context
import android.content.Intent
import android.view.Gravity.FILL
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.renault.parkassist.routing.RouteIdentifier
import com.renault.parkassist.ui.surround.SurroundWarningActivity
import com.renault.parkassist.utility.errorLog
import io.reactivex.rxjava3.core.Observable
import org.koin.core.inject

class SurroundDialogOverlayManager(private val context: Context) :
    OverlayManagerBase() {

    private val layoutParamsProvider: LayoutParamsProvider by inject()

    private val layoutParams: LayoutParams

    init {
        activityType = OverlayType.WARNING

        layoutFlags =
            layoutFlags and FLAG_INSET_TOP_BAR.inv() or FLAG_UX_TYPE_WINDOW

        layoutParams = layoutParamsProvider.makeLayoutParams(
            MATCH_PARENT,
            MATCH_PARENT,
            0,
            0,
            0,
            0,
            FILL,
            layoutFlags,
            OverlayPriorities.DIALOG.value
        )
    }

    override val activityParams: Map<OverlayType, ActivityParams> = mapOf(
        OverlayType.WARNING to ActivityParams(
            Intent(context, SurroundWarningActivity::class.java),
            LAYER_CRITICAL_OVERLAY,
            layoutParams
        )
    )

    override fun setRoute(route: Observable<RouteIdentifier>) {
        serialDisposable.set(route.filter {
            listOf(
                RouteIdentifier.SURROUND_WARNING,
                RouteIdentifier.NONE
            ).contains(it)
        }.map {
            when (it) {
                RouteIdentifier.SURROUND_WARNING -> OverlayType.WARNING
                else -> OverlayType.NONE
            }
        }.subscribe(
            { overlayType ->
                onRouteChange(overlayType!!)
            },
            {
                errorLog("internal", "error while routing surround overlay ")
            }
        ))
    }

    private fun onRouteChange(type: OverlayType) {
        when (type) {
            OverlayType.WARNING -> {
                close()
                open()
            }
            else -> close()
        }
    }
}