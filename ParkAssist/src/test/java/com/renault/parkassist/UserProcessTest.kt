package com.renault.parkassist

import alliance.car.surroundview.SurroundViewCapabilities
import androidx.test.filters.SmallTest
import com.renault.parkassist.camera.CameraConnectionManager
import com.renault.parkassist.camera.CameraManager
import com.renault.parkassist.repository.surroundview.SurroundViewManagerAdapter
import io.mockk.*
import org.junit.Before
import org.junit.Test
import org.koin.dsl.module

@SmallTest
class UserProcessTest : TestBase() {
    private lateinit var userProcess: UserProcess

    private val carSurroundViewManagerAdapter = mockk<SurroundViewManagerAdapter>()
    private val cameraManager = mockk<CameraManager>()
    private val cameraConnectionManager = mockk<CameraConnectionManager>()

    override val koinModule = module {
        single<SurroundViewManagerAdapter> { carSurroundViewManagerAdapter }
        single<CameraManager> { cameraManager }
        single { cameraConnectionManager }
    }

    @Before
    override fun setup() {
        super.setup()
        userProcess = UserProcess(mockk())
    }

    @Test
    fun `should not register to camera with no camera configuration`() {
        every {
            carSurroundViewManagerAdapter.featureConfig
        } returns SurroundViewCapabilities.FEATURE_NOT_SUPPORTED

        verify(exactly = 0) { cameraConnectionManager.connect() }
        verify(exactly = 0) { cameraConnectionManager.registerListener(any()) }
    }
}