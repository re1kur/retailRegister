package project.handlers;

import org.hibernate.Session;
import project.entity.ApplicationRights;
import project.entity.Employee;
import project.entity.Enterprise;

public class HibernateRunner {
    public static void main (String[] args) {
        Session session = HibernateUtility.getCurrentSession();
        session.beginTransaction();
        Enterprise object = session.get(Enterprise.class, 1);
        System.out.println(object.getEmployees().toString());
        session.getTransaction().commit();
        session.close();
    }

    private static Employee createEmployee(Enterprise enterprise) {
        return Employee.builder()
                .enterprise(enterprise)
                .firstname("Alexander")
                .lastname("Ginter")
                .email("alexgin@gmail.com")
                .pinCode(123412)
                .position(ApplicationRights.Owner)
                .build();
    }

}
