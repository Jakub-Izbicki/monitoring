package izbicki.jakub.servicesweb.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("service")
public class ServiceConfig {

  private int maxCalculationTimeMs;

  private String[] availableNodes;
}
