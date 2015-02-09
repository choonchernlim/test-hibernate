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
 * Updating non-Hibernate managed entity where entity with same identifier already exists in session.
 */
@Service
@Transactional
public class Example4 extends Example {

    @Autowired
    public Example4(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public static void main(String[] args) {
        Utils.runExample(Example4.class);
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

        // SHOULD BLOW UP because `anotherUser` is not Hibernate-managed
        session.update(anotherUser);
    }
}
