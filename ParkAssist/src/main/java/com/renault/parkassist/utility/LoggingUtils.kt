package com.renault.parkassist.utility

import alliancex.arch.core.logger.*

fun infoLog(context: String, action: String, property: String?, value: Any? = null) =
    logI { "$context - $action ${property ?: ""} ${value ?: ""}" }

fun warningLog(context: String, fact: String) =
    logW { "$context - $fact" }

fun errorLog(context: String, fact: String, fallback: String? = null) =
    logE { "$context - $fact ${fallback ?: ""}" }

fun wtfLog(context: String, fact: String) =
    logWTF { "$context - $fact" }

// Surround View Service
fun surroundInfoLog(action: String, property: String, value: Any? = null) =
    infoLog("surround", action, property, value)

fun surroundSendInfoLog(property: String, value: Any? = null) =
    surroundInfoLog("send", property, value)

fun surroundReceiveInfoLog(property: String, value: Any? = null) =
    surroundInfoLog("receive", property, value)

fun surroundWarningLog(fact: String) =
    warningLog("surround", fact)

fun surroundErrorLog(fact: String, fallback: String? = null) =
    errorLog("surround", fact, fallback)

// AutoPark
fun autoparkInfoLog(action: String, property: String, value: Any? = null) =
    infoLog("autopark", action, property, value)

fun autoparkSendInfoLog(property: String, value: Any? = null) =
    autoparkInfoLog("send", property, value)

fun autoparkReceiveInfoLog(property: String, value: Any? = null) =
    autoparkInfoLog("receive", property, value)

fun autoparkWarningLog(fact: String) =
    warningLog("autopark", fact)

fun autoparkErrorLog(fact: String, fallback: String? = null) =
    errorLog("autopark", fact, fallback)

// Sonar
fun sonarInfoLog(action: String, property: String, value: Any? = null) =
    infoLog("sonar", action, property, value)

fun sonarSendInfoLog(property: String, value: Any? = null) =
    sonarInfoLog("send", property, value)

fun sonarReceiveInfoLog(property: String, value: Any? = null) =
    sonarInfoLog("receive", property, value)

fun sonarWarningLog(fact: String) =
    warningLog("sonar", fact)

fun sonarErrorLog(fact: String, fallback: String? = null) =
    errorLog("sonar", fact, fallback)

// Routing
fun routingInfoLog(action: String, property: String, value: Any? = null) =
    infoLog("routing", action, property, value)

fun routingReceiveInfoLog(property: String, value: Any? = null) =
    routingInfoLog("receive", property, value)

fun routingSendInfoLog(property: String, value: Any? = null) =
    routingInfoLog("receive", property, value)

fun routingWarningLog(fact: String) =
    warningLog("routing", fact)

fun routingErrorLog(fact: String, fallback: String? = null) =
    errorLog("routing", fact, fallback)

// Overlays
fun overlayInfoLog(action: String, property: String, value: Any? = null) =
    infoLog("overlay", action, property, value)

// Camera
fun cameraInfoLog(action: String, property: String, value: Any? = null) =
    infoLog("camera", action, property, value)

fun cameraWarningLog(fact: String) =
    warningLog("camera", fact)

fun cameraManagerErrorLog(fact: String, fallback: String? = null) =
    errorLog("camera", fact, fallback)

fun cameraWtfLog(fact: String) =
    wtfLog("camera", fact)

// Display
fun displayInfoLog(action: String, property: String, value: Any? = null) =
    infoLog("display", action, property, value)

fun displayError(fact: String, fallback: String? = null) =
    errorLog("display", fact)

// System
fun systemInfoLog(action: String, property: String, value: Any? = null) =
    infoLog("system", action, property, value)

fun systemErrorLog(fact: String, fallback: String? = null) =
    errorLog("system", fact, fallback)