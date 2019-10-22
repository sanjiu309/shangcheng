package chen.e3.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import chen.e3.cart.service.CartService;
import chen.e3.entity.TbItem;
import chen.e3.entity.TbUser;
import chen.e3.service.ItemService;
import chen.e3.utils.CookieUtils;
import chen.e3.utils.E3Result;
import chen.e3.utils.JsonUtils;

@Controller
public class CartController {

	@Autowired
	private ItemService itemService;
	@Value("${CART_COOKIE_DATE}")
	private Integer CART_COOKIE_DATE;
	@Autowired
	private CartService cartService;

	@RequestMapping("/cart/add/{itemId}")
	public String addCart(@PathVariable(value = "itemId") Long itemId, @RequestParam(defaultValue = "1") Integer num,
			HttpServletRequest request, HttpServletResponse response) {
		// 添加购物车前判断是否登录
		TbUser user = (TbUser) request.getAttribute("user");
		// 如果登录，则将购物车信息存入redis
		if (user != null) {
			// 调用服务存入reids
			cartService.addCart(user.getId(), itemId, num);
			return "cartSuccess";
		}
		// 没有登录，则将购物车信息存入cookie
		List<TbItem> cartList = getCartListByCookie(request);
		boolean flag = false;
		// 判断商品是否存在，存在商品数量+num
		for (TbItem tbItem : cartList) {
			if (tbItem.getId() == itemId.longValue()) {
				flag = true;
				tbItem.setNum(tbItem.getNum() + num);
			}
		}
		// 不存在，查询商品，添加到列表中
		if (!flag) {
			TbItem tbItem = itemService.getItemById(itemId);
			tbItem.setNum(num);
			String image = tbItem.getImage();
			if (StringUtils.isNotBlank(image)) {
				tbItem.setImage(image.split(",")[0]);
			}
			cartList.add(tbItem);
		}
		// 存入cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), CART_COOKIE_DATE, true);
		return "cartSuccess";
	}

	@RequestMapping("/cart/cart")
	public String toCart(HttpServletRequest request, HttpServletResponse response, Model model) {
		List<TbItem> cartList = getCartListByCookie(request);

		// 添加购物车前判断是否登录
		TbUser user = (TbUser) request.getAttribute("user");
		// 如果登录,从cookie中取购物车列表
		if (user != null) {
			// 如果不为空，则将其与redis中的购物车合并
			cartService.mergeCart(user.getId(), cartList);
			// 并将cookie中的购物车删除
			CookieUtils.deleteCookie(request, response, "cart");
			// 从服务的取购物车
			cartList = cartService.getCartList(user.getId());
			model.addAttribute("cartList", cartList);
			return "cart";
		}

		// 未登录
		model.addAttribute("cartList", cartList);
		return "cart";
	}

	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public E3Result updageNum(@PathVariable(value = "itemId") Long itemId, @PathVariable(value = "num") Integer num,
			HttpServletRequest request, HttpServletResponse response) {
		TbUser user = (TbUser) request.getAttribute("user");
		if (user != null) {
			return cartService.updateCartNum(user.getId(), itemId, num);
		}
		
		
		List<TbItem> cartList = getCartListByCookie(request);
		for (TbItem tbItem : cartList) {
			if (tbItem.getId() == itemId.longValue()) {
				tbItem.setNum(num);
				break;
			}
		}
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), CART_COOKIE_DATE, true);
		return E3Result.ok();
	}

	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCart(@PathVariable(value = "itemId") Long itemId, HttpServletRequest request,
			HttpServletResponse response) {
		TbUser user = (TbUser) request.getAttribute("user");
		if (user != null) {
			cartService.deleteCart(user.getId(), itemId);
			return "redirect:/cart/cart.html";
		}
		
		
		List<TbItem> cartList = getCartListByCookie(request);
		for (TbItem tbItem : cartList) {
			if (tbItem.getId() == itemId.longValue()) {
				cartList.remove(tbItem);
				break;
			}
		}
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), CART_COOKIE_DATE, true);
		return "redirect:/cart/cart.html";
	}

	/**
	 * 从cookie中取购物车列表
	 * 
	 * @param request
	 * @return
	 */
	private List<TbItem> getCartListByCookie(HttpServletRequest request) {
		String json = CookieUtils.getCookieValue(request, "cart", true);
		if (StringUtils.isBlank(json)) {
			return new ArrayList<TbItem>();
		}

		return JsonUtils.jsonToList(json, TbItem.class);
	}
}
