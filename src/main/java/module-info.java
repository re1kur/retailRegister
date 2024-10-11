module retailRegister {

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.postgresql.jdbc;
    requires java.naming;
    requires static lombok;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    opens project.entity to org.hibernate.orm.core;
    opens project;
}