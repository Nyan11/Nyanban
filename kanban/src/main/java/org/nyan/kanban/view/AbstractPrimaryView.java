package org.nyan.kanban.view;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public abstract class AbstractPrimaryView {

	private Pane view;
	
	
	public Pane getView() {
		return view;
	}
	
	public void setView(Pane view) {
		this.view = view;
	}
	
	protected void resizeFromStage(double multiply, Stage stage) {
		view.prefWidthProperty().bind(stage.widthProperty().multiply(multiply));
		view.prefHeightProperty().bind(stage.widthProperty().multiply(multiply));
	}
	
	protected void resizeFromView(double multiply, Pane pane) {
		view.prefWidthProperty().bind(pane.widthProperty().multiply(multiply));
		view.prefHeightProperty().bind(pane.widthProperty().multiply(multiply));
	}
}
