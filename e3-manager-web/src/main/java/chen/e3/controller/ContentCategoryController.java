package chen.e3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import chen.e3.content.service.ContentCategoryService;
import chen.e3.utils.E3Result;
import chen.e3.utils.EasyUIResult;
import chen.e3.utils.TreeNode;
/**
 * 内容分类
 * @author 陈
 *
 */
@Controller
public class ContentCategoryController {

	@Autowired
	private ContentCategoryService service;
	
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<TreeNode> getContentCategoryList(@RequestParam(name="id",defaultValue="0")Long parentId){
		return service.getContentCategoryList(parentId);
	}

	@RequestMapping(value="/content/category/create",method=RequestMethod.POST)
	@ResponseBody
	public E3Result addContentCategory(Long parentId,String name){
		return service.addContentCate(parentId, name);
	}
	
	@RequestMapping(value="/content/category/update")
	public void updateContentCategory(Long id,String name){
		service.updateContentCategory(id,name);
	}
	
	@RequestMapping(value="/content/category/delete")
	@ResponseBody
	public E3Result deleteContentCategory(Long id){
		return service.deleteContentCategory(id);
	}
	
}
