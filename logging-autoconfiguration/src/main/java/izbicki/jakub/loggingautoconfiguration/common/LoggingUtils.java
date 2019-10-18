package izbicki.jakub.loggingautoconfiguration.common;

import static izbicki.jakub.loggingautoconfiguration.common.LoggingConst.CORRELATION_ID;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMessage;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;

public class LoggingUtils {

  private final LoggingContext context;

  public LoggingUtils(LoggingContext context) {
    this.context = context;
  }

  public void log(HttpRequest request) {
    setCorrelationIdIfMissing(request);

    Logger.log(context.getServiceName(),
        HttpMessageType.OUTCOMING_REQUEST,
        Optional.of(request.getURI().toString()),
        Optional.of(context.getCorrelationId()),
        Optional.of(request.getMethod().name()),
        Optional.empty());
  }

  public void log(ClientHttpResponse response) throws IOException {
    setCorrelationIdIfMissing(response);

    Logger.log(context.getServiceName(),
        HttpMessageType.OUTCOMING_RESPONSE,
        Optional.empty(),
        Optional.of(context.getCorrelationId()),
        Optional.empty(),
        Optional.of(response.getStatusCode().toString()));
  }

  public void log(HttpServletRequest request) {
    setCorrelationIdIfMissing(request);

    Logger.log(context.getServiceName(),
        HttpMessageType.INCOMING_REQUEST,
        Optional.of(request.getRequestURI()),
        Optional.of(context.getCorrelationId()),
        Optional.of(request.getMethod()),
        Optional.empty());
  }

  public void log(HttpServletResponse response) {
    setCorrelationIdIfMissing(response);

    Logger.log(context.getServiceName(),
        HttpMessageType.INCOMING_RESPONSE,
        Optional.empty(),
        Optional.of(context.getCorrelationId()),
        Optional.empty(),
        Optional.of(String.valueOf(response.getStatus())));
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
