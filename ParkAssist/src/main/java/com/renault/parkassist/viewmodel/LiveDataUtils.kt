package com.renault.parkassist.viewmodel

import Quadruple
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.*

object LiveDataUtils {
    fun <A, B> combineLatest(
        a: LiveData<A>,
        b: LiveData<B>
    ): LiveData<Pair<A?, B?>> {

        fun Pair<A?, B?>?.copyWithFirst(first: A?): Pair<A?, B?> {
            if (this@copyWithFirst == null) return Pair(first, null)
            return this@copyWithFirst.copy(first = first)
        }

        fun Pair<A?, B?>?.copyWithSecond(second: B?): Pair<A?, B?> {
            if (this@copyWithSecond == null) return Pair(null, second)
            return this@copyWithSecond.copy(second = second)
        }

        return MediatorLiveData<Pair<A?, B?>>().apply {
            addSource(a) { value = value.copyWithFirst(it) }
            addSource(b) { value = value.copyWithSecond(it) }
        }
    }

    fun <A, B> combineNonNull(
        first: LiveData<A>,
        second: LiveData<B>
    ): MediatorLiveData<Pair<A, B>> {
        var source1emitted = false
        var source2emitted = false

        val result = MediatorLiveData<Pair<A, B>>()

        val mergeF = {
            val source1Value = first.value
            val source2Value = second.value

            if (source1emitted && source2emitted) {
                result.value = Pair(source1Value!!, source2Value!!)
            }
        }

        result.addSource(first) {
            source1emitted = true
            mergeF.invoke()
        }
        result.addSource(second) {
            source2emitted = true
            mergeF.invoke()
        }
        return result
    }

    fun <A, B, C> combineNonNull(
        first: LiveData<A>,
        second: LiveData<B>,
        third: LiveData<C>
    ): MediatorLiveData<Triple<A, B, C>> {
        var source1emitted = false
        var source2emitted = false
        var source3emitted = false

        val result = MediatorLiveData<Triple<A, B, C>>()

        val mergeF = {
            val source1Value = first.value
            val source2Value = second.value
            val source3Value = third.value

            if (source1emitted && source2emitted && source3emitted) {
                result.value = Triple(source1Value!!, source2Value!!, source3Value!!)
            }
        }

        result.addSource(first) {
            source1emitted = true
            mergeF.invoke()
        }
        result.addSource(second) {
            source2emitted = true
            mergeF.invoke()
        }
        result.addSource(third) {
            source3emitted = true
            mergeF.invoke()
        }
        return result
    }

    fun <A, B, C, D> combineNonNull(
        first: LiveData<A>,
        second: LiveData<B>,
        third: LiveData<C>,
        fourth: LiveData<D>
    ): MediatorLiveData<Quadruple<A, B, C, D>> {
        var source1emitted = false
        var source2emitted = false
        var source3emitted = false
        var source4emitted = false

        val result = MediatorLiveData<Quadruple<A, B, C, D>>()

        val mergeF = {
            val source1Value = first.value
            val source2Value = second.value
            val source3Value = third.value
            val source4Value = fourth.value

            if (source1emitted && source2emitted && source3emitted && source4emitted) {
                result.value = Quadruple(source1Value!!, source2Value!!,
                    source3Value!!, source4Value!!)
            }
        }

        result.addSource(first) {
            source1emitted = true
            mergeF.invoke()
        }
        result.addSource(second) {
            source2emitted = true
            mergeF.invoke()
        }
        result.addSource(third) {
            source3emitted = true
            mergeF.invoke()
        }
        result.addSource(fourth) {
            source4emitted = true
            mergeF.invoke()
        }
        return result
    }

