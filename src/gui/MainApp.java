package gui;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root;
            root = FXMLLoader.load(getClass().getResource("FxCalculator.fxml"));

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("FxCalculator.css").toExternalForm());
            primaryStage.setTitle("Desktop Calculator");
            primaryStage.setScene(scene);
            Image icon = new Image("res/calcIcon.png");
            primaryStage.getIcons().add(icon);
            primaryStage.setResizable(false);
            primaryStage.show();

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            System.out.println(ex.getCause());
            ex.printStackTrace();
        }
    }//start

    public static void main(String[] args) {
        launch(args);
    }

}
