package chen.e3.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import chen.e3.entity.TbUser;
import chen.e3.sso.service.LoginService;
import chen.e3.utils.CookieUtils;
import chen.e3.utils.E3Result;

@Controller
public class LoginController {

	@Autowired
	private LoginService service;
	
	@RequestMapping("/page/login")
	public String toLogin(String redirect,Model model){
		model.addAttribute("redirect",redirect);
		return "login";
	}
	
	@RequestMapping(value="/user/login",method=RequestMethod.POST)
	@ResponseBody
	public E3Result login(TbUser user,HttpServletRequest request,HttpServletResponse response){
		E3Result result = service.login(user);
		if(result.getStatus()==200){
			String token = result.getData().toString();
				CookieUtils.setCookie(request, response, "token", token);
		}
		return result;
	}
}
