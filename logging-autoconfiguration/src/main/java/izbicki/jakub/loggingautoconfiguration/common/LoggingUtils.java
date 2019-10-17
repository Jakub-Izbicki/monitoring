package izbicki.jakub.loggingautoconfiguration.common;

import static izbicki.jakub.loggingautoconfiguration.common.LoggingConst.CORRELATION_ID;

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

  private final LoggingContext context;

  public LoggingUtils(LoggingContext context) {
    this.context = context;
  }

  public void log(HttpRequest request) {
    setCorrelationIdIfMissing(request);

    log.info("- - - - - - - - - -");
    log.info("type: " + HttpMessageType.OUTCOMING_REQUEST.name());
    log.info("destination: " + request.getURI().toString());
    log.info("correlationId: " + request.getHeaders().get(CORRELATION_ID.getName()).get(0));
    log.info("method: " + request.getMethod().name());
    log.info("time: " + new Date().toString());
  }

  public void log(ClientHttpResponse response) throws IOException {
    setCorrelationIdIfMissing(response);

    log.info("- - - - - - - - - -");
    log.info("type: " + HttpMessageType.OUTCOMING_RESPONSE.name());
    log.info("correlationId: " + response.getHeaders().get(CORRELATION_ID.getName()).get(0));
    log.info("time: " + new Date().toString());
    log.info("responseCode: " + response.getStatusCode().toString());
  }

  public void log(HttpServletRequest request) {
    setCorrelationIdIfMissing(request);

    log.info("- - - - - - - - - -");
    log.info("type: " + HttpMessageType.INCOMING_REQUEST.name());
    log.info("destination: " + request.getRequestURI());
    log.info("correlationId: " + context.getCorrelationId());
    log.info("method: " + request.getMethod());
    log.info("time: " + new Date().toString());
  }

  public void log(HttpServletResponse response) {
    setCorrelationIdIfMissing(response);

    log.info("- - - - - - - - - -");
    log.info("type: " + HttpMessageType.INCOMING_RESPONSE.name());
    log.info("correlationId: " + context.getCorrelationId());
    log.info("time: " + new Date().toString());
    log.info("responseCode: " + response.getStatus());
  }

  private void setCorrelationIdIfMissing(HttpMessage httpMessage) {
    String correlationId = getCorrelationId(httpMessage).orElse(UUID.randomUUID().toString());

    httpMessage.getHeaders().remove(CORRELATION_ID.getName());
    httpMessage.getHeaders().add(CORRELATION_ID.getName(), correlationId);
    context.setCorrelationId(correlationId);
  }

  private void setCorrelationIdIfMissing(HttpServletResponse response) {
    String correlationId = getCorrelationId(response).orElse(UUID.randomUUID().toString());

    response.setHeader(CORRELATION_ID.getName(), correlationId);
    context.setCorrelationId(correlationId);
  }

  private void setCorrelationIdIfMissing(HttpServletRequest request) {
    String correlationId = getCorrelationId(request).orElse(UUID.randomUUID().toString());

//      request.addHeader(CORRELATION_ID.getName(), correlationId);

    context.setCorrelationId(correlationId);
  }

  private Optional<String> getCorrelationId(HttpMessage httpMessage) {
    Optional<String> correlationIdFromHeaders = getCorrelationIdFromHeaders(httpMessage);
    if (correlationIdFromHeaders.isPresent()) {
      return correlationIdFromHeaders;
    }

    return getCorrelationIdFromContext();
  }

  private Optional<String> getCorrelationIdFromHeaders(HttpMessage httpMessage) {
    Optional<List<String>> correlationIdHeaders = Optional.ofNullable(httpMessage.getHeaders())
        .map(headers -> headers.get(CORRELATION_ID.getName()));
    if (correlationIdHeaders.isPresent() && !correlationIdHeaders.get().isEmpty()) {
      return Optional.ofNullable(correlationIdHeaders.get().get(0));
    } else {
      return Optional.empty();
    }
  }

  private Optional<String> getCorrelationId(HttpServletResponse response) {
    Optional<String> idFromHeaders = Optional.ofNullable(response.getHeader(CORRELATION_ID.getName()));
    if (idFromHeaders.isPresent()) {
      return idFromHeaders;
    }

    return getCorrelationIdFromContext();
  }

  private Optional<String> getCorrelationIdFromContext() {
    return context.isCorrelationIdPresent() ?
        Optional.of(context.getCorrelationId()) : Optional.empty();

  }

  private Optional<String> getCorrelationId(HttpServletRequest request) {
    Optional<String> idFromHeaders = Optional.ofNullable(request.getHeader(CORRELATION_ID.getName()));
    if (idFromHeaders.isPresent()) {
      return idFromHeaders;
    }

    return getCorrelationIdFromContext();
  }
}
