package com.book_recs.demo.model;
import jakarta.persistence.*;

/**
 * JPA entity representing a single recommendation run for a user.
 * Matches the SQL table `recommendations`.
 */
@Entity
@Table(name = "recommendations")
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
    }

    // Getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getAlgo() { return algo; }
    public void setAlgo(String algo) { this.algo = algo; }

    public String getModelVersion() { return modelVersion; }
    public void setModelVersion(String modelVersion) { this.modelVersion = modelVersion; }

    public String getParams() { return params; }
    public void setParams(String params) { this.params = params; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
