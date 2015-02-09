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
 * Using `session.merge(..)` to update non-Hibernate managed entity.
 */
@Service
@Transactional
public class Example4a extends Example {

    @Autowired
    public Example4a(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public static void main(String[] args) {
        Utils.runExample(Example4a.class);
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

        // attempt to update existing entity by manually hardcoding the id
        final User anotherUser = new User();
        anotherUser.setId(1L);
        anotherUser.setName("Kurt");

        // 1 UPDATE statement against DB
        final Object mergedUser = session.merge(anotherUser);

        // the flush and evict are added ONLY to force Hibernate to query against DB on the statement below
        session.flush();
        session.evict(mergedUser);

        // 1 SELECT statement against DB
        User userFromDB = (User) session.get(User.class, 1L);
        checkArgument("Kurt".equals(userFromDB.getName()), "Should display updated value");

        // due to `merge()`, the original reference to user SHOULD also be updated
        checkArgument("Kurt".equals(user.getName()), "Should be Kurt, not Mike");

        displaySessionStats();
    }
}
