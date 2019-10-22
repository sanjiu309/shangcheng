package chen.e3.search.message;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;

import chen.e3.search.mapper.ItemMapper;

public class MyMessageListener implements MessageListener {

	
	@Override
	public void onMessage(Message message) {
		try {
			TextMessage textMessage=(TextMessage) message;
			String text = textMessage.getText();
			System.out.println(text);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
