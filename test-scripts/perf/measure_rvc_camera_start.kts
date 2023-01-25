import java.io.File
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*
import java.util.concurrent.TimeUnit
import java.time.LocalTime
import java.time.LocalDate
import java.time.format.DateTimeFormatter


fun String.runCommand(
    workingDir: File = File("."),
    timeoutAmount: Long = 5,
    timeoutUnit: TimeUnit = TimeUnit.SECONDS
): String = ProcessBuilder(split("\\s".toRegex()))
    .directory(workingDir)
    .redirectOutput(ProcessBuilder.Redirect.PIPE)
    .redirectError(ProcessBuilder.Redirect.PIPE)
    .start().apply { waitFor(timeoutAmount, timeoutUnit) }
    .inputStream.bufferedReader().readText()

fun String.parseTime(): LocalTime {
    val timeStampString = this.substring(6..17)
    return LocalTime.parse(timeStampString)
}

fun getLastLogWithPattern(pattern: String) =
    "adb shell logcat -d -v time | grep $pattern ".runCommand().lines().dropLast(1).last()

fun engaged() {
    "/bin/bash rear-gear-engaged.sh".runCommand(File("../vucs/"))
}

fun notEngaged() {
    "/bin/bash rear-gear-engaged-not-engaged.sh".runCommand(File("../vucs/"))
}

fun startTime(): LocalTime {
    "adb shell log start_time".runCommand()
    val startTimeLog = getLastLogWithPattern("start_time")
    return startTimeLog.parseTime()
}

fun cameraRenderingTime(): LocalTime {
    val cameraRenderingLog = getLastLogWithPattern("'onRenderingStateChange(): RENDERING_RUNNING'")
    return cameraRenderingLog.parseTime()
}


val repeatCount = 20
val sleepCameraTime = 2000L
var durationCount = 0L

repeat(repeatCount) {
    val begin = startTime()
    println("start time : ${begin}")
    engaged()
    Thread.sleep(sleepCameraTime)
    val end = cameraRenderingTime()
    println("end time : $end")
    val duration = Duration.between(begin, end).toMillis()
    println("duration : $duration ms")
    notEngaged()
    durationCount += duration
}

println("average duration: ${durationCount / repeatCount} ms , for $repeatCount runs")


