package org.nyan.kanban.view;

import org.nyan.kanban.logic.Archive;
import org.nyan.kanban.logic.Ticket;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ArchiveView extends AbstractTableView {
	
	public ArchiveView(Archive archive) {
		this.container = archive;
		generateTicketList();
	}
	
	private void generateTicketList() {
		tableView = new TableView<Ticket>();
		tableView.setMinWidth(650);
		tableView.setPlaceholder(new Label("No tickets in archive"));
		
		selectionModel = tableView.getSelectionModel();
		selectionModel.setSelectionMode(SelectionMode.SINGLE);

		TableColumn<Ticket, String> column0 = new TableColumn<>("Ticket ID");
        column0.setCellValueFactory(new PropertyValueFactory<>("id"));
        column0.setMinWidth(100);
        column0.setMaxWidth(100);
        
        TableColumn<Ticket, String> column1 = new TableColumn<>("Name");
        column1.setCellValueFactory(new PropertyValueFactory<>("name"));
        column1.setMinWidth(200);
        column1.prefWidthProperty().bind(tableView.widthProperty().subtract(450));

        TableColumn<Ticket, String> column2 = new TableColumn<>("Finish Date");
        column2.setCellValueFactory(new PropertyValueFactory<>("finish_date"));
        column2.setMinWidth(250);
        column2.setMaxWidth(250);
        
        TableColumn<Ticket, String> column3 = new TableColumn<>("Deleted");
        column3.setCellValueFactory(new PropertyValueFactory<>("deleted"));
        column3.setMinWidth(100);
        column3.setMaxWidth(100);
        
        tableView.getColumns().add(column0);
        tableView.getColumns().add(column1);
        tableView.getColumns().add(column2);
        tableView.getColumns().add(column3);

        for (int i = 0; i < container.size(); i++) {
        	tableView.getItems().add(container.get(i));
        }

        setView(new VBox(tableView));
        VBox.setVgrow(tableView, Priority.ALWAYS);
        VBox.setVgrow(getView(), Priority.ALWAYS);
	}
}
