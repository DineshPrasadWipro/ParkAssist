package com.renault.parkassist.ui.camera

import alliancex.arch.core.logger.logD
import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import com.renault.parkassist.utility.extractLocationOnScreen
import com.renault.parkassist.viewmodel.avm.Finger

class TouchInterceptor(private val listener: TouchListener) : View.OnTouchListener {

    interface TouchListener {
        fun screenPress(@Finger finger: Int, x: Float, y: Float)
        fun screenRelease(@Finger finger: Int)
    }

    private var primaryPointerId = INVALID_POINTER_ID
    private var secondaryPointerId = INVALID_POINTER_ID

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        logD { "onTouchEvent($event)" }
        val (x0, y0) = v.extractLocationOnScreen()
        val action = event.actionMasked
        val index = event.actionIndex
        val id = event.getPointerId(index)
        when (action) {
            ACTION_DOWN -> {
                val x = event.x + x0
                val y = event.y + y0
                primaryPointerId = id

                logD { "ACTION_DOWN screenPress(FIRST_TOUCH) x = $x,  y = $y" }
                listener.screenPress(Finger.FIRST, x, y)
            }
            ACTION_POINTER_DOWN -> {
                if (secondaryPointerId == INVALID_POINTER_ID) {
                    val x = event.x + x0
                    val y = event.y + y0
                    secondaryPointerId = id

                    logD { "ACTION_POINTER_DOWN screenPress(SECOND_TOUCH) x = $x,  y = $y" }
                    listener.screenPress(Finger.SECOND, x, y)
                } else {
                    logD { "ACTION_POINTER_DOWN on more than two pointers no supported" }
                }
            }
            ACTION_MOVE -> {
                val indexFirst = event.findPointerIndex(primaryPointerId)
                val xFirst = event.getX(indexFirst) + x0
                val yFirst = event.getY(indexFirst) + y0
                listener.screenPress(Finger.FIRST, xFirst, yFirst)

                if (secondaryPointerId != INVALID_POINTER_ID) {
                    val indexSecond = event.findPointerIndex(secondaryPointerId)
                    val xSecond = event.getX(indexSecond) + x0
                    val ySecond = event.getY(indexSecond) + y0
                    listener.screenPress(Finger.SECOND, xSecond, ySecond)
                }
            }
            ACTION_UP -> {
                primaryPointerId = INVALID_POINTER_ID
                logD { "ACTION_UP screenRelease(FIRST_TOUCH, SECOND_TOUCH)" }
                listener.screenRelease(Finger.FIRST)
                listener.screenRelease(Finger.SECOND)
            }
            ACTION_POINTER_UP -> {
                when (id) {
                    primaryPointerId -> {
                        // This was our active pointer going up.
                        // Choose a new active pointer and adjust accordingly.
                        val newPointerIndex = if (index == 0) 1 else 0
                        primaryPointerId = event.getPointerId(newPointerIndex)
                        secondaryPointerId = INVALID_POINTER_ID
                        logD { "ACTION_POINTER_UP screenRelease(FIRST_TOUCH)" }
                        listener.screenRelease(Finger.SECOND)
                    }
                    secondaryPointerId -> {
                        secondaryPointerId = INVALID_POINTER_ID
                        logD { "ACTION_POINTER_UP screenRelease(SECOND_TOUCH)" }
                        listener.screenRelease(Finger.SECOND)
                    }
                    else -> {
                        logD { "ACTION_POINTER_UP on unknown pointer" }
                    }
                }
            }
            ACTION_CANCEL -> {
                primaryPointerId = INVALID_POINTER_ID
                logD { "ACTION_CANCEL screenRelease(FIRST_TOUCH)" }
                listener.screenRelease(Finger.FIRST)
            }
        }
        return true
    }
}