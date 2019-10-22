package chen.e3.content.service;

import java.util.List;

import chen.e3.entity.TbContent;
import chen.e3.utils.E3Result;
import chen.e3.utils.EasyUIResult;

public interface ContentService {
	
	public EasyUIResult getContentPageList(Long categoryId, int page,int rows);

	public E3Result addContent(TbContent tbContent);
	
	public List<TbContent> getContentListByCid(Long categoryId);

	public E3Result editContent(TbContent tbContent);

	public E3Result deleteContent(String ids);
}
