package com.pirant.obole.controller;

import com.pirant.obole.model.Device;
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
              connectToDevice(device);
            }
        });
        devicesContainer.getChildren().add(deviceButton);
    }

    private void connectToDevice(Device device) {
        System.out.println("Connected to device " + device.getName());
    }
}