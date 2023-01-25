package com.renault.parkassist.service;

import com.renault.parkassist.routing.IRouteListener;
import com.renault.parkassist.routing.Pursuit;

interface IDisplayService {
    oneway void registerProcessDeath(in IBinder client);

    oneway void registerRouteListener(in IRouteListener listener, in IBinder client);

    oneway void startPursuit(in Pursuit pursuit);
    oneway void stopPursuit(in Pursuit pursuit);
    oneway void stopCurrentPursuit();
    boolean isAlive();
}
