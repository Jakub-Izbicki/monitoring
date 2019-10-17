package izbicki.jakub.loggingautoconfiguration.logging;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMessage;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;

@Slf4j
public class LoggingUtils {

  private static final String CORRELATION_ID = "correlationId";

  public static Optional<String> getCorrelationId(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(CORRELATION_ID));
  }

  public static Optional<String> getCorrelationId(HttpServletResponse response) {
    return Optional.ofNullable(response.getHeader(CORRELATION_ID));
  }

  public static Optional<String> getCorrelationId(HttpMessage httpMessage) {
    Optional<List<String>> correlationIdHeaders = Optional.ofNullable(httpMessage.getHeaders())
        .map(headers -> headers.get(CORRELATION_ID));
    if (correlationIdHeaders.isPresent() && !correlationIdHeaders.get().isEmpty()) {
      return Optional.ofNullable(correlationIdHeaders.get().get(0));
    } else {
      return Optional.empty();
    }
  }

  public static void setCorrelationIdIfMissing(HttpMessage httpMessage) {
    if (!getCorrelationId(httpMessage).isPresent()) {
      httpMessage.getHeaders().add(CORRELATION_ID, UUID.randomUUID().toString());
    }
  }

  public static void setCorrelationIdIfMissing(HttpServletResponse response) {
    if (!getCorrelationId(response).isPresent()) {
      response.addHeader(CORRELATION_ID, UUID.randomUUID().toString());
    }
  }

  public static void log(HttpRequest request) {
    log.info("- - - - - - - - - -");
    log.info("type: request");
    log.info("destination: " + request.getURI().toString());
    log.info("correlationId: " + request.getHeaders().get(CORRELATION_ID).get(0));
    log.info("method: " + request.getMethod().name());
    log.info("time: " + new Date().toString());
  }

  public static void log(ClientHttpResponse response) throws IOException {
    log.info("- - - - - - - - - -");
    log.info("correlationId: " + response.getHeaders().get(CORRELATION_ID).get(0));
    log.info("time: " + new Date().toString());
    log.info("responseCode: " + response.getStatusCode().toString());
  }
}
