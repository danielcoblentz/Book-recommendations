package main.java.com.book_recs.demo.model; 
import jakarta.persistence.*;
import java.util.UUID;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "saved_recommendations")
@Getter
@Setter
public class SavedRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // surrogate id for convenience

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "book_id", nullable = false)
    private Long bookId;

    @Column(name = "source_reco_id")
    private UUID sourceRecoId;

    @Column(name = "note")
    private String note;

    @Column(name = "saved_at", nullable = false)
    private OffsetDateTime savedAt;

    public SavedRecommendation() { this.savedAt = OffsetDateTime.now(); }

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }

    public UUID getSourceRecoId() { return sourceRecoId; }
    public void setSourceRecoId(UUID sourceRecoId) { this.sourceRecoId = sourceRecoId; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public OffsetDateTime getSavedAt() { return savedAt; }
    public void setSavedAt(OffsetDateTime savedAt) { this.savedAt = savedAt; }
}
