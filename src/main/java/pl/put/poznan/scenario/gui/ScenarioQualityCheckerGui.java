package pl.put.poznan.scenario.gui;

import org.json.JSONObject;
import org.json.JSONArray;
import pl.put.poznan.scenario.logic.*;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ScenarioQualityCheckerGui extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        Text text = new Text();

        VBox root = new VBox();
        HBox buttons = new HBox();
        Button chooseFile = new Button("Choose file");
        FileChooser fileChooser = new FileChooser();
        chooseFile.setOnAction(e->{
            File f = fileChooser.showOpenDialog(primaryStage);
            try {
                text.setText(new String(Files.readAllBytes(Paths.get(f.getPath()))));
            } catch(Exception exc) {}
        });
        Button getScenarioText = new Button("Get scenario text");
        getScenarioText.setOnAction(e->{
            try {
                Scenario s = new Scenario(new JSONObject(text.getText()));
                ScenarioToTextVisitor v = new ScenarioToTextVisitor();
                s.accept(v);
                text.setText(v.getResult());
            } catch(Exception exc) {}
        });
        buttons.getChildren().add(chooseFile);
        buttons.getChildren().add(getScenarioText);
        root.getChildren().add(buttons);
        root.getChildren().add(text);

        Scene mainScene = new Scene(root, 600, 800);
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }
}
