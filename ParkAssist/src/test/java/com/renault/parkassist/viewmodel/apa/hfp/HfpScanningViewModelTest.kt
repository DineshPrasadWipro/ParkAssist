package com.renault.parkassist.viewmodel.apa.hfp

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.test.filters.SmallTest
import com.renault.parkassist.R
import com.renault.parkassist.TestBase
import com.renault.parkassist.TestUtils
import com.renault.parkassist.repository.apa.DisplayState
import com.renault.parkassist.repository.apa.IApaRepository
import com.renault.parkassist.repository.apa.Instruction
import com.renault.parkassist.repository.apa.ManeuverSelection
import com.renault.parkassist.repository.apa.ManeuverType
import com.renault.parkassist.repository.apa.mock.ApaRepositoryMock
import com.renault.parkassist.repository.sonar.ISonarRepository
import com.renault.parkassist.repository.sonar.mock.SonarRepositoryMock
import com.renault.parkassist.repository.surroundview.IExtSurroundViewRepository
import com.renault.parkassist.ui.apa.ManeuverButtonMapper
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.*
import org.koin.dsl.module

@SmallTest
class HfpScanningViewModelTest : TestBase() {

    private lateinit var apaRepository: ApaRepositoryMock
    private lateinit var vm: HfpScanningViewModel
    private lateinit var lifecycleOwner: LifecycleOwner
    private val surroundRepository: IExtSurroundViewRepository = mockk(relaxed = true)
    private lateinit var sonarRepository: SonarRepositoryMock

    override val koinModule = module {
        single<IApaRepository>(override = true) { apaRepository }
        single<ISonarRepository>(override = true) { sonarRepository }
        single<IExtSurroundViewRepository>(override = true) { surroundRepository }
        single<ManeuverButtonMapper> { ManeuverButtonMapper() }
    }

    @Before
    override fun setup() {
        super.setup()
        apaRepository = ApaRepositoryMock(io.mockk.mockk(relaxed = true))
        sonarRepository = SonarRepositoryMock(io.mockk.mockk(relaxed = true))
        lifecycleOwner = TestUtils.mockLifecycleOwner()
        vm = HfpScanningViewModel(mockk())
    }

    @Test
    fun `should set straight_background when no maneuver selected and left parking slot suitable is false and right parking slot suitable is false `() { // ktlint-disable max-line-length
        var backgroundresourceId = R.drawable.rimg_bckg_apa_scanning_straight
        vm.backgroundResource.observe(
            lifecycleOwner,
            Observer { resId -> backgroundresourceId = resId ?: backgroundresourceId })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.leftSuitable.postValue(false)
        apaRepository.rightSuitable.postValue(false)

        Assert.assertEquals(R.drawable.rimg_bckg_apa_scanning_straight, backgroundresourceId)
    }

    @Test
    fun `should set straight_background when parallel maneuver selected and left parking slot suitable is false and right parking slot suitable is false `() { // ktlint-disable max-line-length
        var backgroundresourceId = R.drawable.rimg_bckg_apa_scanning_straight
        vm.backgroundResource.observe(
            lifecycleOwner,
            Observer { resId -> backgroundresourceId = resId ?: backgroundresourceId })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.leftSuitable.postValue(false)
        apaRepository.rightSuitable.postValue(false)

        Assert.assertEquals(R.drawable.rimg_bckg_apa_scanning_straight, backgroundresourceId)
    }

    @Test
    fun `should set parallel left background when parallel maneuver selected and left parking slot suitable is true and right parking slot suitable is false `() { // ktlint-disable max-line-length
        var backgroundresourceId = R.drawable.rimg_bckg_apa_scanning_straight
        vm.backgroundResource.observe(
            lifecycleOwner,
            Observer { resId -> backgroundresourceId = resId ?: backgroundresourceId })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.leftSuitable.postValue(true)
        apaRepository.rightSuitable.postValue(false)

        Assert.assertEquals(R.drawable.rimg_bckg_apa_scanning_parallel_left, backgroundresourceId)
    }

    @Test
    fun `should set parallel left background when parallel maneuver selected and left parking slot selected is true and right parking slot suitable is false `() { // ktlint-disable max-line-length
        var backgroundresourceId = R.drawable.rimg_bckg_apa_scanning_straight
        vm.backgroundResource.observe(
            lifecycleOwner,
            Observer { resId -> backgroundresourceId = resId ?: backgroundresourceId })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.leftSelected.postValue(true)
        apaRepository.rightSuitable.postValue(false)

        Assert.assertEquals(R.drawable.rimg_bckg_apa_scanning_parallel_left, backgroundresourceId)
    }

    @Test
    fun `should set parallel left background when parallel maneuver selected and left parking slot suitable is true and left parking slot selected is true and right parking slot suitable is false `() { // ktlint-disable max-line-length
        var backgroundresourceId = R.drawable.rimg_bckg_apa_scanning_straight
        vm.backgroundResource.observe(
            lifecycleOwner,
            Observer { resId -> backgroundresourceId = resId ?: backgroundresourceId })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.leftSuitable.postValue(true)
        apaRepository.leftSelected.postValue(true)
        apaRepository.rightSuitable.postValue(false)

        Assert.assertEquals(R.drawable.rimg_bckg_apa_scanning_parallel_left, backgroundresourceId)
    }

    @Test
    fun `should set parallel right background when parallel maneuver selected and right parking slot suitable is true and left parking slot suitable is false `() { // ktlint-disable max-line-length
        var backgroundresourceId = R.drawable.rimg_bckg_apa_scanning_straight
        vm.backgroundResource.observe(
            lifecycleOwner,
            Observer { resId -> backgroundresourceId = resId ?: backgroundresourceId })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.rightSuitable.postValue(true)
        apaRepository.leftSuitable.postValue(false)

        Assert.assertEquals(R.drawable.rimg_bckg_apa_scanning_parallel_right, backgroundresourceId)
    }

