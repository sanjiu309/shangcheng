package chen.e3.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import chen.e3.entity.TbUser;
import chen.e3.sso.service.TokenService;
import chen.e3.utils.CookieUtils;
import chen.e3.utils.E3Result;

public class LoginInterceptor implements HandlerInterceptor {

	@Autowired
	private TokenService tokenService;
	
	// 执行handler(方法)之前执行此方法，返回true放行
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		// 从cookie中取token
		String token = CookieUtils.getCookieValue(request, "token");
		// 没有返回false ①
		if(StringUtils.isBlank(token)){
			return true;
		}
		// 有，调用sso，根据token取用户信息
		E3Result result = tokenService.getUserByToken(token);
		// 没有取到，过期了 ②
		if(result.getStatus()!=200){
			return true;
		}
		// 取到了，已登录
		TbUser user=(TbUser)result.getData();
		// 把用户信息放到request中 ③
		request.setAttribute("user", user);
		// 三种情况都应该让用户使用购物车，所以全都要放行
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// handler执行之后，返回modelAndView之前
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception exception) throws Exception {
		// 完成处理,返回modelAndView之后

	}

}
