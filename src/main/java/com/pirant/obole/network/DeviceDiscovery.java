package com.pirant.obole.network;

import com.pirant.obole.model.Device;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;

public class DeviceDiscovery {

    private static final String SERVICE_TYPE = "_obole._tcp.local.";
    private static final String SERVICE_NAME = "OboleService";

    private JmDNS jmdns;

    public List<Device> discoveredDevices = new ArrayList<>();

    public void startService(int port) throws IOException {
        InetAddress addr = InetAddress.getLocalHost();
        String deviceName = addr.getHostName();
        System.out.println("deviceName: " + deviceName);
        jmdns = JmDNS.create(addr);

        ServiceInfo serviceInfo = ServiceInfo.create(SERVICE_TYPE, SERVICE_NAME, port, deviceName);
        jmdns.registerService(serviceInfo);

        System.out.println("Service registered: " + serviceInfo);
    }

    public void discoverServices(Consumer<List<Device>> onDeviceListChanged) throws IOException {
        InetAddress addr = InetAddress.getLocalHost();
        jmdns = JmDNS.create(addr);

        jmdns.addServiceListener(SERVICE_TYPE, new ServiceListener() {
            @Override
            public void serviceAdded(ServiceEvent event) {
                System.out.println("Service added: " + event.getName());
            }

            @Override
            public void serviceRemoved(ServiceEvent event) {
                System.out.println("Service removed: " + event.getName());
            }

            @Override
            public void serviceResolved(ServiceEvent event) {
                ServiceInfo info = event.getInfo();
                String currentDeviceName = extractDeviceName(info.getTextBytes());
                String currentAddress = info.getHostAddresses()[0];
                int currentPort = info.getPort();
                Device currentDevice = new Device(currentDeviceName,currentAddress,currentPort);
                System.out.println("Device found: " + currentDevice);
                if(discoveredDevices.isEmpty()) {
                    discoveredDevices.add(currentDevice);
                    onDeviceListChanged.accept(discoveredDevices);
                }
                else if (discoveredDevices.contains(currentDevice)) {
                    discoveredDevices.remove(currentDevice);
                    discoveredDevices.add(currentDevice);
                    onDeviceListChanged.accept(discoveredDevices);
                }
            }
        });

        System.out.println("Discovery started for type: " + SERVICE_TYPE);
    }

    public String extractDeviceName(byte[] textBytes) {
        if (textBytes != null && textBytes.length > 0) {
            byte[] deviceNameBytes = Arrays.copyOfRange(textBytes, 1, textBytes.length);
            return new String(deviceNameBytes, StandardCharsets.UTF_8);
        } else {
            return "";
        }
    }

    public void stop() throws IOException {
        if (jmdns != null) {
            jmdns.unregisterAllServices();
            jmdns.close();
        }
    }
}
