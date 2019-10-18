package izbicki.jakub.loggingautoconfiguration.common;

import java.util.Date;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Logger {

  private static final String EMPTY = "";

  private static final String LOG_TAG = "autoconfigured-logging";

  static void log(String serviceName, HttpMessageType type, Optional<String> contextPath,
      Optional<String> correlationId, Optional<String> method, Optional<String> responseCode) {

    Date now = new Date();

    String logMessage = String.format("\n"
            + "%s \n"
            + "[%s] "
            + "serviceName: %s "
            + "type: %s "
            + "contextPath: %s "
            + "correlationId: %s "
            + "method: %s "
            + "time: %s "
            + "timestamp: %s "
            + "responseCode: %s",
        getSeparator(type),
        LOG_TAG,
        serviceName,
        type.name(),
        get(contextPath),
        get(correlationId),
        get(method),
        now.toString(),
        now.getTime(),
        get(responseCode));

    Logger.log.info(logMessage);
  }

  private static String getSeparator(HttpMessageType type) {
    switch (type) {
      case INCOMING_REQUEST:
        return ">   >   >   > [ ]";
      case INCOMING_RESPONSE:
        return "<   <   <   < [ ]";
      case OUTCOMING_REQUEST:
        return "[ ] >   >   >   >";
      case OUTCOMING_RESPONSE:
        return "[ ] <   <   <   <";
      default:
        return EMPTY;
    }
  }

  private static String get(Optional<String> optional) {
    return optional.orElse(EMPTY);
  }
}
