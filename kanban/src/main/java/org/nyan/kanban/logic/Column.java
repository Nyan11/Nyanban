package org.nyan.kanban.logic;

public class Column extends TicketList {
	private static final long serialVersionUID = -7224129647922140599L;
	private Board board;
	private String column_name;
	private int column_size;
	
	public Column(Board board, String name, int size) {
		super();
		this.board = board;
		this.column_name = name;
		this.column_size = size;
	}

	public boolean isFull() {
		return this.size() > this.column_size;
	}
	
	public Board getBoard() {
		return board;
	}
	
	public String getColumn_name() {
		return column_name;
	}

	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}

	public int getColumn_size() {
		return column_size;
	}

	public void setColumn_size(int column_size) {
		this.column_size = column_size;
	}
	
	
}
