package org.nyan.kanban.view;

import org.nyan.kanban.logic.Queue;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CreateTicketView {
	public Button generate_button;
	public TextField name;
	public TextArea description;
	private Queue queue;
	public CreateTicketView(Queue queue) {
		this.queue = queue;
	}

	public GridPane generateCreateTicketView() {
		GridPane main = new GridPane();
		main.setHgap(10);
		main.setVgap(10);
		
		Text id = new Text("Ticket [" + (queue.getTotal_created() + 1) + "]");
		name = new TextField();
		description = new TextArea();
		
		Label label_name = new Label("Name :");
		Label label_decription = new Label("Description");
		
		generate_button = new Button("Generate");
		
		main.add(id, 1, 0);
		main.add(label_name, 0, 1);
		main.add(name, 1, 1, 2, 1);
		main.add(label_decription, 1, 3);
		main.add(description, 0, 4, 3, 1);
		main.add(generate_button, 0, 5, 3, 1);
		
		return main;
	}
	
	public void createTicket(QueueView queue_view) {
		Scene scene = new Scene(generateCreateTicketView());
		scene.getStylesheets().add(ResourcesPath.MAIN_CSS.accessPath());
		
		Stage secondaryStage = new Stage();
		secondaryStage.setScene(scene);
		secondaryStage.show();
		generate_button.setOnAction(value ->  {
			secondaryStage.close();
			queue.createTicket(name.getText(), description.getText());
			queue_view.update();
		});
	}
}
