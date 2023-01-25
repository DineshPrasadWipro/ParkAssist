package com.renault.parkassist.service

import android.os.IBinder
import androidx.test.filters.SmallTest
import com.renault.parkassist.TestBase
import com.renault.parkassist.camera.ICameraConnectionListener
import io.mockk.mockk
import kotlin.test.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.core.module.Module
import org.koin.dsl.module

@SmallTest
class CameraConnectionPolicyTest : TestBase() {

    private lateinit var cameraConnectionPolicy: CameraConnectionPolicy

    override val koinModule: Module = module { }

    data class Process(
        val cameraListener: CameraListener = CameraListener(false),
        val listener: ICameraConnectionListener = createListener(cameraListener),
        val binder: IBinder = mockk()
    ) {
        companion object {
            data class CameraListener(var isRegistered: Boolean)

            private fun createListener(listener: CameraListener) =
                object : ICameraConnectionListener {
                    override fun asBinder(): IBinder? = null

                    override fun onCameraConnectionAccepted() {
                        listener.isRegistered = true
                    }
                }
        }
    }

    @Before
    override fun setup() {
        super.setup()
        cameraConnectionPolicy = CameraConnectionPolicy()
    }

    @Test
    fun `first client should register to camera`() {
        val process = Process()
        cameraConnectionPolicy.addClient(process.listener, process.binder)
        assertEquals(true, process.cameraListener.isRegistered)
    }

    @Test
    fun `should not add same client twice`() {

        val process = Process()

        cameraConnectionPolicy.addClient(process.listener, process.binder)
        assertEquals(true, process.cameraListener.isRegistered)
        process.cameraListener.isRegistered = false
        cameraConnectionPolicy.addClient(process.listener, process.binder)
        cameraConnectionPolicy.removeClient(process.binder)
        assertEquals(false, process.cameraListener.isRegistered)
    }

    @Test
    fun `should register second process when added before first one is removed`() {
        val process1 = Process()
        val process2 = Process()

        cameraConnectionPolicy.addClient(process1.listener, process1.binder)
        assertEquals(true, process1.cameraListener.isRegistered)
        cameraConnectionPolicy.addClient(process2.listener, process2.binder)
        assertEquals(false, process2.cameraListener.isRegistered)

        cameraConnectionPolicy.removeClient(process1.binder)
        assertEquals(true, process2.cameraListener.isRegistered)
    }

    @Test
    fun `should register second process when added before first one is removed twice`() {
        val process1 = Process()
        val process2 = Process()

        cameraConnectionPolicy.addClient(process1.listener, process1.binder)
        assertEquals(true, process1.cameraListener.isRegistered)
        cameraConnectionPolicy.addClient(process2.listener, process2.binder)
        assertEquals(false, process2.cameraListener.isRegistered)

        // Received to background intent
        cameraConnectionPolicy.removeClient(process1.binder)
        // Process kill, binder died detected, client removed again
        cameraConnectionPolicy.removeClient(process1.binder)

        assertEquals(true, process2.cameraListener.isRegistered)
    }

    @Test
    fun `should register second process when added after first one is removed`() {
        val process1 = Process()
        val process2 = Process()

        cameraConnectionPolicy.addClient(process1.listener, process1.binder)
        assertEquals(true, process1.cameraListener.isRegistered)

        cameraConnectionPolicy.removeClient(process1.binder)

        cameraConnectionPolicy.addClient(process2.listener, process2.binder)
        assertEquals(true, process2.cameraListener.isRegistered)
    }

    @Test
    fun `should register second process when added after first one is removed twice`() {
        val process1 = Process()
        val process2 = Process()

        cameraConnectionPolicy.addClient(process1.listener, process1.binder)
        assertEquals(true, process1.cameraListener.isRegistered)

        // Received to background intent
        cameraConnectionPolicy.removeClient(process1.binder)
        // Process kill, binder died detected, client removed again
        cameraConnectionPolicy.removeClient(process1.binder)

        cameraConnectionPolicy.addClient(process2.listener, process2.binder)
        assertEquals(true, process2.cameraListener.isRegistered)
    }
}