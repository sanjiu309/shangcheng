package test;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import chen.e3.entity.TbItem;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreeMakerTest {

	
	public void testFreeMaker() throws Exception{
		 //1.创建一个模板文件
		 //2.创建一个configuration对象
		Configuration configuration=new Configuration(Configuration.getVersion());
		 //3.设置模板文件保存目录
		configuration.setDirectoryForTemplateLoading(new File("F:/project/e3-item-web/src/main/webapp/WEB-INF/ftl"));
		 //4.设置模板文件字符集
		configuration.setDefaultEncoding("utf-8");
		 //5.加载一个模板文件，创建模板对象
		Template template = configuration.getTemplate("item.ftl");
		 //6.加载一个数据集。可是pojo或map(推荐)
		Map data =new HashMap<>();
		data.put("hello", "hello world!!!");
		
		TbItem tbItem=new TbItem();
		tbItem.setId(1l);
		tbItem.setTitle("灌灌灌灌");
		
		data.put("item", tbItem);
		 //7.创建writer对象，输出模板文件路径和文件
		Writer writer=new FileWriter(new File("F:/ftl/item.html"));
		//8.生成静态页面
		template.process(data, writer);
		//9.关闭流
		writer.close();
		 
		
	}
	
	@Test
	public void testFreeMaker1() throws Exception{
		 //1.创建一个模板文件
		 //2.创建一个configuration对象
		Configuration configuration=new Configuration(Configuration.getVersion());
		 //3.设置模板文件保存目录
		configuration.setDirectoryForTemplateLoading(new File("F:/project/e3-item-web/src/main/webapp/WEB-INF/ftl"));
		 //4.设置模板文件字符集
		configuration.setDefaultEncoding("utf-8");
		 //5.加载一个模板文件，创建模板对象
		Template template = configuration.getTemplate("item.ftl");
		 //6.加载一个数据集。可是pojo或map(推荐)
		Map data =new HashMap<>();
		data.put("hello", "hello world!!!");
		
		
		List<Student> list=new ArrayList<>();
		list.add(new Student(111,"王五"));
		list.add(new Student(222,"嗷嗷待食"));
		list.add(new Student(333,"阿桑的歌五"));
		list.add(new Student(444,"不能"));
		list.add(new Student(555,"热舞特人"));
		list.add(new Student(666,"噶山豆根"));
		
		data.put("studentList", list);
		
		data.put("date", new Date());
		 //7.创建writer对象，输出模板文件路径和文件
		Writer writer=new FileWriter(new File("F:/ftl/item.html"));
		//8.生成静态页面
		template.process(data, writer);
		//9.关闭流
		writer.close();
		 
		
	}
}
