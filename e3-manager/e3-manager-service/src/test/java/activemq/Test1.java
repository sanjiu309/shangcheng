package activemq;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.broker.region.Queue;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class Test1 {

	
	//@Test
	public void sendMessage() throws Exception{
		ApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
		
		JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);
		Destination destination=(Destination) applicationContext.getBean("queueDestination");
		jmsTemplate.send(destination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage("send message!!!");
			}
		});
	}
	
	//@Test
	public void reciveMessage() throws Exception{
		ApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
		
		JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);
		Destination destination=(Destination) applicationContext.getBean("queueDestination");
		TextMessage message = (TextMessage) jmsTemplate.receive(destination);
		System.out.println(message.getText());
	}
}
