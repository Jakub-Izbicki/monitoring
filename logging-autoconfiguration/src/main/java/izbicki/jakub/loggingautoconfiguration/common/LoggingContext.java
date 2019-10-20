package izbicki.jakub.loggingautoconfiguration.common;

import static izbicki.jakub.loggingautoconfiguration.common.LoggingConst.CORRELATION_ID;
import static izbicki.jakub.loggingautoconfiguration.common.LoggingConst.REQUEST_START_TIME;
import static izbicki.jakub.loggingautoconfiguration.common.LoggingConst.SENDER;
import static izbicki.jakub.loggingautoconfiguration.common.LoggingConst.SERVICE_RESPONSE_TIME;
import static izbicki.jakub.loggingautoconfiguration.common.LoggingConst.SUBTRACT_REQUEST_TIME;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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

  public void setRequestStartTime(long timestamp) {
    props.put(REQUEST_START_TIME.getName(), String.valueOf(timestamp));
  }

  public long getRequestStartTime() {
    return Long.valueOf(props.get(REQUEST_START_TIME.getName()));
  }

  public void setResponseTime(long requestTimeMs) {
    props.put(SERVICE_RESPONSE_TIME.getName(), String.valueOf(requestTimeMs));
  }

  public String getResponseTime() {
    return props.get(SERVICE_RESPONSE_TIME.getName());
  }

  public long getSubtractionRequestTime() {
    return Optional.ofNullable(props.get(SUBTRACT_REQUEST_TIME.getName()))
        .map(Long::valueOf)
        .orElse(0L);
  }

  public void setSubtractionRequestTime(long timeMs) {
    props.put(SUBTRACT_REQUEST_TIME.getName(), String.valueOf(timeMs));
  }

  public void setSender(String senderName) {
    props.put(SENDER.getName(), senderName);
  }

  public String getSender() {
    return props.get(SENDER.getName());
  }
}
