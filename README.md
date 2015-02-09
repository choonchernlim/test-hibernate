# Understanding Hibernate 1st Level Cache

This project consists of several examples using Hibernate and H2 in-memory database. Check out the project and run each example.

* Example 1 - Adding a new entity.
* Example 2 - Adding a new entity and retrieving it with `session.get(..)`.
* Example 3 - Adding a new entity and removing it from 1st level cache.
* Example 4 - Updating non-Hibernate managed entity where entity with same identifier already exists in session.
* Example 4a - Using `session.merge(..)` to update non-Hibernate managed entity.
* Example 5 - Deleting non-Hibernate managed entity where entity with same identifier already exists in session.
* Example 5a - Using `session.merge(..)` first before deleting non-Hibernate managed entity.
* Example 6 - One project has 3 users.
* Example 7 - Executing `Example6` where 1 project has 3 users, but querying other entities using getter methods.
* Example 7a - Executing `Example6` where 1 project has 3 users, but instead of using getter methods, performing eager fetch in HQL.
* Example 7b - Executing `Example6` where 1 project has 3 users, but querying other entities and storing them into beans.
* Example 8 - Executing `Example6` where 1 project has 3 users, but deleting all project users with `collection.clear()`
* Example 8a - Executing `Example6` where 1 project has 3 users, but deleting all project users using DML-style operation.
* Example 8b - Executing `Example6` where 1 project has 3 users, but deleting all project users using DML-style operation and handling first level cache properly.
* Example 9 - Executing `Example6` where 1 project has 3 users, examining cache when using query.
