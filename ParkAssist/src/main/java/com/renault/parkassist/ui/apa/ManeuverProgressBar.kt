package com.renault.parkassist.ui.apa

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.renault.parkassist.R
import com.renault.parkassist.utility.UiUtils
import kotlin.math.roundToInt

class ManeuverProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    var progress = 0f
        set(value) {
            field = value
            invalidate()
        }

    var barPadding = 12f

    var forward = true
        set(value) {
            field = value
            invalidate()
        }

    var color: Int
        get() = progressPaint.color
        set(value) {
            progressPaint.color = value
            invalidate()
        }

    private val contourPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var progressBarPath = Path()
    private var contourPath = Path()
    private var barHeight = 0f
    private var barWidth = 0f
    private var rectHeight = 0f
    private val directionMatrix = Matrix()

    init {
        contourPaint.strokeWidth = 1f
        contourPaint.style = Paint.Style.STROKE
        contourPaint.color = context.getColor(R.color.rc_on_primary)
        attrs?.let {
            val ta: TypedArray =
                context.obtainStyledAttributes(attrs, R.styleable.ManeuverProgressBar)
            progress = ta.getFloat(R.styleable.ManeuverProgressBar_progress, 0f)
            forward = ta.getBoolean(R.styleable.ManeuverProgressBar_forward, true)
            val paddingDp = ta.getDimension(R.styleable.ManeuverProgressBar_innerPadding, 12f)
            barPadding = UiUtils.dpToPixels(context, paddingDp.roundToInt()).toFloat()
            ta.recycle()
        }
        progressPaint.isDither = true
        progressPaint.color = context.getColor(R.color.rc_on_secondary)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        barHeight = height - barPadding * 2
        barWidth = width - barPadding * 2
        rectHeight = barHeight - barWidth / 2f
        directionMatrix.setRotate(180f, width / 2f, height / 2f)
    }

    private fun calculateContourPath() {
        val w = width.toFloat()
        val h = height.toFloat()
        contourPath.reset()
        contourPath.moveTo(0f, 0f)
        contourPath.lineTo(0f, h - w / 2f)
        contourPath.lineTo(w / 2f, h)
        contourPath.lineTo(w, h - w / 2f)
        contourPath.lineTo(w, 0f)
        contourPath.lineTo(0f, 0f)
        contourPath.close()
        if (forward) contourPath.transform(directionMatrix)
    }

    override fun onDraw(canvas: Canvas) {
        calculateContourPath()
        canvas.drawPath(contourPath, contourPaint)
        calculateProgressBarPath()
        canvas.drawPath(progressBarPath, progressPaint)
    }

    private fun calculateProgressBarPath() {
        progressBarPath.reset()
        if (barHeight * progress <= rectHeight) {
            // We have to draw a part of the rectangle and the full triangle
            progressBarPath.moveTo(0f, barHeight * progress)
            progressBarPath.lineTo(0f, rectHeight)
            progressBarPath.lineTo(barWidth / 2f, barHeight)
            progressBarPath.lineTo(barWidth, rectHeight)
            progressBarPath.lineTo(barWidth, barHeight * progress)
            progressBarPath.close()
        } else {
            // We only have to draw a small triangle (the progress is greater than the rectangle portion)
            val remainingHeight = barHeight - barHeight * progress
            progressBarPath.moveTo(barWidth / 2 - remainingHeight, barHeight * progress)
            progressBarPath.lineTo(barWidth / 2f, barHeight)
            progressBarPath.lineTo(barWidth / 2 + remainingHeight, barHeight * progress)
            progressBarPath.close()
        }
        progressBarPath.offset(barPadding, barPadding)
        if (forward) progressBarPath.transform(directionMatrix)
    }
}