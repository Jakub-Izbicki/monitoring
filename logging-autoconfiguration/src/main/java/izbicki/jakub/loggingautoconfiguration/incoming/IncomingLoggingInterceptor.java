package izbicki.jakub.loggingautoconfiguration.incoming;

import izbicki.jakub.loggingautoconfiguration.common.LoggingContext;
import izbicki.jakub.loggingautoconfiguration.common.LoggingUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class IncomingLoggingInterceptor implements HandlerInterceptor {

  private static final String ERROR_PAGE = "/error";

  private final LoggingUtils loggingUtils;

  public IncomingLoggingInterceptor(LoggingContext context) {
    this.loggingUtils = new LoggingUtils(context);
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    if (!ERROR_PAGE.equals(request.getRequestURI())) {
      loggingUtils.setRequestStart();
      loggingUtils.log(request);
    }

    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) {
    loggingUtils.setRequestStop();
    loggingUtils.log(response);
  }
}
