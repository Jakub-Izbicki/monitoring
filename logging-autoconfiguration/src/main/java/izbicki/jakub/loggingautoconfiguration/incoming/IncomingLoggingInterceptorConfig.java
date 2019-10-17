package izbicki.jakub.loggingautoconfiguration.incoming;

import izbicki.jakub.loggingautoconfiguration.common.LoggingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConditionalOnWebApplication
public class IncomingLoggingInterceptorConfig implements WebMvcConfigurer {

  @Autowired
  private LoggingContext context;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new IncomingLoggingInterceptor(context));
  }
}
