package org.nyan.kanban.logic;

import java.util.List;

public class Board {
	private String name;
	private List<Column> column_list;
	private Archive archive;
	private Queue queue;
	
	public Board(String name, Archive archive, Queue queue) {
		this.name = name;
		this.column_list = null;
		this.archive = archive;
		this.queue = queue;
	}
	
	public void startTicket(int id) {
		this.column_list.get(0).addTicket(this.queue.removeTicket(id));
	}
	
	public void returnTicketToQueue(int id) {
		try {
			this.queue.addTicket(this.removeTicketInColumn(id));
		} catch (KanbanLogicInfoException e) {
			e.printStackTrace();
		}
	}
	
	public void endTicket(int id) {
		try {
			this.archive.finishTicket(removeTicketInColumn(id));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void moveTicket(int id, int position) {
		try {
			this.column_list.get(position).addTicket(removeTicketInColumn(id));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Ticket removeTicketInColumn(int id) throws KanbanLogicInfoException {
		for (int i = 0; i < this.column_list.size(); i++) {
			try {
				if (this.column_list.get(i).findTicket(id) != null) return this.column_list.get(i).removeTicket(id);
			} catch (Exception e) {}
		}
		throw new KanbanLogicInfoException("Ticket not found");
	}
	
	public boolean containsTicket() {
		for (int i = 0; i < this.column_list.size(); i++) {
			if (this.column_list.get(i).size() != 0) return true;
		}
		return false;
	}
	
	public int findColumnPosition(String name) {
		for (int i = 0; i < this.column_list.size(); i++) {
			if (this.column_list.get(i).getColumn_name().compareTo(name) == 0) return i;
		}
		return 0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Column> getColumn_list() {
		return column_list;
	}
	
	public void setColumn_list(List<Column> column_list) {
		this.column_list = column_list;
	}
	
}
