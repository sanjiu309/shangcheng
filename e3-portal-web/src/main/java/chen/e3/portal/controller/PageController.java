package chen.e3.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import chen.e3.content.service.ContentService;
import chen.e3.entity.TbContent;

@Controller
public class PageController {

	@Autowired
	private ContentService service;
	
	@Value("${categoryId}")
	private Long categoryId;
	
	@RequestMapping("/index")
	public String toIndex(Model model){
		//查询轮播图列表
		List<TbContent> ad1List = service.getContentListByCid(categoryId);
		model.addAttribute("ad1List", ad1List);
		return "index";
	}
}
