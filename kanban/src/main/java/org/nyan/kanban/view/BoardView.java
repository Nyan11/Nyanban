package org.nyan.kanban.view;

import java.util.ArrayList;
import java.util.List;

import org.nyan.kanban.logic.Board;
import org.nyan.kanban.logic.Column;
import org.nyan.kanban.logic.Ticket;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class BoardView extends AbstractPrimaryView {
	private static final String DEFAULT_STYLE_CLASS = "board-view";
	
	private GlobalView globalView;
	
	private List<Pair<Ticket, VBox>> tickets;
	private Board board;
	
	public BoardView(Board board, GlobalView globalView) {
		this.tickets = new ArrayList<Pair<Ticket, VBox>>();
		this.board = board;
		this.globalView = globalView;
		generateView();
	}
	public void update() {
		generateView();
		globalView.update();
	}
	private void generateView() {
		HBox boardBox = new HBox();
		boardBox.setSpacing(10);
		
		Label name = new Label(board.getName());

		for (int i = 0; i < board.getColumn_list().size(); i++) {
			boardBox.getChildren().add(generateColumn(board.getColumn_list().get(i)));
		}
		
		
		setView(new VBox(name, boardBox));
		getView().getStyleClass().add("board-view");
		((VBox) getView()).setAlignment(Pos.TOP_CENTER);
		VBox.setMargin(boardBox, new Insets(10, 10, 10, 10));
	}
	
	private VBox generateColumn(Column column) {
		VBox columnBox = new VBox();
		HBox.setHgrow(columnBox, Priority.ALWAYS);
		columnBox.setSpacing(10);
		columnBox.setAlignment(Pos.TOP_CENTER);
		columnBox.getStyleClass().add("board-column-view");
		
		Label name = new Label(column.getColumn_name());
		Label size = new Label(column.size() + "/" + column.getColumn_size());
		HBox columnHeader = new HBox();
		Separator separator = new Separator(Orientation.HORIZONTAL);
		
		columnHeader.getChildren().add(name);
		columnHeader.getChildren().add(size);
		
		columnBox.getChildren().add(columnHeader);
		columnBox.getChildren().add(separator);
		
		for (int i = 0; i < column.size(); i++) {
			tickets.add(0, new Pair<Ticket, VBox>(column.get(i), generateTicket(column.get(i))));
			columnBox.getChildren().add(tickets.get(0).getValue());
		}
		
		return columnBox;
	}
	
	private VBox generateTicket(Ticket ticket) {
		Label id = new Label("Ticket [" + ticket.getId() + "]");
	    Label name = new Label(ticket.getName());
	    VBox ticketBox = new VBox(id, name);
	    ticketBox.getStyleClass().add("board-ticket-view");
	    VBox.setMargin(ticketBox, new Insets(0 ,5 ,5 ,5));
	    return ticketBox;
	}
	
	public List<Pair<Ticket, VBox>> getTickets() {
		return tickets;
	}
}
