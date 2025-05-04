package com.pirant.obole.controller;

import com.pirant.obole.crypto.KeyManager;
import com.pirant.obole.crypto.TrustedDeviceStore;
import com.pirant.obole.model.Device;
import com.pirant.obole.model.TrustedDevice;
import com.pirant.obole.network.DeviceDiscovery;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import javax.jmdns.ServiceInfo;
import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

public class HelloController {
    @FXML
    private Label lookingForDevice;

    @FXML
    private VBox devicesContainer;

    @FXML
    protected void onFindDeviceButtonClick() throws IOException, InterruptedException {
        lookingForDevice.setText("Looking for device(s)...");
        DeviceDiscovery myDeviceService = new DeviceDiscovery();
        myDeviceService.startService(5000);
        runDiscoveryTask(myDeviceService);

    }

    protected void runDiscoveryTask(DeviceDiscovery myDeviceService) throws IOException, InterruptedException {

        Task<Void> discoveryTask = new Task<>() {
            @Override
            protected Void call(){
                try{
                    myDeviceService.discoverServices(updatedList ->{
                        Platform.runLater(() -> {
                            //clearing children to avoid doubles
                            devicesContainer.getChildren().clear();
                            for (Device d : updatedList){
                                createDeviceButton(d);
                            }
                        });
                    });
                }catch (IOException e){
                    e.printStackTrace();
                }
                return null;
            }
        };

        Thread discoveryThread = new Thread(discoveryTask);
        discoveryThread.setDaemon(true);
        discoveryThread.start();
    }

    private void createDeviceButton(Device device) {
        Button deviceButton = new Button(device.getName() + " (" + device.getAddress() + ")");
        deviceButton.setOnAction(event -> {

            // setting confirmation alerts for connection
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Connect to device");
            alert.setHeaderText("Connect to " + device.getName() + " ?");
            alert.setContentText("Do you want to connect to this device?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK){
                try {
                    connectToDevice(device);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        devicesContainer.getChildren().add(deviceButton);
    }

    private void connectToDevice(Device device) throws NoSuchAlgorithmException {
        try{
            KeyManager keyManager = new KeyManager();

            PublicKey publicKey = keyManager.getPublicKey();
            String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());

            // TODO: Send your key to the device over the network here

            String fakeDevicePublicKeyBase64 = "MIIBIjANBgkqhkiG9";

            TrustedDeviceStore trustedDeviceStore = new TrustedDeviceStore();
            trustedDeviceStore.addTrustedDevice(device, publicKeyBase64);

        } catch(Exception e){
            e.printStackTrace();
        }

    }
}