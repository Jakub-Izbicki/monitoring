package izbicki.jakub.loggingautoconfiguration.incoming;

import izbicki.jakub.loggingautoconfiguration.common.LoggingContext;
import izbicki.jakub.loggingautoconfiguration.common.LoggingUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class IncomingLoggingInterceptor implements HandlerInterceptor {

  private final LoggingUtils loggingUtils;

  public IncomingLoggingInterceptor(LoggingContext context) {
    this.loggingUtils = new LoggingUtils(context);
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    loggingUtils.log(request);
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) {
    loggingUtils.log(response);
  }
}
