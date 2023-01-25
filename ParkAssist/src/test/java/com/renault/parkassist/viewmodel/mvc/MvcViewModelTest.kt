package com.renault.parkassist.viewmodel.mvc

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.test.filters.SmallTest
import com.renault.parkassist.TestBase
import com.renault.parkassist.TestUtils
import com.renault.parkassist.repository.apa.ApaRepository
import com.renault.parkassist.repository.apa.FeatureConfig
import com.renault.parkassist.repository.apa.IApaRepository
import com.renault.parkassist.repository.surroundview.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.koin.dsl.module

@SmallTest
class MvcViewModelTest : TestBase() {
    private val surroundViewRepository: SurroundViewRepository = mockk(relaxed = true)
    private val apaRepository: ApaRepository = mockk(relaxed = true)
    private lateinit var mvcStateViewModel: MvcStateViewModel

    private val surroundViewRepositorySurroundStateLiveData =
        MutableLiveData<SurroundState>()

    private val surroundAuthorizedActionsLiveData =
        MutableLiveData<List<Int>>()

    private val surroundStateMocked =
        MutableLiveData<SurroundState>()

    private val trailerPresenceLiveData = MutableLiveData<Int>()

    private lateinit var lifecycleOwner: LifecycleOwner

    override val koinModule = module {
        single<IExtSurroundViewRepository>(override = true) { surroundViewRepository }
        single<IApaRepository>(override = true) { apaRepository }
        factory { MvcMapper() }
    }

    init {
        mockkStatic("com.renault.parkassist.utility.UiUtilsKt")
    }

    @Before
    override fun setup() {
        super.setup()
        every {
            surroundViewRepository.surroundState
        } returns surroundViewRepositorySurroundStateLiveData
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
        mvcStateViewModel = MvcStateViewModel(mockk())
        surroundStateMocked.postValue(SurroundState(View.NO_DISPLAY))
        surroundAuthorizedActionsLiveData.postValue(emptyList())
    }

