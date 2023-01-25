package com.renault.parkassist.service

import android.content.Context
import android.content.Intent
import android.os.UserManager
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ServiceTestRule
import androidx.test.uiautomator.*
import com.renault.parkassist.R
import com.renault.parkassist.koin.KoinTestBase
import com.renault.parkassist.koin.ParkAssistHwConfig
import com.renault.parkassist.repository.surroundview.TrailerPresence
import com.renault.parkassist.utils.RetryTestRule
import io.mockk.every
import io.mockk.mockk
import org.junit.*
import org.junit.rules.RuleChain
import org.koin.dsl.module

@LargeTest
class TrailerPresenceNotificationTest : KoinTestBase() {

    companion object {
        const val NOTIFICATION_TIMEOUT = 1000L
    }

    val serviceTestRule = ServiceTestRule()

    val retryTestRule = RetryTestRule(5)

    @get:Rule
    val ruleChain = RuleChain.outerRule(retryTestRule)
        .around(serviceTestRule)

    private lateinit var uiDevice: UiDevice

    private fun startNotificationService() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = Intent(context, UserBootService::class.java)
        serviceTestRule.startService(intent)
    }

    private val mockedTrailerPresence = MutableLiveData<Int>()

    private val koinModules = module {
        single<UserManager>(override = true) {
            mockk(relaxed = true) {
                every { isSystemUser } returns false
            }
        }
    }

    @Before
    fun setup() {
        getKoin().loadModules(listOf(koinModules))
        every { surroundViewRepository.trailerPresence } returns mockedTrailerPresence
        startNotificationService()
        uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    }

    @After
    fun teardown() {
        uiDevice.pressHome()
        getKoin().unloadModules(listOf(koinModules))
    }

    @Test
    fun testNotifyTrailerPresenceRVC() {
        setVehicleConfiguration(ParkAssistHwConfig.RVC)
        mockedTrailerPresence.postValue(TrailerPresence.TRAILER_PRESENCE_DETECTED)
        uiDevice.openNotification()
        scrollIfPossible()
        Assert.assertTrue(isTrailerNotificationPresent)
        Assert.assertTrue(isTrailerNotificationContentRVCPresent)
        mockedTrailerPresence.postValue(TrailerPresence.TRAILER_PRESENCE_NOT_DETECTED)
        uiDevice.openNotification()
        scrollIfPossible()
        Assert.assertFalse(isTrailerNotificationPresent)
    }

    @Test
    fun testNotifyTrailerPresenceAVM() {
        setVehicleConfiguration(ParkAssistHwConfig.AVM)
        mockedTrailerPresence.postValue(TrailerPresence.TRAILER_PRESENCE_DETECTED)
        uiDevice.openNotification()
        scrollIfPossible()
        Assert.assertTrue(isTrailerNotificationPresent)
        Assert.assertTrue(isTrailerNotificationContentAVMPresent)
        mockedTrailerPresence.postValue(TrailerPresence.TRAILER_PRESENCE_NOT_DETECTED)
        uiDevice.openNotification()
        scrollIfPossible()
        Assert.assertFalse(isTrailerNotificationPresent)
    }

    private val isTrailerNotificationPresent: Boolean
        get() {
            return uiDevice.wait(
                Until.hasObject(
                    By.text(
                        getNotificationTitleString()
                    )
                ),
                NOTIFICATION_TIMEOUT
            )
        }

    private val isTrailerNotificationContentRVCPresent: Boolean
        get() {
            return uiDevice.wait(
                Until.hasObject(
                    By.text(
                        getNotificationContentRVCString()
                    )
                ),
                NOTIFICATION_TIMEOUT
            )
        }

    private val isTrailerNotificationContentAVMPresent: Boolean
        get() {
            return uiDevice.wait(
                Until.hasObject(
                    By.text(
                        getNotificationContentAVMString()
                    )
                ),
                NOTIFICATION_TIMEOUT
            )
        }

    private fun getNotificationTitleString() =
        InstrumentationRegistry.getInstrumentation()
            .targetContext.getString(R.string.rlb_parkassist_trailer_notif_title)

    private fun getNotificationContentRVCString() =
        InstrumentationRegistry.getInstrumentation()
            .targetContext.getString(R.string.rlb_parkassist_trailer_rvc_notif_content)

    private fun getNotificationContentAVMString() =
        InstrumentationRegistry.getInstrumentation()
            .targetContext.getString(R.string.rlb_parkassist_trailer_avm_notif_content)

    private fun scrollIfPossible() {
        try {
            UiScrollable(UiSelector().scrollable(true)).scrollForward()
        } catch (e: UiObjectNotFoundException) {
            // Do nothing
        }
    }
}