package com.renault.parkassist.utils.actions

import android.view.InputDevice
import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.MotionEvents
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.renault.parkassist.viewmodel.avm.Finger
import org.hamcrest.Matcher

class TouchDownAndUp(private val x: Float, private val y: Float) : ViewAction {

    override fun getDescription(): String {
        return "Send touch events."
    }

    override fun getConstraints(): Matcher<View> {
        return isDisplayed()
    }

    override fun perform(uiController: UiController?, view: View?) {
        // Get view absolute position
        val location: IntArray = intArrayOf(0, 0)
        view?.getLocationOnScreen(location)

        // Offset coordinates by view position
        val coordinates = floatArrayOf(x + location[0], y + location[1])
        val precision = floatArrayOf(1f, 1f)

        // Send down event, pause, and send up
        val down = MotionEvents.sendDown(
            uiController, coordinates, precision,
            InputDevice.SOURCE_TOUCHSCREEN, Finger.FIRST
        ).down
        uiController?.loopMainThreadForAtLeast(200)
        MotionEvents.sendUp(uiController, down, coordinates)
    }
}