package subscribenlike.mogupick.searchKeyword.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribenlike.mogupick.common.domain.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchKeyword extends BaseEntity {
    public static final int DEFAULT_COUNT = 1;
    public static final int INCREASE_COUNT = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @Column(name = "normalized_content", nullable = false, length = 200, unique = true)
    private String normalizedContent;

    private int searchedCount;

    public SearchKeyword(Long id, String content, int searchedCount) {
        this.id = id;
        this.content = content;
        this.normalizedContent = normalize(content);
        this.searchedCount = searchedCount;
        validateBlank();
    }

    public SearchKeyword(String content) {
        this(null, content, DEFAULT_COUNT);
    }

    public static String normalize(String raw) {
        if (raw == null) {
            return "";
        }
        return raw.replaceAll("\\s+", "")  // 모든 공백 제거
                .toLowerCase()
                .trim();
    }

    public void increase() {
        this.searchedCount += INCREASE_COUNT;
    }

    private void validateBlank() {
        if(this.normalizedContent.isEmpty()){
            throw new IllegalArgumentException("Keyword content cannot be empty");
        }
    }
}
