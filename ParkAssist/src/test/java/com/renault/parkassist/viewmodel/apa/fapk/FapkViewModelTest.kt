package com.renault.parkassist.viewmodel.apa.fapk

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.renault.parkassist.TestBase
import com.renault.parkassist.TestUtils
import com.renault.parkassist.repository.apa.*
import com.renault.parkassist.repository.apa.mock.ApaRepositoryMock
import com.renault.parkassist.repository.surroundview.*
import com.renault.parkassist.ui.apa.ManeuverButtonMapper
import com.renault.parkassist.viewmodel.apa.Maneuver
import io.mockk.every
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.dsl.module

class FapkViewModelTest : TestBase() {

    private lateinit var apaRepository: ApaRepositoryMock
    private lateinit var lifecycleOwner: LifecycleOwner
    private lateinit var fapkViewModel: FapkViewModel
    private val surroundViewRepository: SurroundViewRepository = mockk(relaxed = true)
    private val surroundViewRepositorySurroundStateLiveData =
        MutableLiveData<SurroundState>()

    override val koinModule = module {
        single<IApaRepository>(override = true) { apaRepository }
        single<IExtSurroundViewRepository>(override = true) { surroundViewRepository }
        single<ManeuverButtonMapper> { ManeuverButtonMapper() }
    }

    @Before
    override fun setup() {
        super.setup()
        every {
            surroundViewRepository.surroundState
        } returns surroundViewRepositorySurroundStateLiveData

        apaRepository = ApaRepositoryMock(mockk(relaxed = true))
        lifecycleOwner = TestUtils.mockLifecycleOwner()
        fapkViewModel = FapkViewModel(mockk())
    }

    @Test
    fun not_selected_buttons_should_be_not_enabled_in_guidance() {
        var parallelEnabled: Boolean?
        var perpendicularEnabled: Boolean?
        var parkoutEnabled: Boolean?

        fapkViewModel.maneuverParallelButtonEnabled.observe(lifecycleOwner, Observer {
            parallelEnabled = it
        })
        fapkViewModel.maneuverPerpendicularButtonEnabled.observe(lifecycleOwner, Observer {
            perpendicularEnabled = it
        })
        fapkViewModel.maneuverParkoutButtonEnabled.observe(lifecycleOwner, Observer {
            parkoutEnabled = it
        })

        apaRepository.displayState.postValue(DisplayState.DISPLAY_GUIDANCE)

        parallelEnabled = null
        perpendicularEnabled = null
        parkoutEnabled = null

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        assertEquals(false, parallelEnabled)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        assertEquals(false, perpendicularEnabled)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        assertEquals(false, parkoutEnabled)
    }

    @Test
    fun not_selected_buttons_should_be_enabled_in_scanning() {
        var parallelEnabled: Boolean?
        var perpendicularEnabled: Boolean?
        var parkoutEnabled: Boolean?

        fapkViewModel.maneuverParallelButtonEnabled.observe(lifecycleOwner, Observer {
            parallelEnabled = it
        })
        fapkViewModel.maneuverPerpendicularButtonEnabled.observe(lifecycleOwner, Observer {
            perpendicularEnabled = it
        })
        fapkViewModel.maneuverParkoutButtonEnabled.observe(lifecycleOwner, Observer {
            parkoutEnabled = it
        })

        apaRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)

