package chen.e3.item.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import chen.e3.entity.TbItem;
import chen.e3.entity.TbItemDesc;
import chen.e3.item.entity.Item;
import chen.e3.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class HtmlGenListener implements MessageListener {

	@Autowired
	private ItemService service;
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	@Value("${HTML_PATH}")
	private String HTML_PATH;
	
	@Override
	public void onMessage(Message message) {
		TextMessage textMessage=(TextMessage) message;
		try {
			String id = textMessage.getText();
			
			//
			Thread.sleep(1000);
			
			TbItem tbItem = service.getItemById(Long.parseLong(id));
			Item item=new Item(tbItem);
			TbItemDesc itemDesc = service.getItemDesc(Long.parseLong(id));
			
			Map map=new HashMap<>();
			map.put("item", item);
			map.put("itemDesc", itemDesc);
			
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			Template template = configuration.getTemplate("item.ftl");
			
			Writer writer=new FileWriter(new File(HTML_PATH+id+".html"));
			template.process(map, writer);
			
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
