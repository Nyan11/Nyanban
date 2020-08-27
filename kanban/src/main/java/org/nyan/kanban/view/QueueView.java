package org.nyan.kanban.view;

import org.nyan.kanban.logic.Queue;
import org.nyan.kanban.logic.Ticket;

import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class QueueView extends AbstractTableView {
	
	public QueueView(Queue queue) {
		this.container = queue;
		generateTicketList();
	}

	private void generateTicketList() {
		tableView = new TableView<Ticket>();
		tableView.setMinWidth(650);
		tableView.setPlaceholder(new Label("No tickets in queue"));
		
		selectionModel = tableView.getSelectionModel();
		selectionModel.setSelectionMode(SelectionMode.SINGLE);

		TableColumn<Ticket, String> column0 = new TableColumn<>("Ticket ID");
        column0.setCellValueFactory(new PropertyValueFactory<>("id"));
        column0.setMinWidth(100);
        column0.setMaxWidth(100);
        
        TableColumn<Ticket, String> column1 = new TableColumn<>("Name");
        column1.setCellValueFactory(new PropertyValueFactory<>("name"));
        column1.setMinWidth(300);
        column1.prefWidthProperty().bind(tableView.widthProperty().subtract(350));

        TableColumn<Ticket, String> column2 = new TableColumn<>("Creation Date");
        column2.setCellValueFactory(new PropertyValueFactory<>("create_date"));
        column2.setMinWidth(250);
        column2.setMaxWidth(250);
        
        tableView.getColumns().add(column0);
        tableView.getColumns().add(column1);
        tableView.getColumns().add(column2);

        for (int i = 0; i < container.size(); i++) {
        	tableView.getItems().add(container.get(i));
        }
        
        
        setView(new VBox(tableView));
        VBox.setVgrow(tableView, Priority.ALWAYS);
        VBox.setVgrow(getView(), Priority.ALWAYS);
	}
}
