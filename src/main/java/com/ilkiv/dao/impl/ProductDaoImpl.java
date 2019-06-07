package com.ilkiv.dao.impl;

import com.ilkiv.dao.ProductDao;
import com.ilkiv.model.Product;

public class ProductDaoImpl extends AbstractDao<Product, Long> implements ProductDao {

    public ProductDaoImpl(Class<Product> clazz) {
        super(clazz);
    }
}
