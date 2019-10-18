package izbicki.jakub.servicesweb.controller;

import izbicki.jakub.servicesweb.config.ServiceConfig;
import java.util.concurrent.ThreadLocalRandom;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController("/")
public class ServiceController {

  private static final String GREETING = "Hello";

  private static final String COUNTER_HEADER = "counter";

  private static final int LAST_CALL = 0;

  @Autowired
  private ServiceConfig config;

  @Autowired
  private RestTemplate restTemplate;

  @GetMapping
  public String getGreeting(HttpServletRequest request) throws InterruptedException {
    simulateCalculateLogic();

    if (!isLastCall(request)) {
      chainCall(request);
    }

    return GREETING;
  }

  private void simulateCalculateLogic() throws InterruptedException {
    long time = ThreadLocalRandom.current().nextInt(config.getMaxCalculationTimeMs());
    Thread.sleep(time);
  }

  private boolean isLastCall(HttpServletRequest request) {
    return getCounter(request) <= LAST_CALL;
  }

  private int getCounter(HttpServletRequest request) {
    String counter = request.getHeader(COUNTER_HEADER);
    return counter == null || Long.valueOf(counter) <= LAST_CALL ?
        LAST_CALL :
        Integer.valueOf(counter);
  }

  private void chainCall(HttpServletRequest request) {
    String path = getPath();
    HttpHeaders headers = getHeaders(request);

    restTemplate.exchange(path, HttpMethod.GET, new HttpEntity<>(headers), String.class);
  }

  private HttpHeaders getHeaders(HttpServletRequest request) {
    HttpHeaders headers = new HttpHeaders();
    String newCounter = String.valueOf(getCounter(request) - 1);
    headers.add(COUNTER_HEADER, newCounter);
    return headers;
  }

  private String getPath() {
    int index = ThreadLocalRandom.current().nextInt(config.getAvailableNodes().length);
    return config.getAvailableNodes()[index];
  }
}
