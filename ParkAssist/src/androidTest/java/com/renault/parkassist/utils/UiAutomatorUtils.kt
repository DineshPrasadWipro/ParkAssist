package com.renault.parkassist.utils

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import junit.framework.TestCase

val targetContext: Context = InstrumentationRegistry.getInstrumentation().targetContext

fun UiDevice.clickOnView(idRes: Int) {
    val resourceName = targetContext.resources.getResourceEntryName(idRes)
    findObject(By.res(targetContext.packageName, resourceName)).click()
}

fun UiDevice.assertViewNotPresent(idRes: Int, timeOut: Long? = null) =
    TestCase.assertFalse(isViewPresent(idRes, timeOut))

fun UiDevice.isViewPresent(idRes: Int, timeOut: Long? = null): Boolean {
    val resourceName = targetContext.resources.getResourceEntryName(idRes)
    return if (timeOut == null)
        hasObject(By.res(targetContext.packageName, resourceName))
    else
        wait(
            Until.hasObject(
                By.res(targetContext.packageName, resourceName)
            ),
            timeOut
        )
}