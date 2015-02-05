package com.choonchernlim.testHibernate.example.impl;

import com.choonchernlim.testHibernate.domain.User;
import com.choonchernlim.testHibernate.example.Example;
import com.choonchernlim.testHibernate.util.Utils;
import static com.google.common.base.Preconditions.checkArgument;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * Adding a new entity and removing it from 1st level cache.
 */
@Service
@Transactional
public class Example3 extends Example {

    @Autowired
    public Example3(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public static void main(String[] args) {
        Utils.runExample(Example3.class);
    }

    @Override
    public void run() {
        checkAllTablesEmpty();

        final Session session = sessionFactory.getCurrentSession();

        // new user
        final User user = new User();
        user.setName("Mike");

        // 1 INSERT statement against DB
        final Serializable userId = session.save(user);

        checkArgument(1L == userId, "Successfully saved to DB");

        // should show 1 managed entity
        displaySessionStats();

        session.evict(user);

        // should show 0 managed entity
        displaySessionStats();
    }
}
