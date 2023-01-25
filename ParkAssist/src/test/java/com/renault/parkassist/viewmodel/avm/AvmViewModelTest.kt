package com.renault.parkassist.viewmodel.avm

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.test.filters.SmallTest
import com.renault.parkassist.TestBase
import com.renault.parkassist.TestUtils
import com.renault.parkassist.repository.apa.ApaRepository
import com.renault.parkassist.repository.apa.FeatureConfig
import com.renault.parkassist.repository.apa.IApaRepository
import com.renault.parkassist.repository.surroundview.Action.*
import com.renault.parkassist.repository.surroundview.IExtSurroundViewRepository
import com.renault.parkassist.repository.surroundview.SurroundState
import com.renault.parkassist.repository.surroundview.SurroundViewRepository
import com.renault.parkassist.repository.surroundview.TrailerPresence
import com.renault.parkassist.repository.surroundview.View.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import junit.framework.TestCase.assertFalse
import kotlin.test.assertTrue
import org.junit.*
import org.koin.dsl.module

@SmallTest
class AvmViewModelTest : TestBase() {

    private val surroundViewRepository: SurroundViewRepository = mockk(relaxed = true)
    private val apaRepository: ApaRepository = mockk(relaxed = true)
    private lateinit var avmStateViewModel: AvmStateViewModel

    private val surroundAuthorizedActionsLiveData =
        MutableLiveData<List<Int>>()
    private val surroundStateMocked =
        MutableLiveData<SurroundState>()
    private val trailerPresenceLiveData = MutableLiveData<Int>()

    private lateinit var lifecycleOwner: LifecycleOwner

    override val koinModule = module {
        single<IExtSurroundViewRepository>(override = true) { surroundViewRepository }
        single<IApaRepository>(override = true) { apaRepository }
        factory { AvmMapper() }
    }

    init {
        mockkStatic("com.renault.parkassist.utility.UiUtilsKt")
    }

    @Before
    override fun setup() {
        super.setup()
        every {
            surroundViewRepository.authorizedActions
        } returns surroundAuthorizedActionsLiveData
        every {
            surroundViewRepository.surroundState
        } returns surroundStateMocked
        every {
            surroundViewRepository.trailerPresence
        } returns trailerPresenceLiveData

        lifecycleOwner = TestUtils.mockLifecycleOwner()
        avmStateViewModel = AvmStateViewModel(mockk())
        surroundStateMocked.postValue(SurroundState(NO_DISPLAY))
        surroundAuthorizedActionsLiveData.postValue(emptyList())
    }

