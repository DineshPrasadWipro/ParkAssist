package com.renault.parkassist.ui

import androidx.test.filters.SmallTest
import com.renault.parkassist.utility.Size
import com.renault.parkassist.utility.UiUtils.getMaxSizeWithRatio
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

@SmallTest
class UiUtilsTest {
    private val portraitContainer = Size(512, 2048)
    private val landscapeContainer = Size(2048, 512)
    private val portraitRatio = Size(9, 16)
    private val landscapeRatio = Size(16, 9)
    private val nullWidthRatio = Size(0, 16)
    private val nullHeightRatio = Size(9, 0)

    @Test
    fun should_return_container_size_when_ratio_width_is_0() { // ktlint-disable max-line-length

        assertEquals(
            portraitContainer,
            getMaxSizeWithRatio(
                nullWidthRatio.width, nullWidthRatio.height,
                portraitContainer.width, portraitContainer.height
            )
        )
    }

    @Test
    fun should_return_containerSize_when_ratio_height_is_0() { // ktlint-disable max-line-length
        assertEquals(
            portraitContainer,
            getMaxSizeWithRatio(
                nullHeightRatio.width, nullHeightRatio.height,
                portraitContainer.width, portraitContainer.height
            )
        )
    }

    @Test
    fun should_return_a_size_with_container_height_when_ratio_is_portrait_and_container_is_landscape() { // ktlint-disable max-line-length
        assertEquals(
            landscapeContainer.height,
            getMaxSizeWithRatio(
                portraitRatio.width, portraitRatio.height,
                landscapeContainer.width, landscapeContainer.height
            ).height
        )
        assertTrue(
            landscapeContainer.width >= getMaxSizeWithRatio(
                portraitRatio.width, portraitRatio.height,
                landscapeContainer.width, landscapeContainer.height
            ).width
        )
    }

    @Test
    fun should_return_a_size_with_container_width_when_ratio_is_landscape_and_container_is_portrait() { // ktlint-disable max-line-length
        assertEquals(
            portraitContainer.width,
            getMaxSizeWithRatio(
                landscapeRatio.width, landscapeRatio.height,
                portraitContainer.width, portraitContainer.height
            ).width
        )
        assertTrue(
            landscapeContainer.height >= getMaxSizeWithRatio(
                landscapeRatio.width, landscapeRatio.height,
                portraitContainer.width, portraitContainer.height
            ).height
        )
    }
}