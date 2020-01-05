package pl.put.poznan.scenario.gui;

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
import javafx.scene.layout.StackPane;
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
    Text stepCount = new Text("step count: ");
    Text keywordCount = new Text("keyword count: ");

    void updateDisplay() {
        ScenarioToTextVisitor v1 = new ScenarioToTextVisitor();
        processedScenario.accept(v1);
        scenarioDisplay.setText(v1.getResult());

        ScenarioStepCountVisitor v2 = new ScenarioStepCountVisitor();
        processedScenario.accept(v2);
        stepCount.setText("step count: " + String.valueOf(v2.getResult()));

        ScenarioKeywordCountVisitor v3 = new ScenarioKeywordCountVisitor();
        processedScenario.accept(v3);
        keywordCount.setText("keyword count: " + String.valueOf(v3.getResult()));
    }

    void readScenarioFromFile(Stage stage) {
        File f = fileChooser.showOpenDialog(stage);
        try {
            // read the chosen file into a string
            String s = new String(Files.readAllBytes(Paths.get(f.getPath())));
            processedScenario = new Scenario(new JSONObject(s));
            updateDisplay();
        } catch(Exception exc) {} // TODO(piotr): handle errors
    }

    void setNestLimit(int l) {
        try {
            Scenario tempScenario = new Scenario(processedScenario.toJSON());

            ScenarioNestLimitVisitor v1 = new ScenarioNestLimitVisitor(l);
            tempScenario.accept(v1);

            ScenarioToTextVisitor v2 = new ScenarioToTextVisitor();
            tempScenario.accept(v2);
            scenarioDisplay.setText(v2.getResult());
        } catch(Exception exc) {} // TODO(piotr): handle errors
    }

    @Override
    public void start(Stage primaryStage) {

        HBox root = new HBox();
        root.setSpacing(20);
        VBox menu = new VBox();
        menu.setSpacing(20);

        Button chooseFile = new Button("Read scenario from file");
        chooseFile.setOnAction(e->readScenarioFromFile(primaryStage));
        menu.getChildren().add(chooseFile);

        menu.getChildren().add(stepCount);
        menu.getChildren().add(keywordCount);

        VBox nestLimitBox = new VBox();
        Text nestLimitLabel = new Text("Maximal nesting level:");
        // TODO(piotr): restrict input to valid values
        TextField nestLimitInput = new TextField();
        Button nestLimit = new Button("Limit the nesting level");
        nestLimit.setOnAction(e->{
            int l = Integer.parseInt(nestLimitInput.getText());
            setNestLimit(l);
        });
        nestLimitBox.getChildren().add(nestLimitLabel);
        nestLimitBox.getChildren().add(nestLimitInput);
        nestLimitBox.getChildren().add(nestLimit);
        menu.getChildren().add(nestLimitBox);

        Button listSteps = new Button("List incorrect steps");
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

        root.getChildren().add(menu);
        root.getChildren().add(scenarioDisplay);

        Scene mainScene = new Scene(root, 800, 600);
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }
}
