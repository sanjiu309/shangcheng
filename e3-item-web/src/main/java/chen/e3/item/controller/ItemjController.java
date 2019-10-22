package chen.e3.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import chen.e3.entity.TbItem;
import chen.e3.entity.TbItemDesc;
import chen.e3.item.entity.Item;
import chen.e3.service.ItemService;

@Controller
public class ItemjController {
	
	@Autowired
	private ItemService service;
	
	@RequestMapping("/item/{itemId}")
	public String itemDetail(@PathVariable(value="itemId") Long itemId,Model model){
		TbItem tbItem = service.getItemById(itemId);
		Item item=new Item(tbItem);
		TbItemDesc itemDesc = service.getItemDesc(itemId);
		
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", itemDesc);
		return "item";
	};

}
