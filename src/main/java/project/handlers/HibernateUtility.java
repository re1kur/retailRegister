package project.handlers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import project.entity.Enterprise;

import java.util.logging.Logger;


public class HibernateUtility {
    private static Session currentSession;
    private static SessionFactory sessionFactory;
    private static final Logger logger = Logger
            .getLogger(HibernateUtility.class.getName());

    private static void buildSessionFactory() {
        if (sessionFactory == null) {
            logger.info("SessionFactory is null. Creating a new one.");
            Configuration configuration = new Configuration();
            configuration.configure("/hibernate.cfg.xml");
            configuration.addAnnotatedClass(Enterprise.class);
            HibernateUtility.sessionFactory = configuration.buildSessionFactory();
        }
            HibernateUtility.currentSession = sessionFactory.openSession();
            logger.info("Session factory: " + HibernateUtility.sessionFactory);
            logger.info("Current session: " + HibernateUtility.currentSession.toString());
    }

    public static Session getCurrentSession() {
        if (currentSession == null) {
            logger.info("Current session is null. Creating a new one.");
            buildSessionFactory();
            return currentSession;
        }
        return currentSession;
    }

    public static void closeCurrentSession() {
        if (currentSession != null) {
            currentSession.close();
            logger.info("Current session is closed.");
            return;
        }
        logger.info("There is no current session.");
    }
}
