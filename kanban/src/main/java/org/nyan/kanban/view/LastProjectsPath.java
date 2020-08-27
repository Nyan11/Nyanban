package org.nyan.kanban.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class LastProjectsPath extends ArrayList<String> {
	private static final long serialVersionUID = 1455713243073090306L;

	/*
	public LastProjectsPath() {
		loadProjectsPath();
	}
	public void addNewPath(String path) {
		if (this.contains(path)) this.remove(path);
		this.add(0, path);
		if (this.size() > 10) this.removeRange(10, this.size() - 1);
	}
	
	
	
	public void loadProjectsPath() {
		try {
			Properties prop = ResourcesPath.PREVIOUS_PROJECTS.readPropertiesFile();
			String projectPath;
			for (int i = 0; i < 10; i++) {
				projectPath = prop.getProperty("project" + i, "");
				if (!projectPath.equals("")) addNewPath(projectPath);
				else return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	*/
}
