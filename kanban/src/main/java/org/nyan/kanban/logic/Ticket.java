package org.nyan.kanban.logic;

import java.util.Date;

public class Ticket {
	private int id;
	private String name;
	private String description;
	private Date create_date;
	private Date finish_date;
	private boolean deleted;
	private TicketList container;
	
	public Ticket(TicketList container, int id, String name, String description) {
		this.container = container;
		this.id = id;
		this.name = name;
		this.description = description;
		this.create_date = new Date();
		this.deleted = false;
	}
	
	public Ticket(TicketList container, int id, String name, String description, Date create_date, Date finish_date, boolean deleted) {
		this.container = container;
		this.id = id;
		this.name = name;
		this.description = description;
		this.create_date = create_date;
		this.finish_date = finish_date;
		this.deleted = deleted;
	}
	
	public TicketList getContainer() {
		return container;
	}
	
	public void setContainer(TicketList container) {
		this.container = container;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public Date getFinish_date() {
		return finish_date;
	}

	public void setFinish_date(Date finish_date) {
		this.finish_date = finish_date;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
}
