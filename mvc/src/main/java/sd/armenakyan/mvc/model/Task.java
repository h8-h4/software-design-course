package sd.armenakyan.mvc.model;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "Tasks")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString(exclude = "parentList")
@EqualsAndHashCode(exclude = "parentList")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "task_id")
    private long id;

    private boolean isCompleted;

    private String description;

    @ManyToOne
    @JoinColumn(name = "list_id", nullable = false)
    private TaskList parentList;
}
