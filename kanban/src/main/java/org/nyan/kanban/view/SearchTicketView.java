package org.nyan.kanban.view;

import java.util.ArrayList;
import java.util.List;

import org.nyan.kanban.logic.ProjectManager;
import org.nyan.kanban.logic.Ticket;
import org.nyan.kanban.logic.TicketList;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener.Change;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SearchTicketView extends AbstractTableView{

	private ProjectManager project;
	private GlobalView globalView;
	private ListChangeListener<Ticket> listener;
	
	
	
	public SearchTicketView(ProjectManager project, GlobalView globalView, TicketView ticketView) {
		super();
		this.project = project;
		this.globalView = globalView;
		this.listener = new ListChangeListener<Ticket>() {
			@Override
			public void onChanged(Change<? extends Ticket> change) {
				if (change.getList().size() >= 1) {
					Platform.runLater(() -> ticketView.setSelected_ticket(change.getList().get(0)));
					Platform.runLater(() -> globalView.update());
				}
			}
		};
		
		generateSearchTicketView();
	}
	private VBox generateSearchTicketView() {
		VBox main = new VBox();
		
		TextField searchBar = new TextField();
		Button searchButton = new Button("Search");
		Button cancelButton = new Button("Cancel");
		HBox inputTop = new HBox(searchBar, searchButton, cancelButton);
		inputTop.setSpacing(20);
		inputTop.setPadding(new Insets(10, 10, 10, 10));
		inputTop.setAlignment(Pos.CENTER);
		main.getChildren().addAll(inputTop);
		
		Separator separator = new Separator(Orientation.HORIZONTAL);
		main.getChildren().add(separator);
		
		Label searchInLabel = new Label("Search in");
		main.getChildren().add(searchInLabel);
		
		CheckBox boardCheck = new CheckBox("Board");
		boardCheck.setSelected(true);
		CheckBox queueCheck = new CheckBox("Queue");
		queueCheck.setSelected(true);
		CheckBox archiveCheck = new CheckBox("Archive");
		archiveCheck.setSelected(true);
        HBox checkBox = new HBox(boardCheck, queueCheck, archiveCheck);
        main.getChildren().add(checkBox);
        
        HBox columnBox = new HBox();
        CheckBox columnCheck;
        for (int i = 0; i < project.getBoard().getColumn_list().size(); i++) {
        	columnCheck = new CheckBox(project.getBoard().getColumn_list().get(i).getColumn_name());
        	columnCheck.setSelected(true);
        	columnBox.getChildren().add(columnCheck);
        }
        boardCheck.setOnAction(column -> {
        	changeStateOfColumn(boardCheck, columnBox);
        });
        
        checkBox.setSpacing(20);
        checkBox.setPadding(new Insets(10, 10, 10, 10));
        columnBox.setSpacing(20);
        columnBox.setPadding(new Insets(10, 10, 10, 10));
		
        main.getChildren().add(columnBox);
        
        tableView = generateTableView();
        populateTableView(project, archiveCheck, boardCheck, queueCheck, columnBox, "");
        main.getChildren().add(tableView);
        
        searchButton.setOnAction(search ->{
        	populateTableView(project, archiveCheck, boardCheck, queueCheck, columnBox, searchBar.getText());
        });
        
        cancelButton.setOnAction(cancel ->{
        	searchBar.setText("");
        	update();
        });
        
        ObservableList<Ticket> selectedItems = selectionModel.getSelectedItems();
        selectedItems.removeListener(listener);
		selectedItems.addListener(listener);
        
        setView(main);
        VBox.setVgrow(tableView, Priority.ALWAYS);
        VBox.setVgrow(getView(), Priority.ALWAYS);
        return main;
	}
	
	private void populateTableView(ProjectManager project, CheckBox archiveCheck, CheckBox boardCheck,
			CheckBox queueCheck, HBox columnBox, String search) {
		List<Ticket> ticketList = new ArrayList<Ticket>();
		if (archiveCheck.isSelected()) ticketList.addAll(searchTicket(project.getArchive(), search));
		if (queueCheck.isSelected()) ticketList.addAll(searchTicket(project.getQueue(), search));
		if (boardCheck.isSelected()) {
			for (int i = 0; i < project.getBoard().getColumn_list().size(); i++) {
				if (((CheckBox) columnBox.getChildren().get(i)).isSelected()) {
					ticketList.addAll(searchTicket(project.getBoard().getColumn_list().get(i), search));
				}
			}
		}
		if (tableView.getItems().size() > 0) tableView.getItems().clear();
		tableView.getItems().addAll(ticketList);
	}
	
	private List<Ticket> searchTicket(TicketList container, String search) {
		if (search.contentEquals("")) return container;
		else return container.searchTicket(search);
	}

	private TableView<Ticket> generateTableView() {
		TableView<Ticket> tableView = new TableView<Ticket>();
		tableView.setPlaceholder(new Label("No tickets in queue"));
		
		selectionModel = tableView.getSelectionModel();
		selectionModel.setSelectionMode(SelectionMode.SINGLE);

		TableColumn<Ticket, String> column0 = new TableColumn<>("ID");
        column0.setCellValueFactory(new PropertyValueFactory<>("id"));
        column0.setMinWidth(30);
        column0.setMaxWidth(30);
        
        TableColumn<Ticket, String> column1 = new TableColumn<>("Name");
        column1.setCellValueFactory(new PropertyValueFactory<>("name"));
        column1.setMinWidth(180);
        column1.prefWidthProperty().bind(tableView.widthProperty().subtract(250).multiply(0.75));

        TableColumn<Ticket, String> column2 = new TableColumn<>("Creation Date");
        column2.setCellValueFactory(new PropertyValueFactory<>("create_date"));
        column2.setMinWidth(220);
        column2.setMaxWidth(220);
        
        TableColumn<Ticket, String> column3 = new TableColumn<>("Place");
        column3.setCellValueFactory(new PropertyValueFactory<>("container"));
        column3.setMinWidth(100);
        column3.prefWidthProperty().bind(tableView.widthProperty().subtract(250).multiply(0.25));
        
        tableView.getColumns().add(column0);
        tableView.getColumns().add(column1);
        tableView.getColumns().add(column2);
        tableView.getColumns().add(column3);
		return tableView;
	}

	private void changeStateOfColumn(CheckBox boardCheck, HBox columnBox) {
		boolean disable = false;
		if (boardCheck.isSelected()) disable = false;
		else disable = true;
		for (int i = 0; i < columnBox.getChildren().size(); i++) {
			((CheckBox) columnBox.getChildren().get(i)).setSelected(!disable);
			columnBox.getChildren().get(i).setDisable(disable);
		}
		
	}
	
	public void update() {
		generateSearchTicketView();
		globalView.update();
	}
	public void searchTicket() {
		Stage secondaryStage = new Stage();
		Scene searchScene = new Scene(generateSearchTicketView());
		searchScene.getStylesheets().add(ResourcesPath.MAIN_CSS.accessPath());
		secondaryStage.setScene(searchScene);
		secondaryStage.show();
	}
}
