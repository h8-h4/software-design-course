package exchange.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String id;
    private double balance;

    @OneToMany
    @JoinColumn(name = "user_id")
    @Builder.Default
    private List<UserStock> stocks = new ArrayList<>();
}
