package com.choonchernlim.testHibernate.example.impl;

import com.choonchernlim.testHibernate.bean.ProjectUserBean;
import com.choonchernlim.testHibernate.example.Example;
import com.choonchernlim.testHibernate.util.Utils;
import static com.google.common.base.Preconditions.checkArgument;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Executing `Example6` where 1 project has 3 users, but querying other entities using getter methods.
 */
@Service
@Transactional
public class Example7b extends Example {

    private static Logger LOGGER = LoggerFactory.getLogger(Example7b.class);

    private final Example6 example6;

    @Autowired
    public Example7b(SessionFactory sessionFactory, Example6 example6) {
        super(sessionFactory);
        this.example6 = example6;
    }

    public static void main(String[] args) {
        Utils.runExample(Example7b.class);
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
        final List<ProjectUserBean> projectUserBeans = session.createQuery(
                "select new com.choonchernlim.testHibernate.bean.ProjectUserBean(p.name, pu.user.name) from Project p join p.projectUsers pu where p.id = :projectId")
                .setLong("projectId", 1L)
                .list();

        checkArgument(projectUserBeans.size() == 3, "3 project users");

        for (ProjectUserBean projectUserBean : projectUserBeans) {
            LOGGER.debug("Project [{}] has project user [{}]",
                         projectUserBean.getProjectName(),
                         projectUserBean.getUserName());
        }

        // everything is back (Project, User, ProjectUser) in 1st level cache
        displaySessionStats();

        // NOTES:
        // - assuming we are only cherry picking a few fields, another approach is to store them in a bean
        // - reduces number of DTOs needed between layers
    }
}
