package com.renault.parkassist.viewmodel.camera

import alliance.car.sonar.AllianceCarSonarManager
import android.annotation.SuppressLint
import android.app.Application
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.graphics.SurfaceTexture
import android.view.Gravity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import com.renault.parkassist.camera.AvmSize.*
import com.renault.parkassist.isLhd
import com.renault.parkassist.repository.apa.DisplayState
import com.renault.parkassist.repository.apa.IApaRepository
import com.renault.parkassist.repository.apa.ViewMask
import com.renault.parkassist.repository.sonar.ISonarRepository
import com.renault.parkassist.repository.surroundview.*
import com.renault.parkassist.repository.surroundview.TrunkState.TRUNK_DOOR_OPENED
import com.renault.parkassist.utility.errorLog
import com.renault.parkassist.viewmodel.LiveDataUtils
import com.renault.parkassist.viewmodel.avm.Finger
import com.renault.parkassist.viewmodel.filter
import com.renault.parkassist.viewmodel.map
import com.renault.parkassist.viewmodel.mergeNotNullWith
import org.koin.core.KoinComponent
import org.koin.core.inject

class CameraViewModel(application: Application) : ExtCameraViewModelBase(application),
    KoinComponent {

    private val surroundRepository: IExtSurroundViewRepository by inject()

    private val cameraIndicationMapper: CameraIndicationMapper by inject()

    private val apaRepository: IApaRepository by inject()

    private val sonarRepository: ISonarRepository by inject()

    private val cameraIndication: LiveData<Int> =
        Transformations.map(surroundRepository.surroundState) { state ->
            state.toCameraIndicationState()
        }

    private fun SurroundState.toCameraIndicationState() =
        cameraIndicationMapper.map(this.view)

    private val showTailgate: LiveData<Boolean> =
        Transformations.map(surroundRepository.trunkState) { tailgateWarning ->
            if (surroundRepository.isCameraOnTrunk) {
                when (tailgateWarning) {
                    TRUNK_DOOR_OPENED -> true
                    else -> false
                }
            } else false
        }.mergeNotNullWith(cameraIndication) { showTailgate, indication ->
            showTailgate &&
                (indication in listOf(
                    CameraIndication.REAR,
                    CameraIndication.TRAILER
                ))
        }

    private val easyParkIndication: LiveData<Boolean> = LiveDataUtils.combineNonNull(
        apaRepository.automaticManeuver,
        apaRepository.displayState
    ).map { (automaticManeuver, displayState) ->
        automaticManeuver && displayState == DisplayState.DISPLAY_GUIDANCE
    }

    private val rearViews = listOf(
        View.REAR_VIEW,
        View.APA_REAR_VIEW,
        View.PANORAMIC_REAR_VIEW,
        View.SETTINGS_REAR_VIEW
    )

    private val raebOffVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            sonarRepository.raebAlertState,
            sonarRepository.raebAlertEnabled,
            surroundRepository.surroundState.filter { !it.isRequest }
        )
    ) { (raebAlert, raebEnabled, surroundState) ->
        sonarRepository.raebFeaturePresent && surroundState.view in rearViews &&
            (!raebEnabled || raebAlert == AllianceCarSonarManager.RAEB_NOT_OPERATIONAL)
    }

    private val cameraMaskVisible = Transformations.map(
        LiveDataUtils.combineNonNull(
            apaRepository.displayState,
            apaRepository.viewMask
        )
    ) { (displayState, mask) ->
        (displayState == DisplayState.DISPLAY_SCANNING) &&
            (mask != ViewMask.UNAVAILABLE)
    }

    private val cameraSize: LiveData<AvmCameraSize> =
        Transformations.map(
            LiveDataUtils.combineNonNull(
                apaRepository.displayState,
                surroundRepository.surroundState.filter { !it.isRequest }
            )
        ) { (displayState, surroundState) ->
            // Filter on AVM camera screens only
            val hwConfig = surroundRepository.featureConfig
            if (hwConfig == FeatureConfig.AVM) {
                if (displayState == DisplayState.DISPLAY_NONE) {
                    when (surroundState.view) {
                        View.REAR_VIEW,
                        View.FRONT_VIEW -> AVM_STANDARD
                        View.PANORAMIC_FRONT_VIEW,
                        View.PANORAMIC_REAR_VIEW -> AVM_PANORAMIC
                        View.SIDES_VIEW -> AVM_SIDES
                        View.THREE_DIMENSION_VIEW -> AVM_3_D
                        View.SETTINGS_FRONT_VIEW,
                        View.SETTINGS_REAR_VIEW -> AVM_SETTINGS
                        View.POP_UP_VIEW -> NONE // camera size already set as match parent
                        else -> {
                            errorLog(
                                "camera",
                                "Unknown AVM Surround view state (${surroundState.view}) ",
                                "no camera size set"
                            )
                            NONE
                        }
                    }
                } else {
                    when (surroundState.view) {
                        View.REAR_VIEW,
                        View.FRONT_VIEW,
                        View.APA_REAR_VIEW,
                        View.APA_FRONT_VIEW -> AVM_APA
                        else -> {
                            errorLog(
                                "camera",
                                "Unknown AVM Surround view state in " +
                                    "APA screen(${surroundState.view}) ",
                                    "no camera size set"
                            )
                            NONE
                        }
                    }
                }
            } else {
                errorLog(
                    "camera", "not an AVM view (${surroundState.view})",
                    "no camera size set"
                )
                NONE
            }
        }.filter { (it != NONE) }.map { it.sizes }

    private val cameraOverlay: LiveData<Int> =
        Transformations.map(
            LiveDataUtils.combineNonNull(
                apaRepository.displayState,
                surroundRepository.surroundState.filter { !it.isRequest }
            )
        ) { (displayState, surroundState) ->
            val hwConfig = surroundRepository.featureConfig
            if (displayState == DisplayState.DISPLAY_NONE) {
                when (surroundState.view) {
                    View.REAR_VIEW -> {
                        if (hwConfig == FeatureConfig.AVM)
                            CameraOverlay.AVM_STANDARD
                        else
                            CameraOverlay.RVC_STANDARD
                    }
                    View.FRONT_VIEW -> CameraOverlay.AVM_STANDARD
                    View.PANORAMIC_FRONT_VIEW,
                    View.PANORAMIC_REAR_VIEW -> CameraOverlay.AVM_PANORAMIC
                    View.SIDES_VIEW -> CameraOverlay.AVM_SIDES
                    View.THREE_DIMENSION_VIEW -> CameraOverlay.AVM_3_D
                    View.POP_UP_VIEW -> CameraOverlay.AVM_POPUP
                    View.TRAILER_VIEW -> CameraOverlay.TRAILER
                    View.DEALER_VIEW -> CameraOverlay.NONE
                    View.SETTINGS_FRONT_VIEW, View.SETTINGS_REAR_VIEW -> {
                        if (hwConfig == FeatureConfig.AVM)
                            CameraOverlay.AVM_SETTINGS
                        else
                            CameraOverlay.RVC_SETTINGS
                    }
                    else -> {
                        errorLog(
                            "camera",
                            "Unknown Surround state view",
                            "no camera overlay set"
                        )
                        CameraOverlay.NONE
                    }
                }
            } else {
                when (surroundState.view) {
                    View.REAR_VIEW,
                    View.APA_REAR_VIEW -> {
                        if (hwConfig == FeatureConfig.AVM)
                            CameraOverlay.AVM_APA
                        else
                            CameraOverlay.RVC_APA
                    }
                    View.FRONT_VIEW,
                    View.APA_FRONT_VIEW -> CameraOverlay.AVM_APA
                    else -> {
                        errorLog(
                            "camera",
                            "Unknown Surround state view in APA screen",
                            "no camera overlay set"
                        )
                        CameraOverlay.NONE
                    }
                }
            }
        }

    @SuppressLint("RtlHardcoded")
    private val cameraGravity: LiveData<Int> =
        Transformations.map(surroundRepository.surroundState.filter { !it.isRequest }) {
            val (rhdGravity, opposedRhdGravity) = if (application.applicationContext.isLhd)
                Gravity.LEFT to Gravity.RIGHT
            else
                Gravity.RIGHT to Gravity.LEFT
            val isPortrait = application
                .resources.configuration.orientation == ORIENTATION_PORTRAIT
            when (it.view) {
                View.SIDES_VIEW,
                View.PANORAMIC_FRONT_VIEW,
                View.PANORAMIC_REAR_VIEW,
                View.THREE_DIMENSION_VIEW,
                View.REAR_VIEW,
                View.FRONT_VIEW -> if (isPortrait) Gravity.CENTER else (Gravity.TOP or rhdGravity)
                View.APA_REAR_VIEW -> {
                    Gravity.TOP or rhdGravity
                }
                View.APA_FRONT_VIEW -> (Gravity.TOP or rhdGravity)
                View.POP_UP_VIEW -> (Gravity.TOP or Gravity.CENTER_HORIZONTAL)
                View.TRAILER_VIEW -> (Gravity.TOP or Gravity.CENTER_HORIZONTAL)
                View.DEALER_VIEW -> (Gravity.TOP or Gravity.CENTER_HORIZONTAL)
                View.SETTINGS_FRONT_VIEW,
                View.SETTINGS_REAR_VIEW -> (Gravity.TOP or opposedRhdGravity)
                else -> {
                    errorLog("camera",
                    "Unknown Surround state view",
                    "setting default gravity")
                    Gravity.TOP or rhdGravity
                }
            }
        }

    private val cameraVisible = LiveDataUtils.combineNonNull(
        surroundRepository.surroundState.map {
            !it.isRequest && it.view != View.NO_DISPLAY
        },
        surroundRepository.errorState.map {
            it == ErrorState.ERROR_STATE_CAMERA_FAILURE
        },
        apaRepository.displayState
    )
        .map { (surroundStateNoRequest, cameraFailure, apaState) ->
            cameraFailure || surroundStateNoRequest || apaState in listOf(
                DisplayState.DISPLAY_SCANNING,
                DisplayState.DISPLAY_PARKOUT_CONFIRMATION
            )
        }

    private val cameraError = Transformations.map(surroundRepository.errorState) {
        it == ErrorState.ERROR_STATE_CAMERA_FAILURE
    }

    private val emptyStateObserver: Observer<SurroundState> = Observer { }

    private val updateCameraViewPosition: LiveData<Boolean> =
        Transformations.map(surroundRepository.surroundState) {
            when (it.view) {
                View.THREE_DIMENSION_VIEW,
                View.APA_FRONT_VIEW,
                View.APA_REAR_VIEW -> true
                else -> false
            }
        }

    init {
        // Livedatas are not triggered when no one is observing them. Or we need
        // authorizedActions before calling requestView() so we force livedata to be triggered
        // with a false observer.
        surroundRepository.surroundState.observeForever(emptyStateObserver)
    }

    private val screenPressedEnabled
        get() = (surroundRepository.surroundState.value?.view) in listOf(
            View.THREE_DIMENSION_VIEW,
            View.APA_FRONT_VIEW,
            View.APA_REAR_VIEW
        )

    override fun getShowTailgateOpenedWarning(): LiveData<Boolean> = showTailgate

    override fun getUpdateCameraViewPosition(): LiveData<Boolean> = updateCameraViewPosition

    override fun getCameraIndication(): LiveData<Int> = cameraIndication

    override fun getEasyParkIndication(): LiveData<Boolean> = easyParkIndication

    override fun getRaebOffVisible(): LiveData<Boolean> = raebOffVisible

    override fun getCameraMaskVisible(): LiveData<Boolean> = cameraMaskVisible

    override fun onCleared() {
        // Remove observer when no activities or fragments are bound to this viewmodel and the
        // system decides to shut it down.
        surroundRepository.surroundState.removeObserver(emptyStateObserver)
    }

    // FIXME : see CCSEXT-12416
    override fun screenPress(@Finger finger: Int, x: Float, y: Float) {
        if (screenPressedEnabled) {
            surroundRepository.screenPress(finger, x, y)
        }
    }

    // FIXME : see CCSEXT-12416
    override fun screenRelease(@Finger finger: Int) {
        if (screenPressedEnabled) {
            surroundRepository.screenRelease(finger)
        }
    }

    override fun setCameraViewPosition(x0: Int, y0: Int, x1: Int, y1: Int) {
        surroundRepository.setCameraPosition(x0, y0, x1, y1)
    }

    override fun getCameraSize(): LiveData<AvmCameraSize> = cameraSize

    override fun getCameraOverlay(): LiveData<Int> = cameraOverlay

    override fun getCameraGravity(): LiveData<Int> = cameraGravity

    override fun getCameraVisible(): LiveData<Boolean> = cameraVisible

    override fun getCameraError(): LiveData<Boolean> = cameraError

    override fun provideSurface(surface: SurfaceTexture) {
    }

    override fun stopCamera(surface: SurfaceTexture) {
    }
}