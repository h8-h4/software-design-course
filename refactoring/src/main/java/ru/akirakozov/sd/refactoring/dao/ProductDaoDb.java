package ru.akirakozov.sd.refactoring.dao;

import ru.akirakozov.sd.refactoring.model.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ru.akirakozov.sd.refactoring.dao.DatabaseUtils.*;

public class ProductDaoDb implements ProductDao {
    private static final String INSERT_PRODUCT = """
            INSERT INTO PRODUCT
            (NAME, PRICE) VALUES (?, ?)""";

    @Override
    public void addProduct(Product product) {
        executeUpdate(INSERT_PRODUCT, (statement) -> {
            statement.setString(1, product.getName());
            statement.setInt(2, product.getPrice());
        });
    }

    @Override
    public List<Product> getProducts() {
        return executeConstantQuery("SELECT * FROM PRODUCT", (rs) -> {
            ArrayList<Product> result = new ArrayList<>();

            while (rs.next()) {
                result.add(getNextProduct(rs));
            }

            return result;
        });
    }

    @Override
    public Product max() {
        return executeConstantQuery(
                "SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1",
                ProductDaoDb::getNextProduct
        );
    }

    @Override
    public Product min() {
        return executeConstantQuery(
                "SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1",
                ProductDaoDb::getNextProduct
        );
    }

    @Override
    public int count() {
        return executeConstantQuery("SELECT COUNT(*) FROM PRODUCT", (rs) -> rs.getInt(1));
    }

    @Override
    public long sum() {
        return executeConstantQuery("SELECT SUM(price) FROM PRODUCT", (rs) -> rs.getLong(1));
    }

    private static Product getNextProduct(ResultSet rs) throws SQLException {
        String name = rs.getString("name");
        int price = rs.getInt("price");

        return new Product(name, price);
    }
}