    @Test
    fun `should set parallel right background when parallel maneuver selected and right parking slot selected is true and left parking slot suitable is false `() { // ktlint-disable max-line-length
        var backgroundresourceId = R.drawable.rimg_bckg_apa_scanning_straight
        vm.backgroundResource.observe(
            lifecycleOwner,
            Observer { resId -> backgroundresourceId = resId ?: backgroundresourceId })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.rightSelected.postValue(true)
        apaRepository.leftSuitable.postValue(false)

        Assert.assertEquals(R.drawable.rimg_bckg_apa_scanning_parallel_right, backgroundresourceId)
    }

    @Test
    fun `should set parallel right background when parallel maneuver selected and right parking slot suitable is true and right parking slot selected is true and left parking slot suitable is false `() { // ktlint-disable max-line-length
        var backgroundresourceId = R.drawable.rimg_bckg_apa_scanning_straight
        vm.backgroundResource.observe(
            lifecycleOwner,
            Observer { resId -> backgroundresourceId = resId ?: backgroundresourceId })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.rightSuitable.postValue(true)
        apaRepository.rightSelected.postValue(true)
        apaRepository.leftSuitable.postValue(false)

        Assert.assertEquals(R.drawable.rimg_bckg_apa_scanning_parallel_right, backgroundresourceId)
    }

    @Test
    fun `should set parallel both background when parallel maneuver selected and right parking slot suitable is true and left parking slot suitable is true `() { // ktlint-disable max-line-length
        var backgroundresourceId = R.drawable.rimg_bckg_apa_scanning_straight
        vm.backgroundResource.observe(
            lifecycleOwner,
            Observer { resId -> backgroundresourceId = resId ?: backgroundresourceId })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.rightSuitable.postValue(true)
        apaRepository.leftSuitable.postValue(true)

        Assert.assertEquals(R.drawable.rimg_bckg_apa_scanning_parallel_both, backgroundresourceId)
    }

    @Test
    fun `should set parallel both background when parallel maneuver selected and right parking slot selected is true and left parking slot suitable is true `() { // ktlint-disable max-line-length
        var backgroundresourceId = R.drawable.rimg_bckg_apa_scanning_straight
        vm.backgroundResource.observe(
            lifecycleOwner,
            Observer { resId -> backgroundresourceId = resId ?: backgroundresourceId })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.rightSelected.postValue(true)
        apaRepository.leftSuitable.postValue(true)

        Assert.assertEquals(R.drawable.rimg_bckg_apa_scanning_parallel_both, backgroundresourceId)
    }

    @Test
    fun `should set parallel both background when parallel maneuver selected and right parking slot suitable is true and right parking slot selected is true and left parking slot suitable is true `() { // ktlint-disable max-line-length
        var backgroundresourceId = R.drawable.rimg_bckg_apa_scanning_straight
        vm.backgroundResource.observe(
            lifecycleOwner,
            Observer { resId -> backgroundresourceId = resId ?: backgroundresourceId })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.rightSuitable.postValue(true)
        apaRepository.rightSelected.postValue(true)
        apaRepository.leftSuitable.postValue(true)

        Assert.assertEquals(R.drawable.rimg_bckg_apa_scanning_parallel_both, backgroundresourceId)
    }

    @Test
    fun `should set parallel both background when parallel maneuver selected and right parking slot suitable is true and left parking slot selected is true `() { // ktlint-disable max-line-length
        var backgroundresourceId = R.drawable.rimg_bckg_apa_scanning_straight
        vm.backgroundResource.observe(
            lifecycleOwner,
            Observer { resId -> backgroundresourceId = resId ?: backgroundresourceId })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.rightSuitable.postValue(true)
        apaRepository.leftSelected.postValue(true)

        Assert.assertEquals(R.drawable.rimg_bckg_apa_scanning_parallel_both, backgroundresourceId)
    }

    @Test
    fun `should set parallel both background when parallel maneuver selected and right parking slot selected is true and left parking slot selected is true `() { // ktlint-disable max-line-length
        var backgroundresourceId = R.drawable.rimg_bckg_apa_scanning_straight
        vm.backgroundResource.observe(
            lifecycleOwner,
            Observer { resId -> backgroundresourceId = resId ?: backgroundresourceId })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.rightSelected.postValue(true)
        apaRepository.leftSelected.postValue(true)

        Assert.assertEquals(R.drawable.rimg_bckg_apa_scanning_parallel_both, backgroundresourceId)
    }

    @Test
    fun `should set parallel both background when parallel maneuver selected and right parking slot suitable and selected are true and left parking slot suitable and selected are true `() { // ktlint-disable max-line-length
        var backgroundresourceId = R.drawable.rimg_bckg_apa_scanning_straight
        vm.backgroundResource.observe(
            lifecycleOwner,
            Observer { resId -> backgroundresourceId = resId ?: backgroundresourceId })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.rightSuitable.postValue(true)
        apaRepository.rightSelected.postValue(true)
        apaRepository.leftSuitable.postValue(true)
        apaRepository.leftSelected.postValue(true)

        Assert.assertEquals(R.drawable.rimg_bckg_apa_scanning_parallel_both, backgroundresourceId)
    }

    @Test
    fun `should set straight_background when perpendicular maneuver selected and left parking slot suitable is false and right parking slot suitable is false `() { // ktlint-disable max-line-length
        var backgroundresourceId = R.drawable.rimg_bckg_apa_scanning_straight
        vm.backgroundResource.observe(
            lifecycleOwner,
            Observer { resId -> backgroundresourceId = resId ?: backgroundresourceId })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.leftSuitable.postValue(false)
        apaRepository.rightSuitable.postValue(false)

        Assert.assertEquals(R.drawable.rimg_bckg_apa_scanning_straight, backgroundresourceId)
    }

    @Test
    fun `should set perpendicular left background when perpendicular maneuver selected and left parking slot suitable is true and right parking slot suitable is false `() { // ktlint-disable max-line-length
        var backgroundresourceId = R.drawable.rimg_bckg_apa_scanning_straight
        vm.backgroundResource.observe(
            lifecycleOwner,
            Observer { resId -> backgroundresourceId = resId ?: backgroundresourceId })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.leftSuitable.postValue(true)
        apaRepository.rightSuitable.postValue(false)

        Assert.assertEquals(
            R.drawable.rimg_bckg_apa_scanning_perpendicular_left,
            backgroundresourceId
        )
    }

