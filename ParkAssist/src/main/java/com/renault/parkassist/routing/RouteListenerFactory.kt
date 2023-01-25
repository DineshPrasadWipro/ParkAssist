package com.renault.parkassist.routing

class RouteListenerFactory {

    fun getRouteListener(
        onRouteChange: (route: RouteIdentifier) -> Unit,
        onShadowRequestedChange: (shadowRequested: Boolean) -> Unit,
        onVisibilityChange: (visibility: Boolean) -> Unit
    ): IRouteListener {
        return object : IRouteListener.Stub() {
            override fun onRouteChange(route: RouteIdentifier) {
                onRouteChange(route)
            }

            override fun onShadowRequestedChange(shadowRequested: Boolean) {
                onShadowRequestedChange(shadowRequested)
            }

            override fun onVisibilityChange(visibility: Boolean) {
                onVisibilityChange(visibility)
            }
        }
    }
}