package chen.e3.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import chen.e3.content.service.ContentService;
import chen.e3.entity.TbContent;
import chen.e3.entity.TbContentExample;
import chen.e3.entity.TbContentExample.Criteria;
import chen.e3.entity.TbItem;
import chen.e3.mapper.TbContentMapper;
import chen.e3.utils.E3Result;
import chen.e3.utils.EasyUIResult;
import chen.e3.utils.JsonUtils;
import chen.e3.utils.jedis.JedisClient;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
@Service
public class ContentServiceImpl implements ContentService {
	
	@Autowired
	private TbContentMapper mapper;
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${contentList_Hash}")
	private String contentList_Hash;
	
	public EasyUIResult getContentPageList(Long categoryId, int page,
			int rows) {
		//封装pageHelper
		PageHelper pageHelper=new PageHelper();
		pageHelper.startPage(page, rows);
		//查询数据库
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> list = mapper.selectByExample(example);
		//返回结果对象
		EasyUIResult easyUIResult=new EasyUIResult();
		easyUIResult.setRows(list);
		//封装pageinfo对象，查询total
		PageInfo<TbContent> pageInfo=new PageInfo<>(list);
		easyUIResult.setTotal(pageInfo.getTotal());
		return easyUIResult;
	}

	public E3Result addContent(TbContent tbContent) {
		tbContent.setCreated(new Date());
		tbContent.setUpdated(new Date());
		mapper.insert(tbContent);
		//同步缓存，其实就是删除缓存中对应的数据
		jedisClient.hdel(contentList_Hash, tbContent.getCategoryId().toString());
		return E3Result.ok();
	}

	public List<TbContent> getContentListByCid(Long categoryId) {
		//先查询缓存
		try {
			String json = jedisClient.hget(contentList_Hash,categoryId.toString());
			//如果有直接返回结果
			if(StringUtils.isNotBlank(json)){
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//没有查询数据库返回结果
		TbContentExample example=new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> list = mapper.selectByExampleWithBLOBs(example);
		//没有就存入缓存
		try {
			jedisClient.hset(contentList_Hash, categoryId.toString(), JsonUtils.objectToJson(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public E3Result editContent(TbContent tbContent) {
		tbContent.setUpdated(new Date());
		mapper.updateByPrimaryKeyWithBLOBs(tbContent);
		//同步缓存，其实就是删除缓存中对应的数据
		jedisClient.hdel(contentList_Hash, tbContent.getCategoryId().toString());
		return E3Result.ok();
	}

	public E3Result deleteContent(String ids) {
		String[] strings = ids.split(",");
		for (String id : strings) {
			TbContent tbContent = mapper.selectByPrimaryKey(Long.parseLong(id));
			mapper.deleteByPrimaryKey(Long.parseLong(id));
			//同步缓存，其实就是删除缓存中对应的数据
			jedisClient.hdel(contentList_Hash, tbContent.getCategoryId().toString());
		}
		return E3Result.ok();
	}

}
