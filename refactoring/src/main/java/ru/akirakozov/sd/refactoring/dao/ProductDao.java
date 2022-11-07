package ru.akirakozov.sd.refactoring.dao;

import ru.akirakozov.sd.refactoring.model.Product;

import java.util.List;

public interface ProductDao {
    void addProduct(Product product);

    List<Product> getProducts();

    Product max();

    Product min();

    int count();

    long sum();
}
