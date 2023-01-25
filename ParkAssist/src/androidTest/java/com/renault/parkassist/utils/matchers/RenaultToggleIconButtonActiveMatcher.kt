package com.renault.parkassist.utils.matchers

import alliancex.renault.ui.RenaultToggleIconButton
import android.view.View
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description

class RenaultToggleIconButtonActiveMatcher :
    BoundedMatcher<View, RenaultToggleIconButton>(RenaultToggleIconButton::class.java) {
    override fun matchesSafely(item: RenaultToggleIconButton): Boolean {
        return item.isChecked && !item.isClickable
    }

    override fun describeTo(description: Description) {
        description.appendText("should be checked and not clickable")
    }
}