package activemqTest;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

public class TopicTest {

	@Test
	public void testTopicProducer() throws Exception{
		/*
		 * 第一步：创建ConnectionFactory对象，需要指定服务端ip及端口号。
第二步：使用ConnectionFactory对象创建一个Connection对象。
第三步：开启连接，调用Connection对象的start方法。
第四步：使用Connection对象创建一个Session对象。
第五步：使用Session对象创建一个Destination对象（topic、queue），此处创建一个Topic对象。
第六步：使用Session对象创建一个Producer对象。
第七步：创建一个Message对象，创建一个TextMessage对象。
第八步：使用Producer对象发送消息。
第九步：关闭资源。
		 */
		ConnectionFactory factory=new ActiveMQConnectionFactory("tcp://192.168.25.129:61616");
		Connection connection = factory.createConnection();
		connection.start();
		
		Session session = connection.createSession(false, 1);
		
		Topic topic = session.createTopic("topic");
		
		MessageProducer producer = session.createProducer(topic);
		
		TextMessage message1=new ActiveMQTextMessage();
		message1.setText("topic hello world!!!");
		
		TextMessage message2 = session.createTextMessage();
		message2.setText("topic xinqiu!!!");
		
		producer.send(message2);
		
		producer.close();
		session.close();
		connection.close();
	}
	
	@Test
	public void testTopicConsumer() throws Exception{
		/*
		 * 第一步：创建一个ConnectionFactory对象。
第二步：从ConnectionFactory对象中获得一个Connection对象。
第三步：开启连接。调用Connection对象的start方法。
第四步：使用Connection对象创建一个Session对象。
第五步：使用Session对象创建一个Destination对象。和发送端保持一致topic，并且话题的名称一致。
第六步：使用Session对象创建一个Consumer对象。
第七步：接收消息。
第八步：打印消息。
第九步：关闭资源
		 */
		ConnectionFactory factory=new ActiveMQConnectionFactory("tcp://192.168.25.129:61616");
		Connection connection = factory.createConnection();
		connection.start();
		
		Session session = connection.createSession(false, 1);
		Topic topic = session.createTopic("topic");
		
		MessageConsumer consumer = session.createConsumer(topic);
		
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message arg0) {
				try {
					TextMessage message=(TextMessage) arg0;
					System.out.println(message.getText());
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
			}
		});
		System.out.println("topic的消费端3已经启动。。。。。");
		System.in.read();
		
		consumer.close();
		session.close();
		connection.close();
	}
	
	public void testTopicConsumer1() throws Exception {
		// 第一步：创建一个ConnectionFactory对象。
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.129:61616");
		// 第二步：从ConnectionFactory对象中获得一个Connection对象。
		Connection connection = connectionFactory.createConnection();
		// 第三步：开启连接。调用Connection对象的start方法。
		connection.start();
		// 第四步：使用Connection对象创建一个Session对象。
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 第五步：使用Session对象创建一个Destination对象。和发送端保持一致topic，并且话题的名称一致。
		Topic topic = session.createTopic("topic");
		// 第六步：使用Session对象创建一个Consumer对象。
		MessageConsumer consumer = session.createConsumer(topic);
		// 第七步：接收消息。
		consumer.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Message message) {
				try {
					TextMessage textMessage = (TextMessage) message;
					String text = null;
					// 取消息的内容
					text = textMessage.getText();
					// 第八步：打印消息。
					System.out.println(text);
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		System.out.println("topic的消费端03。。。。。");
		// 等待键盘输入
		System.in.read();
		// 第九步：关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
}
