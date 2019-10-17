package izbicki.jakub.loggingautoconfiguration.common;

import lombok.Getter;

@Getter
public enum LoggingConst {
  CORRELATION_ID("correlationId");

  LoggingConst(String name) {
    this.name = name;
  }

  String name;
}
