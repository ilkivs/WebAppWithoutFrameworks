package com.ilkiv.dao.impl;

import com.ilkiv.dao.CategoryDao;
import com.ilkiv.model.Category;

public class CategoryDaoImpl extends AbstractDao<Category, Long> implements CategoryDao {

    public CategoryDaoImpl(Class<Category> clazz) {
        super(clazz);
    }
}
