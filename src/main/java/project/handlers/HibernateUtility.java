package project.handlers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import project.entity.Enterprise;

import java.util.logging.Logger;


public class HibernateUtility {
    private static Session currentSession;
    private static SessionFactory sessionFactory;
    private static final Logger logger = Logger
            .getLogger(HibernateUtility.class.getName());

    private static void buildSessionAnnotationFactory() {
            Configuration configuration = new Configuration();
            configuration.configure("/hibernate.cfg.xml");
            configuration.addAnnotatedClass(Enterprise.class);
            SessionFactory sessionFactory = configuration.buildSessionFactory();
            HibernateUtility.currentSession = sessionFactory.openSession();
            HibernateUtility.sessionFactory = sessionFactory;
            logger.info("Session factory: " + HibernateUtility.sessionFactory);
            logger.info("Current session: " + HibernateUtility.currentSession.toString());
    }

    public static Session getCurrentSession() {
        if (currentSession == null) {
            logger.info("Current session is null. Creating a new one.");
            buildSessionAnnotationFactory();
            return currentSession;
        }
        return currentSession;
    }
}
