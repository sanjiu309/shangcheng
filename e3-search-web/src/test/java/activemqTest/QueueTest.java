package activemqTest;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

/**
 * queue模式生产者
 * @author chen
 *
 */
public class QueueTest {

		@Test
		public void testQueueProducer() throws Exception{
			/*第一步：创建ConnectionFactory对象，需要指定服务端ip及端口号。
第二步：使用ConnectionFactory对象创建一个Connection对象。
第三步：开启连接，调用Connection对象的start方法。
第四步：使用Connection对象创建一个Session对象。
第五步：使用Session对象创建一个Destination对象（topic、queue），此处创建一个Queue对象。
第六步：使用Session对象创建一个Producer对象。
第七步：创建一个Message对象，创建一个TextMessage对象。
第八步：使用Producer对象发送消息。
第九步：关闭资源。*/
			ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://192.168.25.129:61616");
			Connection connection=connectionFactory.createConnection();
			connection.start();
	//第一个参数：是否开启事务。true：开启事务，第二个参数忽略。
	//第二个参数：当第一个参数为false时，才有意义。消息的应答模式。1、自动应答2、手动应答。一般是自动应答。
			Session session = connection.createSession(false, 1);
			//创建queue或topic，队列名称
			Queue queue = session.createQueue("queue01");
			MessageProducer producer = session.createProducer(queue);
			TextMessage message1=new ActiveMQTextMessage();
			message1.setText("hello Wolrd!!!");
			TextMessage message2 = session.createTextMessage();
			message2.setText("xinqiu!!!");
			
			producer.send(message1);
			
			producer.close();
			session.close();
			connection.close();
		}
		
		@Test
		public void testQueueConsumer() throws Exception{
			/*
			 * 第一步：创建一个ConnectionFactory对象。
第二步：从ConnectionFactory对象中获得一个Connection对象。
第三步：开启连接。调用Connection对象的start方法。
第四步：使用Connection对象创建一个Session对象。
第五步：使用Session对象创建一个Destination对象。和发送端保持一致queue，并且队列的名称一致。
第六步：使用Session对象创建一个Consumer对象。
第七步：接收消息。
第八步：打印消息。
第九步：关闭资源
			 */
			ConnectionFactory factory=new ActiveMQConnectionFactory("tcp://192.168.25.129:61616");
			Connection connection = factory.createConnection();
			connection.start();
			
			Session session = connection.createSession(false, 1);
			Queue queue = session.createQueue("queue01");
			MessageConsumer consumer = session.createConsumer(queue);
			
			consumer.setMessageListener(new MessageListener() {
				
				@Override
				public void onMessage(Message message){
					try {
						TextMessage textMessage=(TextMessage) message;
						System.out.println(textMessage.getText());
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			});
			
			System.in.read();
			
			consumer.close();
			session.close();
			connection.close();
		}
}
