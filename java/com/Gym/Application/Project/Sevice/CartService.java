package com.Gym.Application.Project.Sevice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Gym.Application.Project.Model.CartItem;
import com.Gym.Application.Project.Model.User;
import com.Gym.Application.Project.Repository.CartItemRepository;
import com.Gym.Application.Project.Repository.UserRepository;

@Service
public class CartService {

	   @Autowired
	    private UserRepository userRepository;

	    @Autowired
	    private CartItemRepository cartItemRepository;

	    public double getTotalPrice(User user) {
	        List<CartItem> cartItems = cartItemRepository.findByUser(user);
	        return cartItems.stream().mapToDouble(CartItem::getTotalPrice).sum();
	    }

	    public double getTotalDiscount(User user) {
	        List<CartItem> cartItems = cartItemRepository.findByUser(user);
	        return cartItems.stream().mapToDouble(CartItem::getTotalDiscount).sum();
	    }
}
