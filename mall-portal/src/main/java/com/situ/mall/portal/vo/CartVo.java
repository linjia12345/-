package com.situ.mall.portal.vo;

import java.util.ArrayList;
import java.util.List;

public class CartVo {
    private List<CartItemVo> cartItemVos = new ArrayList<>();

	public List<CartItemVo> getCartItemVos() {
		return cartItemVos;
	}

	public void setCartItemVos(List<CartItemVo> cartItemVos) {
		this.cartItemVos = cartItemVos;
	}

	public void addItem(CartItemVo cartItemVo) {
		boolean isExist = false;
		for (CartItemVo item : cartItemVos) {
			if(item.getProduct().getId().intValue() == cartItemVo.getProduct().getId().intValue()){
				isExist = true;
				int amount = item.getAmount() + cartItemVo.getAmount();
				if(amount <= cartItemVo.getProduct().getStock()){
				item.setAmount(amount);	
				}else{
					item.setAmount(cartItemVo.getProduct().getStock());
				}
			}
		}
		if(isExist == false){
			cartItemVos.add(cartItemVo);
		}
		return;
	}

	
}
