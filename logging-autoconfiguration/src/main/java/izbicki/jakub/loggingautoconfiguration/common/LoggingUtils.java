package izbicki.jakub.loggingautoconfiguration.common;

import static izbicki.jakub.loggingautoconfiguration.common.LoggingConst.CORRELATION_ID;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpMessage;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

public class LoggingUtils {

  private final LoggingContext context;

  public LoggingUtils(LoggingContext context) {
    this.context = context;
  }

  public void log(HttpServletRequest request) {
    setCorrelationIdIfMissing(request);

    Logger.log(context,
        HttpMessageType.INCOMING_REQUEST,
        Optional.of(request.getRequestURI()),
        Optional.of(request.getMethod()),
        Optional.empty(),
        Optional.empty(),
        getBody(request));
  }

  public void log(HttpServletResponse response) {
    setCorrelationIdIfMissing(response);

    Logger.log(context,
        HttpMessageType.INCOMING_RESPONSE,
        Optional.empty(),
        Optional.empty(),
        Optional.of(context.getResponseTime()),
        Optional.of(String.valueOf(response.getStatus())),
        Optional.empty());
  }

  public void log(HttpRequest request, byte[] body) {
    setCorrelationIdIfMissing(request);

    Logger.log(context,
        HttpMessageType.OUTCOMING_REQUEST,
        Optional.of(request.getURI().toString()),
        Optional.of(request.getMethod().name()),
        Optional.empty(),
        Optional.empty(),
        getBody(body));
  }

  public void log(ClientHttpResponse response) throws IOException {
    setCorrelationIdIfMissing(response);

    Logger.log(context,
        HttpMessageType.OUTCOMING_RESPONSE,
        Optional.empty(),
        Optional.empty(),
        Optional.empty(),
        Optional.of(response.getStatusCode().toString()),
        getBody(response));
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

  public void setRequestStart() {
    context.setRequestStartTime(new Date().getTime());
  }

  public void subtractFromRequestTime(long subtractMs) {
    long subtractionRequestTime = context.getSubtractionRequestTime();
    context.setSubtractionRequestTime(subtractionRequestTime + subtractMs);
  }

  public void setRequestStop() {
    long end = new Date().getTime();
    long start = context.getRequestStartTime();
    long outgoingRequestsTimeTotalMs = context.getSubtractionRequestTime();

    context.setResponseTime(end - start - outgoingRequestsTimeTotalMs);
  }

  private Optional<String> getBody(HttpServletRequest request) {
    try {
      return Optional.ofNullable(IOUtils.toString(request.getReader()));
    } catch (IOException e) {
      return Optional.empty();
    }
  }

  private Optional<String> getBody(byte[] body) {
    return Optional.ofNullable(body)
        .map(bytes -> new String(body, StandardCharsets.UTF_8));
  }

  private Optional<String> getBody(ClientHttpResponse response) {
    try {
      return Optional.of(StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()));
    } catch (IOException e) {
      return Optional.empty();
    }
  }
}
