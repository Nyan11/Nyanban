package org.nyan.kanban.view;

import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TabView extends AbstractPrimaryView {

	private TypeOfView currentPane;
	private ArchiveView archiveView;
	private BoardView boardView;
	private QueueView queueView;
	private SearchTicketView searchView;
	
	private SingleSelectionModel<Tab> selectionModel;
	
	public TabView(TypeOfView currentPane, ArchiveView archiveView, BoardView boardView, QueueView queueView, SearchTicketView searchView) {
		this.currentPane = currentPane;
		this.archiveView = archiveView;
		this.boardView = boardView;
		this.queueView = queueView;
		this.searchView = searchView;
		ganerateView();
	}
	
	private void ganerateView() {
		TabPane tabPane = new TabPane();
		tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		tabPane.setMinSize(600, 400);

		Tab tab1 = new Tab("Queue", queueView.getView());
		Tab tab2 = new Tab("Boards"  , boardView.getView());
		Tab tab3 = new Tab("Archive" , archiveView.getView());
		Tab tab4 = new Tab("Search Ticket" , searchView.getView());

		tabPane.getTabs().add(tab1);
		tabPane.getTabs().add(tab2);
		tabPane.getTabs().add(tab3);
		tabPane.getTabs().add(tab4);

		selectionModel = tabPane.getSelectionModel();
		if (currentPane == TypeOfView.QUEUE) selectionModel.select(0);
		else if (currentPane == TypeOfView.BOARDS) selectionModel.select(1);
		else if (currentPane == TypeOfView.ARCHIVE) selectionModel.select(2);
		else if (currentPane == TypeOfView.SEARCH) selectionModel.select(3);
		
		setView(new VBox(tabPane));
		getView().getStyleClass().add("tab-pane-view");
		archiveView.resizeFromView(1.0, getView());
		boardView.resizeFromView(1.0, getView());
		queueView.resizeFromView(1.0, getView());
		searchView.resizeFromView(1.0, getView());

		
	}
	
	public void update() {
		ganerateView();
		archiveView.update();
		boardView.update();
		queueView.update();
	}

	public SingleSelectionModel<Tab> getSelectionModel() {
		return selectionModel;
	}

	public TypeOfView getCurrentPane() {
		return currentPane;
	}

	public void setCurrentPane(TypeOfView currentPane) {
		this.currentPane = currentPane;
		ganerateView();
	}
}
