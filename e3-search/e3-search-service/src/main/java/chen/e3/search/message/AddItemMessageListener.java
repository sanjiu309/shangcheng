package chen.e3.search.message;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import chen.e3.entity.SearchItem;
import chen.e3.search.mapper.ItemMapper;

public class AddItemMessageListener implements MessageListener {

	@Autowired
	private ItemMapper dao;
	@Autowired
	private SolrServer server;
	
	@Override
	public void onMessage(Message message) {
		try {
			TextMessage textMessage=(TextMessage) message;
			String id = textMessage.getText();
			
			//等待一会，为了让添加商品事务提交完成，再进行商品查询，也可以在添加商品controller中
			//发送消息
			Thread.sleep(1000);
			
			SearchItem searchItem = dao.getItemById(id);
			
			SolrInputDocument document=new SolrInputDocument();
			document.addField("id", searchItem.getId());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSell_point());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getName());
			
			server.add(document);
			server.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
