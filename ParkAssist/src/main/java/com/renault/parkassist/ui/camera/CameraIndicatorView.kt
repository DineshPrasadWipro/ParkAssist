package com.renault.parkassist.ui.camera

import alliancex.renault.ui.RenaultIconView
import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.renault.parkassist.R

class CameraIndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    RenaultIconView(context, attrs, defStyleAttr) {
    enum class Indication(val id: Int) {
        FRONT(0),
        REAR(1),
        NONE(2),
        TRAILER(3),
        THREE_D(4);

        companion object {
            fun getById(id: Int) = values().firstOrNull { it.id == id }
        }
    }

    private fun Int.toIndication() = Indication.getById(this) ?: Indication.NONE

    var direction: Indication = Indication.FRONT
        set(value) {
            field = value

            when (value) {
                Indication.FRONT -> {
                    visibility = View.VISIBLE
                    setImageResource(R.drawable.ric_adas_avm_front)
                    tag = R.drawable.ric_adas_avm_front
                }
                Indication.REAR -> {
                    visibility = View.VISIBLE
                    setImageResource(R.drawable.ric_adas_avm_rear)
                    tag = R.drawable.ric_adas_avm_rear
                }
                Indication.TRAILER -> {
                    visibility = View.VISIBLE
                    setImageResource(R.drawable.ric_adas_trailer)
                    tag = R.drawable.ric_adas_trailer
                }
                Indication.THREE_D -> {
                    visibility = View.VISIBLE
                    setImageResource(R.drawable.ric_adas_360)
                    tag = R.drawable.ric_adas_360
                }
                Indication.NONE -> {
                    visibility = View.INVISIBLE
                }
            }

            invalidate()
            requestLayout()
        }

    init {
        attrs?.let {
            val ta = context.obtainStyledAttributes(it, R.styleable.CameraIndicatorView)
            direction = ta.getInt(R.styleable.CameraIndicatorView_direction, Indication.FRONT.id)
                .toIndication()
            ta.recycle()
        }
    }
}