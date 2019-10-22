package chen.e3.search.service;

import chen.e3.entity.SearchResult;
import chen.e3.utils.E3Result;

public interface SearchItemService {

	E3Result importAllItems();
	
	SearchResult getSearchItem(String keyWord,int page,int rows) throws Exception;
}
