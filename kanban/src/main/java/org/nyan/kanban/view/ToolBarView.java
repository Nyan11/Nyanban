package org.nyan.kanban.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class ToolBarView extends ArrayList<Button>{

	private VBox view;
	public ToolBarView() {
		super();
		generateView();
	}
	private void generateView() {
		ToolBar toolBar = new ToolBar();
		toolBar.setMinWidth(60);
		toolBar.setOrientation(Orientation.VERTICAL);
		
		Button newTicket = new Button("New ticket");
		this.add(newTicket);
		newTicket.setPrefSize(50, 50);
		newTicket.setMaxSize(50, 50);
		
		Button saveProject = new Button("Save project");
		this.add(saveProject);
		saveProject.setPrefSize(50, 50);
		saveProject.setMaxSize(50, 50);
		
		Button searchTicket = new Button("Search Ticket");
		this.add(searchTicket);
		searchTicket.setPrefSize(50, 50);
		searchTicket.setMaxSize(50, 50);
		
		FileInputStream input;
		Image image;
		ImageView imageView;
		try {
			input = new FileInputStream("icons/create-icon.png");
			image = new Image(input, 40, 40, true, false);
	        imageView = new ImageView(image);
	        newTicket.setGraphic(imageView);
	        newTicket.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
	        
	        input = new FileInputStream("icons/save-icon.png");
			image = new Image(input, 40, 40, true, false);
	        imageView = new ImageView(image);
	        saveProject.setGraphic(imageView);
	        saveProject.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
	        
	        input = new FileInputStream("icons/search-icon.png");
			image = new Image(input, 40, 40, true, false);
	        imageView = new ImageView(image);
	        searchTicket.setGraphic(imageView);
	        searchTicket.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
		toolBar.getItems().addAll(newTicket, saveProject, searchTicket);
		toolBar.getStyleClass().add("primary-color");
		view = new VBox(toolBar);
		view.getStyleClass().add("primary-color");
	}
	
	public VBox getView() {
		return view;
	}
}
