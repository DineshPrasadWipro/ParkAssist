package com.renault.parkassist.routing

import alliance.car.windowoverlay.AllianceCarWindowOverlay
import android.app.Activity
import android.content.Intent
import android.os.UserManager
import androidx.annotation.IdRes
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ServiceTestRule
import androidx.test.uiautomator.UiDevice
import com.renault.parkassist.R
import com.renault.parkassist.koin.KoinTestApp
import com.renault.parkassist.koin.KoinTestApp.Companion.isRunningActivitiesEmpty
import com.renault.parkassist.koin.KoinTestBase
import com.renault.parkassist.overlay.IAllianceCarWindowOverlayManager
import com.renault.parkassist.repository.routing.IAutoParkRouting
import com.renault.parkassist.repository.routing.ISonarRouting
import com.renault.parkassist.repository.routing.ISurroundRouting
import com.renault.parkassist.routing.mock.ApaRoutingMock
import com.renault.parkassist.routing.mock.SonarRoutingMock
import com.renault.parkassist.routing.mock.SurroundRoutingMock
import com.renault.parkassist.routing.policy.IPolicy
import com.renault.parkassist.routing.pursuit.PursuitViewModel
import com.renault.parkassist.service.DisplayService
import com.renault.parkassist.service.UserBootService
import com.renault.parkassist.ui.FullscreenActivity
import com.renault.parkassist.ui.FullscreenShadowActivity
import com.renault.parkassist.ui.PopUpActivity
import com.renault.parkassist.ui.apa.ApaWarningActivity
import com.renault.parkassist.ui.surround.SurroundWarningActivity
import com.renault.parkassist.utils.*
import io.mockk.every
import io.mockk.mockk
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.koin.core.inject
import org.koin.dsl.module

abstract class RoutingTest : KoinTestBase() {

    private var scenario: ActivityScenario<out Activity>? = null

    @get:Rule
    val displayServiceRule =
        ServiceTestRule()

    private val allianceCarOverlayManager: IAllianceCarWindowOverlayManager by inject()

    private val pursuitViewModel: PursuitViewModel by inject()

    private val surroundRouting: ISurroundRouting by inject()
    protected val surroundBridgeMock: SurroundRoutingMock =
        surroundRouting as SurroundRoutingMock

    private val sonarRouting: ISonarRouting by inject()
    protected val sonarBridgeMock: SonarRoutingMock =
        sonarRouting as SonarRoutingMock

    private val apaRouting: IAutoParkRouting by inject()
    protected val apaBridgeMock: ApaRoutingMock =
        apaRouting as ApaRoutingMock

    private val mockedOverlay: AllianceCarWindowOverlay = mockk(relaxed = true) {
        every { remove() } answers {
            finish()
        }
    }

    private fun finish() {
        scenario?.moveToState(Lifecycle.State.DESTROYED)
    }

    private inline fun <reified T : Activity> launchActivity() {
        scenario = ActivityScenario.launch(T::class.java)
        scenario?.moveToState(Lifecycle.State.RESUMED)
    }

    private val mockedAllianceCarWindowOverlayManager: IAllianceCarWindowOverlayManager = mockk {
        every { createOverlay(any(), any()) } answers {
            val intent = arg(0) as Intent
            when (intent.component?.className) {
                FullscreenActivity::class.qualifiedName -> {
                    launchActivity<FullscreenActivity>()
                }
                PopUpActivity::class.qualifiedName -> {
                    launchActivity<PopUpActivity>()
                }
                ApaWarningActivity::class.qualifiedName -> {
                    launchActivity<ApaWarningActivity>()
                }
                SurroundWarningActivity::class.qualifiedName -> {
                    launchActivity<SurroundWarningActivity>()
                }
            }
            mockedOverlay
        }
    }

    private val routingKoinModule = module {
        single<IAllianceCarWindowOverlayManager>(override = true) {
            mockedAllianceCarWindowOverlayManager
        }
        single<UserManager>(override = true) {
            mockk(relaxed = true) {
                every { isSystemUser } returns false
            }
        }
    }

    protected val uiTimeout = 2000L

    private val pkg
        get() = InstrumentationRegistry.getInstrumentation().targetContext.packageName

    private val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    abstract val policy: IPolicy

    private val displayManager: IDisplayManager by inject()

    @Before
    fun init() {
        resetMocks()
        getKoin().loadModules(listOf(routingKoinModule))
        initCfg()
        startDisplayService()
        displayManager.connect()
    }

    @After
    fun tearDown() {
        getKoin().unloadModules(listOf(routingKoinModule))
    }

    private fun resetMocks() {
        surroundBridgeMock.reset()
        sonarBridgeMock.reset()
        apaBridgeMock.reset()
    }

    private fun initCfg() = getKoin().loadModules(listOf(module {
        single<IPolicy>(override = true) {
            policy
        }
    }))

    private fun startDisplayService() =
        displayServiceRule.startService(Intent(context, DisplayService::class.java))

    protected fun startUserService() =
        displayServiceRule.startService(Intent(context, UserBootService::class.java))

    protected fun assertScreenPresent(@IdRes idRes: Int) {
        waitForCondition(timeOutMs = uiTimeout) { !isRunningActivitiesEmpty }
        uiDevice.isViewPresent(idRes)
    }

    protected fun assertScreenNotVisible(@IdRes idRes: Int) {
        waitForCondition(timeOutMs = uiTimeout) { !isRunningActivitiesEmpty }
        uiDevice.isViewPresent(idRes)
        EspressoTestUtils.assertViewHasNoEffectiveVisibility(idRes)
    }

    protected fun assertScreenExited(@IdRes idRes: Int) {
        waitForCondition(timeOutMs = uiTimeout) { isRunningActivitiesEmpty }
        uiDevice.assertViewNotPresent(idRes)
    }

    protected fun clickOnView(@IdRes idRes: Int) {
        waitForCondition(timeOutMs = uiTimeout) { !isRunningActivitiesEmpty }
        uiDevice.isViewPresent(idRes, uiTimeout)
        uiDevice.clickOnView(idRes)
    }

    protected fun clickCameraLauncher() =
        launchIntentForComponent(context, pkg, "ui.MainActivity")

    protected fun clickApaLauncher() =
        launchIntentForComponent(context, pkg, "ui.EasyParkLauncherActivity")

    protected fun clickAvmFavoriteButton() =
        broadcastIntentForComponent(
            context, pkg, "service.AvmBroadcastReceiver",
            context.resources.getString(R.string.action_start_avm)
        )

    protected fun clickEasyParkFavoriteButton() =
        broadcastIntentForComponent(
            context, pkg, "service.EasyParkBroadcastReceiver",
            context.resources.getString(R.string.action_start_easy_park)
        )

    protected fun stopShadowFullscreen() = pursuitViewModel.stop()

    protected fun assertHasShadow() {
        assertTrue(KoinTestApp.runningActivities.any { it is FullscreenShadowActivity })
    }

    protected fun assertHasNotShadow() {
        assertFalse(KoinTestApp.runningActivities.any { it is FullscreenShadowActivity })
    }

    protected fun waitShadow() {
        waitForCondition(uiTimeout) {
            KoinTestApp.runningActivities.any { it is FullscreenShadowActivity }
        }
    }

    protected fun waitShadowKilled() {
        waitForCondition(uiTimeout) {
            !KoinTestApp.runningActivities.any { it is FullscreenShadowActivity }
        }
    }
}