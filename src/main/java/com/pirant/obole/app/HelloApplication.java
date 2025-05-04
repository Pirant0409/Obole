package com.pirant.obole.app;

import com.pirant.obole.crypto.TrustedDeviceStore;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/pirant/obole/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        System.out.println(System.getProperty("user.home"));
        stage.setTitle("Obole");
        stage.setScene(scene);
        stage.show();

        TrustedDeviceStore trustedDeviceStore = new TrustedDeviceStore();
        trustedDeviceStore.getAllTrustedDevices().forEach(trustedDevice -> {System.out.println(trustedDevice);});
    }

    public static void main(String[] args) {
        launch();
    }
}