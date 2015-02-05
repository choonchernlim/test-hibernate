package com.choonchernlim.testHibernate.example.impl;

import com.choonchernlim.testHibernate.domain.Project;
import com.choonchernlim.testHibernate.domain.ProjectUser;
import com.choonchernlim.testHibernate.example.Example;
import com.choonchernlim.testHibernate.util.Utils;
import static com.google.common.base.Preconditions.checkNotNull;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Executing `Example6` where 1 project has 3 users, but querying project users with getter crawling.
 */
@Service
@Transactional
public class Example7 extends Example {

    private static Logger LOGGER = LoggerFactory.getLogger(Example7.class);

    private final Example6 example6;

    @Autowired
    public Example7(SessionFactory sessionFactory, Example6 example6) {
        super(sessionFactory);
        this.example6 = example6;
    }

    public static void main(String[] args) {
        Utils.runExample(Example7.class);
    }

    @Override
    public void run() {
        example6.run();

        final Session session = sessionFactory.getCurrentSession();

        LOGGER.debug("Evicting all managed-entities from 1st level cache...");
        session.clear();

        // SHOULD show nothing in 1st level cache
        displaySessionStats();

        // 1 SELECT statement against DB
        final Project project = (Project) session.get(Project.class, 1L);

        checkNotNull(project, "Project exists");

        // 1 SELECT statement against DB
        for (ProjectUser projectUser : project.getProjectUsers()) {
            LOGGER.debug("Project [{}] has project user [{}]", project.getName(), projectUser.getUser().getName());
        }

        // everything is back (Project, User, ProjectUser) in 1st level cache
        displaySessionStats();

    }
}