    @Test
    fun `Should return close visible true when surroundViewRepository authorizedActions contains CLOSE_VIEW`() { // ktlint-disable max-line-length
        var testedVisibility = false
        avmStateViewModel.closeVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedVisibility = visible })
        surroundAuthorizedActionsLiveData.postValue(
            listOf(CLOSE_VIEW)
        )
        Assert.assertEquals(true, testedVisibility)
    }

    @Test
    fun `Should return maneuver visible true when surroundViewRepository viewState authorizedActions set to ACTIVATE_MANEUVER_VIEW`() { // ktlint-disable max-line-length
        var testedVisibility = false
        avmStateViewModel.maneuverVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedVisibility = visible })
        surroundAuthorizedActionsLiveData.postValue(
            listOf(ACTIVATE_MANEUVER_VIEW)
        )
        Assert.assertEquals(true, testedVisibility)
    }

    @Test
    fun `Should return settings visible true when surroundViewRepository viewState authorizedActions set to ACTIVATE_SETTINGS_VIEW`() { // ktlint-disable max-line-length
        var testedVisibility = false
        avmStateViewModel.settingsVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedVisibility = visible })
        surroundAuthorizedActionsLiveData.postValue(
            listOf(ACTIVATE_SETTINGS_VIEW)
        )
        Assert.assertEquals(true, testedVisibility)
    }

    @Test
    fun `Should return easypark shortcut visibility accordingly to apaRepository configuration`() { // ktlint-disable max-line-length
        // not visible if configuration is NONE
        every { apaRepository.featureConfiguration } returns FeatureConfig.NONE
        Assert.assertEquals(false, avmStateViewModel.easyparkShortcutVisible)
        // visible else
        every { apaRepository.featureConfiguration } returns FeatureConfig.HFP
        Assert.assertEquals(true, avmStateViewModel.easyparkShortcutVisible)
        every { apaRepository.featureConfiguration } returns FeatureConfig.FAPK
        Assert.assertEquals(true, avmStateViewModel.easyparkShortcutVisible)
        every { apaRepository.featureConfiguration } returns FeatureConfig.FPK
        Assert.assertEquals(true, avmStateViewModel.easyparkShortcutVisible)
        every { apaRepository.featureConfiguration } returns FeatureConfig.HFPB
        Assert.assertEquals(true, avmStateViewModel.easyparkShortcutVisible)
    }

    @Test
    fun `Should return trailer visible true when surroundViewRepository viewState authorizedActions set to ACTIVATE_TRAILER_VIEW`() { // ktlint-disable max-line-length
        var testedVisibility = false
        avmStateViewModel.trailerVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedVisibility = visible })
        surroundAuthorizedActionsLiveData.postValue(
            listOf(ACTIVATE_TRAILER_VIEW)
        )
        Assert.assertEquals(true, testedVisibility)
    }

    @Test
    fun `Should return selectPanoramicView visible true when surroundViewRepository authorizedActions set to SELECT_PANORAMIC_VIEW`() { // ktlint-disable max-line-length
        var testedVisibility = false
        avmStateViewModel.selectPanoramicViewVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedVisibility = visible })
        surroundAuthorizedActionsLiveData.postValue(
            listOf(SELECT_PANORAMIC_VIEW)
        )
        Assert.assertEquals(true, testedVisibility)
    }

    @Test
    fun `Should return selectPanoramicView visible true when surroundViewRepository viewState set to PANORAMIC_FRONT_VIEW`() { // ktlint-disable max-line-length
        var testedVisibility = false
        avmStateViewModel.selectPanoramicViewVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedVisibility = visible })
        surroundStateMocked.postValue(
            SurroundState(PANORAMIC_FRONT_VIEW)
        )
        Assert.assertEquals(true, testedVisibility)
    }

    @Test
    fun `Should return selectStandardView visible true when surroundViewRepository authorizedActions set to SELECT_STANDARD_VIEW`() { // ktlint-disable max-line-length
        var testedVisibility = false
        avmStateViewModel.selectStandardViewVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedVisibility = visible })
        surroundAuthorizedActionsLiveData.postValue(
            listOf(SELECT_STANDARD_VIEW)
        )
        Assert.assertEquals(true, testedVisibility)
    }

    @Test
    fun `Should return selectStandardView visible true when surroundViewRepository viewState set to FRONT_VIEW`() { // ktlint-disable max-line-length
        var testedVisibility = false
        avmStateViewModel.selectStandardViewVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedVisibility = visible })
        surroundStateMocked.postValue(
            SurroundState(FRONT_VIEW)
        )
        Assert.assertEquals(true, testedVisibility)
    }

    @Test
    fun `Should return selectSidesViewVisible true when surroundViewRepository authorizedActions set to SELECT_SIDES_VIEW`() { // ktlint-disable max-line-length
        var testedVisibility = false
        avmStateViewModel.selectSidesViewVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedVisibility = visible })
        surroundAuthorizedActionsLiveData.postValue(
            listOf(SELECT_SIDES_VIEW)
        )
        Assert.assertEquals(true, testedVisibility)
    }

    @Test
    fun `Should return selectSidesViewVisible true when surroundViewRepository viewState  set to SIDES_VIEW`() { // ktlint-disable max-line-length
        var testedVisibility = false
        avmStateViewModel.selectSidesViewVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedVisibility = visible })
        surroundStateMocked.postValue(
            SurroundState(SIDES_VIEW)
        )
        Assert.assertEquals(true, testedVisibility)
    }

    @Test
    fun `Should enable buttons clicks when camera view state received`() { // ktlint-disable max-line-length
        var buttonsClickable = false
        avmStateViewModel.buttonsEnabled.observe(
            lifecycleOwner,
            Observer { available: Boolean -> buttonsClickable = available })

        surroundStateMocked.postValue(SurroundState(SIDES_VIEW, false))
        Assert.assertEquals(true, buttonsClickable)
    }

    @Test
    fun `Should disable buttons clicks when view request received`() { // ktlint-disable max-line-length
        var buttonsClickable = false
        avmStateViewModel.buttonsEnabled.observe(
            lifecycleOwner,
            Observer { available: Boolean -> buttonsClickable = available })

        surroundStateMocked.postValue(SurroundState(SIDES_VIEW, true))
        Assert.assertEquals(false, buttonsClickable)
    }

    @Test
    fun `Should return selectThreeDimensionViewVisible true when surroundViewRepository authorizedActions set to SELECT_THREE_DIMENSION_VIEW`() { // ktlint-disable max-line-length
        var testedVisibility = false
        avmStateViewModel.selectThreeDimensionViewVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedVisibility = visible })
        surroundAuthorizedActionsLiveData.postValue(
            listOf(SELECT_THREE_DIMENSION_VIEW)
        )
        Assert.assertEquals(true, testedVisibility)
    }

    @Test
    fun `Should return selectThreeDimensionViewVisible true when surroundViewRepository view set to THREE_DIMENSION_VIEW`() { // ktlint-disable max-line-length
        var testedVisibility = false
        avmStateViewModel.selectThreeDimensionViewVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedVisibility = visible })
        surroundStateMocked.postValue(
            SurroundState(THREE_DIMENSION_VIEW)
        )
        Assert.assertEquals(true, testedVisibility)
    }

    @Test
    fun `Should return button selected STANDARD when surroundViewRepository viewState  set to REAR_VIEW`() { // ktlint-disable max-line-length
        var modeSelected: Int? = null
        avmStateViewModel.modeSelected.observe(
            lifecycleOwner,
            Observer { modeSelected = it })
        surroundStateMocked.postValue(
            SurroundState(
                REAR_VIEW,
                true
            )
        )
        Assert.assertEquals(AvmModeSelected.STANDARD, modeSelected)
    }

    @Test
    fun `Should return button selected STANDARD when surroundViewRepository viewState set to FRONT_VIEW`() { // ktlint-disable max-line-length
        var modeSelected: Int? = null
        avmStateViewModel.modeSelected.observe(
            lifecycleOwner,
            Observer {
                modeSelected = it
            })
        surroundStateMocked.postValue(
            SurroundState(
                FRONT_VIEW,
                true
            )
        )
        Assert.assertEquals(AvmModeSelected.STANDARD, modeSelected)
    }

    @Test
    fun `Should return button selected PANORAMIC when surroundViewRepository requestedState set to PANORAMIC_REAR_VIEW`() { // ktlint-disable max-line-length
        var modeSelected: Int? = null
        avmStateViewModel.modeSelected.observe(
            lifecycleOwner,
            Observer { modeSelected = it })
        surroundStateMocked.postValue(
            SurroundState(
                PANORAMIC_REAR_VIEW,
                true
            )
        )
        Assert.assertEquals(AvmModeSelected.PANORAMIC, modeSelected)
    }

    @Test
    fun `Should return button selected SIDES when surroundViewRepository requestedState set to SIDES_VIEW`() { // ktlint-disable max-line-length
        var modeSelected: Int? = null
        avmStateViewModel.modeSelected.observe(
            lifecycleOwner,
            Observer { modeSelected = it })
        surroundStateMocked.postValue(
            SurroundState(
                SIDES_VIEW,
                true
            )
        )
        Assert.assertEquals(AvmModeSelected.SIDES, modeSelected)
    }

    @Test
    fun `Should return button selected THREE_D when surroundViewRepository requestedState set to THREE_DIMENSION_VIEW`() { // ktlint-disable max-line-length
        var modeSelected: Int? = null
        avmStateViewModel.modeSelected.observe(
            lifecycleOwner,
            Observer { modeSelected = it })
        surroundStateMocked.postValue(
            SurroundState(
                THREE_DIMENSION_VIEW,
                true
            )
        )
        Assert.assertEquals(AvmModeSelected.THREE_D, modeSelected)
    }

    @Test
    fun `Should start with request ACTIVATE_MANEUVER_VIEW maneuver request when launched and trailer presence not detected`() { // ktlint-disable max-line-length
        trailerPresenceLiveData.value = TrailerPresence.TRAILER_PRESENCE_NOT_DETECTED
        avmStateViewModel.requestView()
        verify { surroundViewRepository.request(ACTIVATE_MANEUVER_VIEW) }
    }

    @Test
    fun `Should set camera  horizontal margin when we receive view state SIDES_VIEW  `() { // ktlint-disable max-line-length
        var patchMargin = false
        avmStateViewModel.birdSideCameraMargin.observeForever {
            patchMargin = it
        }
        surroundStateMocked.postValue(
            SurroundState(
                SIDES_VIEW,
                false
            )
        )
        assertTrue(patchMargin)
        surroundStateMocked.postValue(
            SurroundState(
                THREE_DIMENSION_VIEW,
                false
            )
        )
        assertFalse(patchMargin)
    }
}