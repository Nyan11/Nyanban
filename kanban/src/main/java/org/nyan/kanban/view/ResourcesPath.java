package org.nyan.kanban.view;

public enum ResourcesPath {

	CREATE_ICON ("/org/nyan/kanban/view/create-icon.png"),
	SAVE_ICON ("/org/nyan/kanban/view/save-icon.png"),
	SEARCH_ICON ("/org/nyan/kanban/view/search-icon.png"),
	MAIN_CSS ("/org/nyan/kanban/view/kanban.css");

	private String path = "";

	ResourcesPath(String path) {
		this.path = path;
	}

	public String toString() {
		return path;
	}

	public String accessPath() {
		return ResourcesPath.class.getResource(this.toString()).toExternalForm();
	}
}
