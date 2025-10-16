package main.java.com.book_recs.demo.model;
import jakarta.persistence.*;

/**
 * JPA entity representing a single recommendation run for a user.
 * Matches the SQL table `recommendations`.
 */
@Entity
@Table(name = "recommendations")
@Getter
@Setter
public class Recommendation {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "algo", nullable = false)
    private String algo;

    @Column(name = "model_version")
    private String modelVersion;

    @Column(name = "params", columnDefinition = "jsonb")
    private String params; // store JSON as text; map to object in service layer

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public Recommendation() {
        this.id = UUID.randomUUID();
        this.createdAt = OffsetDateTime.now();
    }};
