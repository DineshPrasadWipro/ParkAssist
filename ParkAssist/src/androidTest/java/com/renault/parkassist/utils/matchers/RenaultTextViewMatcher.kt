package com.renault.parkassist.utils.matchers

import alliancex.renault.ui.RenaultTextView
import android.view.View
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description

class RenaultTextViewMatcher(private val expectedValue: String) :
    BoundedMatcher<View, RenaultTextView>(RenaultTextView::class.java) {
    private var actualValue: String = ""

    override fun matchesSafely(item: RenaultTextView): Boolean {
        actualValue = item.text as String
        return actualValue == expectedValue
    }

    override fun describeTo(description: Description) {
        description.appendText(
            "with SeekBar Actual Progress: $actualValue" +
                "does not match expected Progress: $expectedValue"
        )
    }
}