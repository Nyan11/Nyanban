package org.nyan.kanban.view;

import org.nyan.kanban.logic.Ticket;
import org.nyan.kanban.logic.TicketList;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;

public abstract class AbstractTableView extends AbstractPrimaryView {
	public TableViewSelectionModel<Ticket> selectionModel;
	
	protected TableView<Ticket> tableView;
	protected TicketList container;

	public void update() {
		ObservableList<Ticket> ticket_list = this.tableView.getItems();
		ticket_list.clear();
		for (int i = 0; i < container.size(); i++) {
        	tableView.getItems().add(container.get(i));
        }
		resizeTableView();
	}
	
	public void resizeTableView() {
		tableView.prefHeightProperty().bind(getView().heightProperty());
		tableView.prefWidthProperty().bind(getView().widthProperty());
	}
}
