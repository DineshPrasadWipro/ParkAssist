package com.renault.parkassist.ui.camera

import android.app.Application
import android.view.View
import androidx.annotation.DrawableRes
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.LargeTest
import com.renault.parkassist.R
import com.renault.parkassist.utils.matchers.DrawableMatcher
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@LargeTest
class CameraIndicatorViewTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var cameraIndicatorView: CameraIndicatorView
    private lateinit var application: Application

    @Before
    fun setup() {
        application = ApplicationProvider.getApplicationContext()
        cameraIndicatorView = CameraIndicatorView(application)
    }

    @Test
    fun should_not_show_indicator_when_direction_value_is_none() { // ktlint-disable max-line-length
        cameraIndicatorView.direction = CameraIndicatorView.Indication.NONE
        assertEquals(View.INVISIBLE, cameraIndicatorView.visibility)
    }

    @Test
    fun should_show_front_indicator_when_direction_value_is_front() { // ktlint-disable max-line-length
        cameraIndicatorView.direction = CameraIndicatorView.Indication.FRONT
        assertEquals(View.VISIBLE, cameraIndicatorView.visibility)
        checkDrawableIndicator(
            R.drawable.ric_adas_avm_front
        )
    }

    @Test
    fun should_show_rear_indicator_when_direction_value_is_rear() { // ktlint-disable max-line-length
        cameraIndicatorView.direction = CameraIndicatorView.Indication.REAR
        assertEquals(View.VISIBLE, cameraIndicatorView.visibility)
        checkDrawableIndicator(R.drawable.ric_adas_avm_rear)
    }

    @Test
    fun should_show_trailer_indicator_when_direction_value_is_trailer() {
        cameraIndicatorView.direction = CameraIndicatorView.Indication.TRAILER
        assertEquals(View.VISIBLE, cameraIndicatorView.visibility)
        checkDrawableIndicator(
            R.drawable.ric_adas_trailer)
    }

    private fun checkDrawableIndicator(@DrawableRes drawable: Int) =
        assertTrue(DrawableMatcher(drawable).matches(cameraIndicatorView))
}