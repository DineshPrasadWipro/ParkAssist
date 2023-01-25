package com.renault.parkassist.utils.matchers

import android.view.View
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class HeightMatcher(private val height: Int) : TypeSafeMatcher<View>() {

    override fun matchesSafely(item: View?): Boolean {
        return if (item != null) item.measuredHeight == height else false
    }

    override fun describeTo(description: Description?) {
        description?.appendText("has the required heigth: ")
        description?.appendValue(height)
    }
}