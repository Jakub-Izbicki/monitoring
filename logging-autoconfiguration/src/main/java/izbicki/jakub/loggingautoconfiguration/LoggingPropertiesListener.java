package izbicki.jakub.loggingautoconfiguration;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.PriorityOrdered;

public class LoggingPropertiesListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent>,
    PriorityOrdered {

  private static final String LOGGING_PROP = "common.file";

  private static final String DEFAULT_LOGGING_FILE = "app.log";

  @Override
  public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
    event.getEnvironment().getSystemProperties().put(LOGGING_PROP, DEFAULT_LOGGING_FILE);
  }

  @Override
  public int getOrder() {
    return HIGHEST_PRECEDENCE;
  }
}
