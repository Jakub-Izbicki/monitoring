package izbicki.jakub.loggingautoconfiguration.outcoming;

import izbicki.jakub.loggingautoconfiguration.common.LoggingContext;
import izbicki.jakub.loggingautoconfiguration.common.LoggingUtils;
import java.io.IOException;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class OutcomingLoggingInterceptor implements ClientHttpRequestInterceptor {

  private final LoggingUtils loggingUtils;

  public OutcomingLoggingInterceptor(LoggingContext context) {
    this.loggingUtils = new LoggingUtils(context);
  }

  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body,
      ClientHttpRequestExecution execution) throws IOException {
    loggingUtils.log(request);

    ClientHttpResponse response = execution.execute(request, body);

    loggingUtils.log(response);
    return response;
  }
}
