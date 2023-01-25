package com.renault.parkassist.ui.apa

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.renault.parkassist.R

class SonarDisabledView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        View.inflate(context, R.layout.sonar_disabled_view, this)
    }
}