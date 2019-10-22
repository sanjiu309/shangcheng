package chen.e3.cart.service;

import java.util.List;

import chen.e3.entity.TbItem;
import chen.e3.utils.E3Result;

public interface CartService {

	E3Result addCart(Long userId,Long itemId,Integer num);
	
	E3Result mergeCart(Long userId,List<TbItem> cartList);
	
	List<TbItem> getCartList(Long userId);
	
	E3Result updateCartNum(Long userId,Long itemId,Integer num);
	
	E3Result deleteCart(Long userId,Long itemId);
	
	E3Result clearCart(Long userId);
}