        parallelEnabled = null
        perpendicularEnabled = null
        parkoutEnabled = null

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        assertEquals(true, parallelEnabled)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        assertEquals(true, perpendicularEnabled)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        assertEquals(true, parkoutEnabled)
    }

    @Test
    fun selected_buttons_should_be_enabled() {
        var parallelEnabled: Boolean?
        var perpendicularEnabled: Boolean?
        var parkoutEnabled: Boolean?

        fapkViewModel.maneuverParallelButtonEnabled.observe(lifecycleOwner, Observer {
            parallelEnabled = it
        })
        fapkViewModel.maneuverPerpendicularButtonEnabled.observe(lifecycleOwner, Observer {
            perpendicularEnabled = it
        })
        fapkViewModel.maneuverParkoutButtonEnabled.observe(lifecycleOwner, Observer {
            parkoutEnabled = it
        })

        apaRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)

        parallelEnabled = null
        perpendicularEnabled = null
        parkoutEnabled = null

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        assertEquals(true, parallelEnabled)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        assertEquals(true, perpendicularEnabled)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)
        assertEquals(true, parkoutEnabled)
    }

    @Test
    fun not_enabled_buttons_should_not_be_selected() {
        var parallelSelected: Boolean?
        var perpendicularSelected: Boolean?
        var parkoutSelected: Boolean?

        fapkViewModel.maneuverParallelButtonSelected.observe(lifecycleOwner, Observer {
            parallelSelected = it
        })
        fapkViewModel.maneuverPerpendicularButtonSelected.observe(lifecycleOwner, Observer {
            perpendicularSelected = it
        })
        fapkViewModel.maneuverParkoutButtonSelected.observe(lifecycleOwner, Observer {
            parkoutSelected = it
        })

        apaRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)

        parallelSelected = null
        perpendicularSelected = null
        parkoutSelected = null

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.UNAVAILABLE)
        assertEquals(false, parallelSelected)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.UNAVAILABLE)
        assertEquals(false, perpendicularSelected)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.UNAVAILABLE)
        assertEquals(false, parkoutSelected)
    }

    @Test
    fun `maneuver parallel button should be selected when ManeuverSelection SELECTED is sent`() { // ktlint-disable max-line-length
        var actual = false
        fapkViewModel.maneuverParallelButtonSelected.observe(lifecycleOwner, Observer {
            actual = it ?: false
        })
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        assertTrue(actual)
    }

    @Test
    fun `maneuver perpendicular button should be selected when ManeuverSelection SELECTED is sent`() { // ktlint-disable max-line-length
        var actual = false
        fapkViewModel.maneuverPerpendicularButtonSelected.observe(lifecycleOwner, Observer {
            actual = it ?: false
        })
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        assertTrue(actual)
    }

    @Test
    fun `maneuver parkout button should be selected when ManeuverSelection SELECTED is sent`() { // ktlint-disable max-line-length
        var actual = false
        fapkViewModel.maneuverParkoutButtonSelected.observe(lifecycleOwner, Observer {
            actual = it ?: false
        })
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)
        assertTrue(actual)
    }

    @Test
    fun perpendicular_and_parallel_should_be_not_selected_and_parkout_selected_when_user_click_on_parkout() { // ktlint-disable max-line-length
        var parallelSelected: Boolean?
        var perpendicularSelected: Boolean?
        var parkoutSelected: Boolean?

        apaRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)

        fapkViewModel.maneuverParallelButtonSelected.observe(lifecycleOwner, Observer {
            parallelSelected = it
        })
        fapkViewModel.maneuverPerpendicularButtonSelected.observe(lifecycleOwner, Observer {
            perpendicularSelected = it
        })
        fapkViewModel.maneuverParkoutButtonSelected.observe(lifecycleOwner, Observer {
            parkoutSelected = it
        })

        parallelSelected = null
        perpendicularSelected = null
        parkoutSelected = null

        fapkViewModel.setManeuver(ManeuverType.PARKOUT)

        assertEquals(false, parallelSelected)
        assertEquals(false, perpendicularSelected)
        assertEquals(true, parkoutSelected)
    }

    @Test
    fun parkout_and_parallel_should_be_not_selected_and_parkout_selected_when_user_click_on_perpendicular() { // ktlint-disable max-line-length
        var parallelSelected: Boolean?
        var perpendicularSelected: Boolean?
        var parkoutSelected: Boolean?

        apaRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)

        fapkViewModel.maneuverParallelButtonSelected.observe(lifecycleOwner, Observer {
            parallelSelected = it
        })
        fapkViewModel.maneuverPerpendicularButtonSelected.observe(lifecycleOwner, Observer {
            perpendicularSelected = it
        })
        fapkViewModel.maneuverParkoutButtonSelected.observe(lifecycleOwner, Observer {
            parkoutSelected = it
        })

        parallelSelected = null
        perpendicularSelected = null
        parkoutSelected = null

        fapkViewModel.setManeuver(ManeuverType.PERPENDICULAR)

        assertEquals(false, parallelSelected)
        assertEquals(true, perpendicularSelected)
        assertEquals(false, parkoutSelected)
    }

    @Test
    fun perpendicular_and_parkout_should_be_not_selected_and_parkout_selected_when_user_click_on_parallel() { // ktlint-disable max-line-length
        var parallelSelected: Boolean?
        var perpendicularSelected: Boolean?
        var parkoutSelected: Boolean?

        apaRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)

        fapkViewModel.maneuverParallelButtonSelected.observe(lifecycleOwner, Observer {
            parallelSelected = it
        })
        fapkViewModel.maneuverPerpendicularButtonSelected.observe(lifecycleOwner, Observer {
            perpendicularSelected = it
        })
        fapkViewModel.maneuverParkoutButtonSelected.observe(lifecycleOwner, Observer {
            parkoutSelected = it
        })

        parallelSelected = null
        perpendicularSelected = null
        parkoutSelected = null

        fapkViewModel.setManeuver(ManeuverType.PARALLEL)

        assertEquals(true, parallelSelected)
        assertEquals(false, perpendicularSelected)
        assertEquals(false, parkoutSelected)
    }

    @Test
    fun maneuvers_buttons_should_be_not_enabled_when_unavailable_is_sent() {
        var parallelEnabled: Boolean?
        var perpendicularEnabled: Boolean?
        var parkoutEnabled: Boolean?

        fapkViewModel.maneuverParallelButtonEnabled.observe(lifecycleOwner, Observer {
            parallelEnabled = it
        })
        fapkViewModel.maneuverPerpendicularButtonEnabled.observe(lifecycleOwner, Observer {
            perpendicularEnabled = it
        })
        fapkViewModel.maneuverParkoutButtonEnabled.observe(lifecycleOwner, Observer {
            parkoutEnabled = it
        })

        apaRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)

        parallelEnabled = null
        perpendicularEnabled = null
        parkoutEnabled = null

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.UNAVAILABLE)
        assertEquals(false, parallelEnabled)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.UNAVAILABLE)
        assertEquals(false, perpendicularEnabled)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.UNAVAILABLE)
        assertEquals(false, parkoutEnabled)

        apaRepository.displayState.postValue(DisplayState.DISPLAY_GUIDANCE)

        parallelEnabled = null
        perpendicularEnabled = null
        parkoutEnabled = null

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.UNAVAILABLE)
        assertEquals(false, parallelEnabled)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.UNAVAILABLE)
        assertEquals(false, perpendicularEnabled)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.UNAVAILABLE)
        assertEquals(false, parkoutEnabled)
    }

    @Test
    fun maneuver_buttons_should_be_selected_when_selected_is_sent() {
        var parallelSelected: Boolean?
        var perpendicularSelected: Boolean?
        var parkoutSelected: Boolean?

        fapkViewModel.maneuverParallelButtonSelected.observe(lifecycleOwner, Observer {
            parallelSelected = it
        })
        fapkViewModel.maneuverPerpendicularButtonSelected.observe(lifecycleOwner, Observer {
            perpendicularSelected = it
        })
        fapkViewModel.maneuverParkoutButtonSelected.observe(lifecycleOwner, Observer {
            parkoutSelected = it
        })

        apaRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)

        parallelSelected = null
        perpendicularSelected = null
        parkoutSelected = null

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        assertEquals(true, parallelSelected)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        assertEquals(true, perpendicularSelected)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)
        assertEquals(true, parkoutSelected)

        apaRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)

        parallelSelected = null
        perpendicularSelected = null
        parkoutSelected = null

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        assertEquals(true, parallelSelected)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        assertEquals(true, perpendicularSelected)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)
        assertEquals(true, parkoutSelected)
    }

    @Test
    fun settings_button_should_be_visible_only_when_display_state_scanning_and_surround_state_apa_front_view() { // ktlint-disable max-line-length
        var settingsVisible: Boolean?

        fapkViewModel.settingsVisible.observe(lifecycleOwner, Observer {
            settingsVisible = it
        })

        settingsVisible = null

        apaRepository.supportedManeuvers = listOf(Maneuver.PARALLEL, Maneuver.PERPENDICULAR)

        apaRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)

        surroundViewRepositorySurroundStateLiveData.postValue(SurroundState(View.APA_FRONT_VIEW))
        assertEquals(true, settingsVisible)

        surroundViewRepositorySurroundStateLiveData.postValue(SurroundState(View.APA_REAR_VIEW))
        assertEquals(false, settingsVisible)

        apaRepository.displayState.postValue(DisplayState.DISPLAY_GUIDANCE)

        surroundViewRepositorySurroundStateLiveData.postValue(SurroundState(View.APA_FRONT_VIEW))
        assertEquals(false, settingsVisible)

        surroundViewRepositorySurroundStateLiveData.postValue(SurroundState(View.APA_REAR_VIEW))
        assertEquals(false, settingsVisible)
    }

    @Test
    fun settings_button_should_not_be_visible_when_parallel_maneuver_not_available() {
        var settingsVisible: Boolean?

        fapkViewModel.settingsVisible.observe(lifecycleOwner, Observer {
            settingsVisible = it
        })

        settingsVisible = null

        apaRepository.supportedManeuvers = listOf(Maneuver.PERPENDICULAR)

        apaRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)

        surroundViewRepositorySurroundStateLiveData.postValue(SurroundState(View.APA_FRONT_VIEW))
        assertEquals(false, settingsVisible)

        surroundViewRepositorySurroundStateLiveData.postValue(SurroundState(View.APA_REAR_VIEW))
        assertEquals(false, settingsVisible)

        apaRepository.displayState.postValue(DisplayState.DISPLAY_GUIDANCE)

        surroundViewRepositorySurroundStateLiveData.postValue(SurroundState(View.APA_FRONT_VIEW))
        assertEquals(false, settingsVisible)

        surroundViewRepositorySurroundStateLiveData.postValue(SurroundState(View.APA_REAR_VIEW))
        assertEquals(false, settingsVisible)
    }
}