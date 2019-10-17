package izbicki.jakub.loggingautoconfiguration;

import izbicki.jakub.loggingautoconfiguration.common.LoggingContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnWebApplication
public class LoggingConfiguration {

  @Bean
  public LoggingContext loggingContext() {
    return new LoggingContext();
  }
}
