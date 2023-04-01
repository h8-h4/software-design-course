package reactive.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactive.dto.AddProductRequest;
import reactive.model.Product;
import reactive.model.User;
import reactive.repository.ProductRepository;
import reactive.repository.UserRepository;
import reactive.service.CurrencyService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    private final CurrencyService currencyService;

    @PostMapping
    public Mono<Product> addProduct(@RequestBody AddProductRequest addProductRequest) {
        return productRepository.save(
                Product.builder()
                        .id(UUID.randomUUID().toString())
                        .name(addProductRequest.getName())
                        .price(addProductRequest.getPrice())
                        .currency(addProductRequest.getCurrency())
                        .build()
        );
    }

    @GetMapping("/{user_id}")
    public Flux<Product> getProducts(@PathVariable(value = "user_id") String userId) {
        final Mono<User> user = userRepository.findById(userId);
        final Flux<Product> products = productRepository.findAll();
        return products.flatMap(product -> user
                .map(u -> product
                        .withCurrency(u.getCurrency())
                        .withPrice(
                                currencyService.convertPrice(
                                        product.getPrice(),
                                        product.getCurrency(),
                                        u.getCurrency()
                                )
                        )
                )
        );
    }
}
