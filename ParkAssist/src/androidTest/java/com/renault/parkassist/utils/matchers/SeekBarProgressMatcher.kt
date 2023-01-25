package com.renault.parkassist.utils.matchers

import android.view.View
import android.widget.SeekBar
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description

class SeekBarProgressMatcher(private val expectedValue: Int) :
    BoundedMatcher<View, SeekBar>(SeekBar::class.java) {
    private var actualValue: Int = 0

    override fun matchesSafely(item: SeekBar): Boolean {
        actualValue = item.progress
        return actualValue == expectedValue
    }

    override fun describeTo(description: Description) {
        description.appendText(
            "with SeekBar Actual Progress: $actualValue" +
                "does not match expected Progress: $expectedValue"
        )
    }
}