    @Test
    fun `should set perpendicular left background when perpendicular maneuver selected and left parking slot selected is true and right parking slot suitable is false `() { // ktlint-disable max-line-length
        var backgroundresourceId = R.drawable.rimg_bckg_apa_scanning_straight
        vm.backgroundResource.observe(
            lifecycleOwner,
            Observer { resId -> backgroundresourceId = resId ?: backgroundresourceId })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.leftSelected.postValue(true)
        apaRepository.rightSuitable.postValue(false)

        Assert.assertEquals(
            R.drawable.rimg_bckg_apa_scanning_perpendicular_left,
            backgroundresourceId
        )
    }

    @Test
    fun `should set perpendicular left background when perpendicular maneuver selected and left parking slot suitable is true and left parking slot selected is true and right parking slot suitable is false `() { // ktlint-disable max-line-length
        var backgroundresourceId = R.drawable.rimg_bckg_apa_scanning_straight
        vm.backgroundResource.observe(
            lifecycleOwner,
            Observer { resId -> backgroundresourceId = resId ?: backgroundresourceId })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.leftSuitable.postValue(true)
        apaRepository.leftSelected.postValue(true)
        apaRepository.rightSuitable.postValue(false)

        Assert.assertEquals(
            R.drawable.rimg_bckg_apa_scanning_perpendicular_left,
            backgroundresourceId
        )
    }

    @Test
    fun `should set perpendicular right background when perpendicular maneuver selected and right parking slot suitable is true and left parking slot suitable is false `() { // ktlint-disable max-line-length
        var backgroundresourceId = R.drawable.rimg_bckg_apa_scanning_straight
        vm.backgroundResource.observe(
            lifecycleOwner,
            Observer { resId -> backgroundresourceId = resId ?: backgroundresourceId })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.rightSuitable.postValue(true)
        apaRepository.leftSuitable.postValue(false)

        Assert.assertEquals(
            R.drawable.rimg_bckg_apa_scanning_perpendicular_right,
            backgroundresourceId
        )
    }

    @Test
    fun `should set perpendicular right background when perpendicular maneuver selected and right parking slot selected is true and left parking slot suitable is false `() { // ktlint-disable max-line-length
        var backgroundresourceId = R.drawable.rimg_bckg_apa_scanning_straight
        vm.backgroundResource.observe(
            lifecycleOwner,
            Observer { resId -> backgroundresourceId = resId ?: backgroundresourceId })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.rightSelected.postValue(true)
        apaRepository.leftSuitable.postValue(false)

        Assert.assertEquals(
            R.drawable.rimg_bckg_apa_scanning_perpendicular_right,
            backgroundresourceId
        )
    }

    @Test
    fun `should set perpendicular right background when perpendicular maneuver selected and right parking slot suitable is true and right parking slot selected is true and left parking slot suitable is false `() { // ktlint-disable max-line-length
        var backgroundresourceId = R.drawable.rimg_bckg_apa_scanning_straight
        vm.backgroundResource.observe(
            lifecycleOwner,
            Observer { resId -> backgroundresourceId = resId ?: backgroundresourceId })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.rightSuitable.postValue(true)
        apaRepository.rightSelected.postValue(true)
        apaRepository.leftSuitable.postValue(false)

        Assert.assertEquals(
            R.drawable.rimg_bckg_apa_scanning_perpendicular_right,
            backgroundresourceId
        )
    }

    @Test
    fun `should set perpendicular both background when perpendicular maneuver selected and right parking slot suitable is true and left parking slot suitable is true `() { // ktlint-disable max-line-length
        var backgroundresourceId = R.drawable.rimg_bckg_apa_scanning_straight
        vm.backgroundResource.observe(
            lifecycleOwner,
            Observer { resId -> backgroundresourceId = resId ?: backgroundresourceId })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.rightSuitable.postValue(true)
        apaRepository.leftSuitable.postValue(true)

        Assert.assertEquals(
            R.drawable.rimg_bckg_apa_scanning_perpendicular_both,
            backgroundresourceId
        )
    }

    @Test
    fun `should set perpendicular both background when perpendicular maneuver selected and right parking slot selected is true and left parking slot suitable is true `() { // ktlint-disable max-line-length
        var backgroundresourceId = R.drawable.rimg_bckg_apa_scanning_straight
        vm.backgroundResource.observe(
            lifecycleOwner,
            Observer { resId -> backgroundresourceId = resId ?: backgroundresourceId })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.rightSelected.postValue(true)
        apaRepository.leftSuitable.postValue(true)

        Assert.assertEquals(
            R.drawable.rimg_bckg_apa_scanning_perpendicular_both,
            backgroundresourceId
        )
    }

    @Test
    fun `should set perpendicular both background when perpendicular maneuver selected and right parking slot suitable is true and right parking slot selected is true and left parking slot suitable is true `() { // ktlint-disable max-line-length
        var backgroundresourceId = R.drawable.rimg_bckg_apa_scanning_straight
        vm.backgroundResource.observe(
            lifecycleOwner,
            Observer { resId -> backgroundresourceId = resId ?: backgroundresourceId })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.rightSuitable.postValue(true)
        apaRepository.rightSelected.postValue(true)
        apaRepository.leftSuitable.postValue(true)

        Assert.assertEquals(
            R.drawable.rimg_bckg_apa_scanning_perpendicular_both,
            backgroundresourceId
        )
    }

    @Test
    fun `should set perpendicular both background when perpendicular maneuver selected and right parking slot suitable is true and left parking slot selected is true `() { // ktlint-disable max-line-length
        var backgroundresourceId = R.drawable.rimg_bckg_apa_scanning_straight
        vm.backgroundResource.observe(
            lifecycleOwner,
            Observer { resId -> backgroundresourceId = resId ?: backgroundresourceId })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.rightSuitable.postValue(true)
        apaRepository.leftSelected.postValue(true)

        Assert.assertEquals(
            R.drawable.rimg_bckg_apa_scanning_perpendicular_both,
            backgroundresourceId
        )
    }

