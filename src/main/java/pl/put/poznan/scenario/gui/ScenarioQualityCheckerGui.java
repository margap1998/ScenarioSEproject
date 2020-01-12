package pl.put.poznan.scenario.gui;

import javafx.geometry.Insets;
import org.json.JSONObject;
import org.json.JSONArray;
import pl.put.poznan.scenario.logic.*;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ScenarioQualityCheckerGui extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    FileChooser fileChooser = new FileChooser();

    Scenario processedScenario;
    Text scenarioDisplay = new Text();
    Text stepCount = new Text("Step count: ");
    Text keywordCount = new Text("Keyword count: ");

    void updateDisplay() {
        ScenarioToTextVisitor v1 = new ScenarioToTextVisitor();
        processedScenario.accept(v1);
        scenarioDisplay.setText(v1.getResult());

        ScenarioStepCountVisitor v2 = new ScenarioStepCountVisitor();
        processedScenario.accept(v2);
        stepCount.setText("Step count: " + String.valueOf(v2.getResult()));

        ScenarioKeywordCountVisitor v3 = new ScenarioKeywordCountVisitor();
        processedScenario.accept(v3);
        keywordCount.setText("Keyword count: " + String.valueOf(v3.getResult()));
    }

    void readScenarioFromFile(Stage stage) {
        File f = fileChooser.showOpenDialog(stage);
        try {
            if(f != null) {
                // read the chosen file into a string
                String s = new String(Files.readAllBytes(Paths.get(f.getPath())));
                processedScenario = new Scenario(new JSONObject(s));
                updateDisplay();
            }
        } catch(Exception exc) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Loaded file is empty! Please, load a file with the scenario!");
            alert.showAndWait();
        }
    }

    void setNestLimit(int l) {
        try {
            Scenario tempScenario = new Scenario(processedScenario.toJSON());

            ScenarioDeepnessMeterVisitor v0 = new ScenarioDeepnessMeterVisitor();
            tempScenario.accept(v0);
            int nestLevel = v0.getDeepest();
            if( l >= 0 && l <= nestLevel) {

                ScenarioNestLimitVisitor v1 = new ScenarioNestLimitVisitor(l);
                tempScenario.accept(v1);

                ScenarioToTextVisitor v2 = new ScenarioToTextVisitor();
                tempScenario.accept(v2);
                scenarioDisplay.setText(v2.getResult());
            } else {
                throw new Exception();
            }
        } catch(Exception exc) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Wrong nesting limit! Please, enter a valid nesting limit number!");
            alert.showAndWait();
        }
    }

    @Override
    public void start(Stage primaryStage) {

        HBox root = new HBox();
        root.setSpacing(5);
        root.setPadding(new Insets(10,10,10,10));
        VBox menu = new VBox();
        menu.setSpacing(20);
        menu.setMinWidth(210);
        menu.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;"
                + "-fx-border-width: 2;" + "-fx-border-insets: 5;"
                + "-fx-border-radius: 5;"+ "-fx-border-color: grey;");


        Button chooseFile = new Button("Read scenario from file");
        chooseFile.setOnAction(e->readScenarioFromFile(primaryStage));
        chooseFile.setMaxWidth(174);
        menu.getChildren().add(chooseFile);

        menu.getChildren().add(stepCount);
        menu.getChildren().add(keywordCount);

        VBox nestLimitBox = new VBox();
        nestLimitBox.setSpacing(5);
        Text nestLimitLabel = new Text("Maximal nesting level:");
        TextField nestLimitInput = new TextField();
        nestLimitInput.setMaxWidth(174);
        Button nestLimit = new Button("Limit the nesting level");
        nestLimit.setMaxWidth(174);
        nestLimit.setOnAction(e->{
            int l = Integer.parseInt(nestLimitInput.getText());
            setNestLimit(l);
        });

        nestLimitBox.getChildren().add(nestLimitLabel);
        nestLimitBox.getChildren().add(nestLimitInput);
        nestLimitBox.getChildren().add(nestLimit);
        menu.getChildren().add(nestLimitBox);

        Button listSteps = new Button("List incorrect steps");
        listSteps.setMaxWidth(174);
        listSteps.setOnAction(e->{
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Incorrect Steps");
            alert.setHeaderText(null);

            ScenarioIncorrectStepListVisitor v = new ScenarioIncorrectStepListVisitor();
            processedScenario.accept(v);
            ArrayList<String> result = v.getIncorrectSteps();
            StringBuilder builder = new StringBuilder();
            for(int i = 0; i < result.size(); i++) {
                builder.append(String.valueOf(i+1) + ". " + result.get(i) + "\n");
            }

            alert.setContentText(builder.toString());
            alert.showAndWait();
        });
        menu.getChildren().add(listSteps);

        VBox scenarioTextVbox = new VBox();
        scenarioTextVbox.prefWidthProperty().bind(primaryStage.widthProperty());
        Text loadedScenario = new Text("Loaded scenario: ");
        loadedScenario.setUnderline(true);
        scenarioTextVbox.getChildren().add(loadedScenario);
        scenarioTextVbox.getChildren().add(scenarioDisplay);
        scenarioTextVbox.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;"
                + "-fx-border-width: 2;" + "-fx-border-insets: 5;"
                + "-fx-border-radius: 5;"+ "-fx-border-color: grey;");

        root.getChildren().add(menu);
        root.getChildren().add(scenarioTextVbox);

        Scene mainScene = new Scene(root, 1024, 512);
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }
}
