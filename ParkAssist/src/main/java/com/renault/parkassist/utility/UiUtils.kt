package com.renault.parkassist.utility

import alliancex.renault.ui.RenaultSlider
import alliancex.renault.ui.RenaultSwitch
import alliancex.renault.ui.RenaultToggleIconButton
import android.content.Context
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.CompoundButton
import android.widget.SeekBar
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlin.reflect.KProperty

object UiUtils {

    fun dpToPixels(context: Context, dp: Int): Int =
        (dp * context.resources.displayMetrics.density).toInt()

    fun getMaxSizeWithRatio(
        ratioWidth: Int,
        ratioHeight: Int,
        maxWidth: Int,
        maxHeight: Int
    ): Size {
        var width = maxWidth
        var height = maxHeight

        if (0 != ratioWidth && 0 != ratioHeight) {
            if (maxWidth < maxHeight * ratioWidth / ratioHeight) {
                height = maxWidth * ratioHeight / ratioWidth
            } else {
                width = maxHeight * ratioWidth / ratioHeight
            }
        }
        return Size(width, height)
    }
}

class Size(val width: Int, val height: Int) {
    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (this === other) return true
        if (other is Size) {
            val otherSize = other as Size?
            return width == otherSize!!.width && height == otherSize.height
        }
        return false
    }
}

fun View.extractLocationOnScreen(): Pair<Int, Int> {
    val location = IntArray(2)
    getLocationOnScreen(location)
    return windowLocationToScreenLocation(location[0], location[1])
}

fun View.windowLocationToScreenLocation(x: Float, y: Float): Pair<Float, Float> =
    Pair(x, y + windowTopInset)

fun View.windowLocationToScreenLocation(x: Int, y: Int): Pair<Int, Int> =
    Pair(x, y + windowTopInset)

// FIXME: Use dedicated Android API to perform the translation
// Follow-up JIRA: https://jira.dt.renault.com/browse/CCSEXT-18528)
// This is a work-around to translate window position (application area)
// into screen position (screen area incl. all system UI)
// We manually add status bar height to window coordinates => not maintainable
private val View.windowTopInset: Int
    get() {
        val statusBarId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (statusBarId > 0) resources.getDimensionPixelSize(statusBarId)
        else 0
    }

fun View.setPresent(visible: Boolean = true) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.setVisible(visible: Boolean = true) {
    this.visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

const val ALPHA_0F = 0f
const val ALPHA_1F = 1f
const val FADE_IN_DURATION = 180L
const val FADE_OUT_DURATION = 180L

fun View.setVisibleWithFadeInFadeOut(visible: Boolean = true) {
    if (visible) {
        this.visibility = View.VISIBLE
        this.apply {
            alpha = ALPHA_0F
            animate()
                .alpha(ALPHA_1F)
                .setDuration(FADE_IN_DURATION)
                .setInterpolator(AccelerateInterpolator())
        }
    } else {
        if (this.visibility == View.VISIBLE)
            this.apply {
                alpha = ALPHA_1F
                animate()
                    .alpha(ALPHA_0F)
                    .setDuration(FADE_OUT_DURATION)
                    .setInterpolator(DecelerateInterpolator())
                    .withEndAction { visibility = View.INVISIBLE }
            }
    }
}

/**
 * Use this extension value to create an hot observable that emits when view
 * get clicked
 */
val <V : View> V.click: Observable<V>
    get() = Observable.create<V> { emitter ->
        setOnClickListener {
            it.id
            emitter.onNext(this)
        }
    }.doOnDispose { setOnClickListener(null) }

// TODO("Replace by alliancex arch lib when tis ViewBindings will be available")
/**
 * Use this extension value to create an hot observable that emits when seek bar
 * progress change
 */
val RenaultSlider.onSeekBarChange by event1<RenaultSlider, Int> { view, emit ->
    view.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            emit(progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {
            // called when tracking the seekbar is started
        }

        override fun onStopTrackingTouch(seekBar: SeekBar) {
            // called when tracking the seekbar is stopped
        }
    })
}

// TODO("To be replaced by arch alliance event when available")
/**
 * Use this extension value to create an hot observable that emits when RenaultSwitch
 * is checked.
 */
val RenaultSwitch.onCheck by event1<RenaultSwitch, Boolean> { view, emit ->
    view.setOnCheckedChangeListener { _, isChecked ->
        emit(isChecked)
    }
}

// TODO("To be replaced by arch alliance event when available")
/**
 * Use this extension value to create an hot observable that emits when RenaultToggleIconButton
 * is checked.
 */
val CompoundButton.onCheck by event1<CompoundButton, Boolean> { view, emit ->
    view.setOnCheckedChangeListener { _, isChecked ->
        emit(isChecked)
    }
}

val View.centerX
    get() = (this.x + this.width) / 2

var RenaultToggleIconButton.isActive: Boolean
    get() = isChecked && !isClickable
    set(value) {
        isClickable = !value
        isChecked = value
    }


open class ViewTaggedLazy<in R : View, out T>(
    private val initializer: (thisRef: R) -> T
) {
    operator fun getValue(thisRef: R, property: KProperty<*>): T {
        synchronized(thisRef) {
            @Suppress("UNCHECKED_CAST")
            return initializer(thisRef)
        }
    }
}

class ViewEvent1<T>(emitter: ((t: T) -> Unit) -> Unit) : Observable<T>() {
    private val subject = PublishSubject.create<T>()
        .apply {
            emitter {
                onNext(it)
            }
        }

    operator fun invoke(dispatch: (t: T) -> Unit) {
        subject.subscribe {
            dispatch(it)
        }
    }

    override fun subscribeActual(observer: Observer<in T>?) {
        subject.subscribe(observer)
    }
}

fun <V : View, T> event1(
    register: (V, (t: T) -> Unit) -> Unit
): ViewTaggedLazy<V, ViewEvent1<T>> {
    return ViewTaggedLazy { view ->
        ViewEvent1 {
            register(view, it)
        }
    }
}