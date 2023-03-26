package exchange.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Data
@Entity
@Table(name = "companies")
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    @Id
    private String id;
    private String name;
    private int stockCount;
    private double stockPrice;
}
