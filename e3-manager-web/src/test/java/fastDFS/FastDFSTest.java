package fastDFS;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import chen.e3.utils.FastDFSClient;


public class FastDFSTest {

	@Test
	public void testUpload() throws Exception{
		//创建一个配置文件，内容是tracker服务器的地址
		//使用全局对象加载配置文件
		ClientGlobal.init("F:/project/e3-manager-web/src/main/resources/conf/client.conf");
		//创建一个trackerClient对象
		TrackerClient trackerClient=new TrackerClient();
		//通过trackerClient获得trackerServer对象
		TrackerServer trackerServer = trackerClient.getConnection();
		//创建一个StorageServer的引用，可为null
		StorageServer storageServer=null;
		//创建一个StorageClient，参数需要trackerServer，StorageServer
		StorageClient storageClient=new StorageClient(trackerServer, storageServer);
		//使用StorageClient上传文件
//		String[] strings = storageClient.upload_file("G:/test/ab40ceaa848548af9fb28b73408b2881.jpg", "jpg", null);
//		for (String string : strings) {
//			System.out.println(string);
//		}
	}
	
	@Test
	public void testUploadUtils() throws Exception{
		FastDFSClient fastDFSClient=new FastDFSClient("F:/project/e3-manager-web/src/main/resources/conf/client.conf");
//		String string = fastDFSClient.uploadFile("C:/Users/陈/Pictures/lovewallpaper/81590-106.jpg");
//		System.out.println(string);
	}
}
