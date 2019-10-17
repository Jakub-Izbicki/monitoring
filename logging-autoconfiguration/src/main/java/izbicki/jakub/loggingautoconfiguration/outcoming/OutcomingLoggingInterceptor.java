package izbicki.jakub.loggingautoconfiguration.outcoming;

import izbicki.jakub.loggingautoconfiguration.logging.LoggingUtils;
import java.io.IOException;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class OutcomingLoggingInterceptor implements ClientHttpRequestInterceptor {

  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body,
      ClientHttpRequestExecution execution) throws IOException {
    LoggingUtils.setCorrelationIdIfMissing(request);
    LoggingUtils.log(request);

    ClientHttpResponse response = execution.execute(request, body);

    LoggingUtils.setCorrelationIdIfMissing(response);
    LoggingUtils.log(response);
    return response;
  }
}
