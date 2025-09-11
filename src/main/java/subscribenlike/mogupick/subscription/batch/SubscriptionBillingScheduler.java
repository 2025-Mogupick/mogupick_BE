package subscribenlike.mogupick.subscription.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.*;
import java.time.format.DateTimeFormatter;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SubscriptionBillingScheduler {

    private final JobLauncher jobLauncher;
    private final Job subscriptionBillingJob;

    @Value("${app.billing.enabled:true}")
    private boolean enabled;

    @Scheduled(cron = "0 0 2 * * *", zone = "Asia/Seoul")
    public void runDaily() {
        if (!enabled) {
            log.info("[BillingScheduler] disabled");
            return;
        }
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        String billingDate = today.format(DateTimeFormatter.ISO_DATE);

        try {
            JobParameters params = new JobParametersBuilder()
                    .addString("billingDate", billingDate)
                    .addLong("ts", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution ex = jobLauncher.run(subscriptionBillingJob, params);
            log.info("[BillingScheduler] Job finished with status={}", ex.getStatus());
        } catch (Exception e) {
            log.error("[BillingScheduler] Job run failed", e);
        }
    }
}
