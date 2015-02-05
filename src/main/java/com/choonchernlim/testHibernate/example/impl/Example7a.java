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
 * Executing `Example6` where 1 project has 3 users, but instead of using getter methods, performing eager fetch in HQL.
 */
@Service
@Transactional
public class Example7a extends Example {

    private static Logger LOGGER = LoggerFactory.getLogger(Example7a.class);

    private final Example6 example6;

    @Autowired
    public Example7a(SessionFactory sessionFactory, Example6 example6) {
        super(sessionFactory);
        this.example6 = example6;
    }

    public static void main(String[] args) {
        Utils.runExample(Example7a.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        example6.run();

        final Session session = sessionFactory.getCurrentSession();

        LOGGER.debug("Evicting all managed-entities from 1st level cache...");
        session.clear();

        // SHOULD show nothing in 1st level cache
        displaySessionStats();

        // 1 SELECT statement against DB
        final Project project = (Project) session.createQuery(
                "select p from Project p join fetch p.projectUsers pu join fetch pu.user where p.id = :projectId")
                .setLong("projectId", 1L)
                .uniqueResult();

        checkNotNull(project, "Project exists");

        // no SELECT statement against DB because the needed entities are already in 1st level cache
        for (ProjectUser projectUser : project.getProjectUsers()) {
            LOGGER.debug("Project [{}] has project user [{}]", project.getName(), projectUser.getUser().getName());
        }

        // everything is back (Project, User, ProjectUser) in 1st level cache
        displaySessionStats();

        // NOTES:
        // - while the code is very similar to Example 7, there's only one SELECT statement against DB
    }
}
