package com.ilkiv.dao.impl;

import com.ilkiv.dao.UserDao;
import com.ilkiv.model.User;

public class UserDaoImpl extends AbstractDao<User, Long> implements UserDao {

    public UserDaoImpl(Class<User> clazz) {
        super(clazz);
    }
}
