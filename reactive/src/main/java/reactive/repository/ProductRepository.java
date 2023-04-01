package reactive.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactive.model.Product;

public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
}
