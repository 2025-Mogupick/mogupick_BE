package subscribenlike.mogupick.subscription.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;
import subscribenlike.mogupick.billing.domain.PaymentStatus;
import subscribenlike.mogupick.billing.dto.PaymentStateResponse;
import subscribenlike.mogupick.billing.service.PaymentService;
import subscribenlike.mogupick.subscription.batch.dto.ChargeCommand;
import subscribenlike.mogupick.subscription.domain.Subscription;
import subscribenlike.mogupick.subscription.domain.SubscriptionStatus;
import subscribenlike.mogupick.subscription.repository.SubscriptionRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class SubscriptionBillingBatchConfig {
    private static final int CHUNK_SIZE = 50;

    private final SubscriptionRepository subscriptionRepository;
    private final PaymentService paymentService;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Job subscriptionBillingJob(Step subscriptionBillingStep) {
        return new JobBuilder("subscriptionBillingJob", jobRepository)
                .start(subscriptionBillingStep)
                .build();
    }

    @Bean
    public Step subscriptionBillingStep(
            ItemReader<Subscription> dueSubscriptionReader,
            ItemProcessor<Subscription, ChargeCommand> subscriptionToChargeProcessor,
            ItemWriter<ChargeCommand> chargeWriter
    ) {
        return new StepBuilder("subscriptionBillingStep", jobRepository)
                .<Subscription, ChargeCommand>chunk(CHUNK_SIZE, transactionManager)
                .reader(dueSubscriptionReader)
                .processor(subscriptionToChargeProcessor)
                .writer(chargeWriter)
                .faultTolerant()
                .retryLimit(2)
                .retry(Exception.class)
                .skipLimit(10)
                .skip(Exception.class)
                .listener(stepExecutionListener())
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<Subscription> dueSubscriptionReader(
            @Value("#{jobParameters['billingDate']}") String billingDateStr
    ) {
        LocalDate billingDate = LocalDate.parse(billingDateStr, DateTimeFormatter.ISO_DATE);

        return new RepositoryItemReaderBuilder<Subscription>()
                .name("dueSubscriptionReader")
                .repository(subscriptionRepository)
                .methodName("findByStatusAndNextBillingDate")
                .arguments(List.of(SubscriptionStatus.ONGOING, billingDate))
                .pageSize(CHUNK_SIZE)
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<Subscription, ChargeCommand> subscriptionToChargeProcessor(
            @Value("#{jobParameters['billingDate']}") String billingDateStr
    ) {
        return sub -> {
            // 멱등키: 구독ID-날짜-회차
            String orderId = "SUB-" + sub.getId() + "-" + billingDateStr + "-R" + sub.getProgressRound();
            String orderName = "[정기결제] " + sub.getProduct().getBrandName() + " " + sub.getProduct().getName();
            String customerKey = sub.getMember().getCustomerKey();
            int amount = sub.getProduct().getPrice();

            return new ChargeCommand(sub, orderId, orderName, customerKey, amount);
        };
    }

    @Bean
    @StepScope
    public ItemWriter<ChargeCommand> chargeWriter() {
        return items -> {
            List<Subscription> toSave = new ArrayList<>();
            for (ChargeCommand cmd : items) {
                PaymentStateResponse res = paymentService.charge(
                        cmd.orderId(), cmd.customerKey(), cmd.orderName(), cmd.amount()
                );
                if (res.status() == PaymentStatus.APPROVED) {
                    Subscription sub = cmd.subscription();
                    sub.proceedNextRound();
                    toSave.add(sub);
                }
            }
            if (!toSave.isEmpty()) {
                subscriptionRepository.saveAll(toSave);
            }
        };
    }

    @Bean
    public StepExecutionListener stepExecutionListener() {
        return new StepExecutionListener() {
            @Override public void beforeStep(StepExecution stepExecution) {}
            @Override public ExitStatus afterStep(StepExecution stepExecution) {
                return stepExecution.getExitStatus();
            }
        };
    }
}
