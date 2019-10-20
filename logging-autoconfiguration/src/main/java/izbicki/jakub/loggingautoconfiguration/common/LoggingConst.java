package izbicki.jakub.loggingautoconfiguration.common;

import lombok.Getter;

@Getter
public enum LoggingConst {
  CORRELATION_ID("correlationId"),
  REQUEST_START_TIME("requestStartTimeMs"),
  SUBTRACT_REQUEST_TIME("subtractRequestTimeMs"),
  SERVICE_RESPONSE_TIME("serviceResponseTimeMs"),
  SENDER("sender");

  LoggingConst(String name) {
    this.name = name;
  }

  String name;
}
