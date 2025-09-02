package subscribenlike.mogupick.product.service;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum RedisKeyTemplate {

    PRODUCT_VIEW_COUNT("view:product:"),
    PRODUCT_DAILY_VIEW_COUNT("daily-view:product"),

    ;

    private final String prefix;

    public String of(String... objectValue) {
        StringBuilder sb = new StringBuilder();

        Arrays.stream(objectValue).forEach(v -> {
            if (v.contains(":")) {
                throw new IllegalArgumentException("Redis key value cannot contain ':' character.");
            }
            sb.append(":").append(v);
        });

        return prefix + sb;
    }
}
