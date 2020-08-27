package org.nyan.kanban.logic;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

public class FileManager {
	
	public static void quickSaveProject(ProjectManager project_manager) {
		saveProject(project_manager, project_manager.getSaveFile());
	}
	
	public static void saveProject(ProjectManager project_manager, File file) {
		try {
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();

			// root element
			Element root = document.createElement("project");
			document.appendChild(root);
			Attr project_name = document.createAttribute("name");
			project_name.setValue(project_manager.getProject_name());
			root.setAttributeNode(project_name);

			saveArchive(document, project_manager.getArchive(), root);
			saveQueue(document, project_manager.getQueue(), root);
			saveBoard(document, project_manager.getBoard(), root);

			// create the XML file
			//transform the DOM Object to an XML File
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource domSource = new DOMSource(document);
			StreamResult streamResult = new StreamResult(file);

			// If you use
			// StreamResult result = new StreamResult(System.out);
			// the output will be pushed to the standard output ...
			// You can use that for debugging 

			transformer.transform(domSource, streamResult);

			System.out.println("Done creating XML File");
		} catch (ParserConfigurationException | TransformerException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveProjectFromFilePath(ProjectManager project_manager, String filePath) {
		saveProject(project_manager, new File("filePath"));
	}
	
	public static ProjectManager loadProject(File file) {
		try {
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder builder = factory.newDocumentBuilder();
			final Document document = builder.parse(file);
			final Element root = document.getDocumentElement();
			final Element project = (Element) root.getChildNodes();
			Archive archive = loadArchive(project);
			Queue queue = loadQueue(project, archive);
			Board board = loadBoard(project, archive, queue);
			return loadProject(project, archive, board, queue);
		} catch (final ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static ProjectManager loadProjectFromFilePath(String filePath) {
		return loadProject(new File(filePath));
	}

	private static ProjectManager loadProject(Element project, Archive archive, Board board, Queue queue) {
		return new ProjectManager(project.getAttribute("name"), archive, board, queue);
	}

	private static Board loadBoard(Element project, Archive archive, Queue queue) {
		final Element boardElement = (Element) project.getElementsByTagName("board").item(0);
		Board board = new Board(boardElement.getAttribute("name"), archive, queue);
		board.setColumn_list(loadColumnList(board, boardElement));
		return board;

	}

	private static List<Column> loadColumnList(Board board, Element boardElement) {
		List<Column> return_column = new ArrayList<Column>();
		final NodeList columnListElement = boardElement.getElementsByTagName("column");
		final int nbColumns = columnListElement.getLength();
		for(int j = 0; j<nbColumns; j++) {
			return_column.add(loadColumn(board, (Element) columnListElement.item(j)));
		}
		return return_column;
	}

	private static Column loadColumn(Board board, Element columnElement) {
		String name = columnElement.getAttribute("name");
		int total = Integer.parseInt(columnElement.getElementsByTagName("total").item(0).getTextContent());
		Column column = new Column(board, name, total);
		column.addAll(loadTicketList(column, columnElement));
		return column;
	}

	private static Queue loadQueue(Element project, Archive archive) {
		final Element queueElement = (Element) project.getElementsByTagName("queue").item(0);
		Queue queue = new Queue(archive, Integer.parseInt(queueElement.getAttribute("total")));
		queue.addAll(loadTicketList(queue, queueElement));
		return queue;
	}

	private static Archive loadArchive(Element project) {
		final Element archiveElement = (Element) project.getElementsByTagName("archive").item(0);
		Archive archive = new Archive(Integer.parseInt(archiveElement.getAttribute("total")));
		archive.addAll(loadTicketList(archive, archiveElement));
		return archive;
	}

	private static List<Ticket> loadTicketList(TicketList ticketList, Element ticketListElement) {
		List<Ticket> return_tickets = new ArrayList<Ticket>();
		final NodeList tickets = ticketListElement.getElementsByTagName("ticket");
		final int nbTickets = tickets.getLength();
		for(int j = 0; j<nbTickets; j++) {
			final Element ticket = (Element) tickets.item(j);
			return_tickets.add(loadTicket(ticketList, ticket));
		}
		return return_tickets;
	}

	private static Ticket loadTicket(TicketList ticketList, Element ticket) {
		int id = Integer.parseInt(ticket.getAttribute("id"));
		String name = ticket.getElementsByTagName("name").item(0).getTextContent();
		String description = ticket.getElementsByTagName("description").item(0).getTextContent();
		Date create_date = new Date(Long.parseLong(ticket.getElementsByTagName("create_date").item(0).getTextContent()));

		long finish_date_long = Long.parseLong(ticket.getElementsByTagName("create_date").item(0).getTextContent());
		Date finish_date;
		if (finish_date_long == 0) finish_date = null;
		else finish_date = new Date(Long.parseLong(ticket.getElementsByTagName("finish_date").item(0).getTextContent()));

		boolean deleted = Boolean.parseBoolean(ticket.getElementsByTagName("deleted").item(0).getTextContent());

		return new Ticket(ticketList, id, name, description, create_date, finish_date, deleted);
	}

	

	private static void saveBoard(Document document, Board board, Element root) {
		Element boardElement = document.createElement("board");
		root.appendChild(boardElement);
		Attr name = document.createAttribute("name");
		name.setValue(board.getName());
		boardElement.setAttributeNode(name);
		saveColumnList(document, board, boardElement);
	}

	private static void saveColumnList(Document document, Board board, Element boardElement) {
		for (int i = 0; i < board.getColumn_list().size(); i++) {
			saveColumn(document, board.getColumn_list().get(i), boardElement);
		}
	}

	private static void saveColumn(Document document, Column column, Element boardElement) {
		Element columnElement = document.createElement("column");
		boardElement.appendChild(columnElement);
		Attr name = document.createAttribute("name");
		name.setValue(column.getColumn_name());
		columnElement.setAttributeNode(name);
		
		Element totalElement = document.createElement("total");
		totalElement.appendChild(document.createTextNode(Integer.toString(column.getColumn_size())));
		columnElement.appendChild(totalElement);
		
		saveTicketList(document, column, columnElement);
	}

	private static void saveQueue(Document document, Queue queue, Element root) {
		Element queueElement = document.createElement("queue");
		root.appendChild(queueElement);
		Attr total = document.createAttribute("total");
		total.setValue(Integer.toString(queue.getTotal_created()));
		queueElement.setAttributeNode(total);
		saveTicketList(document, queue, queueElement);
	}

	private static void saveArchive(Document document, Archive archive, Element root) {
		Element archiveElement = document.createElement("archive");
		root.appendChild(archiveElement);
		Attr total = document.createAttribute("total");
		total.setValue(Integer.toString(archive.getTotal_finish()));
		archiveElement.setAttributeNode(total);
		saveTicketList(document, archive, archiveElement);
	}

	private static void saveTicketList(Document document, TicketList ticket_list, Element containerElement) {
		for (int i = 0; i < ticket_list.size(); i++) {
			saveTicket(document, ticket_list.get(i), containerElement);
		}
	}

	public static void saveTicket(Document document, Ticket ticket, Element containerElement) {
		// ticket element
		Element ticketElement = document.createElement("ticket");
		containerElement.appendChild(ticketElement);
		// set an attribute to ticket element
		Attr id = document.createAttribute("id");
		id.setValue(String.valueOf(ticket.getId()));
		ticketElement.setAttributeNode(id);
		// set name
		Element nameElement = document.createElement("name");
		nameElement.appendChild(document.createTextNode(ticket.getName()));
		ticketElement.appendChild(nameElement);
		// set description
		Element descriptionElement = document.createElement("description");
		descriptionElement.appendChild(document.createTextNode(ticket.getDescription()));
		ticketElement.appendChild(descriptionElement);
		// set creation_date
		Element createDateElement = document.createElement("create_date");
		createDateElement.appendChild(document.createTextNode(String.valueOf(ticket.getCreate_date().getTime())));
		ticketElement.appendChild(createDateElement);
		// set creation_date
		Element finishDateElement = document.createElement("finish_date");
		Date finish_date = ticket.getFinish_date();
		long date_time;
		if (finish_date == null) date_time = 0;
		else date_time = ticket.getFinish_date().getTime();
		finishDateElement.appendChild(document.createTextNode(String.valueOf(date_time)));
		ticketElement.appendChild(finishDateElement);
		// set deleted element
		Element deletedElement = document.createElement("deleted");
		deletedElement.appendChild(document.createTextNode(String.valueOf(ticket.isDeleted())));
		ticketElement.appendChild(deletedElement);
	}
}