    @Test
    fun `should set perpendicular both background when perpendicular maneuver selected and right parking slot selected is true and left parking slot selected is true `() { // ktlint-disable max-line-length
        var backgroundresourceId = R.drawable.rimg_bckg_apa_scanning_straight
        vm.backgroundResource.observe(
            lifecycleOwner,
            Observer { resId -> backgroundresourceId = resId ?: backgroundresourceId })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.rightSelected.postValue(true)
        apaRepository.leftSelected.postValue(true)

        Assert.assertEquals(
            R.drawable.rimg_bckg_apa_scanning_perpendicular_both,
            backgroundresourceId
        )
    }

    @Test
    fun `should set perpendicular both background when perpendicular maneuver selected and right parking slot suitable and selected are true and left parking slot suitable and selected are true `() { // ktlint-disable max-line-length
        var backgroundresourceId = R.drawable.rimg_bckg_apa_scanning_straight
        vm.backgroundResource.observe(
            lifecycleOwner,
            Observer { resId -> backgroundresourceId = resId ?: backgroundresourceId })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.rightSuitable.postValue(true)
        apaRepository.rightSelected.postValue(true)
        apaRepository.leftSuitable.postValue(true)
        apaRepository.leftSelected.postValue(true)

        Assert.assertEquals(
            R.drawable.rimg_bckg_apa_scanning_perpendicular_both,
            backgroundresourceId
        )
    }

    @Test
    fun `should set large right parking slot resource when right parking slot selected is true`() { // ktlint-disable max-line-length
        var rightSlotResourceId = R.drawable.rimg_adas_parking
        vm.rightSlotResource.observe(
            lifecycleOwner,
            Observer { resId -> rightSlotResourceId = resId ?: rightSlotResourceId })

        apaRepository.rightSelected.postValue(true)
        apaRepository.leftSelected.postValue(false)
        Assert.assertEquals(R.drawable.rimg_adas_parking_large, rightSlotResourceId)
    }

    @Test
    fun `should set small right parking slot resource when right parking slot suitable is true`() { // ktlint-disable max-line-length
        var rightSlotResourceId = R.drawable.rimg_adas_parking
        vm.rightSlotResource.observe(
            lifecycleOwner,
            Observer { resId -> rightSlotResourceId = resId ?: rightSlotResourceId })

        apaRepository.rightSuitable.postValue(true)
        apaRepository.rightSelected.postValue(false)
        apaRepository.leftSuitable.postValue(false)
        apaRepository.leftSelected.postValue(false)
        Assert.assertEquals(R.drawable.rimg_adas_parking, rightSlotResourceId)
    }

    @Test
    fun `should set large left parking slot resource when left parking slot selected is true`() { // ktlint-disable max-line-length
        var leftSlotResourceId = R.drawable.rimg_adas_parking
        vm.leftSlotResource.observe(
            lifecycleOwner,
            Observer { resId -> leftSlotResourceId = resId ?: leftSlotResourceId })

        apaRepository.leftSelected.postValue(true)
        apaRepository.rightSelected.postValue(false)
        Assert.assertEquals(R.drawable.rimg_adas_parking_large, leftSlotResourceId)
    }

    @Test
    fun `should set small left parking slot resource when left parking slot suitable is true`() { // ktlint-disable max-line-length
        var leftSlotResourceId = R.drawable.rimg_adas_parking
        vm.leftSlotResource.observe(
            lifecycleOwner,
            Observer { resId -> leftSlotResourceId = resId ?: leftSlotResourceId })

        apaRepository.rightSuitable.postValue(false)
        apaRepository.rightSelected.postValue(false)
        apaRepository.leftSuitable.postValue(true)
        apaRepository.leftSelected.postValue(false)
        Assert.assertEquals(R.drawable.rimg_adas_parking, leftSlotResourceId)
    }

    @Test
    fun `should set right parking slot parallel resource invisible when no maneuver selected`() { // ktlint-disable max-line-length
        var visible = false
        vm.rightParkingSlotParallelVisible.observe(
            lifecycleOwner,
            Observer { value: Boolean -> visible = value })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.rightSuitable.postValue(true)
        apaRepository.rightSelected.postValue(true)

        Assert.assertEquals(false, visible)
    }

    @Test
    fun `should set right parking slot parallel resource invisible when parallel maneuver selected and right parking slot suitable is false`() { // ktlint-disable max-line-length
        var visible = false
        vm.rightParkingSlotParallelVisible.observe(
            lifecycleOwner,
            Observer { value: Boolean -> visible = value })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.rightSuitable.postValue(false)
        apaRepository.rightSelected.postValue(false)

        Assert.assertEquals(false, visible)
    }

    @Test
    fun `should set right parking slot parallel resource visible when parallel maneuver selected and right parking slot suitable is true`() { // ktlint-disable max-line-length
        var visible = false
        vm.rightParkingSlotParallelVisible.observe(
            lifecycleOwner,
            Observer { value: Boolean -> visible = value })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.rightSuitable.postValue(true)
        apaRepository.rightSelected.postValue(false)

        Assert.assertEquals(true, visible)
    }

    @Test
    fun `should set right parking slot parallel resource visible when parallel maneuver selected and right parking slot selected is true`() { // ktlint-disable max-line-length
        var visible = false
        vm.rightParkingSlotParallelVisible.observe(
            lifecycleOwner,
            Observer { value: Boolean -> visible = value })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.rightSuitable.postValue(true)
        apaRepository.rightSelected.postValue(true)

        Assert.assertEquals(true, visible)
    }

    @Test
    fun `should set left parking slot parallel resource invisible when no maneuver selected`() { // ktlint-disable max-line-length
        var visible = false
        vm.leftParkingSlotParallelVisible.observe(
            lifecycleOwner,
            Observer { value: Boolean -> visible = value })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.leftSuitable.postValue(true)
        apaRepository.leftSelected.postValue(true)

        Assert.assertEquals(false, visible)
    }

    @Test
    fun `should set left parking slot parallel resource invisible when parallel maneuver selected and left parking slot suitable is false`() { // ktlint-disable max-line-length
        var visible = false
        vm.leftParkingSlotParallelVisible.observe(
            lifecycleOwner,
            Observer { value: Boolean -> visible = value })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.leftSuitable.postValue(false)
        apaRepository.leftSelected.postValue(false)

        Assert.assertEquals(false, visible)
    }

