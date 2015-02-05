package com.choonchernlim.testHibernate.example.impl;

import com.choonchernlim.testHibernate.domain.User;
import com.choonchernlim.testHibernate.example.Example;
import com.choonchernlim.testHibernate.util.Utils;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * Adding a new entity and retrieving it with `session.get(..)`.
 */
@Service
@Transactional
public class Example2 extends Example {

    @Autowired
    public Example2(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public static void main(String[] args) {
        Utils.runExample(Example2.class);
    }

    @Override
    public void run() {
        checkAllTablesEmpty();

        final Session session = sessionFactory.getCurrentSession();

        // new user
        final User user = new User();
        user.setName("Mike");

        // 1 INSERT statement against DB
        // 1 managed entity in 1st level cache
        final Serializable userId = session.save(user);

        checkArgument(1L == userId, "Successfully saved to DB");

        // SHOULD NOT invoke a select statement against DB because it is already hibernate-managed
        final User someUser = (User) session.get(User.class, 1L);

        checkNotNull(someUser, "User exists");

        displaySessionStats();
    }
}
