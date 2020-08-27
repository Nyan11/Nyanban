package org.nyan.kanban.logic;

public class Queue extends TicketList {
	private static final long serialVersionUID = 293645009146434123L;
	private int total_created;
	private Archive archive;
	
	public Queue(Archive archive) {
		super();
		this.archive = archive;
		this.total_created = 0;
	}
	
	public Queue(Archive archive, int total_created) {
		super();
		this.archive = archive;
		this.total_created = total_created;
	}
	
	public void createTicket(String name, String description) {
		this.total_created ++;
		this.add(new Ticket(this, total_created, name, description));
	}
	
	public void deleteTicket(int id) {
		Ticket deleted_ticket = this.removeTicket(id);
		deleted_ticket.setDeleted(true);
		archive.finishTicket(deleted_ticket);
	}
	
	public int getTotal_created() {
		return total_created;
	}
}
