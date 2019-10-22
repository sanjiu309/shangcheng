package chen.e3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import chen.e3.service.ItemCatService;
import chen.e3.utils.TreeNode;

/**
 * 商品目录
 * @author 陈
 *
 */
@Controller
public class ItemCatController {

	@Autowired
	private ItemCatService service;
	
	@RequestMapping("/item/cat/list")
	@ResponseBody
	public List<TreeNode> getItemCat(@RequestParam(name="id",defaultValue="0")Long parentId){
		return service.getItemCatList(parentId);
	}
}
