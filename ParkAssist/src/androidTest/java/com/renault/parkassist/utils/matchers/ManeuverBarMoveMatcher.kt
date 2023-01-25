package com.renault.parkassist.utils.matchers

import android.view.View
import androidx.test.espresso.matcher.BoundedMatcher
import com.renault.parkassist.ui.apa.ManeuverProgressBar
import org.hamcrest.Description

class ManeuverBarMoveMatcher(private val expectedValue: Boolean) :
    BoundedMatcher<View, ManeuverProgressBar>(ManeuverProgressBar::class.java) {

    private var actualValue: Boolean = false

    override fun matchesSafely(item: ManeuverProgressBar?): Boolean {
        actualValue = item!!.forward
        return actualValue == expectedValue
    }

    override fun describeTo(description: Description) {
        description.appendText(
            "with ManeuverBar Actual Progress: $actualValue" +
                "does not match expected Progress: $expectedValue"
        )
    }
}