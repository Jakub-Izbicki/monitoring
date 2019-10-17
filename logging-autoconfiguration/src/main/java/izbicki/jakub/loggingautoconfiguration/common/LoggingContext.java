package izbicki.jakub.loggingautoconfiguration.common;

import static izbicki.jakub.loggingautoconfiguration.common.LoggingConst.CORRELATION_ID;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@RequestScope
@Component
public class LoggingContext {

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
}
