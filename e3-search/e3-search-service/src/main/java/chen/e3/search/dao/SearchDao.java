package chen.e3.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import chen.e3.entity.SearchItem;
import chen.e3.entity.SearchResult;

@Repository
public class SearchDao {

	@Autowired
	private SolrServer server;
	
	public SearchResult getResult(SolrQuery query) throws Exception{
		SearchResult result=new SearchResult();
		
		QueryResponse response = server.query(query);
		SolrDocumentList documentList = response.getResults();
		
		result.setRecordCount(documentList.getNumFound());
		
		List<SearchItem> itemList=new ArrayList<>();
		
		for (SolrDocument solrDocument : documentList) {
			SearchItem item=new SearchItem();
			item.setId((String)solrDocument.get("id"));
			Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			String title;
			if(list !=null && list.size()>0){
				title=list.get(0);
			}else {
				title=(String) solrDocument.get("item_title");
			}
			item.setTitle(title);
			item.setSell_point((String)solrDocument.get("item_sell_point"));
			item.setPrice((long)solrDocument.get("item_price"));
			item.setImage((String)solrDocument.get("item_image"));
			item.setName((String)solrDocument.get("item_category_name"));
			
			itemList.add(item);
		}
		result.setItemList(itemList);
		return result;
	}
}
