package chen.e3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import chen.e3.content.service.ContentService;
import chen.e3.entity.TbContent;
import chen.e3.utils.E3Result;
import chen.e3.utils.EasyUIResult;

@Controller
public class ContentController {

	@Autowired
	private ContentService service;
	
	@RequestMapping(value="/content/query/list")
	@ResponseBody
	public EasyUIResult getContentPageList(Long categoryId,int page,int rows){
		return service.getContentPageList(categoryId,page,rows);
	}
	
	@RequestMapping(value="/content/save")
	@ResponseBody
	public E3Result addContent(TbContent tbContent){
		return service.addContent(tbContent);
	}
	
	@RequestMapping(value="/rest/content/edit")
	@ResponseBody
	public E3Result editContent(TbContent tbContent){
		return service.editContent(tbContent);
	}
	
	@RequestMapping(value="/content/delete")
	@ResponseBody
	public E3Result deleteContent(String ids){
		return service.deleteContent(ids);
	}
}
