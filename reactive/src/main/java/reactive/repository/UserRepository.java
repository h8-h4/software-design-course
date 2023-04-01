package reactive.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactive.model.User;

public interface UserRepository extends ReactiveMongoRepository<User, String> {
}
