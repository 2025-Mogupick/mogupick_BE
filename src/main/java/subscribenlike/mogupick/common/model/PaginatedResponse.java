package subscribenlike.mogupick.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
public class PaginatedResponse<T> {
    private List<T> content;
    private int size;
    private int page;
    private int totalPages;

    public static <T> PaginatedResponse<T> from(Page<T> page) {
        return new PaginatedResponse<>(
                page.getContent(),
                page.getSize(),
                page.getNumber(),
                page.getTotalPages()
        );
    }

    public static <T> PaginatedResponse<T> of(List<T> content, int size, int page, int totalPages) {
        return new PaginatedResponse<>(content, size, page, totalPages);
    }
}
