package test;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class SolrCloudTest {

	@Test
	public void addItem() throws Exception{
		CloudSolrServer server=new CloudSolrServer("192.168.25.129:2191,192.168.25.129:2192,192.168.25.129:2193");
		server.setDefaultCollection("collection2");
		SolrInputDocument document=new SolrInputDocument();
		document.addField("id", "doc01");
		document.addField("item_title", "测试商品01");
		document.addField("item_price", 1000);
		server.add(document);
		server.commit();
	}
	
	@Test
	public void deleteItem() throws Exception{
		SolrServer server=new HttpSolrServer("http://192.168.25.129:8080/solr/collection1");
		
		server.deleteById("doc01");
		server.commit();
	}
	
	@Test
	public void selectItem() throws Exception{
		CloudSolrServer server=new CloudSolrServer("192.168.25.129:2191,192.168.25.129:2192,192.168.25.129:2193");
		server.setDefaultCollection("collection2");
		SolrQuery query=new SolrQuery();
		query.setQuery("*:*");
		QueryResponse response = server.query(query);
		SolrDocumentList results = response.getResults();
		System.err.println(results.getNumFound());
//		for (SolrDocument solrDocument : results) {
//			System.err.println(solrDocument.get("id"));
//			System.err.println(solrDocument.get("item_title"));
//			System.err.println(solrDocument.get("item_sell_point"));
//			System.err.println(solrDocument.get("item_price"));
//			System.err.println(solrDocument.get("item_image"));
//			System.err.println(solrDocument.get("item_category_name"));
//		}
	}
	
	@Test
	public void selectItemFuZa() throws Exception{
		SolrServer server=new HttpSolrServer("http://192.168.25.129:8080/solr/collection1");
		SolrQuery query=new SolrQuery();
		query.set("q", "手机");
		query.set("start", 0);
		query.set("rows", 5);
		query.set("df", "item_title");
		query.setHighlight(true);
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<em>");
		query.setHighlightSimplePost("/<em>");
		
		QueryResponse response = server.query(query);
		SolrDocumentList results = response.getResults();
		System.err.println(results.getNumFound());
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		for (SolrDocument solrDocument : results) {
			System.err.println(solrDocument.get("id"));
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			String title;
			if(list !=null && list.size()>0){
				title=list.get(0);
			}else {
				title=(String) solrDocument.get("item_title");
			}
//			System.err.println(title);
//			System.err.println(solrDocument.get("item_sell_point"));
//			System.err.println(solrDocument.get("item_price"));
//			System.err.println(solrDocument.get("item_image"));
//			System.err.println(solrDocument.get("item_category_name"));
		}
	}
}
