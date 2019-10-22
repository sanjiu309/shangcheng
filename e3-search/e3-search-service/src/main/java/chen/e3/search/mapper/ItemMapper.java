package chen.e3.search.mapper;

import java.util.List;

import chen.e3.entity.SearchItem;

public interface ItemMapper {

	List<SearchItem> getItemList();
	
	SearchItem getItemById(String id);
}
