package com.renault.parkassist.ui.apa

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.renault.parkassist.R
import kotlinx.android.synthetic.main.fragment_hfp_guidance.*

class GuidanceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    init {
        View.inflate(context, R.layout.guidance_view, this)
    }
}