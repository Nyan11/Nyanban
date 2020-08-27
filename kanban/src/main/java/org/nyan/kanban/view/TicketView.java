package org.nyan.kanban.view;

import org.nyan.kanban.logic.Archive;
import org.nyan.kanban.logic.Column;
import org.nyan.kanban.logic.KanbanLogicFatalException;
import org.nyan.kanban.logic.KanbanLogicInfoException;
import org.nyan.kanban.logic.ProjectManager;
import org.nyan.kanban.logic.Queue;
import org.nyan.kanban.logic.Ticket;
import org.nyan.kanban.logic.TicketList;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class TicketView extends AbstractPrimaryView {
	private final static int buttonSize = 30;

	private ProjectManager project;
	private Ticket ticket;

	private TextField name;
	private TextArea description;
	private SplitPane splitButton;

	private GlobalView global_view;
	private QueueView queue_view;
	private BoardView board_view;
	private ArchiveView archive_view;

	private TypeOfView type_of_view;

	public TicketView(ProjectManager pm, GlobalView gv, QueueView qv, BoardView bv, ArchiveView av) {
		this.ticket = null;
		this.global_view = gv;
		this.archive_view = av;
		this.board_view = bv;
		this.queue_view = qv;
		this.project = pm;
		splitButton = new SplitPane();
		name = new TextField();
		description = new TextArea();
		description.setWrapText(true);
		generateTicketView();
	}

	private void generateTicketView() {
		if (ticket == null) {
			setView(new Pane(new Label("No ticket selectionned")));
			return;
		}

		boolean editable;
		if (type_of_view == TypeOfView.QUEUE || type_of_view == TypeOfView.BOARDS) editable = true;
		else editable = false;

		name.setText(ticket.getName());
		name.setEditable(editable);
		description.setText(ticket.getDescription());
		description.setEditable(editable);

		GridPane grid = generateGridPaneTemplate();
		grid.setPadding(new Insets(0, 10, 0, 10));
		RowConstraints row1 = new RowConstraints(50);
		RowConstraints row2 = new RowConstraints(50);
		RowConstraints row3 = new RowConstraints(20);
		RowConstraints row4 = new RowConstraints(20);
		RowConstraints row5 = new RowConstraints(200, 1000, Double.MAX_VALUE);
		row1.setVgrow(Priority.ALWAYS);
		grid.getRowConstraints().addAll(row1, row2, row3, row4, row5);

		// Container info in column 1, row 1
		Label container = getContainerName();
		grid.add(container, 0, 0);

		// Ticket id in column 2, row 1
		Label id = new Label("Ticket [" + ticket.getId() + "]");
		GridPane.setHalignment(id, HPos.CENTER);
		grid.add(id, 1, 0);

		// Deleted tag in column 3, row 1
		Label deleted = getDeletedProperty();
		grid.add(deleted, 2, 0);

		// Date info grid in column 1-3 row 2
		GridPane mainDateGrid = generateDateBox();
		grid.add(mainDateGrid, 0, 1, 3, 1);

		// Label name in column 1 row 3
		Label labelName = new Label("Name :");
		grid.add(labelName, 0, 2);

		// Name input in column 2 row 3
		grid.add(name, 1, 2, 2, 1);

		// Label description in column 1-3 row 4
		Label labelDecription = new Label("Description");
		GridPane.setHalignment(id, HPos.CENTER);
		grid.add(labelDecription, 0, 3, 3, 1);

		// Description and buttons in column 1-3 row 5
		if (this.type_of_view == TypeOfView.ARCHIVE) {
			grid.add(description, 0, 4, 3, 1);
		}
		else {
			grid.add(generateButtonBox(), 0, 4, 3, 1);
		}
		setView(grid);
		getView().getStyleClass().add("primary-color");
	}

	private GridPane generateGridPaneTemplate() {
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);

		ColumnConstraints column1 = new ColumnConstraints(100);
		ColumnConstraints column2 = new ColumnConstraints(100, 100, Double.MAX_VALUE);
		ColumnConstraints column3 = new ColumnConstraints(100);
		column2.setHgrow(Priority.ALWAYS);
		grid.getColumnConstraints().addAll(column1, column2, column3);
		return grid;
	}

	private SplitPane generateButtonBox() {
		VBox buttonTop = new VBox();
		buttonTop.setSpacing(5);
		buttonTop.setPadding(new Insets(10, 0, 10, 0));
		VBox buttonBot = new VBox();
		buttonBot.setSpacing(5);
		buttonBot.setPadding(new Insets(10, 0, 10, 0));

		BorderPane buttonPanel = new BorderPane();
		buttonPanel.setCenter(buttonTop);
		buttonPanel.setBottom(buttonBot);
		buttonPanel.setPadding(new Insets(10, 0, 10, 0));
		buttonPanel.getStyleClass().add("primary-color");

		if (this.type_of_view == TypeOfView.QUEUE) {
			Button edit = editButton(TypeOfView.QUEUE);
			edit.setPrefSize(Double.MAX_VALUE, buttonSize);
			edit.setMinHeight(buttonSize);
			Button start = new Button("Start ticket");
			start.setPrefSize(Double.MAX_VALUE, buttonSize);
			start.setMinHeight(buttonSize);
			Button delete = new Button("Delete ticket");
			delete.setPrefSize(Double.MAX_VALUE, buttonSize);
			delete.setMinHeight(buttonSize);

			buttonTop.getChildren().addAll(edit, start);
			buttonBot.getChildren().add(delete);

			start.setOnAction(startTicket -> {
				project.getBoard().startTicket(this.ticket.getId());
				this.queue_view.update();
				this.board_view.update();
				this.global_view.update(TypeOfView.BOARDS);
			});
			delete.setOnAction(deleteTicket ->  {
				generatePopUp("Are you sure you want to delete this ticket ?", TypeOfAction.DELETE);
			});
		}
		else if (this.type_of_view == TypeOfView.BOARDS) {
			Button edit = editButton(TypeOfView.BOARDS);
			edit.setPrefSize(Double.MAX_VALUE, buttonSize);
			edit.setMinHeight(buttonSize);
			ChoiceBox<String> columnChoice = new ChoiceBox<String>();
			columnChoice.setPrefSize(Double.MAX_VALUE, buttonSize);
			columnChoice.setMinHeight(buttonSize);
			Button move = new Button("Move");
			move.setPrefSize(100, buttonSize);
			move.setMinHeight(buttonSize);
			move.setPadding(new Insets(0, 10, 0, 0));
			Button send = new Button("Send to queue");
			send.setPrefSize(Double.MAX_VALUE, buttonSize);
			send.setMinHeight(buttonSize);
			Button finish = new Button("Archive ticket");
			finish.setPrefSize(Double.MAX_VALUE, buttonSize);
			finish.setMinHeight(buttonSize);

			BorderPane movePanel = new BorderPane();
			movePanel.setLeft(move);
			movePanel.setCenter(columnChoice);
			buttonTop.getChildren().add(edit);
			buttonTop.getChildren().add(movePanel);
			buttonBot.getChildren().add(finish);
			buttonBot.getChildren().add(send);

			for (int i = 0; i < project.getBoard().getColumn_list().size(); i++) {
				columnChoice.getItems().add(project.getBoard().getColumn_list().get(i).getColumn_name());
			}
			int index_column_choice = project.getBoard().findColumnPosition(((Column) this.ticket.getContainer()).getColumn_name());
			if (index_column_choice < project.getBoard().getColumn_list().size() - 1) index_column_choice++;
			columnChoice.getSelectionModel().select(index_column_choice);

			move.setOnAction(moveColumn -> {
				String value = (String) columnChoice.getValue();
				project.getBoard().moveTicket(ticket.getId(), project.getBoard().findColumnPosition(value));
				this.board_view.update();
				update();
				global_view.update(TypeOfView.BOARDS);
			});

			send.setOnAction(moveQueue -> {
				project.getBoard().returnTicketToQueue(this.ticket.getId());
				int buffer_ticket_id = getSelected_ticket().getId();
				setSelected_ticket(null);
				this.board_view.update();
				this.queue_view.update();
				try {
					setSelected_ticket(project.getQueue().findTicket(buffer_ticket_id));
				} catch (KanbanLogicInfoException | KanbanLogicFatalException e) {
					e.printStackTrace();
				}
				this.global_view.update(TypeOfView.QUEUE);
			});

			finish.setOnAction(deleteTicket ->  {
				generatePopUp("Are you sure you want to archive this ticket ?", TypeOfAction.FINISH);
			});
		}

		double[] position = {0.5};
		if (splitButton.getItems().size() > 0) {
			position = splitButton.getDividerPositions();
			splitButton.getItems().clear();
		}
		splitButton = new SplitPane(description, buttonPanel);
		splitButton.setDividerPositions(position);
		splitButton.setOrientation(Orientation.VERTICAL);

		return splitButton;
	}

	private GridPane generateDateBox() {
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		ColumnConstraints column1 = new ColumnConstraints(100);
		ColumnConstraints column2 = new ColumnConstraints(100, 100, Double.MAX_VALUE);
		column2.setHgrow(Priority.ALWAYS);
		grid.getColumnConstraints().addAll(column1, column2);

		Label createDate = new Label(ticket.getCreate_date().toLocaleString());
		Label labelCreation = new Label("Creation :");

		grid.add(labelCreation, 0, 0);
		grid.add(createDate, 1, 0);

		if (ticket.getContainer().getClass() == Archive.class) {
			Label finishDate = new Label(ticket.getFinish_date().toLocaleString());
			Label labelCompletion = new Label("Completion :");
			grid.add(labelCompletion, 0, 1);
			grid.add(finishDate, 1, 1);
		}
		return grid;
	}

	private Label getDeletedProperty() {
		Label deletedLabel = new Label();
		if (ticket.isDeleted()) deletedLabel.setText("Deleted");
		return deletedLabel;
	}

	private Label getContainerName() {
		Class<? extends TicketList> containerClass = ticket.getContainer().getClass();
		Label containerLabel = new Label();
		if (containerClass == Queue.class) containerLabel.setText("[QUEUE]");
		else if (containerClass == Archive.class) containerLabel.setText("[ARCHIVE]");
		else if (containerClass == Column.class) containerLabel.setText("[BOARD]\n" + 
				((Column) ticket.getContainer()).getBoard().getName() +
				"\n" +
				((Column) ticket.getContainer()).getColumn_name());

		return containerLabel;
	}

	private Button editButton(TypeOfView type_of_view) {
		Button edit_button = new Button("Edit ticket");
		edit_button.setOnAction(editTicket ->  {
			this.ticket.setName(this.name.getText());
			this.ticket.setDescription(this.description.getText());
			if (type_of_view == TypeOfView.QUEUE) this.queue_view.update();
			else if (type_of_view == TypeOfView.BOARDS) this.board_view.update();
		});

		return edit_button;
	}

	private void generatePopUp(String message, TypeOfAction type_of_action) {
		Label message_label = new Label(message);
		Button yes = new Button("Yes");
		Button no = new Button("No");
		HBox buttons = new HBox(yes, no);
		VBox main = new VBox(message_label, buttons);
		Scene scene = new Scene(main);
		scene.getStylesheets().add(ResourcesPath.MAIN_CSS.accessPath());
		Stage secondaryStage = new Stage();
		secondaryStage.setScene(scene);
		secondaryStage.show();
		yes.setOnAction(yesButton ->  {
			secondaryStage.close();
			if (type_of_action == TypeOfAction.DELETE) {
				project.getQueue().deleteTicket(this.getSelected_ticket().getId());
				queue_view.update();
				archive_view.update();
			}
			else if (type_of_action == TypeOfAction.FINISH) {

				project.getBoard().endTicket(this.ticket.getId());
				setSelected_ticket(null);
				board_view.update();
				archive_view.update();
			}
		});
		no.setOnAction(noButton ->  {
			secondaryStage.close();
		});
	}

	public Ticket getSelected_ticket() {
		return ticket;
	}
	public void setSelected_ticket(Ticket selected_ticket) {
		this.ticket = selected_ticket;
		TicketList containerClass = selected_ticket.getContainer();
		if (containerClass.getClass() == Archive.class) setType_of_view(TypeOfView.ARCHIVE);
		else if (containerClass.getClass() == Column.class) setType_of_view(TypeOfView.BOARDS);
		else if (containerClass.getClass() == Queue.class) setType_of_view(TypeOfView.QUEUE);
	}
	public void update() {
		generateTicketView();
	}
	public TypeOfView getType_of_view() {
		return type_of_view;
	}
	public void setType_of_view(TypeOfView type_of_view) {
		this.type_of_view = type_of_view;
		generateTicketView();
	}

}
