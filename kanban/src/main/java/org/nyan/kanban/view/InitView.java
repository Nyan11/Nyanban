package org.nyan.kanban.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.nyan.kanban.logic.Column;
import org.nyan.kanban.logic.FileManager;
import org.nyan.kanban.logic.ProjectManager;

import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class InitView {
	
	private File projectFile;
	private ProjectManager project;
	
	private VBox view;

	public InitView() {
		generateView();
	}
	
	public void generateView() {
		
	}
	public HBox generateInitView(Stage stage) {
		
		Button newButton = new Button("New Project");
		Button loadButton = new Button("Load Project");
		HBox buttonBox = new HBox(newButton, loadButton);
		
		
		loadButton.setOnAction(load -> {
			loadProject(stage);
		});
		
		newButton.setOnAction(create -> {
			newProject(stage);
		});
		return buttonBox;
	}
	
	private void newProject(Stage stage) {
		VBox main = new VBox();
		
		Label projectName = new Label("Project Name :");
		TextField projectNameInput = new TextField();
		HBox name = new HBox(projectName, projectNameInput);
		Separator separator1 = new Separator(Orientation.HORIZONTAL);
		main.getChildren().addAll(name, separator1);
		
		Label projectPath = new Label("Project Path :");
		TextField projectPathInput = new TextField();
		Button projectPathButton = new Button("Choose");
		projectPathButton.setOnAction(path -> {
			projectPathInput.setText(loadPath(stage));
		});
		HBox path = new HBox(projectPath, projectPathInput, projectPathButton);
		Separator separator2 = new Separator(Orientation.HORIZONTAL);
		main.getChildren().addAll(path, separator2);
		
		Button addColumn = new Button("+");
		TextField columnNameInput = new TextField();
		Spinner<Integer> columnMaxInput = new Spinner<Integer>();
		columnMaxInput.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 5));
		HBox columnBox = new HBox(addColumn, columnNameInput, columnMaxInput);
		VBox column = new VBox(columnBox);
		addColumn.setOnAction(refreshColumn -> {
			newProjectColumn(stage, column);
		});
		Separator separator3 = new Separator(Orientation.HORIZONTAL);
		main.getChildren().addAll(column, separator3);
		
		Button create = new Button("Create new project");
		main.getChildren().add(create);
		create.setOnAction(createProject -> {
			createNewProject(projectNameInput, projectPathInput, column);
			accessProject(stage);
		});
		Scene scene = new Scene(main);
		scene.getStylesheets().add(ResourcesPath.MAIN_CSS.accessPath());
		stage.setScene(scene);
	}

	private void createNewProject(TextField projectNameInput, TextField projectPathInput, VBox column) {
		String projectName = projectNameInput.getText();
		this.projectFile = new File(projectPathInput.getText() + "/" + projectName + ".xml");
		this.project = new ProjectManager(projectName, projectFile);
		project.changeBoard(projectName, generateColumnList(column));
	}
	
	private List<Column> generateColumnList(VBox column) {
		List<Column> columnList = new ArrayList<Column>();
		String name;
		int max;
		for (int i = 0; i < column.getChildren().size(); i++) {
			name = ((TextField) ((HBox) column.getChildren().get(i)).getChildren().get(1)).getText();
			max = ((Spinner<Integer>) ((HBox) column.getChildren().get(i)).getChildren().get(2)).getValue();
			columnList.add(new Column(null, name, max));
		}
		return columnList;
	}

	private void newProjectColumn(Stage stage, VBox main) {
		Button addColumn = new Button("+");
		TextField columnNameInput = new TextField();
		Spinner<Integer> columnMaxInput = new Spinner<Integer>();
		columnMaxInput.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 5));
		Button deleteColumn = new Button("delete");
		HBox column = new HBox(addColumn, columnNameInput, columnMaxInput, deleteColumn);
		main.getChildren().add(column);
		
		
		addColumn.setOnAction(makeNewColumn -> {
			newProjectColumn(stage, main);
		});
		
		deleteColumn.setOnAction(delete -> {
			deleteColumn(main, column);
		});
	}

	private void deleteColumn(VBox main, HBox column) {
		for (int i = 0; i < main.getChildren().size(); i++) {
			if (main.getChildren().get(i).equals(column)) {
				main.getChildren().remove(i);
				return;
			}
		}
	}
	
	private void accessProject(Stage stage) {
		Scene scene = new Scene(new Label("Loading ..."));
		GlobalView gv = new GlobalView(project, projectFile, stage, scene);
		
        scene = new Scene(gv.getView());
        scene.getStylesheets().add(ResourcesPath.MAIN_CSS.accessPath());
        stage.setScene(scene);
	}
	
	private String loadPath(Stage stage) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File selectedDirectory = directoryChooser.showDialog(stage);
		System.out.println(selectedDirectory.getAbsolutePath());
		return selectedDirectory.getAbsolutePath();
	}
	
	private void loadProject(Stage stage) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
		projectFile = fileChooser.showOpenDialog(stage);
		project = FileManager.loadProject(projectFile);
		accessProject(stage);
	}
}
