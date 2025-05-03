package com.pirant.obole.controller;

import com.pirant.obole.network.DeviceDiscovery;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.List;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() throws IOException, InterruptedException {
        DeviceDiscovery device1 = new DeviceDiscovery();
        DeviceDiscovery device2 = new DeviceDiscovery();

        device1.startService(5000);
        device2.discoverServices();

        Thread.sleep(30000);
        device1.stop();
        device2.stop();
        welcomeText.setText("Discovery done");
    }
}