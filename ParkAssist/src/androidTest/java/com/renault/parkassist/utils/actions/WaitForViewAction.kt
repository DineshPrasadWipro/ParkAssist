package com.renault.parkassist.utils.actions

import android.view.View
import androidx.annotation.IdRes
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.util.HumanReadables
import androidx.test.espresso.util.TreeIterables
import java.util.concurrent.TimeoutException
import org.hamcrest.Matcher

class WaitForViewAction(@IdRes private val expectedId: Int, private val timeout: Long) :
    ViewAction {

    companion object {
        const val DEFAULT_DELAY_IN_MS = 50L
    }

    private var resourceName: String? = null

    override fun getConstraints(): Matcher<View> {
        return ViewMatchers.isRoot()
    }

    override fun getDescription(): String {
        var description = "Wait for view with id $expectedId"
        if (resourceName != null) {
            description += " ~ [$resourceName]"
        }
        description += " during $timeout milliseconds."
        return description
    }

    override fun perform(uiController: UiController, view: View) {
        uiController.loopMainThreadUntilIdle()
        val startTime = System.currentTimeMillis()
        val endTime = startTime + timeout
        val viewMatcher = ViewMatchers.withId(expectedId)
        val resources = view.context.resources
        resourceName = resources.getResourceEntryName(expectedId)

        do {
            for (child in TreeIterables.breadthFirstViewTraversal(view)) {
                // found view with required ID
                if (viewMatcher.matches(child)) {
                    return
                }
            }
            uiController.loopMainThreadForAtLeast(DEFAULT_DELAY_IN_MS)
        } while (System.currentTimeMillis() < endTime)

        throw PerformException.Builder()
            .withActionDescription(this.description)
            .withViewDescription(HumanReadables.describe(view))
            .withCause(TimeoutException())
            .build()
    }
}