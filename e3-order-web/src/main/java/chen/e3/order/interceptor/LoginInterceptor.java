package chen.e3.order.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import chen.e3.cart.service.CartService;
import chen.e3.entity.TbItem;
import chen.e3.entity.TbUser;
import chen.e3.sso.service.TokenService;
import chen.e3.utils.CookieUtils;
import chen.e3.utils.E3Result;
import chen.e3.utils.JsonUtils;

public class LoginInterceptor implements HandlerInterceptor {

	@Autowired
	private TokenService tokenService;
	@Autowired
	private CartService cartService;
	@Value("${SSO_URL}")
	private String SSO_URL;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String token = CookieUtils.getCookieValue(request, "token");
		if(StringUtils.isBlank(token)){
			//返回登录，且登录后返回原路径
			response.sendRedirect(SSO_URL + "/page/login?redirect=" + request.getRequestURL());
			return false;
		}
		//根据token取用户信息
		E3Result result = tokenService.getUserByToken(token);
		if(result.getStatus() != 200){
			//取不到
			response.sendRedirect(SSO_URL + "/page/login?redirect=" + request.getRequestURL());
			return false;
		}
		//取到
		TbUser user=(TbUser)result.getData();
		request.setAttribute("user", user);
		//判断cookie是否有数据，有存入redis
		String jsonCartList = CookieUtils.getCookieValue(request, "cart", true);
		if(StringUtils.isNotBlank(jsonCartList)){
			List<TbItem> cartList = JsonUtils.jsonToList(jsonCartList, TbItem.class);
			cartService.mergeCart(user.getId(), cartList);
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}


	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
}
