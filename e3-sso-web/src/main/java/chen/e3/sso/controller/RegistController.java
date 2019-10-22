package chen.e3.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import chen.e3.entity.TbUser;
import chen.e3.sso.service.RegisterService;
import chen.e3.utils.E3Result;

@Controller
public class RegistController {

	@Autowired
	private RegisterService service;
	
	@RequestMapping("/page/register")
	public String goRegister(){
		return "register";
	}
	
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public E3Result checkData(@PathVariable String param,@PathVariable int type){
		return service.checkData(param, type);
	}
	
	@RequestMapping(value="/user/register",method=RequestMethod.POST)
	@ResponseBody
	public E3Result register(TbUser user){
		return service.register(user);
	}
	
}
