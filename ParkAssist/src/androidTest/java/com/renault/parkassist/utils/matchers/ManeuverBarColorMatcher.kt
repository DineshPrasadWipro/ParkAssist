package com.renault.parkassist.utils.matchers

import android.view.View
import androidx.test.espresso.matcher.BoundedMatcher
import com.renault.parkassist.ui.apa.ManeuverProgressBar
import org.hamcrest.Description

class ManeuverBarColorMatcher(private val expectedValueRes: Int) :
    BoundedMatcher<View, ManeuverProgressBar>(ManeuverProgressBar::class.java) {

    private var actualValue: Int = 0
    private var expectedValue: Int = 0

    override fun matchesSafely(item: ManeuverProgressBar?): Boolean {
        actualValue = item!!.color
        expectedValue = item.context.getColor(expectedValueRes)
        return actualValue == expectedValue
    }

    override fun describeTo(description: Description) {
        description.appendText(
            "with ManeuverBar Actual color: $actualValue" +
                "does not match expected color: $expectedValue"
        )
    }
}