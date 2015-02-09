package com.choonchernlim.testHibernate.example.impl;

import com.choonchernlim.testHibernate.example.Example;
import com.choonchernlim.testHibernate.util.Utils;
import static com.google.common.base.Preconditions.checkArgument;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Executing `Example6` where 1 project has 3 users, but deleting all project users using DML-style operation.
 */
@Service
@Transactional
public class Example8a extends Example {

    private final Example6 example6;

    @Autowired
    public Example8a(SessionFactory sessionFactory, Example6 example6) {
        super(sessionFactory);
        this.example6 = example6;
    }

    public static void main(String[] args) {
        Utils.runExample(Example8a.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        example6.run();

        final Session session = sessionFactory.getCurrentSession();

        // 1 DELETE statement against DB
        final int rowsUpdated = session.createQuery("delete ProjectUser pu where pu.project.id = :projectId")
                .setLong("projectId", 1L)
                .executeUpdate();

        checkArgument(rowsUpdated == 3, "3 rows deleted");

        // ProjectUser entities are not evicted from cache
        displaySessionStats();

        // NOTE
        // - DML-style operation DOES NOT evict entities that are already exist in first level cache!
    }
}
