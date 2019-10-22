package chen.e3.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import chen.e3.entity.SearchResult;
import chen.e3.search.service.SearchItemService;

@Controller
public class SearchController {

	@Autowired
	private SearchItemService service;
	
	@Value("${SEARCH_ROWS}")
	private int SEARCH_ROWS;
	
	@RequestMapping("/search")
	public String searchItemList(@RequestParam(value="keyword") String keyword,
			@RequestParam(defaultValue="1") int page,Model model) throws Exception{
		keyword= new String(keyword.getBytes("iso-8859-1"), "utf-8");
		
		SearchResult searchResult = service.getSearchItem(keyword, page, SEARCH_ROWS);
		model.addAttribute("query", keyword);
		model.addAttribute("totalPages", searchResult.getTotalPages());
		model.addAttribute("page", page);
		model.addAttribute("recourdCount", searchResult.getRecordCount());

		model.addAttribute("itemList", searchResult.getItemList());
		
		return "search";
	}
}
