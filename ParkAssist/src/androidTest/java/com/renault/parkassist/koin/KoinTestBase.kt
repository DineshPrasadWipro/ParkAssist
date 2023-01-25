package com.renault.parkassist.koin

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.renault.parkassist.repository.apa.IApaRepository
import com.renault.parkassist.repository.apa.mock.ApaRepositoryMock
import com.renault.parkassist.repository.settings.ISoundRepository
import com.renault.parkassist.repository.settings.mock.SoundRepositoryMock
import com.renault.parkassist.repository.sonar.ISonarRepository
import com.renault.parkassist.repository.sonar.mock.SonarRepositoryMock
import com.renault.parkassist.repository.surroundview.Action.*
import com.renault.parkassist.repository.surroundview.FeatureConfig
import com.renault.parkassist.repository.surroundview.IExtSurroundViewRepository
import com.renault.parkassist.repository.surroundview.SurroundState
import com.renault.parkassist.repository.surroundview.View
import com.renault.parkassist.repository.surroundview.View.*
import com.renault.parkassist.surround.ExtSurroundViewRepositoryMock
import io.mockk.clearAllMocks
import org.koin.core.KoinComponent
import org.koin.core.inject

abstract class KoinTestBase : KoinComponent {

    private val _automaticParkAssistRepository: IApaRepository by inject()
    protected lateinit var automaticParkAssistRepository: ApaRepositoryMock

    private val _surroundViewRepository: IExtSurroundViewRepository by inject()
    protected lateinit var surroundViewRepository: ExtSurroundViewRepositoryMock

    private val _sonarRepository: ISonarRepository by inject()
    protected lateinit var sonarRepository: SonarRepositoryMock

    private val _soundRepository: ISoundRepository by inject()
    protected lateinit var soundRepository: SoundRepositoryMock

    protected val context: Context = ApplicationProvider.getApplicationContext()

    init {
        runOnUiThread {
            automaticParkAssistRepository = _automaticParkAssistRepository as ApaRepositoryMock
            surroundViewRepository = _surroundViewRepository as ExtSurroundViewRepositoryMock
            sonarRepository = _sonarRepository as SonarRepositoryMock
            soundRepository = _soundRepository as SoundRepositoryMock
        }
        clearAll()
    }

    protected fun clearAll() {
        clearAllMocks()
        runOnUiThread {
            sonarRepository.reset()
            surroundViewRepository.reset()
            automaticParkAssistRepository.reset()
            soundRepository.reset()
        }
    }

    protected fun setVehicleConfiguration(config: ParkAssistHwConfig) {
        when (config) {
            ParkAssistHwConfig.AVM -> surroundViewRepository.featureConfig = FeatureConfig.AVM
            ParkAssistHwConfig.RVC -> surroundViewRepository.featureConfig = FeatureConfig.RVC
            ParkAssistHwConfig.SONAR -> {
                surroundViewRepository.featureConfig = FeatureConfig.NONE
                sonarRepository.upaFkpVisualFeedbackFeaturePresent = true
                sonarRepository.upaRearFeaturePresent = true
            }
            ParkAssistHwConfig.SONAR_RR -> {
                surroundViewRepository.featureConfig = FeatureConfig.NONE
                sonarRepository.upaFkpVisualFeedbackFeaturePresent = false
                sonarRepository.upaRearFeaturePresent = true
                sonarRepository.upaFrontFeaturePresent = false
            }
            ParkAssistHwConfig.MVC -> {
                surroundViewRepository.featureConfig = FeatureConfig.MVC
                sonarRepository.upaFkpVisualFeedbackFeaturePresent = true
                sonarRepository.upaRearFeaturePresent = true
            }
            ParkAssistHwConfig.NONE -> {
                surroundViewRepository.featureConfig = FeatureConfig.NONE
                sonarRepository.upaFkpVisualFeedbackFeaturePresent = false
            }
        }
    }

    enum class ButtonsMode {
        REGULATORY,
        ALL
    }

    protected fun displayCameraView(
        @View view: Int = NO_DISPLAY,
        buttonMode: ButtonsMode = ButtonsMode.REGULATORY,
        forceCloseable: Boolean = false
    ) {
        val actions: MutableList<Int> = mutableListOf()
        when (surroundViewRepository.featureConfig) {
            FeatureConfig.AVM -> when (buttonMode) {
                ButtonsMode.REGULATORY -> {
                    actions.add(SELECT_STANDARD_VIEW)
                    actions.add(SELECT_PANORAMIC_VIEW)
                    actions.add(ACTIVATE_SETTINGS_VIEW)
                    actions.add(SELECT_THREE_DIMENSION_VIEW)
                    actions.add(SELECT_SIDES_VIEW)
                }
                ButtonsMode.ALL -> {
                    actions.add(SELECT_STANDARD_VIEW)
                    actions.add(SELECT_PANORAMIC_VIEW)
                    actions.add(SELECT_SIDES_VIEW)
                    actions.add(SELECT_THREE_DIMENSION_VIEW)
                    actions.add(ACTIVATE_SETTINGS_VIEW)
                    actions.add(CLOSE_VIEW)
                }
            }
            FeatureConfig.RVC -> when (buttonMode) {
                ButtonsMode.REGULATORY -> {
                    actions.add(ACTIVATE_SETTINGS_VIEW)
                }
                ButtonsMode.ALL -> {
                    actions.add(ACTIVATE_SETTINGS_VIEW)
                    actions.add(CLOSE_VIEW)
                }
            }
        }
        if (POP_UP_VIEW == view || forceCloseable)
            actions.add(CLOSE_VIEW)

        if (TRAILER_VIEW == view)
            actions.add(ACTIVATE_TRAILER_VIEW)

        if (SETTINGS_REAR_VIEW == view || SETTINGS_FRONT_VIEW == view)
            actions.add(BACK_FROM_SETTINGS_VIEW)

        surroundViewRepository.surroundState.postValue(
            SurroundState(view, true)
        )
        surroundViewRepository.surroundState.postValue(
            SurroundState(view, false)
        )
        surroundViewRepository.authorizedActions.postValue(
            actions
        )
        surroundViewRepository.startRendering()
    }
}