    @Test
    fun `Should return close visible true when surroundViewRepository authorizedActions contains CLOSE_VIEW`() {
        var testedVisibility = false
        mvcStateViewModel.closeVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedVisibility = visible })
        surroundAuthorizedActionsLiveData.postValue(
            listOf(Action.CLOSE_VIEW)
        )
        Assert.assertEquals(true, testedVisibility)
    }

    @Test
    fun `Should return maneuver visible true when surroundViewRepository viewState authorizedActions set to ACTIVATE_MANEUVER_VIEW`() { // ktlint-disable max-line-length
        var testedVisibility = false
        mvcStateViewModel.maneuverVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedVisibility = visible })
        surroundAuthorizedActionsLiveData.postValue(
            listOf(Action.ACTIVATE_MANEUVER_VIEW)
        )
        Assert.assertEquals(true, testedVisibility)
    }

    @Test
    fun `Should return settings visible true when surroundViewRepository viewState authorizedActions set to ACTIVATE_SETTINGS_VIEW`() { // ktlint-disable max-line-length
        var testedVisibility = false
        mvcStateViewModel.settingsVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedVisibility = visible })
        surroundAuthorizedActionsLiveData.postValue(
            listOf(Action.ACTIVATE_SETTINGS_VIEW)
        )
        Assert.assertEquals(true, testedVisibility)
    }

    @Test
    fun `Should return easypark shortcut visibility accordingly to apaRepository configuration`() {
        // not visible if configuration is NONE
        every { apaRepository.featureConfiguration } returns FeatureConfig.NONE
        Assert.assertEquals(false, mvcStateViewModel.easyparkShortcutVisible)
        // visible else
        every { apaRepository.featureConfiguration } returns FeatureConfig.HFP
        Assert.assertEquals(true, mvcStateViewModel.easyparkShortcutVisible)
        every { apaRepository.featureConfiguration } returns FeatureConfig.FAPK
        Assert.assertEquals(true, mvcStateViewModel.easyparkShortcutVisible)
        every { apaRepository.featureConfiguration } returns FeatureConfig.FPK
        Assert.assertEquals(true, mvcStateViewModel.easyparkShortcutVisible)
        every { apaRepository.featureConfiguration } returns FeatureConfig.HFPB
        Assert.assertEquals(true, mvcStateViewModel.easyparkShortcutVisible)
    }

    @Test
    fun `Should return trailer visible true when surroundViewRepository viewState authorizedActions set to ACTIVATE_TRAILER_VIEW`() {
        var testedVisibility = false
        mvcStateViewModel.trailerVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedVisibility = visible })
        surroundAuthorizedActionsLiveData.postValue(
            listOf(Action.ACTIVATE_TRAILER_VIEW)
        )
        Assert.assertEquals(true, testedVisibility)
    }

    @Test
    fun `Should return selectFrontViewVisible true when surroundViewRepository authorizedActions set to SELECT_MVC_FRONT_VIEW`() {
        var testedVisibility = false
        mvcStateViewModel.selectFrontViewVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedVisibility = visible })
        surroundAuthorizedActionsLiveData.postValue(
            listOf(Action.SELECT_FRONT_CAMERA)
        )
        Assert.assertEquals(true, testedVisibility)
    }

    @Test
    fun `Should return selectFrontViewVisible true when surroundViewRepository viewState  set to FRONT_VIEW`() {
        var testedVisibility = false
        mvcStateViewModel.selectFrontViewVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedVisibility = visible })
        surroundStateMocked.postValue(
            SurroundState(View.FRONT_VIEW)
        )
        Assert.assertEquals(true, testedVisibility)
    }

    @Test
    fun `Should return rearViewInfoVisible true when surroundViewRepository authorizedActions set to SELECT_MVC_REAR_VIEW`() {
        var testedVisibility = false
        mvcStateViewModel.rearViewInfoVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedVisibility = visible })
        surroundAuthorizedActionsLiveData.postValue(
            listOf(Action.SELECT_REAR_CAMERA)
        )
        Assert.assertEquals(true, testedVisibility)
    }

    @Test
    fun `Should return rearViewInfoVisible true when surroundViewRepository viewState  set to REAR_VIEW`() {
        var testedVisibility = false
        mvcStateViewModel.rearViewInfoVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedVisibility = visible })
        surroundStateMocked.postValue(
            SurroundState(View.REAR_VIEW)
        )
        Assert.assertEquals(true, testedVisibility)
    }

    @Test
    fun `Should return selectLeftSideViewVisible true when surroundViewRepository authorizedActions set to SELECT_MVC_LEFT_VIEW`() {
        var testedVisibility = false
        mvcStateViewModel.selectLeftSideViewVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedVisibility = visible })
        surroundAuthorizedActionsLiveData.postValue(
            listOf(Action.SELECT_LEFT_CAMERA)
        )
        Assert.assertEquals(true, testedVisibility)
    }

    @Test
    fun `Should return selectLeftSideViewVisible true when surroundViewRepository viewState set to LEFT_VIEW`() {
        var testedVisibility = false
        mvcStateViewModel.selectLeftSideViewVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedVisibility = visible })
        surroundStateMocked.postValue(
            SurroundState(View.LEFT_VIEW)
        )
        Assert.assertEquals(true, testedVisibility)
    }

    @Test
    fun `Should return selectRightSideViewVisible true when surroundViewRepository authorizedActions set to SELECT_MVC_RIGHT_VIEW`() {
        var testedVisibility = false
        mvcStateViewModel.selectRightSideViewVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedVisibility = visible })
        surroundAuthorizedActionsLiveData.postValue(
            listOf(Action.SELECT_RIGHT_CAMERA)
        )
        Assert.assertEquals(true, testedVisibility)
    }

    @Test
    fun `Should return selectRightSideViewVisible true when surroundViewRepository viewState set to RIGHT_VIEW`() {
        var testedVisibility = false
        mvcStateViewModel.selectRightSideViewVisible.observe(
            lifecycleOwner,
            Observer { visible: Boolean -> testedVisibility = visible })
        surroundStateMocked.postValue(
            SurroundState(View.RIGHT_VIEW)
        )
        Assert.assertEquals(true, testedVisibility)
    }

    @Test
    fun `Should return button selected FRONT when surroundViewRepository requestedState set to FRONT_VIEW`() { // ktlint-disable max-line-length
        var modeSelected: Int? = null
        mvcStateViewModel.modeSelected.observe(
            lifecycleOwner,
            Observer { modeSelected = it })
        surroundStateMocked.postValue(
            SurroundState(
                View.FRONT_VIEW,
                true
            )
        )
        Assert.assertEquals(MvcModeSelected.FRONT, modeSelected)
    }

    @Test
    fun `Should return button selected REAR when surroundViewRepository requestedState set to REAR_VIEW`() { // ktlint-disable max-line-length
        var modeSelected: Int? = null
        mvcStateViewModel.modeSelected.observe(
            lifecycleOwner,
            Observer { modeSelected = it })
        surroundStateMocked.postValue(
            SurroundState(
                View.REAR_VIEW,
                true
            )
        )
        Assert.assertEquals(MvcModeSelected.REAR, modeSelected)
    }

    @Test
    fun `Should return button selected LEFT when surroundViewRepository requestedState set to LEFT_VIEW`() { // ktlint-disable max-line-length
        var modeSelected: Int? = null
        mvcStateViewModel.modeSelected.observe(
            lifecycleOwner,
            Observer { modeSelected = it })
        surroundStateMocked.postValue(
            SurroundState(
                View.LEFT_VIEW,
                true
            )
        )
        Assert.assertEquals(MvcModeSelected.LEFT, modeSelected)
    }

    @Test
    fun `Should return button selected RIGHT when surroundViewRepository requestedState set to RIGHT_VIEW`() { // ktlint-disable max-line-length
        var modeSelected: Int? = null
        mvcStateViewModel.modeSelected.observe(
            lifecycleOwner,
            Observer { modeSelected = it })
        surroundStateMocked.postValue(
            SurroundState(
                View.RIGHT_VIEW,
                true
            )
        )
        Assert.assertEquals(MvcModeSelected.RIGHT, modeSelected)
    }


}
