package com.renault.parkassist.utility

/**
 * Vehicle configuration
 * Specification ref: H-B02_SETTINGS_v1.32
 */
const val CFG_ADAS_UPA_Visual_Rear = "persist.car.config.adas.upa.rear.enabled"
const val DEFAULT_CFG_ADAS_UPA_Visual_Rear = true
const val CFG_ADAS_UPA_Visual_Front = "persist.car.config.adas.upa.front.enabled"
const val DEFAULT_CFG_ADAS_UPA_Visual_Front = true
const val CFG_ADAS_FKP = "persist.car.config.adas.fkp.enabled"
const val DEFAULT_CFG_ADAS_FKP = true
const val CFG_ADAS_RCTA = "persist.car.config.adas.rcta.enabled"
const val DEFAULT_CFG_ADAS_RCTA = true
const val CFG_ADAS_RAEB = "persist.car.config.adas.raeb.enabled"
const val DEFAULT_CFG_ADAS_RAEB = true
const val CFG_ADAS_RVC = "persist.adas.surroundview.rvc.ActivationState"
const val DEFAULT_CFG_ADAS_RVC = 1
const val CFG_ADAS_AVM_D = "persist.adas.surroundview.avm.ActivationState"
const val DEFAULT_CFG_ADAS_AVM_D = 0
const val CFG_ADAS_TRAILER_PRESENCE = "persist.adas.surroundview.TrailerPresence"
const val DEFAULT_CFG_ADAS_TRAILER_PRESENCE = 0

/**
 * SurroundView service configuration
 */
const val CFG_ADAS_RVC_STATIC_GUIDELINES =
    "persist.adas.surroundview.rvc.StaticGuidelines.ActivationState"
const val DEFAULT_CFG_ADAS_RVC_STATIC_GUIDELINES = 0
const val CFG_ADAS_RVC_DYNAMIC_GUIDELINES =
    "persist.adas.surroundview.rvc.DynamicGuidelines.ActivationState"
const val DEFAULT_CFG_ADAS_RVC_DYNAMIC_GUIDELINES = 0
const val CFG_ADAS_RVC_TRAILER_GUIDELINES =
    "persist.adas.surroundview.rvc.TrailerGuideline.ActivationState"
const val DEFAULT_CFG_ADAS_RVC_TRAILER_GUIDELINES = 0
const val CFG_ADAS_RVC_AUTO_ZOOM =
    "persist.adas.surroundview.rvc.AutoZoom.ActivationState"
const val DEFAULT_CFG_ADAS_RVC_AUTO_ZOOM = 0
const val CFG_ADAS_AVM_TRAILER_GUIDELINES = "" +
    "persist.adas.surroundview.avm.TrailerGuideline.ActivationState"
const val DEFAULT_CFG_ADAS_AVM_TRAILER_GUIDELINES = 0
const val CFG_ADAS_AVM_AUTO_ZOOM =
    "persist.adas.surroundview.avm.AutoZoom.ActivationState"
const val DEFAULT_CFG_ADAS_AVM_AUTO_ZOOM = 0

/**
 * Additional development configurations
 */
const val PERF_LOG = "persist.product.alliance.perflog"
const val DEFAULT_PERF_LOG = 0
const val CFG_ADAS_USE_SYSTEM_PROPS_SURROUND_CONFIG =
    "persist.car.config.adas.use.system.props.surround.config"
const val DEFAULT_CFG_ADAS_USE_SYSTEM_PROPS_SURROUND_CONFIG = false
const val CFG_ADAS_USE_SYSTEM_PROPS_SONAR_CONFIG =
    "persist.car.config.adas.use.system.props.sonar.config"
const val DEFAULT_CFG_ADAS_USE_SYSTEM_PROPS_SONAR_CONFIG = false