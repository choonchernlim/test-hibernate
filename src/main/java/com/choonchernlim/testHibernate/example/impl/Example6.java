package com.choonchernlim.testHibernate.example.impl;

import com.choonchernlim.testHibernate.domain.Project;
import com.choonchernlim.testHibernate.domain.ProjectUser;
import com.choonchernlim.testHibernate.domain.User;
import com.choonchernlim.testHibernate.example.Example;
import com.choonchernlim.testHibernate.util.Utils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * One project has 3 users.
 */
@Service
@Transactional
public class Example6 extends Example {

    private static Logger LOGGER = LoggerFactory.getLogger(Example6.class);

    @Autowired
    public Example6(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public static void main(String[] args) {
        Utils.runExample(Example6.class);
    }

    @Override
    public void run() {
        checkAllTablesEmpty();

        final Session session = sessionFactory.getCurrentSession();

        // new project
        final Project newProject = new Project();
        newProject.setName("Project");

        // 1 INSERT statements against DB
        // 1 managed entities in 1st level cache
        session.save(newProject);

        // adding 3 new users and associate them to the project
        // 3 INSERT statements against DB for User
        // 3 INSERT statements against DB for ProjectUser
        // 3 managed entities in 1st level cache for User
        // 3 managed entities in 1st level cache for ProjectUser
        // 1 managed collection in 1st level cache for ProjectUsers
        for (int i = 0; i < 3; ++i) {
            final User newUser = new User();
            newUser.setName("User " + i);

            session.save(newUser);

            final ProjectUser newProjectUser = new ProjectUser();
            newProjectUser.setUser(newUser);
            newProjectUser.setDatetime(LocalDate.now());

            newProject.addProjectUser(newProjectUser);

            session.saveOrUpdate(newProject);
        }

        LOGGER.debug("Syncing against DB so that the session displays latest stats...");

        session.flush();

        displaySessionStats();
    }
}
