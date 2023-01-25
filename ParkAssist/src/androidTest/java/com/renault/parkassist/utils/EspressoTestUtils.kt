package com.renault.parkassist.utils

import android.view.View
import android.widget.*
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.CoordinatesProvider
import androidx.test.espresso.action.GeneralClickAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Tap
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.renault.parkassist.R
import com.renault.parkassist.utils.actions.WaitForViewAction
import com.renault.parkassist.utils.matchers.*
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher

object EspressoTestUtils {

    const val DEFAULT_TIMEOUT = 2L * 1000

    fun clickOnView(@IdRes resId: Int, includeOverlays: Boolean = false) {
        onView(withId(resId))
            .includeOverlays(includeOverlays)
            .perform(click())
    }

    fun clickOnView(title: String, includeOverlays: Boolean = false) {
        onView(withText(title))
            .includeOverlays(includeOverlays)
            .perform(click())
    }

    fun assertViewIsCompletelyDisplayed(@IdRes resId: Int, includeOverlays: Boolean = false) {
        assertViewIsCompletelyDisplayed(resId, null, includeOverlays)
    }

    fun assertViewIsCompletelyDisplayed(
        @IdRes resId: Int,
        @IdRes ancestorResId: Int? = null,
        includeOverlays: Boolean = false
    ) {
        val view = if (null != ancestorResId)
            onView(allOf(withId(resId), isDescendantOfA(withId(ancestorResId))))
        else
            onView(withId(resId))

        view.includeOverlays(includeOverlays).check(matches(isCompletelyDisplayed()))
    }

    fun assertViewIsNotDisplayed(@IdRes resId: Int, includeOverlays: Boolean = false) {
        assertViewIsNotDisplayed(resId, null, includeOverlays)
    }

    fun assertViewIsNotDisplayed(
        @IdRes resId: Int,
        @IdRes ancestorResId: Int? = null,
        includeOverlays: Boolean = false
    ) {
        val view = if (null != ancestorResId)
            onView(allOf(withId(resId), isDescendantOfA(withId(ancestorResId))))
        else
            onView(withId(resId))

        view.includeOverlays(includeOverlays).check(matches(not(isDisplayed())))
    }

    fun assertViewHasEffectiveVisibility(@IdRes resId: Int, includeOverlays: Boolean = false) {
        assertViewHasEffectiveVisibility(resId, null, includeOverlays)
    }

