package com.renault.parkassist

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.renault.parkassist.activity.OverlayActivityTest
import com.renault.parkassist.koin.ParkAssistHwConfig
import com.renault.parkassist.repository.sonar.*
import com.renault.parkassist.utils.EspressoTestUtils.assertViewHasEffectiveVisibility
import com.renault.parkassist.utils.EspressoTestUtils.assertViewHasNoEffectiveVisibility
import com.renault.parkassist.utils.checkDrawableLevel
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class SonarTest : OverlayActivityTest() {

    @Before
    fun setup() {
        setVehicleConfiguration(ParkAssistHwConfig.SONAR)
        launchPopUp()
        navigatePopUp(R.id.sonarPipFragment)
        sonarRepository.displayRequest.postValue(DisplayType.WIDGET)
        sonarRepository.frontState.postValue(GroupState.ENABLED)
        sonarRepository.upaDisplayRequest.postValue(UpaDisplayRequestType.REAR_FRONT)
        sonarRepository.fkpDisplayRequest.postValue(FkpDisplayRequestType.FLANK)
    }

    @After
    fun tearDown() {
        sonarRepository.displayRequest.postValue(DisplayType.NONE)
        sonarRepository.upaDisplayRequest.postValue(UpaDisplayRequestType.NO_DISPLAY)
        sonarRepository.fkpDisplayRequest.postValue(FkpDisplayRequestType.NO_DISPLAY)
    }

    @Test
    fun sensor_upa_front_center() { // ktlint-disable max-line-length
        val sensorViewId = R.id.front_center
        val sensorBackgroundViewId = R.id.bg_front_center
        with(sonarRepository.frontCenter) {
            postValue(sensor(false, 0))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(true, 0))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(false, 1))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_0_01)
            postValue(sensor(true, 1))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_0_01lines)
            postValue(sensor(false, 2))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_1_04)
            postValue(sensor(true, 2))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_1_04lines)
            postValue(sensor(false, 3))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_2_04)
            postValue(sensor(true, 3))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_2_04lines)
            postValue(sensor(false, 4))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_3_04)
            postValue(sensor(true, 4))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_3_04lines)
            postValue(sensor(false, 5))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_3_04)
            postValue(sensor(true, 5))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_3_04lines)
            sonarRepository.upaDisplayRequest.postValue(UpaDisplayRequestType.NO_DISPLAY)
            assertViewHasNoEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
        }
    }

    @Test
    fun sensor_upa_front_right() { // ktlint-disable max-line-length
        val sensorViewId = R.id.front_right
        val sensorBackgroundViewId = R.id.bg_front_right
        with(sonarRepository.frontRight) {
            postValue(sensor(false, 0))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(true, 0))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(false, 1))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(true, 1))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(false, 2))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_1_03)
            postValue(sensor(true, 2))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_1_03lines)
            postValue(sensor(false, 3))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_2_03)
            postValue(sensor(true, 3))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_2_03lines)
            postValue(sensor(false, 4))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_3_03)
            postValue(sensor(true, 4))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_3_03lines)
            postValue(sensor(false, 5))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_3_03)
            postValue(sensor(true, 5))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_3_03lines)
            sonarRepository.upaDisplayRequest.postValue(UpaDisplayRequestType.NO_DISPLAY)
            assertViewHasNoEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
        }
    }

    @Test
    fun sensor_fkp_right_front() { // ktlint-disable max-line-length
        val sensorViewId = R.id.right_front
        val sensorBackgroundViewId = R.id.bg_right_front
        with(sonarRepository.rightFront) {
            postValue(sensor(false, 0, false))
            assertViewHasNoEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(true, 0, false))
            assertViewHasNoEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(false, 0))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(true, 0))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(false, 1))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_1_02)
            postValue(sensor(true, 1))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_1_02lines)
            postValue(sensor(false, 2))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_2_02)
            postValue(sensor(true, 2))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_2_02lines)
            postValue(sensor(false, 3))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_3_02)
            postValue(sensor(true, 3))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_3_02lines)
            sonarRepository.fkpDisplayRequest.postValue(FkpDisplayRequestType.NO_DISPLAY)
            assertViewHasNoEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
        }
    }

    @Test
    fun sensor_fkp_right_front_center() { // ktlint-disable max-line-length
        val sensorViewId = R.id.right_front_center
        val sensorBackgroundViewId = R.id.bg_right_front_center
        with(sonarRepository.rightFrontCenter) {
            postValue(sensor(false, 0, false))
            assertViewHasNoEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(true, 0, false))
            assertViewHasNoEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(false, 0))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(true, 0))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(false, 1))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_1_01)
            postValue(sensor(true, 1))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_1_01lines)
            postValue(sensor(false, 2))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_2_01)
            postValue(sensor(true, 2))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_2_01lines)
            postValue(sensor(false, 3))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_3_01)
            postValue(sensor(true, 3))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_3_01lines)
            sonarRepository.fkpDisplayRequest.postValue(FkpDisplayRequestType.NO_DISPLAY)
            assertViewHasNoEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
        }
    }

    @Test
    fun sensor_fkp_right_rear_center() { // ktlint-disable max-line-length
        val sensorViewId = R.id.right_rear_center
        val sensorBackgroundViewId = R.id.bg_right_rear_center
        with(sonarRepository.rightRearCenter) {
            postValue(sensor(false, 0, false))
            assertViewHasNoEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(true, 0, false))
            assertViewHasNoEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(false, 0))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(true, 0))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(false, 1))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_1_14)
            postValue(sensor(true, 1))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_1_14lines)
            postValue(sensor(false, 2))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_2_14)
            postValue(sensor(true, 2))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_2_14lines)
            postValue(sensor(false, 3))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_3_14)
            postValue(sensor(true, 3))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_3_14lines)
            sonarRepository.fkpDisplayRequest.postValue(FkpDisplayRequestType.NO_DISPLAY)
            assertViewHasNoEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
        }
    }

    @Test
    fun sensor_fkp_right_rear() { // ktlint-disable max-line-length
        val sensorViewId = R.id.right_rear
        val sensorBackgroundViewId = R.id.bg_right_rear
        with(sonarRepository.rightRear) {
            postValue(sensor(false, 0, false))
            assertViewHasNoEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(true, 0, false))
            assertViewHasNoEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(false, 0))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(true, 0))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(false, 1))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_1_13)
            postValue(sensor(true, 1))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_1_13lines)
            postValue(sensor(false, 2))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_2_13)
            postValue(sensor(true, 2))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_2_13lines)
            postValue(sensor(false, 3))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_3_13)
            postValue(sensor(true, 3))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_3_13lines)
            sonarRepository.fkpDisplayRequest.postValue(FkpDisplayRequestType.NO_DISPLAY)
            assertViewHasNoEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
        }
    }

    @Test
    fun sensor_upa_rear_right() { // ktlint-disable max-line-length
        val sensorViewId = R.id.rear_right
        val sensorBackgroundViewId = R.id.bg_rear_right
        with(sonarRepository.rearRight) {
            postValue(sensor(false, 0))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(true, 0))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(false, 1))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(true, 1))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(false, 2))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_1_12)
            postValue(sensor(true, 2))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_1_12lines)
            postValue(sensor(false, 3))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_2_12)
            postValue(sensor(true, 3))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_2_12lines)
            postValue(sensor(false, 4))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_3_12)
            postValue(sensor(true, 4))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_3_12lines)
            postValue(sensor(false, 5))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_3_12)
            postValue(sensor(true, 5))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_3_12lines)
            sonarRepository.upaDisplayRequest.postValue(UpaDisplayRequestType.NO_DISPLAY)
            assertViewHasNoEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
        }
    }

    @Test
    fun sensor_upa_rear_center() { // ktlint-disable max-line-length
        val sensorViewId = R.id.rear_center
        val sensorBackgroundViewId = R.id.bg_rear_center
        with(sonarRepository.rearCenter) {
            postValue(sensor(false, 0))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(true, 0))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(false, 1))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_0_02)
            postValue(sensor(true, 1))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_0_02lines)
            postValue(sensor(false, 2))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_1_11)
            postValue(sensor(true, 2))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_1_11lines)
            postValue(sensor(false, 3))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_2_11)
            postValue(sensor(true, 3))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_2_11lines)
            postValue(sensor(false, 4))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_3_11)
            postValue(sensor(true, 4))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_3_11lines)
            postValue(sensor(false, 5))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_3_11)
            postValue(sensor(true, 5))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_3_11lines)
            sonarRepository.upaDisplayRequest.postValue(UpaDisplayRequestType.NO_DISPLAY)
            assertViewHasNoEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
        }
    }

    @Test
    fun sensor_upa_rear_left() { // ktlint-disable max-line-length
        val sensorViewId = R.id.rear_left
        val sensorBackgroundViewId = R.id.bg_rear_left
        with(sonarRepository.rearLeft) {
            postValue(sensor(false, 0))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(true, 0))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(false, 1))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(true, 1))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(false, 2))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_1_10)
            postValue(sensor(true, 2))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_1_10lines)
            postValue(sensor(false, 3))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_2_10)
            postValue(sensor(true, 3))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_2_10lines)
            postValue(sensor(false, 4))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_3_10)
            postValue(sensor(true, 4))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_3_10lines)
            postValue(sensor(false, 5))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_3_10)
            postValue(sensor(true, 5))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_3_10lines)
            sonarRepository.upaDisplayRequest.postValue(UpaDisplayRequestType.NO_DISPLAY)
            assertViewHasNoEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
        }
    }

    @Test
    fun sensor_fkp_left_rear() { // ktlint-disable max-line-length
        val sensorViewId = R.id.left_rear
        val sensorBackgroundViewId = R.id.bg_left_rear
        with(sonarRepository.leftRear) {
            postValue(sensor(false, 0, false))
            assertViewHasNoEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(true, 0, false))
            assertViewHasNoEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(false, 0))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(true, 0))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(false, 1))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_1_09)
            postValue(sensor(true, 1))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_1_09lines)
            postValue(sensor(false, 2))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_2_09)
            postValue(sensor(true, 2))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_2_09lines)
            postValue(sensor(false, 3))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_3_09)
            postValue(sensor(true, 3))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_3_09lines)
            sonarRepository.fkpDisplayRequest.postValue(FkpDisplayRequestType.NO_DISPLAY)
            assertViewHasNoEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
        }
    }

    @Test
    fun sensor_fkp_left_rear_center() { // ktlint-disable max-line-length
        val sensorViewId = R.id.left_rear_center
        val sensorBackgroundViewId = R.id.bg_left_rear_center
        with(sonarRepository.leftRearCenter) {
            postValue(sensor(false, 0, false))
            assertViewHasNoEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(true, 0, false))
            assertViewHasNoEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(false, 0))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(true, 0))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(false, 1))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_1_08)
            postValue(sensor(true, 1))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_1_08lines)
            postValue(sensor(false, 2))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_2_08)
            postValue(sensor(true, 2))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_2_08lines)
            postValue(sensor(false, 3))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_3_08)
            postValue(sensor(true, 3))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_3_08lines)
            sonarRepository.fkpDisplayRequest.postValue(FkpDisplayRequestType.NO_DISPLAY)
            assertViewHasNoEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
        }
    }

    @Test
    fun sensor_fkp_left_front_center() { // ktlint-disable max-line-length
        val sensorViewId = R.id.left_front_center
        val sensorBackgroundViewId = R.id.bg_left_front_center
        with(sonarRepository.leftFrontCenter) {
            postValue(sensor(false, 0, false))
            assertViewHasNoEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(true, 0, false))
            assertViewHasNoEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(false, 0))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(true, 0))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(false, 1))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_1_07)
            postValue(sensor(true, 1))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_1_07lines)
            postValue(sensor(false, 2))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_2_07)
            postValue(sensor(true, 2))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_2_07lines)
            postValue(sensor(false, 3))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_3_07)
            postValue(sensor(true, 3))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_3_07lines)
            sonarRepository.fkpDisplayRequest.postValue(FkpDisplayRequestType.NO_DISPLAY)
            assertViewHasNoEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
        }
    }

    @Test
    fun sensor_fkp_left_front() { // ktlint-disable max-line-length
        val sensorViewId = R.id.left_front
        val sensorBackgroundViewId = R.id.bg_left_front
        with(sonarRepository.leftFront) {
            postValue(sensor(false, 0, false))
            assertViewHasNoEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(true, 0, false))
            assertViewHasNoEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(false, 0))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(true, 0))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(false, 1))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_1_06)
            postValue(sensor(true, 1))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_1_06lines)
            postValue(sensor(false, 2))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_2_06)
            postValue(sensor(true, 2))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_2_06lines)
            postValue(sensor(false, 3))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_3_06)
            postValue(sensor(true, 3))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_3_06lines)
            sonarRepository.fkpDisplayRequest.postValue(FkpDisplayRequestType.NO_DISPLAY)
            assertViewHasNoEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
        }
    }

    @Test
    fun sensor_upa_front_left() { // ktlint-disable max-line-length
        val sensorViewId = R.id.front_left
        val sensorBackgroundViewId = R.id.bg_front_left
        with(sonarRepository.frontLeft) {
            postValue(sensor(false, 0))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(true, 0))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(false, 1))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(true, 1))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
            postValue(sensor(false, 2))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_1_05)
            postValue(sensor(true, 2))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_1_05lines)
            postValue(sensor(false, 3))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_2_05)
            postValue(sensor(true, 3))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_2_05lines)
            postValue(sensor(false, 4))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_3_05)
            postValue(sensor(true, 4))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_3_05lines)
            postValue(sensor(false, 5))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_3_05)
            postValue(sensor(true, 5))
            assertViewHasEffectiveVisibility(sensorBackgroundViewId)
            checkSensor(sensorViewId, R.drawable.rimg_element_upa_level_3_05lines)
            sonarRepository.upaDisplayRequest.postValue(UpaDisplayRequestType.NO_DISPLAY)
            assertViewHasNoEffectiveVisibility(sensorBackgroundViewId)
            assertViewHasNoEffectiveVisibility(sensorViewId)
        }
    }

    private fun checkSensor(
        @IdRes viewId: Int,
        @DrawableRes drawableId: Int
    ) {
        assertViewHasEffectiveVisibility(viewId)
        checkDrawableLevel(viewId, drawableId)
    }

    private fun sensor(hatched: Boolean, level: Int, scanned: Boolean = true) =
        SensorState(true, hatched, scanned, level)
}