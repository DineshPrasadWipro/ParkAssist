package com.renault.parkassist.ui.apa.hfp

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.renault.parkassist.R

class HfpGuidanceVehicleRightView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    init {
        View.inflate(context, R.layout.guidance_vehicle_right_view, this)
    }
}