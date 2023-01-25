package com.renault.parkassist.utils.matchers

import android.view.View
import androidx.annotation.IdRes
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class TagAsResIdMatcher(@IdRes private val expectedId: Int?) : TypeSafeMatcher<View>() {
    private var resourceName: String? = null

    override fun matchesSafely(target: View): Boolean {
        if (expectedId == null)
            return target.tag == null

        val resources = target.context.resources
        resourceName = resources.getResourceEntryName(expectedId)
        return expectedId == target.tag
    }

    override fun describeTo(description: Description) {
        description.appendText("with tag as resource id: ")
        description.appendValue(expectedId)
        if (resourceName != null) {
            description.appendText(" ~ [")
            description.appendText(resourceName)
            description.appendText("]")
        }
    }
}