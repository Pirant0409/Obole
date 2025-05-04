package com.pirant.obole.controller;

import com.pirant.obole.model.Device;
import com.pirant.obole.network.DeviceDiscovery;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import javax.jmdns.ServiceInfo;
import java.io.IOException;
import java.util.List;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() throws IOException, InterruptedException {
        DeviceDiscovery myDevice = new DeviceDiscovery();
        myDevice.startService(5000);

        Task<Void> discoveryTask = new Task<>() {
            @Override
            protected Void call(){
                try{
                    myDevice.discoverServices(updatedList ->{
                        Platform.runLater(() -> {
                            StringBuilder textToSet = new StringBuilder();
                            for (Device d : updatedList){
                                textToSet.append(d.toString()).append("\n");
                            }
                            welcomeText.setText(textToSet.toString());
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
}