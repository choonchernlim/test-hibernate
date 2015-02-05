package com.choonchernlim.testHibernate.example;

import static com.google.common.base.Preconditions.checkArgument;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.CollectionKey;
import org.hibernate.engine.spi.EntityKey;
import org.hibernate.stat.SessionStatistics;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Transactional
public abstract class Example {

    protected final SessionFactory sessionFactory;

    public Example(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public abstract void run();

    /**
     * Ensures all tables do not have any rows. Otherwise, fail it.
     */
    protected void checkAllTablesEmpty() {
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("--- Check to make sure all tables are empty");
        System.out.println("-------------------------------------------------------------------------------------");

        final Session session = sessionFactory.getCurrentSession();

        for (String domain : Arrays.asList("User", "Project", "ProjectUser")) {
            checkArgument(0L == session.createQuery("select count(*) from " + domain).uniqueResult(),
                          domain + " table must be empty");
        }

        System.out.println("-------------------------------------------------------------------------------------");
    }

    /**
     * Displays nicely formatted session stats.
     */
    protected void displaySessionStats() {
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("--- Session Statistics");
        System.out.println("-------------------------------------------------------------------------------------");

        final SessionStatistics stats = sessionFactory.getCurrentSession().getStatistics();

        System.out.println(String.format("Total managed entities in 1st level cache = %d",
                                         stats.getEntityCount()));

        for (final Object o : stats.getEntityKeys()) {
            final EntityKey entityKey = (EntityKey) o;

            System.out.println(String.format("\t-> %s [id=%s]",
                                             entityKey.getEntityName(),
                                             entityKey.getIdentifier()));
        }

        System.out.println(String.format("Total managed collections in 1st level cache = %d",
                                         stats.getCollectionCount()));

        for (final Object o : stats.getCollectionKeys()) {
            final CollectionKey collectionKey = (CollectionKey) o;

            System.out.println(String.format("\t-> %s with role %s",
                                             collectionKey.getKey(),
                                             collectionKey.getRole()));
        }

        System.out.println("-------------------------------------------------------------------------------------");
    }

}
