package com.renault.parkassist.routing.pursuit

import com.renault.parkassist.routing.Pursuit

interface IPursuit {
    fun startPursuit(pursuit: Pursuit)
    fun stopPursuit(pursuit: Pursuit)
    fun stopCurrentPursuit()
}