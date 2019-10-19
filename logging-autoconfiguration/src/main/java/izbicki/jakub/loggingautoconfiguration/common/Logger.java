package izbicki.jakub.loggingautoconfiguration.common;

import java.util.Date;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Logger {

  private static final String EMPTY = "";

  private static final String LOG_TAG = "autoconfigured-logging";
  private static final int RESPONSE_CODE_SIZE = 3;

  static void log(LoggingContext context, HttpMessageType type, Optional<String> contextPath, Optional<String> method,
      Optional<String> responseTime, Optional<String> responseCode, Optional<String> body) {

    Date now = new Date();

    String logMessage = String.format("\n"
            + "%s \n"
            + "[%s] "
            + "serviceName: [%s] "
            + "type: [%s] "
            + "contextPath: [%s] "
            + "correlationId: [%s] "
            + "method: [%s] "
            + "timestamp: [%s] "
            + "responseTime: [%s] "
            + "responseCode: [%s] "
            + "body: [%s]",
        getSeparator(type),
        LOG_TAG,
        context.getServiceName(),
        type.name(),
        get(contextPath),
        context.getCorrelationId(),
        get(method),
        now.getTime(),
        get(responseTime),
        getResponseCode(responseCode),
        get(body));

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

  private static String getResponseCode(Optional<String> responseCode) {
    return responseCode
        .map(code -> code.length() > RESPONSE_CODE_SIZE ? code.substring(0, RESPONSE_CODE_SIZE) : code)
        .orElse(EMPTY);
  }
}
