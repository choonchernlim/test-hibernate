package com.choonchernlim.testHibernate.example.impl;

import com.choonchernlim.testHibernate.domain.Project;
import com.choonchernlim.testHibernate.example.Example;
import com.choonchernlim.testHibernate.util.Utils;
import static com.google.common.base.Preconditions.checkArgument;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.EntityKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Executing `Example6` where 1 project has 3 users, but deleting all project users using DML-style operation and handling first level cache properly.
 */
@Service
@Transactional
public class Example8c extends Example {

    private final Example6 example6;

    @Autowired
    public Example8c(SessionFactory sessionFactory, Example6 example6) {
        super(sessionFactory);
        this.example6 = example6;
    }

    public static void main(String[] args) {
        Utils.runExample(Example8c.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        example6.run();

        final Session session = sessionFactory.getCurrentSession();

        final Long projectId = 1L;

        final boolean inFirstLevelCache = Iterables.any(session.getStatistics().getEntityKeys(), new Predicate() {
            @Override
            public boolean apply(Object o) {
                EntityKey entityKey = (EntityKey) o;
                return Project.class.getName().equals(entityKey.getEntityName()) &&
                       projectId.equals(entityKey.getIdentifier());
            }
        });

        if (inFirstLevelCache) {
            Project project = (Project) session.get(Project.class, projectId);
            session.evict(project);
        }

        // 1 DELETE statement against DB
        final int rowsUpdated = session.createQuery("delete ProjectUser pu where pu.project.id = :projectId")
                .setLong("projectId", projectId)
                .executeUpdate();

        checkArgument(rowsUpdated == 3, "3 rows deleted");

        // No Project and ProjectUser entities in first level cache
        displaySessionStats();
    }
}
