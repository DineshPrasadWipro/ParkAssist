/*
 * 2019 Developed by RENAULT s.a.s. which holds all intellectual property rights.
 * Use of this software is subject to a specific license granted by Renault s.a.s.
 */

package com.renault.uilibrary.utils.extensions

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.forEach

/**
 * Extension that sets the image resource resource @see `ImageView.setImageResource`
 * and stores the resource ID in the view tag.
 *
 * Notes:
 *  * This is a workaround for test purpose as for instance we have issues testing
 *    bitmaps equality when generated from xml vector drawables resources.
 *  * Use `TagAsResIdMatcher` in your test scripts
 */
fun ImageView.setImageResourceAsTag(resId: Int) {
    setImageResource(resId)
    tag = resId
}

fun View.enableClick(enabled: Boolean) {
    isClickable = enabled
    if (this is ViewGroup)
        forEach { it.enableClick(enabled) }
}