    fun <A, B, C> combineLatest(
        a: LiveData<A>,
        b: LiveData<B>,
        c: LiveData<C>
    ): LiveData<Triple<A?, B?, C?>> {
        fun Triple<A?, B?, C?>?.copyWithFirst(first: A?): Triple<A?, B?, C?> {
            if (this@copyWithFirst == null) return Triple<A?, B?, C?>(first, null, null)
            return this@copyWithFirst.copy(first = first)
        }

        fun Triple<A?, B?, C?>?.copyWithSecond(second: B?): Triple<A?, B?, C?> {
            if (this@copyWithSecond == null) return Triple<A?, B?, C?>(null, second, null)
            return this@copyWithSecond.copy(second = second)
        }

        fun Triple<A?, B?, C?>?.copyWithThird(third: C?): Triple<A?, B?, C?> {
            if (this@copyWithThird == null) return Triple<A?, B?, C?>(null, null, third)
            return this@copyWithThird.copy(third = third)
        }

        return MediatorLiveData<Triple<A?, B?, C?>>().apply {
            addSource(a) { value = value.copyWithFirst(it) }
            addSource(b) { value = value.copyWithSecond(it) }
            addSource(c) { value = value.copyWithThird(it) }
        }
    }
}

fun <T> LiveData<T>.filterFirst(): MutableLiveData<T> {
    val mediatorLiveData: MediatorLiveData<T> = MediatorLiveData()
    var firstValue = true
    mediatorLiveData.addSource(this) {
        if (firstValue) {
            firstValue = false
        } else {
            mediatorLiveData.value = it
        }
    }
    return mediatorLiveData
}

fun <T> LiveData<T>.filter(filter: (t: T) -> Boolean): LiveData<T> {
    val mediatorLiveData: MediatorLiveData<T> = MediatorLiveData()
    mediatorLiveData.addSource(this) {
        if (filter.invoke(it)) {
            mediatorLiveData.value = it
        }
    }
    return mediatorLiveData
}

fun <T> LiveData<T?>.filterNull(): LiveData<T> {
    val mediatorLiveData: MediatorLiveData<T> = MediatorLiveData()
    mediatorLiveData.addSource(this) {
        if (it != null)
            mediatorLiveData.value = it
    }
    return mediatorLiveData
}

fun <T, V> LiveData<T>.map(map: (t: T) -> V): LiveData<V> {
    val mediatorLiveData: MediatorLiveData<V> = MediatorLiveData()
    mediatorLiveData.addSource(this) {
        mediatorLiveData.value = map.invoke(it)
    }
    return mediatorLiveData
}

fun <T> LiveData<T>.observeOnce(observer: Observer<T>) {
    observeForever(object : Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}

fun <T, X, V> mapAndMerge(
    firstSource: LiveData<T>,
    secondSource: LiveData<X>,
    firstMap: (t: T) -> V,
    secondMap: (x: X) -> V
): LiveData<V> = MediatorLiveData<V>().apply {
    addSource(firstSource) { v ->
        value = firstMap(v)
    }
    addSource(secondSource) { v ->
        value = secondMap(v)
    }
}

fun <T> LiveData<T>.merge(otherSource: LiveData<T>): LiveData<T> = MediatorLiveData<T>().apply {
    addSource(this@merge) { v ->
        value = v
    }
    addSource(otherSource) { v ->
        value = v
    }
}

fun <T, K, R> LiveData<T>.mergeNotNullWith(
    otherSource: LiveData<K>,
    merge: (T, K) -> R
): LiveData<R> {

    fun <K, R, T> LiveData<T>.mergeNotNull(
        otherSource: LiveData<K>,
        result: MutableLiveData<R>,
        merge: (T, K) -> R
    ) {
        val first = this.value
        val second = otherSource.value
        if (first != null && second != null)
            result.value = merge(first, second)
    }

    return MediatorLiveData<R>().apply {
        addSource(this@mergeNotNullWith) {
            mergeNotNull(otherSource, this, merge)
        }
        addSource(otherSource) {
            mergeNotNull(otherSource, this, merge)
        }
    }
}

fun <T> LiveData<T>.toMutable(defaultValue: T): MutableLiveData<T> = MediatorLiveData<T>().apply {
    value = defaultValue
    addSource(this@toMutable) { value = it }
}

fun <T> LiveData<T>.debounce(duration: Long = 100L) = MediatorLiveData<T>().also { mld ->
    val source = this
    val handler = Handler(Looper.getMainLooper())

    val runnable = Runnable {
        mld.value = source.value
    }

    mld.addSource(source) {
        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, duration)
    }
}