package com.situ.mall.portal.controller;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.situ.mall.core.constant.Const;
import com.situ.mall.core.entity.Cart;
import com.situ.mall.core.entity.Product;
import com.situ.mall.core.service.IProductService;
import com.situ.mall.portal.vo.CartItemVo;
import com.situ.mall.portal.vo.CartVo;

@Controller
@RequestMapping("/cart")
public class CartController {
	private String CART_COOKIE = "cart_cookit";
	@Autowired
	private IProductService productService;
	
  @RequestMapping("/getCartPage")
  public String getCartPage(Integer productId,Integer amount,HttpServletRequest request,HttpServletResponse response,Model model){
	  System.out.println("productId"+productId);
	  System.out.println("amount"+amount);
	  ObjectMapper objectMapper = new ObjectMapper();
	  objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	  CartVo cartVo = null;
	  
	  
	  Cookie[] cookies = request.getCookies();
	  if (null != cookies && cookies.length != 0) {
		  for (Cookie cookie : cookies) {
			if(CART_COOKIE.equals(cookie.getName())){
				String value = cookie.getValue();
			try {
				cartVo = objectMapper.readValue(value, CartVo.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		}
		
	}
	  if (cartVo == null) {
          cartVo = new CartVo();
        }
	    if (null != productId ) {
	    	CartItemVo cartItemVo = new CartItemVo();
	    	Product productTemp = productService.selectById(productId);
	    Product product = new Product();
	    product.setId(productId);
	    product.setStock(productTemp.getStock());
	    	cartItemVo.setProduct(product);
	    	cartItemVo.setIsChecked(Const.CartChecked.UNCHECKED);
	    	cartItemVo.setAmount(amount);
			cartVo.addItem(cartItemVo);
			StringWriter stringWriter = new StringWriter();
			try {
				objectMapper.writeValue(stringWriter, cartVo);
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Cookie cookie = new Cookie(CART_COOKIE,stringWriter.toString() );
		    cookie.setMaxAge(60*60*24);
		    cookie.setPath("/");
			response.addCookie(cookie);
	    }
	    List<CartItemVo> cartItemVos = cartVo.getCartItemVos();
	    for (CartItemVo item : cartItemVos) {
			Product product = productService.selectById(item.getProduct().getId());
			item.setProduct(product);
		}
	   model.addAttribute("cartVo", cartVo);
	  return "cart";
  }
	

}
