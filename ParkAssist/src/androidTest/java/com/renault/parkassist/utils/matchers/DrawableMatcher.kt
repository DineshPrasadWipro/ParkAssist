package com.renault.parkassist.utils.matchers

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.IdRes
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description

class DrawableMatcher(@IdRes private val expectedId: Int?) :
    BoundedMatcher<View, ImageView>(ImageView::class.java) {
    private var resourceName: String? = null

    override fun matchesSafely(target: ImageView): Boolean {
        if (expectedId == null)
            return target.drawable == null

        val resources = target.context.resources
        resourceName = resources.getResourceEntryName(expectedId)
        val expectedDrawable =
            resources.getDrawable(expectedId, target.context.theme) ?: return false
        val bitmap = getBitmap(target.drawable)
        val expectedBitmap = getBitmap(expectedDrawable)
        return bitmap.sameAs(expectedBitmap)
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