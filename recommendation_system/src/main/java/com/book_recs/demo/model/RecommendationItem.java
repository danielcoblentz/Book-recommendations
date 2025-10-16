package com.book_recs.demo.model;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.UUID;


@Entity
@Table(name = "recommendation_items")
public class RecommendationItem implements Serializable {

    @EmbeddedId
    private RecommendationItemId id;

    @Column(name = "book_id", nullable = false)
    private Long bookId;

    @Column(name = "score")
    private Double score;

    @Column(name = "reason", columnDefinition = "jsonb")
    private String reason;

    public RecommendationItem() {}

    public RecommendationItem(RecommendationItemId id, Long bookId, Double score, String reason) {
        this.id = id;
        this.bookId = bookId;
        this.score = score;
        this.reason = reason;
    }

    public RecommendationItemId getId() { return id; }
    public void setId(RecommendationItemId id) { this.id = id; }

    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }

    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}

@Embeddable
class RecommendationItemId implements Serializable {
    @Column(name = "recommendation_id")
    private UUID recommendationId;

    @Column(name = "rank")
    private Integer rank;

    public RecommendationItemId() {}

    public RecommendationItemId(UUID recommendationId, Integer rank) {
        this.recommendationId = recommendationId;
        this.rank = rank;
    }

    public UUID getRecommendationId() { return recommendationId; }
    public void setRecommendationId(UUID recommendationId) { this.recommendationId = recommendationId; }

    public Integer getRank() { return rank; }
    public void setRank(Integer rank) { this.rank = rank; }
}
