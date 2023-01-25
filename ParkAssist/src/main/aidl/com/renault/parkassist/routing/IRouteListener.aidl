package com.renault.parkassist.routing;

import com.renault.parkassist.routing.RouteIdentifier;

interface IRouteListener {
    oneway void onRouteChange(in RouteIdentifier routeId);
    oneway void onVisibilityChange(in boolean visibility);
    oneway void onShadowRequestedChange(in boolean shadowRequested);
}
