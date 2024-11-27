module retailRegister {

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.postgresql.jdbc;
    requires java.naming;
    requires static lombok;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    opens project.controllers to javafx.controls, javafx.fxml, javafx.graphics, jakarta.persistence, org.hibernate.orm.core;
    opens project.entity to org.hibernate.orm.core;
    opens project.handlers to org.hibernate.orm.core, jakarta.persistence;
    opens project;
    opens project.other to org.hibernate.orm.core;

}