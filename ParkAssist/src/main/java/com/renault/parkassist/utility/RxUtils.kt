package com.renault.parkassist.utility


import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.Observables

inline fun <T1 : Any, T2 : Any, R : Any> Observable<T1>.combineLatestWith(
    other: Observable<T2>,
    crossinline combineFunction: (T1, T2) -> R
) =
    Observables.combineLatest(this, other, combineFunction)