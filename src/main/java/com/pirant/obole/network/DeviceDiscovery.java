package com.pirant.obole.network;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class DeviceDiscovery {

    private static final String SERVICE_TYPE = "_obole._tcp.local.";
    private static final String SERVICE_NAME = "OboleService";

    private JmDNS jmdns;

    public void startService(int port) throws IOException {
        InetAddress addr = InetAddress.getLocalHost();
        String deviceName = addr.getHostName();
        System.out.println("deviceName: " + deviceName);
        jmdns = JmDNS.create(addr);

        ServiceInfo serviceInfo = ServiceInfo.create(SERVICE_TYPE, SERVICE_NAME, port, deviceName);
        jmdns.registerService(serviceInfo);

        System.out.println("Service registered: " + serviceInfo);
    }

    public void discoverServices() throws IOException {
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
                String resolvedName = extractDeviceName(info.getTextBytes());
                System.out.println("Device found: " + resolvedName + " at " + info.getHostAddresses()[0] + ":" + info.getPort());
            }
        });

        System.out.println("Discovery started for type: " + SERVICE_TYPE);
    }

    public String extractDeviceName(byte[] textBytes) {
        if (textBytes != null && textBytes.length > 0 && textBytes[0] == 4) {
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