    fun assertViewHasEffectiveVisibility(
        @IdRes resId: Int,
        @IdRes ancestorResId: Int? = null,
        includeOverlays: Boolean = false
    ) {
        val view = if (null != ancestorResId)
            onView(allOf(withId(resId), isDescendantOfA(withId(ancestorResId))))
        else
            onView(withId(resId))

        view.includeOverlays(includeOverlays)
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    fun assertViewHasEffectiveVisibility(title: String, includeOverlays: Boolean = false) {
        assertViewHasEffectiveVisibility(title, null, includeOverlays)
    }

    fun assertViewHasEffectiveVisibility(
        title: String,
        @IdRes ancestorResId: Int? = null,
        includeOverlays: Boolean = false
    ) {
        val view = if (null != ancestorResId)
            onView(allOf(withText(title), isDescendantOfA(withId(ancestorResId))))
        else
            onView(withText(title))

        view.includeOverlays(includeOverlays)
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    fun assertViewWithTagHasEffectiveVisibility(tag: String) {
        onView(withTagValue(`is`(tag))).check(
            matches(
                withEffectiveVisibility(Visibility.VISIBLE)
            )
        )
    }

    fun assertViewHasNoEffectiveVisibility(@IdRes resId: Int, includeOverlays: Boolean = false) {
        assertViewHasNoEffectiveVisibility(resId, null, includeOverlays)
    }

    fun assertViewHasNoEffectiveVisibility(
        @IdRes resId: Int,
        @IdRes ancestorResId: Int? = null,
        includeOverlays: Boolean = false
    ) {
        val view = if (null != ancestorResId)
            onView(allOf(withId(resId), isDescendantOfA(withId(ancestorResId))))
        else
            onView(withId(resId))

        view.includeOverlays(includeOverlays)
            .check(matches(not(withEffectiveVisibility(Visibility.VISIBLE))))
    }

    fun assertViewHasNoEffectiveVisibility(title: String, includeOverlays: Boolean = false) {
        assertViewHasNoEffectiveVisibility(title, null, includeOverlays)
    }

    fun assertViewHasNoEffectiveVisibility(
        title: String,
        @IdRes ancestorResId: Int? = null,
        includeOverlays: Boolean = false
    ) {
        val view = if (null != ancestorResId)
            onView(allOf(withText(title), isDescendantOfA(withId(ancestorResId))))
        else
            onView(withText(title))

        view.includeOverlays(includeOverlays)
            .check(matches(not(withEffectiveVisibility(Visibility.VISIBLE))))
    }

    fun assertViewDoesNotExist(
        @IdRes resId: Int,
        @IdRes ancestorResId: Int? = null,
        includeOverlays: Boolean = false
    ) {
        val view = if (null != ancestorResId)
            onView(allOf(withId(resId), isDescendantOfA(withId(ancestorResId))))
        else
            onView(withId(resId))

        view.includeOverlays(includeOverlays)
            .check(doesNotExist())
    }

    fun assertViewIsGone(@IdRes resId: Int, includeOverlays: Boolean = false) {
        onView(withId(resId))
            .includeOverlays(includeOverlays)
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    fun assertTextIsDisplayed(@IdRes resId: Int, includeOverlays: Boolean = false) {
        onView(withText(resId))
            .includeOverlays(includeOverlays)
            .check(matches(isDisplayed()))
    }

    fun assertViewIsDisabled(@IdRes resId: Int, includeOverlays: Boolean = false) {
        onView(withId(resId))
            .includeOverlays(includeOverlays)
            .check(matches(not(isEnabled())))
    }

    fun assertViewWithTagIsNotSelected(tag: String, includeOverlays: Boolean = false) {
        onView(withTagValue(`is`(tag)))
            .includeOverlays(includeOverlays)
            .check(matches(not(isSelected())))
    }

    fun assertViewIsNotSelected(@IdRes resId: Int, includeOverlays: Boolean = false) {
        onView(withId(resId))
            .includeOverlays(includeOverlays)
            .check(matches(not(isSelected())))
    }

    fun assertViewIsChecked(@IdRes resId: Int, includeOverlays: Boolean = false) {
        onView(withId(resId))
            .includeOverlays(includeOverlays)
            .check(matches(isChecked()))
    }

    fun assertViewWithTagIsChecked(tag: String, includeOverlays: Boolean = false) {
        onView(withTagValue(`is`(tag)))
            .includeOverlays(includeOverlays)
            .check(matches(isChecked()))
    }

    fun assertViewWithTagIsNotChecked(tag: String, includeOverlays: Boolean = false) {
        onView(withTagValue(`is`(tag)))
            .includeOverlays(includeOverlays)
            .check(matches(isNotChecked()))
    }

    fun assertViewIsNotChecked(@IdRes resId: Int, includeOverlays: Boolean = false) {
        onView(withId(resId))
            .includeOverlays(includeOverlays)
            .check(matches(isNotChecked()))
    }

    fun assertRenaultSwitchPreferenceDoesNotExist(title: String, includeOverlays: Boolean = false) {
        onView(
            allOf(
                `is`(instanceOf(Switch::class.java)),
                withParent(
                    withParent(hasDescendant(withText(title)))
                )
            )
        ).includeOverlays(includeOverlays)
            .check(doesNotExist())
    }

    fun assertRenaultPreferenceHasSummary(
        @StringRes title: Int,
        summary: String,
        includeOverlays: Boolean = false
    ) {
        onView(
            allOf(
                `is`(instanceOf(TextView::class.java)),
                withId(android.R.id.summary),
                withParent(
                    withParent(hasDescendant(withText(title)))
                )
            )
        ).includeOverlays(includeOverlays)
            .check(matches(withText(summary)))
    }

    fun assertRenaultPreferenceHasSummary(
        @StringRes title: Int,
        @StringRes summary: Int,
        includeOverlays: Boolean = false
    ) {
        onView(
            allOf(
                `is`(instanceOf(TextView::class.java)),
                withId(android.R.id.summary),
                withParent(
                    hasDescendant(withText(title))
                )
            )
        ).includeOverlays(includeOverlays)
            .check(matches(withText(summary)))
    }

    fun assertRenaultSwitchPreferenceHasEffectiveVisibility(
        title: String,
        includeOverlays: Boolean = false
    ) {
        onView(
            allOf(
                `is`(instanceOf(Switch::class.java)),
                withParent(
                    withParent(hasDescendant(withText(title)))
                )
            )
        ).includeOverlays(includeOverlays)
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    fun assertRenaultSwitchPreferenceIsChecked(title: String, includeOverlays: Boolean = false) {
        onView(
            allOf(
                `is`(instanceOf(Switch::class.java)),
                withParent(
                    withParent(hasDescendant(withText(title)))
                )
            )
        ).includeOverlays(includeOverlays)
            .check(matches(SwitchCheckedMatcher()))
    }

    fun assertRenaultSwitchPreferenceIsNotChecked(title: String, includeOverlays: Boolean = false) {
        onView(
            allOf(
                `is`(instanceOf(Switch::class.java)),
                withParent(
                    withParent(hasDescendant(withText(title)))
                )
            )
        ).includeOverlays(includeOverlays)
            .check(matches(not(SwitchCheckedMatcher())))
    }

    fun assertRenaultRadioPreferenceDoesNotExist(
        @StringRes title: Int,
        includeOverlays: Boolean = false
    ) {
        onView(
            allOf(withText(title), withParent(withId(R.id.radio_button)))
        ).includeOverlays(includeOverlays)
            .check(doesNotExist())
    }

    fun assertRenaultRadioPreferenceIsDisplayed(
        @StringRes title: Int,
        includeOverlays: Boolean = false
    ) {
        onView(
            allOf(withText(title), withParent(withId(R.id.radio_button)))
        ).includeOverlays(includeOverlays)
            .check(matches(isDisplayed()))
    }

    fun assertRenaultRadioPreferenceIsChecked(
        @StringRes titleRes: Int,
        includeOverlays: Boolean = false
    ) {
        onView(
            allOf(withText(titleRes), withParent(withId(R.id.radio_button)))
        ).includeOverlays(includeOverlays)
            .check(matches(isChecked()))
    }

    fun assertRenaultRadioPreferenceIsEnabled(
        @StringRes title: Int,
        includeOverlays: Boolean = false
    ) {
        onView(
            allOf(withText(title), withParent(withId(R.id.radio_button)))
        ).includeOverlays(includeOverlays)
            .check(matches(isEnabled()))
    }

    fun assertRenaultPreferenceIsEnabled(
        title: String,
        includeOverlays: Boolean = false
    ) {
        onView(
            allOf(
                `is`(instanceOf(TextView::class.java)),
                withId(android.R.id.summary),
                withParent(
                    hasDescendant(withText(title))
                )
            )
        ).includeOverlays(includeOverlays)
            .check(matches(isEnabled()))
    }

    fun assertRenaultPreferenceIsDisabled(
        title: String,
        includeOverlays: Boolean = false
    ) {
        onView(
            allOf(
                `is`(instanceOf(TextView::class.java)),
                withId(android.R.id.summary),
                withParent(
                    hasDescendant(withText(title))
                )
            )
        ).includeOverlays(includeOverlays)
            .check(matches(not(isEnabled())))
    }

    fun assertRenaultSeekBarPreferenceIsEnabled(
        includeOverlays: Boolean = false
    ) {
        onView(
            allOf(
                `is`(instanceOf(SeekBar::class.java))
            )
        ).includeOverlays(includeOverlays)
            .check(matches(isEnabled()))
    }

    fun assertRenaultRadioPreferenceIsNotChecked(
        @StringRes titleRes: Int,
        includeOverlays: Boolean = false
    ) {
        onView(
            allOf(withText(titleRes), withParent(withId(R.id.radio_button)))
        ).includeOverlays(includeOverlays)
            .check(matches(isNotChecked()))
    }

    fun assertRenaultRadioPreferenceHasEffectiveVisibility(
        @StringRes title: Int,
        includeOverlays: Boolean = false
    ) {
        onView(
            allOf(withText(title), withParent(withId(R.id.radio_button)))
        ).includeOverlays(includeOverlays)
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    fun assertRenaultSliderValue(
        @IdRes resId: Int,
        expectedValue: Int,
        includeOverlays: Boolean = false
    ) {
        onView(withId(resId))
            .includeOverlays(includeOverlays)
            .check(matches(SeekBarProgressMatcher(expectedValue)))
    }

    fun assertProgressBarValue(expectedValue: Int) {
        onView(
            allOf(
                `is`(instanceOf(SeekBar::class.java))
            )
        ).check(matches(SeekBarProgressMatcher(expectedValue)))
    }

    fun assertRenaultSeekBarPreferenceHasEffectiveVisibility(
        includeOverlays: Boolean = false
    ) {
        onView(
            allOf(
                `is`(instanceOf(SeekBar::class.java))
            )
        ).includeOverlays(includeOverlays)
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    fun assertRenaultSeekBarPreferenceDoesNotExist(
        includeOverlays: Boolean = false
    ) {
        onView(
            allOf(
                `is`(instanceOf(SeekBar::class.java))
            )
        ).includeOverlays(includeOverlays)
            .check(doesNotExist())
    }

    fun assertManeuverProgressBarValue(@IdRes resId: Int, expectedValue: Float) {
        onView(withId(resId))
            .check(matches(ManeuverBarProgressMatcher(expectedValue)))
    }

    fun assertManeuverProgressBarMove(@IdRes resId: Int, expectedValue: Boolean) {
        onView(withId(resId))
            .check(matches(ManeuverBarMoveMatcher(expectedValue)))
    }

    fun assertProgressBarMinValue(expectedValue: Int) {
        onView(
            allOf(
                `is`(instanceOf(SeekBar::class.java))
            )
        ).check(matches(SeekBarMinValueMatcher(expectedValue)))
    }

    fun assertProgressBarMaxValue(expectedValue: Int) {
        onView(
            allOf(
                `is`(instanceOf(SeekBar::class.java))
            )
        ).check(matches(SeekBarMaxValueMatcher(expectedValue)))
    }

    fun assertManeuverProgressBarColor(@IdRes resId: Int, @ColorInt expectedValue: Int) {
        onView(withId(resId))
            .check(matches(ManeuverBarColorMatcher(expectedValue)))
    }

    fun withTagAsResourceId(@IdRes resourceId: Int?): Matcher<View> {
        return TagAsResIdMatcher(resourceId)
    }

    fun setCheck(isCheck: Boolean): ViewAction {
        return object : ViewAction {
            override fun perform(uiController: UiController, view: View) {
                (view as Checkable).isChecked = isCheck
            }

            override fun getDescription(): String {
                return "Set a check"
            }

            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(CompoundButton::class.java)
            }
        }
    }

    fun toggleRenaultSwitchPreference(prefTitle: String, isCheck: Boolean) {
        onView(
            allOf(
                `is`(instanceOf(Switch::class.java)),
                withParent(
                    withParent(hasDescendant(withText(prefTitle)))
                )
            )
        ).perform(setCheck(isCheck))
    }

    fun clickRenaultSwitchPreference(prefTitle: String) {
        onView(
            allOf(
                `is`(instanceOf(Switch::class.java)),
                withParent(
                    withParent(hasDescendant(withText(prefTitle)))
                )
            )
        ).perform(click())
    }

    fun setRenaultRadioPreferenceChecked(@StringRes textItemRes: Int, isCheck: Boolean) {
        onView(
            allOf(withText(textItemRes), withParent(withId(R.id.radio_button)))
        ).perform(setCheck(isCheck))
    }

    fun setPercentageProgressOnSlider(
        @IdRes resId: Int,
        progress: Int,
        includeOverlays: Boolean = false
    ) {
        onView(withId(resId))
            .includeOverlays(includeOverlays)
            .perform(setPercentageProgress(progress))
    }

    fun setPercentageProgressOnPreferenceSeekBar(
        progress: Int,
        includeOverlays: Boolean = false
    ) {
        onView(
            allOf(
                `is`(instanceOf(SeekBar::class.java))
            )
        ).includeOverlays(includeOverlays)
            .perform(clickSeekBar(progress))
    }

    fun setPercentageProgress(progress: Int): ViewAction {
        return object : ViewAction {
            override fun perform(uiController: UiController, view: View) {
                (view as SeekBar).progress = progress
            }

            override fun getDescription(): String {
                return "Set a progress"
            }

            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(SeekBar::class.java)
            }
        }
    }

    fun waitForView(
        @IdRes viewId: Int,
        includeOverlays: Boolean = false,
        timeout: Long = DEFAULT_TIMEOUT
    ) {
        onView(isRoot()).includeOverlays(includeOverlays).perform(
            WaitForViewAction(viewId, timeout)
        )
    }

    fun checkToolbarTitle(
        @IdRes toolbar: Int,
        @StringRes text: Int,
        includeOverlays: Boolean = false
    ) = onView(withId(toolbar)).includeOverlays(includeOverlays)
        .check(matches(hasDescendant(withText(text))))

    fun waitFor(duration: Long) {
        onView(isRoot()).perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> = isRoot()

            override fun getDescription(): String = "wait for ${duration}ms"

            override fun perform(uiController: UiController, view: View?) {
                uiController.loopMainThreadForAtLeast(duration)
            }
        })
    }
}

private fun ViewInteraction.includeOverlays(include: Boolean) =
    if (include) this.includeOverlays() else this

fun ViewInteraction.includeOverlays(): ViewInteraction = this.inRoot(RootMatchers.isTouchable())

fun checkDrawableLevel(@IdRes viewId: Int, @DrawableRes drawableId: Int) =
    onView(withId(viewId))
        .check(matches(withDrawableId(drawableId)))

fun withDrawableId(@DrawableRes id: Int): TypeSafeMatcher<View?>? {
    return VectorLevelListDrawableMatcher(id)
}

fun assertRenautToggleIconButtonActive(@IdRes viewId: Int): ViewInteraction =
    onView(withId(viewId)).check(matches(activeRenaultToggleIconButton()))

fun assertRenautToggleIconButtonNotActive(@IdRes viewId: Int): ViewInteraction =
    onView(withId(viewId)).check(matches(not(activeRenaultToggleIconButton())))

fun activeRenaultToggleIconButton() = RenaultToggleIconButtonActiveMatcher()

// https://stackoverflow.com/questions/35655741/seekbar-test-not-calling-onseekbarchangelistener
fun clickSeekBar(pos: Int): ViewAction? {
    return GeneralClickAction(
        Tap.SINGLE,
        CoordinatesProvider { view ->
            val seekBar = view as SeekBar
            val screenPos = IntArray(2)
            seekBar.getLocationOnScreen(screenPos)

            // get the width of the actual bar area
            // by removing padding
            val trueWidth = (seekBar.width -
                seekBar.paddingLeft - seekBar.paddingRight)

            // what is the position on a 0-1 scale
            //  add 0.3f to avoid roundoff to the next smaller position
            var relativePos = (0.3f + pos) / seekBar.max.toFloat()
            if (relativePos > 1.0f) relativePos = 1.0f

            // determine where to click
            val screenX = (trueWidth * relativePos + screenPos[0] +
                seekBar.paddingLeft)
            val screenY = seekBar.height / 2f + screenPos[1]
            floatArrayOf(screenX, screenY)
        },
        Press.FINGER, 0, 0
    )
}