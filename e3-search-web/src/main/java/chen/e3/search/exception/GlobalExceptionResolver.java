package chen.e3.search.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class GlobalExceptionResolver implements HandlerExceptionResolver {

	private static final Logger logger=LoggerFactory.getLogger(GlobalExceptionResolver.class);
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, 
			Object method,Exception ex) {
		logger.error("系统发生异常",ex);
		
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.addObject("message", "系统发生异常");
		modelAndView.setViewName("error/exception");
		return modelAndView;
	}

}
