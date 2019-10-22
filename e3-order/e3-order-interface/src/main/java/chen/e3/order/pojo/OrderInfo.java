package chen.e3.order.pojo;

import java.io.Serializable;
import java.util.List;

import chen.e3.entity.TbOrder;
import chen.e3.entity.TbOrderItem;
import chen.e3.entity.TbOrderShipping;
/**
 * 订单页面提交的数据接受对象
 * @author chen
 *
 */
public class OrderInfo extends TbOrder implements Serializable{

	private List<TbOrderItem> orderItems;
	
	private TbOrderShipping orderShipping;

	public List<TbOrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<TbOrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public TbOrderShipping getOrderShipping() {
		return orderShipping;
	}

	public void setOrderShipping(TbOrderShipping orderShipping) {
		this.orderShipping = orderShipping;
	}
	
}
