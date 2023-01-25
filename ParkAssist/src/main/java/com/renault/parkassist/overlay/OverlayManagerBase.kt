package com.renault.parkassist.overlay

import alliance.car.windowoverlay.AllianceCarWindowOverlay
import alliance.car.windowoverlay.AllianceCarWindowOverlay.LayoutParams.*
import alliancex.arch.core.logger.logD
import android.content.Intent
import androidx.core.os.TraceCompat.*
import com.renault.parkassist.routing.RouteIdentifier
import com.renault.parkassist.utility.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.SerialDisposable
import org.koin.core.KoinComponent
import org.koin.core.inject

abstract class OverlayManagerBase : KoinComponent {

    private val awosManager: IAllianceCarWindowOverlayManager by inject()

    protected var activityType: OverlayType =
        OverlayType.NONE

    protected var overlay: AllianceCarWindowOverlay? = null

    protected var layoutFlags = FLAG_INSET_TOP_BAR or FLAG_INSET_BOTTOM_BAR or
        FLAG_INSET_LEFT_BAR or FLAG_INSET_RIGHT_BAR or FLAG_DISMISS_KEYGUARD

    protected abstract val activityParams: Map<OverlayType, ActivityParams>

    protected val serialDisposable = SerialDisposable()

    private val listener =
        object : AllianceCarWindowOverlay.IAllianceCarWindowOverlayEventListener {
            override fun onAdd(overlay: AllianceCarWindowOverlay?) {
                overlayInfoLog("added", "overlay")
            }

            override fun onRemove(overlay: AllianceCarWindowOverlay?) {
                overlayInfoLog("removed", "overlay")
            }

            override fun onUpdate(overlay: AllianceCarWindowOverlay?) {
                overlayInfoLog("updated", "overlay")
            }

            override fun onError(throwable: Throwable?) {
                warningLog("internal", "AWOS Error:" + throwable?.message)
            }
        }

    abstract fun setRoute(route: Observable<RouteIdentifier>)

    protected fun open() {

        logD { "open activityType = $activityType" }

        // Sanity checks
        val params = activityParams[activityType] ?: return

        beginSection("Overlay open")

        if (overlay != null) {
            errorLog("internal", "Another overlay is already displayed")
            endSection()
            return
        }

        params.layoutParams.flags = getFlags()

        logD { "open: create overlay with layoutParams=${params.layoutParams}" }

        try {
            overlay = awosManager.createOverlay(
                params.intent,
                params.layer
            )
            overlay?.registerListener(listener)
        } catch (e: RuntimeException) {
            wtfLog("internal", "Could not create overlay or register listener: ${e.message}")
            endSection()
            return
        }

        overlay?.add(params.layoutParams) ?: wtfLog(
            "internal",
            "unknown error, unable to create overlay"
        )
        endSection()
    }

    private fun getFlags(): Int =
        layoutFlags or FLAG_MODELESS

    protected fun close() {
        logD { "close activityType = $activityType" }
        if (overlay == null) {
            warningLog("internal", "trying to close non existing overlay")
            return
        }

        overlay?.remove()
        overlay?.dispose()
        overlay = null
    }

    protected data class ActivityParams(
        val intent: Intent,
        @AllianceCarWindowOverlay.LayoutParams.OverlayLayer val layer: Int,
        val layoutParams: AllianceCarWindowOverlay.LayoutParams
    )

    enum class OverlayType {
        FULLSCREEN,
        POPUP,
        WARNING,
        NONE
    }
}