package org.nyan.kanban.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProjectManager {
	private String project_name;
	private Board board;
	private Archive archive;
	private Queue queue;
	private File saveFile;
	
	public ProjectManager(String project_name, File saveFile) {
		this.project_name = project_name;
		this.board = createBoard(project_name);
		this.archive = new Archive();
		this.queue = new Queue(this.archive);
		this.saveFile = saveFile;
	}
	
	public ProjectManager(String project_name, Archive archive, Board board, Queue queue) {
		this.project_name = project_name;
		this.archive = archive;
		this.board = board;
		this.queue = queue;
	}
	
	public Board createBoard(String name) {
		Board board = new Board(name, this.archive, this.queue);
		board.setColumn_list(generateColumnList(board));
		return board;
	}
	
	public void changeBoard(String name, List<Column> column_list) {
		this.board = new Board(name, this.archive, this.queue);
		List<Column> final_column_list = new ArrayList();
		for (int i = 0; i < column_list.size(); i++) {
			final_column_list.add(new Column(board, column_list.get(i).getColumn_name(), column_list.get(i).getColumn_size()));
		}
		this.board.setColumn_list(column_list);
	}
	
	public void deleteBoard(int id) throws KanbanLogicInfoException {
		if (this.board.containsTicket()) this.board = null;
		else throw new KanbanLogicInfoException("Board is not empty");
		
	}
	
	public Board getBoard() {
		return this.board;
	}
	
	public List<Column> generateColumnList(Board board) {
		List<Column> column_list = new ArrayList<Column>();
		column_list.add(new Column(board, "TODO", 5));
		column_list.add(new Column(board, "IN PROGRESS", 5));
		column_list.add(new Column(board, "DONE", 5));
		return column_list;
	}

	public String getProject_name() {
		return project_name;
	}

	public Archive getArchive() {
		return archive;
	}

	public Queue getQueue() {
		return queue;
	}
	
	public File getSaveFile() {
		return saveFile;
	}
	
	
}
