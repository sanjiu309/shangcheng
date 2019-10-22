package chen.e3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import chen.e3.entity.TbItem;
import chen.e3.entity.TbItemDesc;
import chen.e3.service.ItemService;
import chen.e3.utils.E3Result;
import chen.e3.utils.EasyUIResult;

/**
 * 商品管理
 * @author 陈
 *
 */
@Controller
public class ItemController {

	@Autowired
	private ItemService service;
	
	@RequestMapping("/item/{itemId}")
	@ResponseBody
	public TbItem getItemById(@PathVariable Long itemId){
		return service.getItemById(itemId);
	}
	
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIResult getItemList(int page,int rows){
		return service.getItemList(page, rows);
	}
	
	@RequestMapping("/item/save")
	@ResponseBody
	public E3Result addItem(TbItem tbItem,String desc){
		try {
			return service.addItem(tbItem, desc);
		} catch (Exception e) {
			e.printStackTrace();
			return E3Result.build(123, "插入失败");
		}
	}
	
	@RequestMapping("/item/query/desc/{id}")
	@ResponseBody
	public TbItemDesc getItemDesc(@PathVariable Long id){
		return service.getItemDesc(id);
	}
	
	@RequestMapping("/item/update")
	@ResponseBody
	public E3Result updateItem(TbItem tbItem,String desc){
		return service.updateItem(tbItem,desc);
	}
	
	@RequestMapping("/item/delete")
	@ResponseBody
	public E3Result deleteItem(String ids){
		return service.deleteItem(ids);
	}
	
	//下架
	@RequestMapping("/item/instock")
	@ResponseBody
	public E3Result instockItem(String ids){
		return service.instockItem(ids);
	}
	
	//上架
	@RequestMapping("/item/reshelf")
	@ResponseBody
	public E3Result reshelfItem(String ids){
		return service.reshelfItem(ids);
	}
}
