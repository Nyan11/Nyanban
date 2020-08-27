package org.nyan.kanban.view;

import java.io.File;
import java.util.List;

import org.nyan.kanban.logic.FileManager;
import org.nyan.kanban.logic.ProjectManager;
import org.nyan.kanban.logic.Ticket;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.SplitPane.Divider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

public class GlobalView extends AbstractPrimaryView {

	private ProjectManager projectManager;

	private QueueView queue_view;
	private BoardView board_view;
	private ArchiveView archive_view;
	private TicketView ticket_view;
	private SearchTicketView search_view;
	private MenuBarView menu_view;
	private CreateTicketView create_ticket_view;
	private TabView tab_view;
	private ToolBarView tool_view;

	private ListChangeListener<Ticket> listener;

	private File projectFile;
	private Scene currentScene;
	private Stage primaryStage;

	private SplitPane splitScreen;

	public GlobalView(ProjectManager projectManager, File projectFile, Stage primaryStage, Scene currentScene) {
		this.projectManager = projectManager;

		this.queue_view = new QueueView(projectManager.getQueue());
		this.board_view = new BoardView(projectManager.getBoard(), this);
		this.archive_view = new ArchiveView(projectManager.getArchive());
		this.ticket_view = new TicketView(projectManager, this, queue_view, board_view, archive_view);
		this.search_view = new SearchTicketView(projectManager, this, ticket_view);
		this.menu_view = new MenuBarView();
		this.create_ticket_view = new CreateTicketView(projectManager.getQueue());
		this.tab_view = new TabView(TypeOfView.BOARDS, archive_view, board_view, queue_view, search_view);
		this.tool_view = new ToolBarView();


		this.projectFile = projectFile;
		this.currentScene = currentScene;
		this.primaryStage = primaryStage;

		this.currentScene.getStylesheets().add(ResourcesPath.MAIN_CSS.accessPath());


		this.listener = new ListChangeListener<Ticket>() {
			@Override
			public void onChanged(Change<? extends Ticket> change) {
				if (change.getList().size() >= 1) {
					Platform.runLater(() -> ticket_view.setSelected_ticket(change.getList().get(0)));
					Platform.runLater(() -> update());
				}
			}
		};


		splitScreen = new SplitPane(this.tab_view.getView(), this.ticket_view.getView());


		generateGlobal(TypeOfView.BOARDS);
	}

	public void generateGlobal (TypeOfView currentPane) {
		this.tab_view.setCurrentPane(currentPane);
		this.tab_view.resizeFromStage(1, primaryStage);

		this.tool_view.get(0).setOnAction(value ->  {
			this.create_ticket_view.createTicket(queue_view);
		});
		this.tool_view.get(1).setOnAction(save -> {
			FileManager.saveProject(projectManager, projectFile);
		});
		this.tool_view.get(2).setOnAction(search -> {
			search_view.searchTicket();
			updateSelectedTicketTable(search_view);
			update();
		});

		updateSelectedTicketTable(archive_view);
		updateSelectedTicketTable(queue_view);
		updateSelectedTicketBoard();

		double[] position = splitScreen.getDividerPositions();
		splitScreen.getItems().clear();
		splitScreen = new SplitPane(this.tab_view.getView(), this.ticket_view.getView());
		splitScreen.setDividerPositions(position);
		System.out.println(position);

		BorderPane globalPane = new BorderPane();
		globalPane.setTop(this.menu_view.getView());
		globalPane.setLeft(this.tool_view.getView());
		globalPane.setCenter(splitScreen);

		globalPane.prefHeightProperty().bind(currentScene.heightProperty());
		globalPane.prefWidthProperty().bind(currentScene.widthProperty());
		
		setView(globalPane);
		getView().getStyleClass().add("primary-color");
	}

	public void update() {
		int panIndex = this.tab_view.getSelectionModel().getSelectedIndex();
		if (panIndex == 0) update(TypeOfView.QUEUE);
		else if (panIndex == 1) update(TypeOfView.BOARDS);
		else if (panIndex == 2) update(TypeOfView.ARCHIVE);
		else if (panIndex == 3) update(TypeOfView.SEARCH);

	}

	public void update(TypeOfView type_of_view) {
		generateGlobal(type_of_view);
		currentScene.setRoot(getView());
		primaryStage.setScene(currentScene);
	}

	private void updateSelectedTicketTable(AbstractTableView view) {
		ObservableList<Ticket> selectedItems = view.selectionModel.getSelectedItems();
		selectedItems.removeListener(listener);
		selectedItems.addListener(listener);
	}

	private void updateSelectedTicketBoard() {
		List<Pair<Ticket, VBox>> tickets = board_view.getTickets();
		for (int i = 0; i < tickets.size(); i++) {
			final Ticket ticket = tickets.get(i).getKey();
			tickets.get(i).getValue().setOnMouseClicked(event -> {
				ticket_view.setSelected_ticket(ticket);
				update();
			});
		}
	}
}
