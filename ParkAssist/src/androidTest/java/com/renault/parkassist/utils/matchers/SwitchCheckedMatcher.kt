package com.renault.parkassist.utils.matchers

import android.view.View
import android.widget.Switch
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description

class SwitchCheckedMatcher : BoundedMatcher<View, Switch>(Switch::class.java) {
    override fun matchesSafely(item: Switch): Boolean {
        return item.isChecked
    }

    override fun describeTo(description: Description) {
        description.appendText("should be checked")
    }
}