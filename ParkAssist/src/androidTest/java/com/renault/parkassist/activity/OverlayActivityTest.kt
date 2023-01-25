package com.renault.parkassist.activity

import android.content.Intent
import androidx.annotation.IdRes
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.renault.parkassist.koin.KoinTestBase
import com.renault.parkassist.ui.FullscreenActivity
import com.renault.parkassist.ui.PopUpActivity
import com.renault.parkassist.ui.apa.ApaWarningActivity
import com.renault.parkassist.ui.surround.SurroundWarningActivity
import org.junit.Rule

abstract class OverlayActivityTest : KoinTestBase() {

    @get:Rule
    val fullscreenActivityTestRule =
        ActivityTestRule(FullscreenActivity::class.java, true, false)

    @get:Rule
    val popupActivityTestRule =
        ActivityTestRule(PopUpActivity::class.java, true, false)

    @get:Rule
    val surroundWarningActivityTestRule =
        ActivityTestRule(SurroundWarningActivity::class.java, true, false)

    @get:Rule
    val apaWarningActivityTestRule =
        ActivityTestRule(ApaWarningActivity::class.java, true, false)

    protected fun launchFullScreen() {
        fullscreenActivityTestRule.launchActivity(Intent())
    }

    protected fun launchPopUp() {
        popupActivityTestRule.launchActivity(Intent())
    }

    protected fun launchSurroundViewWarning() {
        surroundWarningActivityTestRule.launchActivity(Intent())
    }

    protected fun launchApaWarning() {
        apaWarningActivityTestRule.launchActivity(Intent())
    }

    protected fun navigateFullscreen(@IdRes idRes: Int) =
        runOnUiThread {
            fullscreenActivityTestRule.activity
                .renaultNavController.navigate(idRes)
        }

    protected fun navigatePopUp(@IdRes idRes: Int) =
        runOnUiThread {
            popupActivityTestRule.activity
                .renaultNavController.navigate(idRes)
        }

    protected val orientation
        get() =
            InstrumentationRegistry.getInstrumentation().targetContext
                .resources.configuration.orientation
}