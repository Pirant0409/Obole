package com.pirant.obole.model;

import java.util.Objects;

public class Device {
    private String name;
    private  String address;
    private  int port;

    public Device(String name, String address, int port) {
        this.name = name;
        this.address = address;
        this.port = port;
    }
    //no-argument constructor for JSON object
    public Device(){}

    @Override
    public String toString() {
        return "Device [name=" + name + ", address=" + address + ":" + port + "]";
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof Device device)) return false;
        return (Objects.equals(address, device.address));
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }

    // Getters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }
}
