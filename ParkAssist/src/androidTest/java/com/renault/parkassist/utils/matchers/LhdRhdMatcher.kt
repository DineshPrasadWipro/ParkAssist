package com.renault.parkassist.utils.matchers

import alliancex.renault.ui.RenaultDriverOrientedLayout
import android.view.View
import com.renault.parkassist.utility.centerX
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class LhdRhdMatcher(private vararg val viewIds: Int) :
    TypeSafeMatcher<View>(RenaultDriverOrientedLayout::class.java) {

    private var parentViewId: Int? = null

    override fun matchesSafely(parentView: View): Boolean {
        parentViewId = parentView.id
        return matchOrder(parentView)
    }

    private fun matchOrder(parentView: View) =
        viewIds.mapIndexed { index, viewId ->
            if (viewIds.lastIndex == index)
                null
            else
                viewId to viewIds[index + 1]
        }.filterNotNull().all {
            val leftView = parentView.findViewById<View>(it.first)
            val rightView = parentView.findViewById<View>(it.second)

            (leftView.centerX < rightView.centerX)
        }

    override fun describeTo(description: Description) {
        description.appendText("Views ${viewIds.toList()} should be ordered from " +
            "left to right in parent $parentViewId")
    }
}

fun matchOrderLeftToRight(vararg viewIds: Int) = LhdRhdMatcher(*viewIds)