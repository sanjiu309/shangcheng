package activemqTest;

import java.io.IOException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * queue模式生产者
 * @author chen
 *
 */
public class QueueProvider {

		@Test
		public void testProvider() throws Exception{
			ApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
			System.in.read();
		}
}
