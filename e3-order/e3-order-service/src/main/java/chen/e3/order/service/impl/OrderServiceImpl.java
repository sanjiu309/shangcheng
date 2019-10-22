package chen.e3.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import chen.e3.entity.TbOrderItem;
import chen.e3.entity.TbOrderShipping;
import chen.e3.mapper.TbOrderItemMapper;
import chen.e3.mapper.TbOrderMapper;
import chen.e3.mapper.TbOrderShippingMapper;
import chen.e3.order.pojo.OrderInfo;
import chen.e3.order.service.OrderService;
import chen.e3.utils.E3Result;
import chen.e3.utils.jedis.JedisClient;
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private TbOrderMapper tbOrderMapper;
	@Autowired
	private TbOrderItemMapper tbOrderItemMapper;
	@Autowired
	private TbOrderShippingMapper tbOrderShippingMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${Order_Id}")
	private String Order_Id;
	@Value("${Order_Id_Start}")
	private String Order_Id_Start;
	@Value("${Order_Items_id}")
	private String Order_Items_id;
	
	@Override
	public E3Result createOrder(OrderInfo orderInfo) {
		//生成订单号。使用redis的incr
		if(!jedisClient.exists(Order_Id)){
			jedisClient.set(Order_Id, Order_Id_Start);
		}
		String orderId = jedisClient.incr("Order_Id").toString();
		//补全OrderInfo的属性
		orderInfo.setOrderId(orderId);
		orderInfo.setStatus(1);//1未付款
		orderInfo.setCreateTime(new Date());
		orderInfo.setUpdateTime(new Date());
		//插入表
		tbOrderMapper.insert(orderInfo);
		
		List<TbOrderItem> orderItems = orderInfo.getOrderItems();
		for (TbOrderItem tbOrderItem : orderItems) {
			String odid = jedisClient.incr("Order_Items_id").toString();
			tbOrderItem.setId(odid);
			tbOrderItem.setOrderId(orderId);
			
			tbOrderItemMapper.insert(tbOrderItem);
		}
		
		TbOrderShipping orderShipping = orderInfo.getOrderShipping();
		orderShipping.setOrderId(orderId);
		orderShipping.setCreated(new Date());
		orderShipping.setUpdated(new Date());
		tbOrderShippingMapper.insert(orderShipping);
		
		return E3Result.ok(orderId);
	}

}
