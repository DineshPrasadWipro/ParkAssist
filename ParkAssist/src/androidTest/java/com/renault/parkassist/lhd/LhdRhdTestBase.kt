package com.renault.parkassist.lhd

import com.renault.parkassist.activity.OverlayActivityTest

// FIXME: Update when RHD/LHD is supported again
//  ref CCSEXT-71793
abstract class LhdRhdTestBase : OverlayActivityTest() {

    init {
//        mockkObject(UxConfig)
    }

    protected fun setLhd() {
//        every { UxConfig.get(any()) } returns mockk {
//            every { lhd } returns true
//        }
    }

    protected fun setRhd() {
//        every { UxConfig.get(any()) } returns mockk {
//            every { lhd } returns false
//        }
    }
}