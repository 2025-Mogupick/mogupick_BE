package subscribenlike.mogupick.common;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import subscribenlike.mogupick.support.annotation.ServiceTest;

@ServiceTest
class RedisTemplateTest {
    @Autowired
    private RedisTemplate<String, Long> redisTemplate;

    @Test
    void 레디스_동작_확인() {
        ValueOperations<String, Long> valueOperations = redisTemplate.opsForValue();

        String key = "name";
        valueOperations.set(key, 1L);
        Long value = valueOperations.get(key);
        assertThat(value).isEqualTo(1L);
    }
}
