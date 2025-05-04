package com.pirant.obole.crypto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pirant.obole.model.Device;
import com.pirant.obole.model.TrustedDevice;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrustedDeviceStore {
    private static final String userHome = System.getProperty("user.home");
    private static final String FILE_PATH = userHome + File.separator + ".obole" + File.separator + "trusted_devices.json";
    private final ObjectMapper mapper = new ObjectMapper();
    private final Map<String, TrustedDevice> trustedDevices = new HashMap<>();

    public TrustedDeviceStore(){
        File dir = new File(userHome + File.separator + ".obole");
        File trustedDevicesFile = new File(dir, "trusted_devices.json");
        if (!dir.exists()){
            boolean created = dir.mkdir();
            if (!created){
                System.err.println("Warning: Could not create directory: " + dir.getAbsolutePath());
            }
        }
        if (trustedDevicesFile.exists()){
            load();
        }
    }
    public void addTrustedDevice(Device device, String publicKeyBase64){
        TrustedDevice td = new TrustedDevice(device, publicKeyBase64);
        trustedDevices.put(td.getDevice().getName(), td);
        save();
    }

    public TrustedDevice getTrustedDevice(String deviceName){
        return trustedDevices.get(deviceName);
    }

    public boolean isTrusted(String deviceName){
        return trustedDevices.containsKey(deviceName);
    }

    public List<TrustedDevice> getAllTrustedDevices(){
        return new ArrayList<>(trustedDevices.values());
    }

    private void load(){
        try{
            File file = new File(FILE_PATH);
            if(file.exists()){
                System.out.println("Loading trusted devices");
                List<TrustedDevice> list = mapper.readValue(file, new TypeReference<List<TrustedDevice>>() {});
                for (TrustedDevice trustedDevice : list) {
                    trustedDevices.put(trustedDevice.getDevice().getName(), trustedDevice);
                }
            }
        }catch (IOException e){
            System.err.println("Failed to load trusted devices: " + e.getMessage());
        }
    }

    private void save(){
        try{
            System.out.println("Saving trusted devices: " + trustedDevices.keySet());
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), trustedDevices.values());
        }catch (IOException e){
            System.err.println("Failed to save trusted devices: " + e.getMessage());
        }
    }

}
