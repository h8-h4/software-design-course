package ru.akirakozov.sd.refactoring.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.model.Product;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

public class AddQueryScenarioTest extends IntegrationTestBase {
    private static final Pattern PRODUCT_PATTERN = Pattern.compile("(\\w+)\\s+(\\d+)");
    private static final Comparator<Product> PRODUCT_COMPARATOR = Comparator.comparing(Product::getPrice);
    private static final List<Product> PRODUCTS = List.of(
            new Product("product1", 100),
            new Product("product2", 200),
            new Product("product3", 1000),
            new Product("product4", Integer.MAX_VALUE)
    );

    @BeforeEach
    public void addProducts() {
        PRODUCTS.forEach(super::addProduct);
    }

    @Test
    public void testMaxQuery() {
        List<Product> products = extractProducts(query("max"), PRODUCT_PATTERN);


        Assertions.assertEquals(1, products.size());
        Assertions.assertEquals(
                PRODUCTS.stream().max(PRODUCT_COMPARATOR).orElseThrow(),
                products.get(0)
        );
    }

    @Test
    public void testMinQuery() {
        List<Product> products = extractProducts(query("min"), PRODUCT_PATTERN);


        Assertions.assertEquals(1, products.size());
        Assertions.assertEquals(
                PRODUCTS.stream().min(PRODUCT_COMPARATOR).orElseThrow(),
                products.get(0)
        );
    }

    @Test
    public void testCountQuery() {
        List<Long> counts = extractPrices(query("count"));

        Assertions.assertEquals(1, counts.size());
        Assertions.assertEquals(PRODUCTS.size(), counts.get(0));
    }

    @Test
    public void testSumQuery() {
        List<Long> sums = extractPrices(query("sum"));

        Assertions.assertEquals(1, sums.size());
        Assertions.assertEquals(
                PRODUCTS.stream().mapToLong(Product::getPrice).sum(),
                sums.get(0)
        );
    }

}
