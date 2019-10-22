package chen.e3.cart.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import chen.e3.cart.service.CartService;
import chen.e3.entity.TbItem;
import chen.e3.mapper.TbItemMapper;
import chen.e3.utils.E3Result;
import chen.e3.utils.JsonUtils;
import chen.e3.utils.jedis.JedisClient;
@Service
@Transactional
public class CartServiceImpl implements CartService {

	@Autowired
	private JedisClient jedisClient;
	@Value("${Redis_Cart_Pre}")
	private String Redis_Cart_Pre;
	@Autowired
	private TbItemMapper tbItemMapper;
	
	@Override
	public E3Result addCart(Long userId, Long itemId,Integer num) {
		//向redis中添加购物车
		//数据类型是hash key为userId，field为itemId，value为商品信息
		//判断redis中是否存在该商品
		Boolean hexists = jedisClient.hexists(Redis_Cart_Pre+":"+userId, itemId+"");
		//存在，更新数量
		if(hexists){
			String json = jedisClient.hget(Redis_Cart_Pre+":"+userId, itemId+"");
			TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
			item.setNum(item.getNum()+num);
			//写回redis
			jedisClient.hset(Redis_Cart_Pre+":"+userId, itemId+"", JsonUtils.objectToJson(item));
			return E3Result.ok();
		}
		//不存在，根据id取商品信息
		TbItem item = tbItemMapper.selectByPrimaryKey(itemId);
		//设置为购物车数量
		item.setNum(num);
		//取一张图片
		String image = item.getImage();
		if(StringUtils.isNotBlank(image)){
			item.setImage(image.split(",")[0]);
		}
		//添加到redis的购物车列表
		jedisClient.hset(Redis_Cart_Pre+":"+userId, itemId+"", JsonUtils.objectToJson(item));
		return E3Result.ok();
	}

	@Override
	public E3Result mergeCart(Long userId, List<TbItem> cartList) {
		//遍历购物车列表
		for (TbItem tbItem : cartList) {
			//如果商品存在，数量相加
			//没有。存进新商品
			addCart(userId, tbItem.getId(), tbItem.getNum());
		}
		return E3Result.ok();
	}

	@Override
	public List<TbItem> getCartList(Long userId) {
		List<String> jsonList = jedisClient.hvals(Redis_Cart_Pre+":"+userId);
		
		List<TbItem> cartList=new ArrayList<>();
		for (String json : jsonList) {
			cartList.add(JsonUtils.jsonToPojo(json, TbItem.class));
		}
		return cartList;
	}

	@Override
	public E3Result updateCartNum(Long userId, Long itemId, Integer num) {
		String json = jedisClient.hget(Redis_Cart_Pre+":"+userId, itemId+"");
		TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
		item.setNum(num);
		jedisClient.hset(Redis_Cart_Pre+":"+userId, itemId+"", JsonUtils.objectToJson(item));
		return E3Result.ok();
	}

	@Override
	public E3Result deleteCart(Long userId, Long itemId) {
		jedisClient.hdel(Redis_Cart_Pre+":"+userId, itemId+"");
		return E3Result.ok();
	}

	@Override
	public E3Result clearCart(Long userId) {
		jedisClient.del(Redis_Cart_Pre+":"+userId);
		return E3Result.ok();
	}

}
