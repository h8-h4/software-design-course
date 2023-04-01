package reactive;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactive.controller.ProductController;
import reactive.dto.AddProductRequest;
import reactive.model.Currency;
import reactive.model.Product;
import reactive.model.User;
import reactive.repository.ProductRepository;
import reactive.repository.UserRepository;
import reactive.service.CurrencyService;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SuppressWarnings("ALL")
@ExtendWith(SpringExtension.class)
@DataMongoTest
public class ProductIntegrationTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    private ProductController productController;

    @BeforeEach
    public void init() {
        productController = new ProductController(userRepository, productRepository, new CurrencyService());
    }

    @Test
    public void testAddProduct() {
        final AddProductRequest request = new AddProductRequest("product", 100, Currency.RUB);
        final Product product = productController.addProduct(request).block();


        assertEquals(request.getName(), product.getName());
        assertEquals(request.getPrice(), product.getPrice());
        assertEquals(request.getCurrency(), product.getCurrency());

        final Product persisted = productRepository.findById(product.getId()).block();

        assertEquals(persisted, product);
    }

    @Test
    public void testGetProduct() {
        final User user = userRepository.save(new User("id", "user", Currency.RUB)).block();
        final AddProductRequest request = new AddProductRequest("product", 100, Currency.USD);
        productController.addProduct(request).block();

        final Flux<Product> products = productController.getProducts(user.getId());

        StepVerifier.create(products)
                .expectSubscription()
                .thenRequest(Long.MAX_VALUE)
                .assertNext(p -> {
                    assertEquals(p.getCurrency(), user.getCurrency());
                    assertNotEquals(100, p.getPrice());
                })
                .verifyComplete();
    }

    @Test
    public void testMultipleCurrencies() {
        final User user1 = userRepository.save(new User("id", "user", Currency.RUB)).block();
        final User user2 = userRepository.save(new User("id2", "user2", Currency.USD)).block();

        final AddProductRequest request = new AddProductRequest("product", 100, Currency.USD);
        final AddProductRequest request2 = new AddProductRequest("product", 200, Currency.RUB);
        productController.addProduct(request).block();
        productController.addProduct(request2).block();

        final Flux<Product> products = productController.getProducts(user1.getId());
        final Flux<Product> products2 = productController.getProducts(user2.getId());

        StepVerifier.create(products)
                .expectSubscription()
                .thenRequest(Long.MAX_VALUE)
                .assertNext(p -> {
                    assertEquals(p.getCurrency(), user1.getCurrency());
                    assertNotEquals(100, p.getPrice());
                })
                .assertNext(p -> {
                    assertEquals(p.getCurrency(), user1.getCurrency());
                    assertEquals(200, p.getPrice());
                })
                .verifyComplete();

        StepVerifier.create(products2)
                .expectSubscription()
                .thenRequest(Long.MAX_VALUE)
                .assertNext(p -> {
                    assertEquals(p.getCurrency(), user2.getCurrency());
                    assertEquals(100, p.getPrice());
                })
                .assertNext(p -> {
                    assertEquals(p.getCurrency(), user2.getCurrency());
                    assertNotEquals(200, p.getPrice());
                })
                .verifyComplete();
    }
}
