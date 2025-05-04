package com.pirant.obole.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TrustedDevice {
    private Device device;
    private String publicKey;


    public TrustedDevice(Device device, String publicKey) {
        this.device = device;
        this.publicKey = publicKey;
    }
    //no-argument constructor for JSON object
    public TrustedDevice(){}

    @Override
    public String toString() {
        return "TrustedDevice [device=" + device + ", publicKey=" + publicKey + "]";
    }
    //getters
    public Device getDevice() {
        return device;
    }

    public String getPublicKey() {
        return publicKey;
    }

    //setters
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
