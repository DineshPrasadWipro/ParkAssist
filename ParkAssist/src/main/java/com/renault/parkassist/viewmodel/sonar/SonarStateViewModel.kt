package com.renault.parkassist.viewmodel.sonar

import alliance.car.sonar.AllianceCarSonarManager.*
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.renault.parkassist.repository.sonar.*
import com.renault.parkassist.viewmodel.LiveDataUtils
import com.renault.parkassist.viewmodel.mergeNotNullWith
import org.koin.core.KoinComponent
import org.koin.core.inject

class SonarStateViewModel(application: Application) : SonarStateViewModelBase(application),
    KoinComponent {

    private val repository: ISonarRepository by inject()

    private val rearUpaDisplayRequested = Transformations.map(repository.upaDisplayRequest) {
        it == UpaDisplayRequestType.REAR || it == UpaDisplayRequestType.REAR_FRONT
    }

    private val frontUpaDisplayRequested = Transformations.map(repository.upaDisplayRequest) {
        it == UpaDisplayRequestType.FRONT || it == UpaDisplayRequestType.REAR_FRONT
    }

    private val fkpDisplayRequested = Transformations.map(repository.fkpDisplayRequest) {
        it != FkpDisplayRequestType.NO_DISPLAY
    }

    private val frontLeft = Transformations.map(
        LiveDataUtils.combineNonNull(
            repository.frontLeft,
            frontUpaDisplayRequested
        )
    ) { (sonar, displayRequested) ->
        sonarToParkingSensor(sonar, SensorGroup.UPA, displayRequested, MAX_LEVEL_5, MIN_LEVEL_2)
    }

    private val frontCenter = Transformations.map(
        LiveDataUtils.combineNonNull(
            repository.frontCenter,
            frontUpaDisplayRequested
        )
    ) { (sonar, displayRequested) ->
        sonarToParkingSensor(sonar, SensorGroup.UPA, displayRequested, MAX_LEVEL_5, MIN_LEVEL_1)
    }

    private val frontRight = Transformations.map(
        LiveDataUtils.combineNonNull(
            repository.frontRight,
            frontUpaDisplayRequested
        )
    ) { (sonar, displayRequested) ->
        sonarToParkingSensor(sonar, SensorGroup.UPA, displayRequested, MAX_LEVEL_5, MIN_LEVEL_2)
    }

    private val rearLeft = Transformations.map(
        LiveDataUtils.combineNonNull(
            repository.rearLeft,
            rearUpaDisplayRequested
        )
    ) { (sonar, displayRequested) ->
        sonarToParkingSensor(sonar, SensorGroup.UPA, displayRequested, MAX_LEVEL_5, MIN_LEVEL_2)
    }

    private val rearCenter = Transformations.map(
        LiveDataUtils.combineNonNull(
            repository.rearCenter,
            rearUpaDisplayRequested
        )
    ) { (sonar, displayRequested) ->
        sonarToParkingSensor(sonar, SensorGroup.UPA, displayRequested, MAX_LEVEL_5, MIN_LEVEL_1)
    }

    private val rearRight = Transformations.map(
        LiveDataUtils.combineNonNull(
            repository.rearRight,
            rearUpaDisplayRequested
        )
    ) { (sonar, displayRequested) ->
        sonarToParkingSensor(sonar, SensorGroup.UPA, displayRequested, MAX_LEVEL_5, MIN_LEVEL_2)
    }

    private val leftFront = Transformations.map(
        LiveDataUtils.combineNonNull(
            repository.leftFront,
            fkpDisplayRequested
        )
    ) { (sonar, displayRequested) ->
        sonarToParkingSensor(sonar, SensorGroup.FKP, displayRequested, MAX_LEVEL_3, MIN_LEVEL_1)
    }

    private val leftFrontCenter = Transformations.map(
        LiveDataUtils.combineNonNull(
            repository.leftFrontCenter,
            fkpDisplayRequested
        )
    ) { (sonar, displayRequested) ->
        sonarToParkingSensor(sonar, SensorGroup.FKP, displayRequested, MAX_LEVEL_3, MIN_LEVEL_1)
    }

    private val leftRearCenter = Transformations.map(
        LiveDataUtils.combineNonNull(
            repository.leftRearCenter,
            fkpDisplayRequested
        )
    ) { (sonar, displayRequested) ->
        sonarToParkingSensor(sonar, SensorGroup.FKP, displayRequested, MAX_LEVEL_3, MIN_LEVEL_1)
    }

    private val leftRear = Transformations.map(
        LiveDataUtils.combineNonNull(
            repository.leftRear,
            fkpDisplayRequested
        )
    ) { (sonar, displayRequested) ->
        sonarToParkingSensor(sonar, SensorGroup.FKP, displayRequested, MAX_LEVEL_3, MIN_LEVEL_1)
    }

    private val rightFront = Transformations.map(
        LiveDataUtils.combineNonNull(
            repository.rightFront,
            fkpDisplayRequested
        )
    ) { (sonar, displayRequested) ->
        sonarToParkingSensor(sonar, SensorGroup.FKP, displayRequested, MAX_LEVEL_3, MIN_LEVEL_1)
    }

    private val rightFrontCenter = Transformations.map(
        LiveDataUtils.combineNonNull(
            repository.rightFrontCenter,
            fkpDisplayRequested
        )
    ) { (sonar, displayRequested) ->
        sonarToParkingSensor(sonar, SensorGroup.FKP, displayRequested, MAX_LEVEL_3, MIN_LEVEL_1)
    }

    private val rightRearCenter = Transformations.map(
        LiveDataUtils.combineNonNull(
            repository.rightRearCenter,
            fkpDisplayRequested
        )
    ) { (sonar, displayRequested) ->
        sonarToParkingSensor(sonar, SensorGroup.FKP, displayRequested, MAX_LEVEL_3, MIN_LEVEL_1)
    }

    private val rightRear = Transformations.map(
        LiveDataUtils.combineNonNull(
            repository.rightRear,
            fkpDisplayRequested
        )
    ) { (sonar, displayRequested) ->
        sonarToParkingSensor(sonar, SensorGroup.FKP, displayRequested, MAX_LEVEL_3, MIN_LEVEL_1)
    }

    private val upaEnabledInSettings = Transformations.map(
        LiveDataUtils.combineLatest(
            repository.flankState,
            repository.frontState,
            repository.rearState
        )
    ) { (flankState, frontState, rearState) ->
        when {
            flankState == GroupState.DISABLED &&
                frontState == GroupState.DISABLED &&
                rearState == GroupState.DISABLED -> false
            else -> true
        }
    }

    private val avatarVisible =
        Transformations.map(repository.displayRequest) { displayRequest ->
            displayRequest != DisplayType.NONE
        }.mergeNotNullWith(upaEnabledInSettings) { displayRequest, upaEnabled ->
            upaEnabled && displayRequest
        }

    enum class SensorGroup { UPA, FKP }

    override fun getFrontLeft(): LiveData<ParkingSensor> = frontLeft

    override fun getFrontCenter(): LiveData<ParkingSensor> = frontCenter

    override fun getFrontRight(): LiveData<ParkingSensor> = frontRight

    override fun getRearLeft(): LiveData<ParkingSensor> = rearLeft

    override fun getRearCenter(): LiveData<ParkingSensor> = rearCenter

    override fun getRearRight(): LiveData<ParkingSensor> = rearRight

    override fun getLeftFront(): LiveData<ParkingSensor> = leftFront

    override fun getLeftFrontCenter(): LiveData<ParkingSensor> = leftFrontCenter

    override fun getLeftRearCenter(): LiveData<ParkingSensor> = leftRearCenter

    override fun getLeftRear(): LiveData<ParkingSensor> = leftRear

    override fun getRightFront(): LiveData<ParkingSensor> = rightFront

    override fun getAvatarVisible(): LiveData<Boolean> = avatarVisible

    override fun getRightFrontCenter(): LiveData<ParkingSensor> = rightFrontCenter

    override fun getRightRearCenter(): LiveData<ParkingSensor> = rightRearCenter

    override fun getRightRear(): LiveData<ParkingSensor> = rightRear

    override fun getObstacle(): LiveData<Boolean> = repository.obstacle

    override fun getFrontEnabled(): LiveData<Boolean> {
        return Transformations.map(repository.frontState) { state -> state == GroupState.ENABLED }
    }

    override fun getFlankEnabled(): LiveData<Boolean> {
        return Transformations.map(repository.flankState) { state -> state == GroupState.ENABLED }
    }

    override fun getRearEnabled(): LiveData<Boolean> {
        return Transformations.map(repository.rearState) { state -> state == GroupState.ENABLED }
    }

    private fun sonarToParkingSensor(
        sonar: SensorState,
        sensorGroup: SensorGroup,
        displayRequested: Boolean,
        maxLevel: Int,
        minLevel: Int
    ): ParkingSensor {
        val sensorLevel =
            when (sensorGroup) {
                SensorGroup.UPA ->
                    when {
                        !displayRequested || !sonar.isHwSupported -> SensorLevel.INVISIBLE
                        sonar.level < minLevel -> SensorLevel.GREYED
                        sonar.level == maxLevel -> SensorLevel.VERY_CLOSE
                        sonar.level == maxLevel - 1 -> SensorLevel.CLOSE
                        sonar.level == maxLevel - 2 -> SensorLevel.MEDIUM
                        sonar.level == maxLevel - 3 -> SensorLevel.FAR
                        sonar.level == maxLevel - 4 -> SensorLevel.VERY_FAR
                        else -> SensorLevel.GREYED
                    }
                SensorGroup.FKP ->
                    when {
                        !displayRequested ||
                            !sonar.isHwSupported ||
                            !sonar.isScanned -> SensorLevel.INVISIBLE
                        sonar.level < minLevel -> SensorLevel.GREYED
                        sonar.level == maxLevel -> SensorLevel.VERY_CLOSE
                        sonar.level == maxLevel - 1 -> SensorLevel.MEDIUM
                        sonar.level == maxLevel - 2 -> SensorLevel.FAR
                        else -> SensorLevel.GREYED
                    }
            }
        return ParkingSensor(
            sonar.isHatched,
            sensorLevel
        )
    }

    override fun enableFront(enable: Boolean) {
        repository.setSonarGroup(SONAR_GROUP_FRONT, enable)
    }

    override fun enableRear(enable: Boolean) {
        repository.setSonarGroup(SONAR_GROUP_REAR, enable)
    }

    override fun enableFlank(enable: Boolean) {
        repository.setSonarGroup(SONAR_GROUP_FLANK, enable)
    }

    override fun getCloseVisible(): LiveData<Boolean> = repository.closeAllowed

    companion object {
        const val MAX_LEVEL_3 = 3
        const val MAX_LEVEL_5 = 5
        const val MIN_LEVEL_1 = 1
        const val MIN_LEVEL_2 = 2
    }
}