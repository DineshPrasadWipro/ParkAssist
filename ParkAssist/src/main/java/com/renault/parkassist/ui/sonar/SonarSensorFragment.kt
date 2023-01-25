package com.renault.parkassist.ui.sonar

import android.animation.AnimatorInflater
import android.animation.ValueAnimator
import android.view.View
import android.widget.ImageView
import androidx.annotation.IntegerRes
import com.renault.parkassist.R
import com.renault.parkassist.ui.FragmentBase
import com.renault.parkassist.utility.setPresent
import com.renault.parkassist.viewmodel.sonar.ParkingSensor
import com.renault.parkassist.viewmodel.sonar.SensorLevel
import com.renault.parkassist.viewmodel.sonar.SonarStateViewModelBase
import kotlinx.android.synthetic.main.sonar_sensor_layout.*
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class SonarSensorFragment : FragmentBase() {

    private val sonarStateViewModel: SonarStateViewModelBase by viewModel()

    private val animatedViews = mutableListOf<View>()

    private val blinkAnimator by lazy {
        (AnimatorInflater.loadAnimator(
            requireContext(),
            R.animator.blinking
        ) as ValueAnimator).apply {
            addUpdateListener { valueAnimator ->
                animatedViews.forEach { animatedView ->
                    animatedView.alpha = valueAnimator.animatedValue as Float
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        blinkAnimator.removeAllUpdateListeners()
    }

    override fun onBind() {
        sonarStateViewModel.avatarVisible.observe {
            car.setPresent(it)
        }

        with(sonarStateViewModel) {
            listOf(
                Triple(frontCenter, front_center, bg_front_center),
                Triple(frontRight, front_right, bg_front_right),
                Triple(rightFront, right_front, bg_right_front),
                Triple(rightFrontCenter, right_front_center, bg_right_front_center),
                Triple(rightRearCenter, right_rear_center, bg_right_rear_center),
                Triple(rightRear, right_rear, bg_right_rear),
                Triple(rearRight, rear_right, bg_rear_right),
                Triple(rearCenter, rear_center, bg_rear_center),
                Triple(rearLeft, rear_left, bg_rear_left),
                Triple(leftRear, left_rear, bg_left_rear),
                Triple(leftRearCenter, left_rear_center, bg_left_rear_center),
                Triple(leftFrontCenter, left_front_center, bg_left_front_center),
                Triple(leftFront, left_front, bg_left_front),
                Triple(frontLeft, front_left, bg_front_left)
            ).forEach { (sensor, view_sensor, view_sensor_bg) ->
                sensor.observe {
                    view_sensor.updateSensor(it)
                    view_sensor_bg.updateSensorBackground(it)
                }
            }
        }
    }

    private fun ImageView.updateSensorBackground(parkingSensor: ParkingSensor) {
        setPresent(parkingSensor.level != SensorLevel.INVISIBLE)
    }

    private fun ImageView.updateSensor(parkingSensor: ParkingSensor) {
        setPresent(true)
        stopBlinking()
        getDrawableLevel(parkingSensor)?.let { setImageLevel(it) }
        when (parkingSensor.level) {
            SensorLevel.VERY_CLOSE -> startBlinking()
            SensorLevel.INVISIBLE, SensorLevel.GREYED -> setPresent(false)
            else -> {
                // Do nothing
            }
        }
    }

    private fun getDrawableLevel(parkingSensor: ParkingSensor): Int? {
        return when (parkingSensor.level) {
            SensorLevel.VERY_FAR -> getSonarLevelResource(R.integer.sonar_senor_level_0)
            SensorLevel.FAR -> getSonarLevelResource(R.integer.sonar_senor_level_1)
            SensorLevel.MEDIUM -> getSonarLevelResource(R.integer.sonar_senor_level_2)
            SensorLevel.CLOSE -> getSonarLevelResource(R.integer.sonar_senor_level_3)
            SensorLevel.VERY_CLOSE -> getSonarLevelResource(R.integer.sonar_senor_level_3)
            else -> null
        }?.plus(if (parkingSensor.hatched) 1 else 0)
    }

    private fun getSonarLevelResource(@IntegerRes intRes: Int) =
        requireContext().resources.getInteger(intRes)

    private fun ImageView.startBlinking() {
        animatedViews += this
        if (!blinkAnimator.isStarted)
            blinkAnimator.start()
        else
            blinkAnimator.resume()
    }

    private fun ImageView.stopBlinking() {
        animatedViews.remove(this)
        if (animatedViews.isEmpty())
            blinkAnimator.pause()
        alpha = 1f
    }
}