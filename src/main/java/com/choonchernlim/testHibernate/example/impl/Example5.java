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
 * Deleting non-Hibernate managed entity where entity with same identifier already exists in session.
 */
@Service
@Transactional
public class Example5 extends Example {

    @Autowired
    public Example5(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public static void main(String[] args) {
        Utils.runExample(Example5.class);
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

        // SHOULD BLOW UP because `anotherUser` is not Hibernate-managed
        session.delete(anotherUser);
    }
}
