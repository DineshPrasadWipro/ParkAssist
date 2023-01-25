package com.renault.parkassist.ui

import android.view.View
import android.widget.CompoundButton
import io.reactivex.rxjava3.core.Observable

/**
 * Selector is a RadioGroup-like control.
 */
class Selector<K>(private val members: Map<K, CompoundButton>) {
    private var selection: K? = null
    private var isClickable: Boolean = true

    val click: Observable<View> = Observable.create<View> { emitter ->
        val listener = View.OnClickListener { view ->
            val clickedKey = members.filterValues { it == view }.keys.first()
            clickedKey?.let { select(clickedKey) }
            emitter.onNext(view)
        }
        members.values.forEach {
            it.setOnClickListener(listener)
        }
    }.doOnDispose {
        members.values.forEach {
            it.setOnClickListener(null)
        }
    }

    fun select(selection: K) {
        if (selection == this.selection) return

        members.forEach {
            it.value.isChecked = it.key == selection
            it.value.isClickable = it.key != selection
        }

        this.selection = selection
        setClickable(isClickable)
    }

    fun setClickable(isClickable: Boolean) {
        members.forEach {
            if (isClickable) {
                it.value.isClickable = it.key != selection
            } else {
                it.value.isClickable = false
            }
        }
        this.isClickable = isClickable
    }
}