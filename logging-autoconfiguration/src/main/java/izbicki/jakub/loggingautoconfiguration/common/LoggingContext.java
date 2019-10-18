package izbicki.jakub.loggingautoconfiguration.common;

import static izbicki.jakub.loggingautoconfiguration.common.LoggingConst.CORRELATION_ID;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;

public class LoggingContext {

  @Value("${spring.application.name}")
  private String serviceName;

  private Map<String, String> props = new HashMap<>();

  public boolean isCorrelationIdPresent() {
    return props.containsKey(CORRELATION_ID.getName());
  }

  public String getCorrelationId() {
    return props.get(CORRELATION_ID.getName());
  }

  public void setCorrelationId(String value) {
    props.put(CORRELATION_ID.getName(), value);
  }

  public String getServiceName() {
    return serviceName;
  }
}
