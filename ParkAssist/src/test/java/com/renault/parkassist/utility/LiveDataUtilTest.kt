package com.renault.parkassist.utility

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.filters.SmallTest
import com.renault.parkassist.viewmodel.filter
import com.renault.parkassist.viewmodel.filterFirst
import com.renault.parkassist.viewmodel.map
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

@SmallTest
class LiveDataUtilTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun ignoreFirstValue() {
        val liveData = MutableLiveData<Int>()
        val events = mutableListOf<Int>()
        liveData.filterFirst().observeForever { events += it }

        liveData.postValue(1)
        Assert.assertEquals(0, events.size)
        liveData.postValue(1)
        Assert.assertEquals(1, events.size)
    }

    @Test
    fun filter() {
        val liveData = MutableLiveData<Int>()
        val events = mutableListOf<Int>()
        liveData.filter {
            it != 2
        }.observeForever { events += it }

        liveData.postValue(1)
        Assert.assertEquals(1, events.size)
        Assert.assertEquals(1, events[events.size - 1])
        liveData.postValue(2)
        Assert.assertEquals(1, events.size)
        Assert.assertEquals(1, events[events.size - 1])
        liveData.postValue(3)
        Assert.assertEquals(2, events.size)
        Assert.assertEquals(3, events[events.size - 1])
    }

    @Test
    fun map() {
        val liveData = MutableLiveData<Int>()
        val events = mutableListOf<Int>()
        liveData.map {
            it + 1
        }.observeForever { events += it }

        liveData.postValue(1)
        Assert.assertEquals(1, events.size)
        Assert.assertEquals(2, events[events.size - 1])
        liveData.postValue(2)
        Assert.assertEquals(2, events.size)
        Assert.assertEquals(3, events[events.size - 1])
        liveData.postValue(3)
        Assert.assertEquals(3, events.size)
        Assert.assertEquals(4, events[events.size - 1])
    }
}