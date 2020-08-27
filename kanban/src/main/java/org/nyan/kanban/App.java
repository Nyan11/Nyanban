package org.nyan.kanban;

import org.nyan.kanban.view.InitView;
import org.nyan.kanban.view.LastProjectsPath;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
    	LastProjectsPath lp = new LastProjectsPath();
    	InitView init = new InitView();
    	Scene scene = new Scene(init.generateInitView(primaryStage));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}