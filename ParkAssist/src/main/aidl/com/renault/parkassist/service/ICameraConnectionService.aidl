package com.renault.parkassist.service;

import com.renault.parkassist.camera.ICameraConnectionListener;

interface ICameraConnectionService {
    oneway void registerProcessDeath(in IBinder client);
    oneway void registerCameraListener(in ICameraConnectionListener listener, in IBinder handle);
    oneway void unregisterCameraListener(in IBinder handle);
}