package org.nyan.kanban.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class TicketList extends ArrayList<Ticket>{
	
	private static final long serialVersionUID = 1L;

	public void addTicket(Ticket ticket) {
		try {
			findTicket(ticket.getId());
			throw new KanbanLogicInfoException("Ticket already exist");
		} catch (KanbanLogicFatalException e) {
			e.printStackTrace();
		} catch (KanbanLogicInfoException e) {
			this.add(ticket);
			ticket.setContainer(this);
		}
	}
	
	public Ticket findTicket(int id) throws KanbanLogicInfoException, KanbanLogicFatalException {
		List<Ticket> ticket = this.stream().filter(x -> x.getId() == id).collect(Collectors.toList());
		if (ticket.size() == 0) throw new KanbanLogicInfoException("Ticket don't exist");
		else if (ticket.size() >= 2) throw new KanbanLogicFatalException("Ticket duplicated :" + ticket);
		else return ticket.get(0);
	}
	
	public Ticket removeTicket(int id) {
		try {
			findTicket(id);
			for (int i = 0; i < this.size(); i++) {
				if (this.get(i).getId() == id) return this.remove(i);
			}
		} catch (KanbanLogicInfoException e) {
			e.printStackTrace();
		} catch (KanbanLogicFatalException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Ticket> searchTicket(String search) {
		List<Ticket> ticket_list = this.stream().filter(
				x -> x.getName().toLowerCase().contains(search.toLowerCase()) ||
				x.getDescription().toLowerCase().contains(search.toLowerCase()) ||
				Integer.toString(x.getId()).contains(search) ||
				((x.getContainer().getClass() == Queue.class) ? "queue".contains(search.toLowerCase()) : false) ||
				((x.getContainer().getClass() == Archive.class) ? "archive".contains(search.toLowerCase()) : false) ||
				((x.getContainer().getClass() == Column.class) ? "board".contains(search.toLowerCase()) : false) ||
				((x.getContainer().getClass() == Column.class) ? ((Column) x.getContainer()).getColumn_name().toLowerCase().contains(search.toLowerCase()) : false) 
			).collect(Collectors.toList());
		return ticket_list;
	}

	@Override
	public String toString() {
		String string = "---";
		if (this.getClass() == Archive.class) string = "Archive";
		else if (this.getClass() == Queue.class) string = "Queue";
		else if (this.getClass() == Column.class) string = ((Column) this).getBoard().getName() + " - " + ((Column) this).getColumn_name();
		return string;
	}
}
