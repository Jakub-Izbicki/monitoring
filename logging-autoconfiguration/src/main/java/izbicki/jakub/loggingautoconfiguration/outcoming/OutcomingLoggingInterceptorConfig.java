package izbicki.jakub.loggingautoconfiguration.outcoming;

import izbicki.jakub.loggingautoconfiguration.common.LoggingContext;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

@Configuration
@ConditionalOnWebApplication
public class OutcomingLoggingInterceptorConfig {

  @Bean
  @ConditionalOnBean(LoggingContext.class)
  public RestTemplate restTemplate(LoggingContext context) {
    RestTemplate restTemplate = new RestTemplate();
    List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();

    if (CollectionUtils.isEmpty(interceptors)) {
      interceptors = new ArrayList<>();
    }

    interceptors.add(new OutcomingLoggingInterceptor(context));
    restTemplate.setInterceptors(interceptors);
    return restTemplate;
  }
}
