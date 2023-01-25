package com.renault.parkassist.utils.matchers

import android.view.View
import android.widget.SeekBar
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description

class SeekBarMaxValueMatcher(private val expectedMax: Int) :
    BoundedMatcher<View, SeekBar>(SeekBar::class.java) {
    private var actualValue: Int = 0

    override fun matchesSafely(item: SeekBar): Boolean {
        actualValue = item.max
        return actualValue == expectedMax
    }

    override fun describeTo(description: Description) {
        description.appendText(
            "with RenaultSlider Actual Max value: $actualValue" +
                "does not match expected Max value: $expectedMax"
        )
    }
}