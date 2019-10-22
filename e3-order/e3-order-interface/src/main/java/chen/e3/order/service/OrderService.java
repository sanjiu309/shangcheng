package chen.e3.order.service;

import chen.e3.order.pojo.OrderInfo;
import chen.e3.utils.E3Result;

public interface OrderService {

	E3Result createOrder(OrderInfo orderInfo);
}
