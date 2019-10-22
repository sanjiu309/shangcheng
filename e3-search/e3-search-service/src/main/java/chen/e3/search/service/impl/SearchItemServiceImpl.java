package chen.e3.search.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chen.e3.entity.SearchItem;
import chen.e3.entity.SearchResult;
import chen.e3.search.dao.SearchDao;
import chen.e3.search.mapper.ItemMapper;
import chen.e3.search.service.SearchItemService;
import chen.e3.utils.E3Result;
@Service
public class SearchItemServiceImpl implements SearchItemService {

	@Autowired
	private ItemMapper mapper;
	@Autowired
	private SolrServer server;
	@Autowired
	private SearchDao dao;
	
	public E3Result importAllItems() {
		try {
			List<SearchItem> itemList = mapper.getItemList();
			for (SearchItem searchItem : itemList) {
				SolrInputDocument document=new SolrInputDocument();
				document.addField("id", searchItem.getId());
				document.addField("item_title", searchItem.getTitle());
				document.addField("item_sell_point", searchItem.getSell_point());
				document.addField("item_price", searchItem.getPrice());
				document.addField("item_image", searchItem.getImage());
				document.addField("item_category_name", searchItem.getName());

				server.add(document);
			}
			server.commit();
			return E3Result.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return E3Result.build(500, "添加失败!");
		}
	}

	@Override
	public SearchResult getSearchItem(String keyWord,int page,int rows) throws Exception{	
			SolrQuery query=new SolrQuery();
			query.set("q", keyWord);
			if(page<=0){page=1;}
			query.set("start", (page-1)*rows);
			query.set("rows", rows);
			query.set("df", "item_title");
			query.setHighlight(true);
			query.addHighlightField("item_title");
			query.setHighlightSimplePre("<em style=\"color:red\">");
			query.setHighlightSimplePost("/<em>");
			
			SearchResult result = dao.getResult(query);
			long pages=result.getRecordCount()/rows;
			if(result.getRecordCount()%rows>0){pages++;}
			result.setTotalPages(pages);
			return result;
	}

}
