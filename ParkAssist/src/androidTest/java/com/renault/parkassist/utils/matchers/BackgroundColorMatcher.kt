package com.renault.parkassist.utils.matchers

import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class BackgroundColorMatcher(@IdRes private val expectedColor: Int) : TypeSafeMatcher<View>() {
    private var resourceName: String? = null

    override fun matchesSafely(target: View): Boolean {
        val color = (target.background as ColorDrawable).color
        return color == ContextCompat.getColor(target.context, expectedColor)
    }

    override fun describeTo(description: Description) {
        description.appendText("with background color from color id: ")
        description.appendValue(expectedColor)
        if (resourceName != null) {
            description.appendText("[")
            description.appendText(resourceName)
            description.appendText("]")
        }
    }
}