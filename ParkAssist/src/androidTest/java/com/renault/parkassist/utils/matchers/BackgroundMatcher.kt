package com.renault.parkassist.utils.matchers

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.IdRes
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class BackgroundMatcher(@IdRes private val expectedId: Int?) : TypeSafeMatcher<View>() {
    private var resourceName: String? = null

    override fun matchesSafely(target: View): Boolean {
        val resources = target.context.resources
        val background = target.background
        if (expectedId == null) {
            return background == null
        }
        resourceName = resources.getResourceEntryName(expectedId)
        val expectedDrawable =
            resources.getDrawable(expectedId, target.context.theme) ?: return false
        val actualBitmap = getBitmap(background)
        val expectedBitmap = getBitmap(expectedDrawable)
        return actualBitmap.sameAs(expectedBitmap)
    }

    private fun getBitmap(drawable: Drawable): Bitmap {
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    override fun describeTo(description: Description) {
        description.appendText("with drawable from resource id: ")
        description.appendValue(expectedId)
        if (resourceName != null) {
            description.appendText(" ~ [")
            description.appendText(resourceName)
            description.appendText("]")
        }
    }
}