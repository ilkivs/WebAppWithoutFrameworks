package com.ilkiv;

import com.ilkiv.dao.CategoryDao;
import com.ilkiv.dao.ProductDao;
import com.ilkiv.dao.UserDao;
import com.ilkiv.dao.impl.CategoryDaoImpl;
import com.ilkiv.dao.impl.ProductDaoImpl;
import com.ilkiv.dao.impl.UserDaoImpl;
import com.ilkiv.model.Category;
import com.ilkiv.model.Product;
import com.ilkiv.model.User;

import java.util.List;

public class TestClass {

    private static final CategoryDao categoryDao = new CategoryDaoImpl(Category.class);
    private static final UserDao userDao = new UserDaoImpl(User.class);
    private static final ProductDao productDao = new ProductDaoImpl(Product.class);

    public static void main(String[] args) {
        List<Category> categories = categoryDao.getAll();
        List<User> users = userDao.getAll();
        List<Product> products = productDao.getAll();

        System.out.println(categories);
        System.out.println(users);
        System.out.println(products);

        Category category = categoryDao.get(2L);
        User user = userDao.get(1L);
        Product product = productDao.get(3L);

        System.out.println(category);
        System.out.println(user);
        System.out.println(product);

        Category newCategory = new Category(100, "newCategory", "000");
        Product newProduct = new Product(100, "newProduct", "000", 200);
        User newUser = new User(3, "newUser", "pass");

        categoryDao.save(newCategory);
        productDao.save(newProduct);
        userDao.save(newUser);

        newCategory.setName("updatedCategoryName");
        newProduct.setName("updatedProductName");
        newUser.setUsername("updatedUserName");

        categoryDao.update(newCategory);
        productDao.update(newProduct);
        userDao.update(newUser);

        categoryDao.delete(newCategory.getId());
        productDao.delete(newProduct.getId());
        userDao.delete(newUser.getId());

        categories = categoryDao.getAll();
        users = userDao.getAll();
        products = productDao.getAll();

        System.out.println(categories);
        System.out.println(users);
        System.out.println(products);
    }
}
