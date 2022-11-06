package ru.akirakozov.sd.refactoring.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.model.Product;

import java.util.List;
import java.util.regex.Pattern;

public class AddGetScenarioTest extends IntegrationTestBase {
    private static final Pattern PRODUCT_PATTERN = Pattern.compile("(\\w+)\\s+(\\d+)\\s*</br>");

    @Test
    public void testAddAndGet() {
        Product product = new Product("product", 10000);
        addProduct(product);

        List<Product> products = extractProducts(getProducts(), PRODUCT_PATTERN);

        Assertions.assertEquals(1, products.size());
        Assertions.assertEquals(product, products.get(0));
    }

    @Test
    public void testAddAndGetMultiple() {
        Product product1 = new Product("product", 10000);
        Product product2 = new Product("product2", 20000);
        Product product3 = new Product("product3", 30000);
        addProduct(product1);
        addProduct(product2);
        addProduct(product3);

        List<Product> products = extractProducts(getProducts(), PRODUCT_PATTERN);

        Assertions.assertEquals(3, products.size());
        Assertions.assertEquals(product1, products.get(0));
        Assertions.assertEquals(product2, products.get(1));
        Assertions.assertEquals(product3, products.get(2));
    }

}
