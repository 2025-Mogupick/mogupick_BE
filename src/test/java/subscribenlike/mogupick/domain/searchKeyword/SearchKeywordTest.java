package subscribenlike.mogupick.domain.searchKeyword;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import subscribenlike.mogupick.searchKeyword.domain.SearchKeyword;

class SearchKeywordTest {
    @Test
    void 생성_성공() {
        SearchKeyword searchKeyword = new SearchKeyword(1L, "모구픽", 1);
        assertThat(searchKeyword.getId()).isEqualTo(1L);
    }

    @Test
    void 내용이_공백일_경우_생성할_수_없다() {
        assertThatThrownBy(() -> new SearchKeyword(1L, "", 1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
