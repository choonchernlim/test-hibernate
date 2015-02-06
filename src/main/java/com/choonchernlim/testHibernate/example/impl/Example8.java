package com.choonchernlim.testHibernate.example.impl;

import com.choonchernlim.testHibernate.domain.Project;
import com.choonchernlim.testHibernate.example.Example;
import com.choonchernlim.testHibernate.util.Utils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Executing `Example6` where 1 project has 3 users, but deleting all project users with `clear()`
 */
@Service
@Transactional
public class Example8 extends Example {

    private static Logger LOGGER = LoggerFactory.getLogger(Example8.class);

    private final Example6 example6;

    @Autowired
    public Example8(SessionFactory sessionFactory, Example6 example6) {
        super(sessionFactory);
        this.example6 = example6;
    }

    public static void main(String[] args) {
        Utils.runExample(Example8.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        example6.run();

        final Session session = sessionFactory.getCurrentSession();

        Project project = (Project) session.get(Project.class, 1L);

        project.getProjectUsers().clear();

        session.saveOrUpdate(project);

        // so that session stats is accurate
        session.flush();

        // SHOULD not have ProjectUser in 1st level cache
        displaySessionStats();

        // NOTE
        // - very chatty against DB!
    }
}
