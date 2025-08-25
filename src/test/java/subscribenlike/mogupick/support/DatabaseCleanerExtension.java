package subscribenlike.mogupick.support;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
public class DatabaseCleanerExtension implements AfterEachCallback {
    @Override
    public void afterEach(final ExtensionContext context) {
        log.info("Running afterEach method...");

        var dataCleaner = (DatabaseCleaner) SpringExtension.getApplicationContext(context)
                .getBean("databaseCleaner");
        log.info("Cleaning database using DatabaseCleaner...");
        dataCleaner.execute();
    }
}
