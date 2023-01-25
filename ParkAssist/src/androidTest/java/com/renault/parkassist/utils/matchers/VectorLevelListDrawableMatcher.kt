package com.renault.parkassist.utils.matchers

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.graphics.drawable.LevelListDrawable
import android.graphics.drawable.VectorDrawable
import android.view.View
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class VectorLevelListDrawableMatcher(private val expectedId: Int) :
    TypeSafeMatcher<View?>(View::class.java) {
    private var resourceName: String? = null

    override fun describeTo(description: Description) {
        description.appendText("with drawable from resource id: ")
        description.appendValue(expectedId)
        if (resourceName != null) {
            description.appendText("[")
            description.appendText(resourceName)
            description.appendText("]")
        }
    }

    override fun matchesSafely(target: View?): Boolean {
        if (target !is ImageView) {
            return false
        }
        if (expectedId < 0) {
            return target.drawable == null
        }
        val resources: Resources = target.getContext().resources
        resourceName = resources.getResourceEntryName(expectedId)
        val expectedDrawable: Drawable? = try {
            resources.getDrawable(expectedId, null)
        } catch (e: Resources.NotFoundException) {
            return false
        }
        val bitmap = (target.drawable as LevelListDrawable).toBitmap()
        val otherBitmap = (expectedDrawable as VectorDrawable).toBitmap()
        return bitmap.sameAs(otherBitmap)
    }
}