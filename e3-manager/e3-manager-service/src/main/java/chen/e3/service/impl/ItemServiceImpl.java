package chen.e3.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import chen.e3.entity.TbItem;
import chen.e3.entity.TbItemDesc;
import chen.e3.entity.TbItemExample;
import chen.e3.mapper.TbItemDescMapper;
import chen.e3.mapper.TbItemMapper;
import chen.e3.service.ItemService;
import chen.e3.utils.E3Result;
import chen.e3.utils.EasyUIResult;
import chen.e3.utils.IDUtils;
import chen.e3.utils.JsonUtils;
import chen.e3.utils.jedis.JedisClient;
/**
 * 商品管理
 * @author 陈
 *
 */
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemDao;
	@Autowired
	private TbItemDescMapper itemDescDao;
	@Autowired
	private JmsTemplate template;
	@Resource
	private Destination topicDestination;
	@Autowired
	private JedisClient jedisClient;
	@Value("${REDIS_ITEM_PRE}")
	private String REDIS_ITEM_PRE;
	@Value("${REDIS_CACHE_SECOND}")
	private Integer REDIS_CACHE_SECOND;
	
	public TbItem getItemById(Long id) {
		//查询缓存
		try {
			String json = jedisClient.get(REDIS_ITEM_PRE+":"+id+":BASE");
			//如果缓存中有，直接返回
			if(StringUtils.isNotBlank(json)){
				TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
				return tbItem;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//没有缓存。查询数据库
		TbItem tbItem = itemDao.selectByPrimaryKey(id);
		
		//将数据库中新查询的数据添加到缓存
		try {
			jedisClient.set(REDIS_ITEM_PRE+":"+id+":BASE", JsonUtils.objectToJson(tbItem));
			//设置过期时间
			jedisClient.expire(REDIS_ITEM_PRE+":"+id+":BASE", REDIS_CACHE_SECOND);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tbItem;
	}

	public EasyUIResult getItemList(int page, int rows) {
		//封装pageHelper
		PageHelper pageHelper=new PageHelper();
		pageHelper.startPage(page, rows);
		//查询数据库
		List<TbItem> list = itemDao.selectByExample(new TbItemExample());
		//返回结果对象
		EasyUIResult easyUIResult=new EasyUIResult();
		easyUIResult.setRows(list);
		//封装pageinfo对象，查询total
		PageInfo<TbItem> pageInfo=new PageInfo<>(list);
		easyUIResult.setTotal(pageInfo.getTotal());
		return easyUIResult;
	}

	public E3Result addItem(TbItem tbItem, String desc) {
		//生成商品id，补全商品对象属性
		final Long itemId=IDUtils.genItemId();
		tbItem.setId(itemId);
		tbItem.setStatus((byte) 1);//1正常，2下架，3删除
		tbItem.setCreated(new Date());
		tbItem.setUpdated(new Date());
		
		itemDao.insert(tbItem);
		//创建商品描述对象，补全属性
		TbItemDesc itemDesc=new TbItemDesc();
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		
		itemDescDao.insert(itemDesc);
		//同步缓存。删除原缓存中的数据
		jedisClient.del(REDIS_ITEM_PRE+":"+itemId+":BASE");
		jedisClient.del(REDIS_ITEM_PRE+":"+itemId+":DESC");
		
		template.send(topicDestination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(itemId+"");
			}
		});
		//返回成功
		return E3Result.ok();
	}

	public TbItemDesc getItemDesc(Long id) {
		//查询缓存
		try {
			String json = jedisClient.get(REDIS_ITEM_PRE + ":" + id + ":DESC");
			// 如果缓存中有，直接返回
			if (StringUtils.isNotBlank(json)) {
				TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return tbItemDesc;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//查询数据库
		TbItemDesc tbItemDesc = itemDescDao.selectByPrimaryKey(id);
		
		// 将数据库中新查询的数据添加到缓存
		try {
			jedisClient.set(REDIS_ITEM_PRE + ":" + id + ":DESC", JsonUtils.objectToJson(tbItemDesc));
			// 设置过期时间
			jedisClient.expire(REDIS_ITEM_PRE + ":" + id + ":DESC", REDIS_CACHE_SECOND);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tbItemDesc;
	}

	public E3Result updateItem(TbItem tbItem, String desc) {
		try {
			//更新修改时间,并修改更新数据库
			tbItem.setUpdated(new Date());
			itemDao.updateByPrimaryKeySelective(tbItem);
			//根据上传的商品查询对应的商品描述对象，更新描述和修改时间,并修改更新数据库
			TbItemDesc itemDesc = itemDescDao.selectByPrimaryKey(tbItem.getId());
			itemDesc.setUpdated(new Date());
			itemDesc.setItemDesc(desc);
			itemDescDao.updateByPrimaryKeySelective(itemDesc);
			
			//同步缓存。删除原缓存中的数据
			jedisClient.del(REDIS_ITEM_PRE+":"+tbItem.getId()+":BASE");
			jedisClient.del(REDIS_ITEM_PRE+":"+tbItem.getId()+":DESC");
			
			return E3Result.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return E3Result.build(123, "修改失败");
		}
	}

	public E3Result deleteItem(String ids) {
		try {
			String[] split = ids.split(",");
			//循环遍历要删除的id
			for (String id : split) {
				itemDao.deleteByPrimaryKey(Long.parseLong(id));
				itemDescDao.deleteByPrimaryKey(Long.parseLong(id));
				
				//同步缓存。删除原缓存中的数据
				jedisClient.del(REDIS_ITEM_PRE+":"+id+":BASE");
				jedisClient.del(REDIS_ITEM_PRE+":"+id+":DESC");
			}
			return E3Result.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return E3Result.build(123, "删除失败");
		}
	}

	public E3Result instockItem(String ids) {
		try {
			String[] split = ids.split(",");
			//循环遍历要下架的id,获得要下架的商品对象，修改status为2
			for (String id : split) {
				TbItem tbItem = itemDao.selectByPrimaryKey(Long.parseLong(id));
				tbItem.setStatus((byte) 2);
				itemDao.updateByPrimaryKeySelective(tbItem);
			}
			return E3Result.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return E3Result.build(123, "下架失败");
		}
	}

	public E3Result reshelfItem(String ids) {
		try {
			String[] split = ids.split(",");
			//循环遍历要上架的id，获得要上架的商品对象，修改status为1
			for (String id : split) {
				TbItem tbItem = itemDao.selectByPrimaryKey(Long.parseLong(id));
				tbItem.setStatus((byte) 1);
				itemDao.updateByPrimaryKeySelective(tbItem);
			}
			return E3Result.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return E3Result.build(123, "上架失败");
		}
	}
}
