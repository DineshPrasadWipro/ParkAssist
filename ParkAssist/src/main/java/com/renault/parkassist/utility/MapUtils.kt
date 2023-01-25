package com.renault.parkassist.utility

import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.subjects.Subject

fun List<Pair<Int, Int>>.map(input: Int): Int = first { it.first == input }.second
fun List<Pair<Int, Int>>.unMap(input: Int): Int = first { it.second == input }.first
fun List<Pair<Int, Int>>.mapNullable(input: Int): Int? =
    firstOrNull { it.first == input }?.second

inline fun catchStringMap(crossinline mapToString: () -> String) = catchMapError(
    {
        mapToString()
    },
    {
        ""
    }
)

inline fun <T> MutableLiveData<T>.safeMapPost(
    crossinline map: () -> T,
    crossinline fallback: () -> Unit
) =
    catchMapError(
        {
            postValue(map())
        },
        {
            fallback()
        }
    )

inline fun <T> Subject<T>.safeMapOnNext(
    crossinline map: () -> T,
    crossinline fallback: () -> Unit
) =
    catchMapError(
        {
            onNext(map())
        },
        {
            fallback()
        }
    )

inline fun <T : R, R> catchMapError(map: () -> T, fallback: () -> R): R = try {
    map()
} catch (e: NoSuchElementException) {
    fallback()
}