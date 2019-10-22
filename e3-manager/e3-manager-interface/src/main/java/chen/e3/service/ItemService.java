package chen.e3.service;

import java.util.List;

import chen.e3.entity.TbItem;
import chen.e3.entity.TbItemDesc;
import chen.e3.utils.E3Result;
import chen.e3.utils.EasyUIResult;

public interface ItemService {

	TbItem getItemById(Long id);
	EasyUIResult getItemList(int page,int rows);
	E3Result addItem(TbItem tbItem,String desc);
	TbItemDesc getItemDesc(Long id);
	E3Result updateItem(TbItem tbItem, String desc);
	E3Result deleteItem(String ids);
	E3Result reshelfItem(String ids);
	E3Result instockItem(String ids);
}
