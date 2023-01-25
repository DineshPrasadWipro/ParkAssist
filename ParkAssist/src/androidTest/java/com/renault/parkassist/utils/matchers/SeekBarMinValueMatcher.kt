package com.renault.parkassist.utils.matchers

import android.view.View
import android.widget.SeekBar
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description

class SeekBarMinValueMatcher(private val expectedMin: Int) :
    BoundedMatcher<View, SeekBar>(SeekBar::class.java) {
    private var actualValue: Int = 0

    override fun matchesSafely(item: SeekBar): Boolean {
        actualValue = item.min
        return actualValue == expectedMin
    }

    override fun describeTo(description: Description) {
        description.appendText(
            "with renaultSlider Actual Min value: $actualValue does not match" +
                "expected Min value: $expectedMin"
        )
    }
}