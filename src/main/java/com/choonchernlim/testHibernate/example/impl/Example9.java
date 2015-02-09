package com.choonchernlim.testHibernate.example.impl;

import com.choonchernlim.testHibernate.domain.Project;
import com.choonchernlim.testHibernate.example.Example;
import com.choonchernlim.testHibernate.util.Utils;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Executing `Example6` where 1 project has 3 users, examining cache when using query.
 */
@Service
@Transactional
public class Example9 extends Example {

    private final Example6 example6;

    @Autowired
    public Example9(SessionFactory sessionFactory, Example6 example6) {
        super(sessionFactory);
        this.example6 = example6;
    }

    public static void main(String[] args) {
        Utils.runExample(Example9.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        example6.run();

        final Session session = sessionFactory.getCurrentSession();

        // evicting all entities from 1st level cache
        session.clear();

        // should be empty
        displaySessionStats();

        // 1 SELECT statement against DB
        checkArgument(session.createQuery("from Project").list().size() == 1);

        // 1 SELECT statement against DB
        checkArgument(session.createQuery("from Project").list().size() == 1);

        // Entities from the query are stored in first level cache
        displaySessionStats();

        // no SELECT statement against DB
        checkNotNull(session.get(Project.class, 1L));

        // NOTE
        // - query cache not enabled by default
        // - when enabled, it's happens on second level cache, not first level cache
    }


}