    @Test
    fun `should set left parking slot parallel resource visible when parallel maneuver selected and left parking slot suitable is true`() { // ktlint-disable max-line-length
        var visible = false
        vm.leftParkingSlotParallelVisible.observe(
            lifecycleOwner,
            Observer { value: Boolean -> visible = value })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.leftSuitable.postValue(true)
        apaRepository.leftSelected.postValue(false)

        Assert.assertEquals(true, visible)
    }

    @Test
    fun `should set left parking slot parallel resource visible when parallel maneuver selected and left parking slot selected is true`() { // ktlint-disable max-line-length
        var visible = false
        vm.leftParkingSlotParallelVisible.observe(
            lifecycleOwner,
            Observer { value: Boolean -> visible = value })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.leftSuitable.postValue(true)
        apaRepository.leftSelected.postValue(true)

        Assert.assertEquals(true, visible)
    }

    @Test
    fun `should set right parking slot perpendicular resource invisible when no maneuver selected`() { // ktlint-disable max-line-length
        var visible = false
        vm.rightParkingSlotPerpendicularVisible.observe(
            lifecycleOwner,
            Observer { value: Boolean -> visible = value })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.rightSuitable.postValue(true)
        apaRepository.rightSelected.postValue(true)

        Assert.assertEquals(false, visible)
    }

    @Test
    fun `should set right parking slot perpendicular resource invisible when perpendicular maneuver selected and right parking slot suitable is false`() { // ktlint-disable max-line-length
        var visible = false
        vm.rightParkingSlotPerpendicularVisible.observe(
            lifecycleOwner,
            Observer { value: Boolean -> visible = value })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.rightSuitable.postValue(false)
        apaRepository.rightSelected.postValue(false)

        Assert.assertEquals(false, visible)
    }

    @Test
    fun `should set right parking slot perpendicular resource visible when perpendicular maneuver selected and right parking slot suitable is true`() { // ktlint-disable max-line-length
        var visible = false
        vm.rightParkingSlotPerpendicularVisible.observe(
            lifecycleOwner,
            Observer { value: Boolean -> visible = value })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.rightSuitable.postValue(true)
        apaRepository.rightSelected.postValue(false)

        Assert.assertEquals(true, visible)
    }

    @Test
    fun `should set right parking slot perpendicular resource visible when perpendicular maneuver selected and right parking slot selected is true`() { // ktlint-disable max-line-length
        var visible = false
        vm.rightParkingSlotPerpendicularVisible.observe(
            lifecycleOwner,
            Observer { value: Boolean -> visible = value })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.rightSuitable.postValue(true)
        apaRepository.rightSelected.postValue(true)

        Assert.assertEquals(true, visible)
    }

    @Test
    fun `should set left parking slot perpendicular resource invisible when no maneuver selected`() { // ktlint-disable max-line-length
        var visible = false
        vm.leftParkingSlotPerpendicularVisible.observe(
            lifecycleOwner,
            Observer { value: Boolean -> visible = value })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.leftSuitable.postValue(true)
        apaRepository.leftSelected.postValue(true)

        Assert.assertEquals(false, visible)
    }

    @Test
    fun `should set left parking slot perpendicular resource invisible when perpendicular maneuver selected and left parking slot suitable is false`() { // ktlint-disable max-line-length
        var visible = false
        vm.leftParkingSlotPerpendicularVisible.observe(
            lifecycleOwner,
            Observer { value: Boolean -> visible = value })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.leftSuitable.postValue(false)
        apaRepository.leftSelected.postValue(false)

        Assert.assertEquals(false, visible)
    }

    @Test
    fun `should set left parking slot perpendicular resource visible when perpendicular maneuver selected and left parking slot suitable is true`() { // ktlint-disable max-line-length
        var visible = false
        vm.leftParkingSlotPerpendicularVisible.observe(
            lifecycleOwner,
            Observer { value: Boolean -> visible = value })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.leftSuitable.postValue(true)
        apaRepository.leftSelected.postValue(false)

        Assert.assertEquals(true, visible)
    }

    @Test
    fun `should set left parking slot perpendicular resource visible when perpendicular maneuver selected and left parking slot selected is true`() { // ktlint-disable max-line-length
        var visible = false
        vm.leftParkingSlotPerpendicularVisible.observe(
            lifecycleOwner,
            Observer { value: Boolean -> visible = value })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.leftSuitable.postValue(true)
        apaRepository.leftSelected.postValue(true)

        Assert.assertEquals(true, visible)
    }

    @Test
    fun `should set stop resource when extended instruction is STOP`() { // ktlint-disable max-line-length
        var carFrontStopResourceVisible = false
        vm.carFrontStopResourceVisible.observe(
            lifecycleOwner,
            Observer { value: Boolean -> carFrontStopResourceVisible = value })

        apaRepository.extendedInstruction.postValue(Instruction.STOP)
        Assert.assertEquals(true, carFrontStopResourceVisible)
    }

    @Test
    fun `should set engage front active arrow resource when extended instruction is not STOP`() { // ktlint-disable max-line-length
        var carFrontArrowResourceVisible = false
        vm.carFrontArrowResourceVisible.observe(
            lifecycleOwner,
            Observer { value: Boolean -> carFrontArrowResourceVisible = value })

        apaRepository.extendedInstruction.postValue(Instruction.SELECT_SIDE)
        Assert.assertEquals(true, carFrontArrowResourceVisible)
    }

