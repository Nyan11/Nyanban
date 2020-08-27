package org.nyan.kanban.logic;

import java.util.Date;

public class Archive extends TicketList {
	private static final long serialVersionUID = 1668221892156973596L;
	private int total_finish;
	
	public Archive() {
		super();
		this.total_finish = 0;
	}
	
	public Archive(int total_finish) {
		super();
		this.total_finish = total_finish;
	}
	
	public void finishTicket(Ticket ticket) {
		ticket.setFinish_date(new Date());
		this.addTicket(ticket);
		this.total_finish ++;
	}
	
	public int getTotal_finish() {
		return total_finish;
	}
}
