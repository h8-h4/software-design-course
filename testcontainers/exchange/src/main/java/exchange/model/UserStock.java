package exchange.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "user_stocks")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStock {
    @Id
    private String companyId;

    @Id
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private int amount;
    private double price;
}