    @Test
    fun `should set backright short arrow visible when perpendicular maneuver set and right parking slot suitable is true and instruction engage rear set`() { // ktlint-disable max-line-length
        var rearRightShortArrowVisible = false
        vm.rearRightShortArrowVisible.observe(
            lifecycleOwner,
            Observer { value: Boolean -> rearRightShortArrowVisible = value })
        var rearArrowResourceVisible = false
        vm.rearArrowResourceVisible.observe(
            lifecycleOwner,
            Observer { value: Boolean -> rearArrowResourceVisible = value })

        apaRepository.rightSuitable.postValue(true)
        apaRepository.leftSuitable.postValue(false)
        apaRepository.rightSelected.postValue(false)
        apaRepository.leftSelected.postValue(false)
        apaRepository.extendedInstruction.postValue(
            Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        Assert.assertEquals(true, rearArrowResourceVisible)
        Assert.assertEquals(true, rearRightShortArrowVisible)
    }

    @Test
    fun `should set backright short arrow visible when perpendicular maneuver set and right parking slot selected is true and instruction engage rear set`() { // ktlint-disable max-line-length
        var rearRightShortArrowVisible = false
        vm.rearRightShortArrowVisible.observe(
            lifecycleOwner,
            Observer { value: Boolean -> rearRightShortArrowVisible = value })
        var rearArrowResourceVisible = false
        vm.rearArrowResourceVisible.observe(
            lifecycleOwner,
            Observer { value: Boolean -> rearArrowResourceVisible = value })

        apaRepository.rightSelected.postValue(true)
        apaRepository.leftSelected.postValue(false)
        apaRepository.extendedInstruction.postValue(
            Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        Assert.assertEquals(true, rearArrowResourceVisible)
        Assert.assertEquals(true, rearRightShortArrowVisible)
    }

    @Test
    fun `should set backleft short arrow visible when perpendicular maneuver set and left parking slot suitable is true and instruction engage rear set`() { // ktlint-disable max-line-length
        var rearLeftShortArrowVisible = false
        vm.rearLeftShortArrowVisible.observe(
            lifecycleOwner,
            Observer { value: Boolean -> rearLeftShortArrowVisible = value })
        var rearArrowResourceVisible = false
        vm.rearArrowResourceVisible.observe(
            lifecycleOwner,
            Observer { value: Boolean -> rearArrowResourceVisible = value })

        apaRepository.rightSuitable.postValue(false)
        apaRepository.leftSuitable.postValue(true)
        apaRepository.rightSelected.postValue(false)
        apaRepository.leftSelected.postValue(false)
        apaRepository.extendedInstruction.postValue(
            Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        Assert.assertEquals(true, rearArrowResourceVisible)
        Assert.assertEquals(true, rearLeftShortArrowVisible)
    }

    @Test
    fun `should set backleft short arrow visible when perpendicular maneuver set and left parking slot selected is true and instruction engage rear set`() { // ktlint-disable max-line-length
        var rearLeftShortArrowVisible = false
        vm.rearLeftShortArrowVisible.observe(
            lifecycleOwner,
            Observer { value: Boolean -> rearLeftShortArrowVisible = value })
        var rearArrowResourceVisible = false
        vm.rearArrowResourceVisible.observe(
            lifecycleOwner,
            Observer { value: Boolean -> rearArrowResourceVisible = value })

        apaRepository.rightSelected.postValue(false)
        apaRepository.leftSelected.postValue(true)
        apaRepository.extendedInstruction.postValue(
            Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        Assert.assertEquals(true, rearArrowResourceVisible)
        Assert.assertEquals(true, rearLeftShortArrowVisible)
    }

    @Test
    fun `should set backright long arrow visible when parallel maneuver set and right parking slot suitable is true and instruction engage rear set`() { // ktlint-disable max-line-length
        var rearRightLongArrowVisible = false
        vm.rearRightLongArrowVisible.observe(
            lifecycleOwner,
            Observer { value: Boolean -> rearRightLongArrowVisible = value })
        var rearArrowResourceVisible = false
        vm.rearArrowResourceVisible.observe(
            lifecycleOwner,
            Observer { value: Boolean -> rearArrowResourceVisible = value })

        apaRepository.rightSuitable.postValue(true)
        apaRepository.leftSuitable.postValue(false)
        apaRepository.rightSelected.postValue(false)
        apaRepository.leftSelected.postValue(false)
        apaRepository.extendedInstruction.postValue(
            Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        Assert.assertEquals(true, rearArrowResourceVisible)
        Assert.assertEquals(true, rearRightLongArrowVisible)
    }

    @Test
    fun `should set backright long arrow visible when parallel maneuver set and right parking slot selected is true and instruction engage rear set`() { // ktlint-disable max-line-length
        var rearRightLongArrowVisible = false
        vm.rearRightLongArrowVisible.observe(
            lifecycleOwner,
            Observer { value: Boolean -> rearRightLongArrowVisible = value })
        var rearArrowResourceVisible = false
        vm.rearArrowResourceVisible.observe(
            lifecycleOwner,
            Observer { value: Boolean -> rearArrowResourceVisible = value })

        apaRepository.rightSelected.postValue(true)
        apaRepository.leftSelected.postValue(false)
        apaRepository.extendedInstruction.postValue(
            Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        Assert.assertEquals(true, rearArrowResourceVisible)
        Assert.assertEquals(true, rearRightLongArrowVisible)
    }

    @Test
    fun `should set backleft long arrow visible when parallel maneuver set and left parking slot suitable is true and instruction engage rear set`() { // ktlint-disable max-line-length
        var rearLeftLongArrowVisible = false
        vm.rearLeftLongArrowVisible.observe(
            lifecycleOwner,
            Observer { value: Boolean -> rearLeftLongArrowVisible = value })
        var rearArrowResourceVisible = false
        vm.rearArrowResourceVisible.observe(
            lifecycleOwner,
            Observer { value: Boolean -> rearArrowResourceVisible = value })

        apaRepository.rightSuitable.postValue(false)
        apaRepository.leftSuitable.postValue(true)
        apaRepository.rightSelected.postValue(false)
        apaRepository.leftSelected.postValue(false)
        apaRepository.extendedInstruction.postValue(
            Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        Assert.assertEquals(true, rearArrowResourceVisible)
        Assert.assertEquals(true, rearLeftLongArrowVisible)
    }

    @Test
    fun `should set backleft long arrow visible when parallel maneuver set and left parking slot selected is true and instruction engage rear set`() { // ktlint-disable max-line-length
        var rearLeftLongArrowVisible = false
        vm.rearLeftLongArrowVisible.observe(
            lifecycleOwner,
            Observer { value: Boolean -> rearLeftLongArrowVisible = value })
        var rearArrowResourceVisible = false
        vm.rearArrowResourceVisible.observe(
            lifecycleOwner,
            Observer { value: Boolean -> rearArrowResourceVisible = value })

        apaRepository.rightSelected.postValue(false)
        apaRepository.leftSelected.postValue(true)
        apaRepository.extendedInstruction.postValue(
            Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        Assert.assertEquals(true, rearArrowResourceVisible)
        Assert.assertEquals(true, rearLeftLongArrowVisible)
    }

    @Test
    fun `should set rear arrow resource invisible when right and left parking slot selected are false`() { // ktlint-disable max-line-length
        var visible = false
        vm.rearArrowResourceVisible.observe(
            lifecycleOwner,
            Observer { value: Boolean -> visible = value })

        apaRepository.rightSuitable.postValue(false)
        apaRepository.leftSuitable.postValue(false)
        apaRepository.leftSelected.postValue(false)
        apaRepository.rightSelected.postValue(false)
        apaRepository.extendedInstruction.postValue(Instruction.ENGAGE_REAR_GEAR)

        Assert.assertEquals(false, visible)
    }

    @Test
    fun `should set parkout Maneuver Enabled when parkout maneuver selection is SELECTED `() { // ktlint-disable max-line-length
        var enable = false
        vm.maneuverParkoutButtonEnabled.observe(
            lifecycleOwner,
            Observer { value: Boolean -> enable = value })

        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)

        Assert.assertEquals(true, enable)
    }

    @Test
    fun `should set parkout Maneuver Enabled when parkout maneuver selection is NOT SELECTED `() { // ktlint-disable max-line-length
        var enable = false
        vm.maneuverParkoutButtonEnabled.observe(
            lifecycleOwner,
            Observer { value: Boolean -> enable = value })

        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        Assert.assertEquals(true, enable)
    }

    @Test
    fun `should set parallel Maneuver Enabled when parallel maneuver selection is SELECTED `() { // ktlint-disable max-line-length
        var enable = false
        vm.maneuverParallelButtonEnabled.observe(
            lifecycleOwner,
            Observer { value: Boolean -> enable = value })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)

        Assert.assertEquals(true, enable)
    }

    @Test
    fun `should set parallel Maneuver Enabled when parallel maneuver selection is NOT SELECTED `() { // ktlint-disable max-line-length
        var enable = false
        vm.maneuverParallelButtonEnabled.observe(
            lifecycleOwner,
            Observer { value: Boolean -> enable = value })

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        Assert.assertEquals(true, enable)
    }

    @Test
    fun `should set perpendicular Maneuver Enabled when perpendicular maneuver selection is SELECTED `() { // ktlint-disable max-line-length
        var enable = false
        vm.maneuverPerpendicularButtonEnabled.observe(
            lifecycleOwner,
            Observer { value: Boolean -> enable = value })

        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)

        Assert.assertEquals(true, enable)
    }

    @Test
    fun `should set perpendicular Maneuver Enabled when perpendicular maneuver selection is NOT SELECTED `() { // ktlint-disable max-line-length
        var enable = false
        vm.maneuverPerpendicularButtonEnabled.observe(
            lifecycleOwner,
            Observer { value: Boolean -> enable = value })

        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)

        Assert.assertEquals(true, enable)
    }

    @Test
    fun not_selected_buttons_should_be_enabled() {
        var parallelEnabled: Boolean?
        var perpendicularEnabled: Boolean?
        var parkoutEnabled: Boolean?

        vm.maneuverParallelButtonEnabled.observe(lifecycleOwner, Observer {
            parallelEnabled = it
        })
        vm.maneuverPerpendicularButtonEnabled.observe(lifecycleOwner, Observer {
            perpendicularEnabled = it
        })
        vm.maneuverParkoutButtonEnabled.observe(lifecycleOwner, Observer {
            parkoutEnabled = it
        })

        parallelEnabled = null
        perpendicularEnabled = null
        parkoutEnabled = null

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        assertEquals(true, parallelEnabled)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        assertEquals(true, perpendicularEnabled)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.NOT_SELECTED)
        assertEquals(true, parkoutEnabled)
    }

    @Test
    fun selected_buttons_should_be_enabled() {
        var parallelEnabled: Boolean?
        var perpendicularEnabled: Boolean?
        var parkoutEnabled: Boolean?

        vm.maneuverParallelButtonEnabled.observe(lifecycleOwner, Observer {
            parallelEnabled = it
        })
        vm.maneuverPerpendicularButtonEnabled.observe(lifecycleOwner, Observer {
            perpendicularEnabled = it
        })
        vm.maneuverParkoutButtonEnabled.observe(lifecycleOwner, Observer {
            parkoutEnabled = it
        })

        parallelEnabled = null
        perpendicularEnabled = null
        parkoutEnabled = null

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        assertEquals(true, parallelEnabled)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        assertEquals(true, perpendicularEnabled)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)
        assertEquals(true, parkoutEnabled)
    }

    @Test
    fun not_enabled_buttons_should_not_be_selected() {
        var parallelSelected: Boolean?
        var perpendicularSelected: Boolean?
        var parkoutSelected: Boolean?

        vm.maneuverParallelButtonSelected.observe(lifecycleOwner, Observer {
            parallelSelected = it
        })
        vm.maneuverPerpendicularButtonSelected.observe(lifecycleOwner, Observer {
            perpendicularSelected = it
        })
        vm.maneuverParkoutButtonSelected.observe(lifecycleOwner, Observer {
            parkoutSelected = it
        })

        apaRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)

        parallelSelected = null
        perpendicularSelected = null
        parkoutSelected = null

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.UNAVAILABLE)
        assertEquals(false, parallelSelected)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.UNAVAILABLE)
        assertEquals(false, perpendicularSelected)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.UNAVAILABLE)
        assertEquals(false, parkoutSelected)
    }

    @Test
    fun `maneuver parallel button should be selected when ManeuverSelection SELECTED is sent`() { // ktlint-disable max-line-length
        var actual = false
        vm.maneuverParallelButtonSelected.observe(lifecycleOwner, Observer {
            actual = it ?: false
        })
        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        assertTrue(actual)
    }

    @Test
    fun `maneuver perpendicular button should be selected when ManeuverSelection SELECTED is sent`() { // ktlint-disable max-line-length
        var actual = false
        vm.maneuverPerpendicularButtonSelected.observe(lifecycleOwner, Observer {
            actual = it ?: false
        })
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        assertTrue(actual)
    }

    @Test
    fun `maneuver parkout button should be selected when ManeuverSelection SELECTED is sent`() { // ktlint-disable max-line-length
        var actual = false
        vm.maneuverParkoutButtonSelected.observe(lifecycleOwner, Observer {
            actual = it ?: false
        })
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)
        assertTrue(actual)
    }

    @Test
    fun perpendicular_and_parallel_should_be_not_selected_and_parkout_selected_when_user_click_on_parkout() { // ktlint-disable max-line-length
        var parallelSelected: Boolean?
        var perpendicularSelected: Boolean?
        var parkoutSelected: Boolean?

        vm.maneuverParallelButtonSelected.observe(lifecycleOwner, Observer {
            parallelSelected = it
        })
        vm.maneuverPerpendicularButtonSelected.observe(lifecycleOwner, Observer {
            perpendicularSelected = it
        })
        vm.maneuverParkoutButtonSelected.observe(lifecycleOwner, Observer {
            parkoutSelected = it
        })

        parallelSelected = null
        perpendicularSelected = null
        parkoutSelected = null

        vm.setManeuver(ManeuverType.PARKOUT)

        assertEquals(false, parallelSelected)
        assertEquals(false, perpendicularSelected)
        assertEquals(true, parkoutSelected)
    }

    @Test
    fun parkout_and_parallel_should_be_not_selected_and_parkout_selected_when_user_click_on_perpendicular() { // ktlint-disable max-line-length
        var parallelSelected: Boolean?
        var perpendicularSelected: Boolean?
        var parkoutSelected: Boolean?

        apaRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)

        vm.maneuverParallelButtonSelected.observe(lifecycleOwner, Observer {
            parallelSelected = it
        })
        vm.maneuverPerpendicularButtonSelected.observe(lifecycleOwner, Observer {
            perpendicularSelected = it
        })
        vm.maneuverParkoutButtonSelected.observe(lifecycleOwner, Observer {
            parkoutSelected = it
        })

        parallelSelected = null
        perpendicularSelected = null
        parkoutSelected = null

        vm.setManeuver(ManeuverType.PERPENDICULAR)

        assertEquals(false, parallelSelected)
        assertEquals(true, perpendicularSelected)
        assertEquals(false, parkoutSelected)
    }

    @Test
    fun perpendicular_and_parkout_should_be_not_selected_and_parkout_selected_when_user_click_on_parallel() { // ktlint-disable max-line-length
        var parallelSelected: Boolean?
        var perpendicularSelected: Boolean?
        var parkoutSelected: Boolean?

        apaRepository.displayState.postValue(DisplayState.DISPLAY_SCANNING)

        vm.maneuverParallelButtonSelected.observe(lifecycleOwner, Observer {
            parallelSelected = it
        })
        vm.maneuverPerpendicularButtonSelected.observe(lifecycleOwner, Observer {
            perpendicularSelected = it
        })
        vm.maneuverParkoutButtonSelected.observe(lifecycleOwner, Observer {
            parkoutSelected = it
        })

        parallelSelected = null
        perpendicularSelected = null
        parkoutSelected = null

        vm.setManeuver(ManeuverType.PARALLEL)

        assertEquals(true, parallelSelected)
        assertEquals(false, perpendicularSelected)
        assertEquals(false, parkoutSelected)
    }

    @Test
    fun maneuvers_buttons_should_be_not_enabled_when_unavailable_is_sent() {
        var parallelEnabled: Boolean?
        var perpendicularEnabled: Boolean?
        var parkoutEnabled: Boolean?

        vm.maneuverParallelButtonEnabled.observe(lifecycleOwner, Observer {
            parallelEnabled = it
        })
        vm.maneuverPerpendicularButtonEnabled.observe(lifecycleOwner, Observer {
            perpendicularEnabled = it
        })
        vm.maneuverParkoutButtonEnabled.observe(lifecycleOwner, Observer {
            parkoutEnabled = it
        })

        parallelEnabled = null
        perpendicularEnabled = null
        parkoutEnabled = null

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.UNAVAILABLE)
        assertEquals(false, parallelEnabled)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.UNAVAILABLE)
        assertEquals(false, perpendicularEnabled)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.UNAVAILABLE)
        assertEquals(false, parkoutEnabled)
    }

    @Test
    fun maneuver_buttons_should_be_selected_when_selected_is_sent() {
        var parallelSelected: Boolean?
        var perpendicularSelected: Boolean?
        var parkoutSelected: Boolean?

        vm.maneuverParallelButtonSelected.observe(lifecycleOwner, Observer {
            parallelSelected = it
        })
        vm.maneuverPerpendicularButtonSelected.observe(lifecycleOwner, Observer {
            perpendicularSelected = it
        })
        vm.maneuverParkoutButtonSelected.observe(lifecycleOwner, Observer {
            parkoutSelected = it
        })

        parallelSelected = null
        perpendicularSelected = null
        parkoutSelected = null

        apaRepository.parallelManeuverSelection.postValue(ManeuverSelection.SELECTED)
        assertEquals(true, parallelSelected)
        apaRepository.perpendicularManeuverSelection.postValue(ManeuverSelection.SELECTED)
        assertEquals(true, perpendicularSelected)
        apaRepository.parkOutManeuverSelection.postValue(ManeuverSelection.SELECTED)
        assertEquals(true, parkoutSelected)
    }

    @Test
    fun `side indicator should not display when instruction is reverse`() {
        var leftSideIndicator = true
        var rightSideIndicator = true
        vm.leftIndicatorSelected.observe(
            lifecycleOwner,
            Observer { value: Boolean -> leftSideIndicator = value })

        vm.rightIndicatorSelected.observe(
            lifecycleOwner,
            Observer { value: Boolean -> rightSideIndicator = value })
        apaRepository.extendedInstruction
            .postValue(Instruction.SELECT_REVERSE_GEAR_OR_PRESS_START_BUTTON)
        Assert.assertEquals(false, leftSideIndicator)
        Assert.assertEquals(false, rightSideIndicator)
    }
}