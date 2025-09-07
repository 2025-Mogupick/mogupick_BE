package subscribenlike.mogupick.subscription.domain;

public enum SubscriptionStatus {
    ONGOING("진행중"),
    PAUSED("일시정지"),
    ENDED("종료"),
    CANCELLED("해지");

    private final String description;

    SubscriptionStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return this == ONGOING;
    }

    public boolean isTerminated() {
        return this == ENDED || this == CANCELLED;
    }
}
