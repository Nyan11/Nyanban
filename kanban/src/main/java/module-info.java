module org.nyan.kanban {
    requires javafx.controls;
	requires javafx.base;
	requires javafx.graphics;
	requires java.xml;
    exports org.nyan.kanban;
    opens org.nyan.kanban.logic to javafx.base;
}