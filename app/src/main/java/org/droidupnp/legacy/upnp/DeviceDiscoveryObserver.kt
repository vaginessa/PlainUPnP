package org.droidupnp.legacy.upnp

import com.m3sv.plainupnp.data.upnp.UpnpDeviceEvent

interface DeviceDiscoveryObserver {
    fun addedDevice(event: UpnpDeviceEvent)

    fun removedDevice(event: UpnpDeviceEvent)
}
