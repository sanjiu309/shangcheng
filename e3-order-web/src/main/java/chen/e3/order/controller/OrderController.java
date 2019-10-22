package chen.e3.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import chen.e3.cart.service.CartService;
import chen.e3.entity.TbItem;
import chen.e3.entity.TbUser;
import chen.e3.order.pojo.OrderInfo;
import chen.e3.order.service.OrderService;
import chen.e3.utils.E3Result;

@Controller
public class OrderController {

	@Autowired
	private CartService cartService;
	@Autowired
	private OrderService orderService;
	
	@RequestMapping("/order/order-cart")
	public String toOrderCart(HttpServletRequest request,Model model){
		TbUser user=(TbUser) request.getAttribute("user");
		//根据用户id取收货地址列表..现在使用静态数据
		//支付方式列表...静态数据
		List<TbItem> cartList = cartService.getCartList(user.getId());
		model.addAttribute("cartList", cartList);
		return "order-cart";
	}
	
	@RequestMapping("/order/create")
	public String createOrder(OrderInfo orderInfo,HttpServletRequest request,Model model){
		TbUser user=(TbUser) request.getAttribute("user");
		orderInfo.setUserId(user.getId());
		orderInfo.setBuyerNick(user.getUsername());
		
		E3Result result = orderService.createOrder(orderInfo);
		if(result.getStatus()==200){
			//清空购物车
			cartService.clearCart(user.getId());
			
			model.addAttribute("orderId", result.getData().toString());
			model.addAttribute("payment", orderInfo.getPayment());
		}
		return "success";
	}
	
}
