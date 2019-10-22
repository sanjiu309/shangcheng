package chen.e3.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import chen.e3.utils.FastDFSClient;
import chen.e3.utils.JsonUtils;

/**
 * 图片上传
 * @author 陈
 *
 */
@Controller
public class PictureController {

	@Value("${IMAGE_SERVER_URL}")
	private String IMAGE_SERVER_URL;
	
	@RequestMapping(value="/pic/upload",produces=MediaType.TEXT_PLAIN_VALUE+";charset=utf-8")
	@ResponseBody
	public String uploadPic(MultipartFile uploadFile){
		try {
			//使用工具类上传图片
			FastDFSClient fastDFSClient=new FastDFSClient("classpath:conf/client.conf");
			//取文件扩展名
			String filename = uploadFile.getOriginalFilename();
			String ext = filename.substring(filename.lastIndexOf(".")+1);
			//上传图片，得到图片地址和文件名
			String url = fastDFSClient.uploadFile(uploadFile.getBytes(), ext);
			//补充成完整的url
			url=IMAGE_SERVER_URL+url;
			//返回map
			Map result=new HashMap();
			result.put("error", 0);
			result.put("url", url);
			return JsonUtils.objectToJson(result);
		} catch (Exception e) {
			e.printStackTrace();
			Map result=new HashMap();
			result.put("error", 1);
			result.put("message", "图片上传失败");
			return JsonUtils.objectToJson(result);
		}
	}
}
