package izbicki.jakub.loggingautoconfiguration;

import izbicki.jakub.loggingautoconfiguration.common.LoggingContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

@Configuration
@ConditionalOnWebApplication
public class LoggingConfiguration {

  @Bean
  @RequestScope
  public LoggingContext loggingContext() {
    return new LoggingContext();
  }
}
