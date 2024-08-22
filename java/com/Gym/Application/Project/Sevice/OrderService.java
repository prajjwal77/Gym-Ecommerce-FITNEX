package com.Gym.Application.Project.Sevice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Gym.Application.Project.Model.OrderStatus;
import com.Gym.Application.Project.Model.PurchaseOrder;
import com.Gym.Application.Project.Repository.OrderRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {

	
	 @Autowired
	    private OrderRepository orderRepository;

	    @Transactional
	    public void updateOrderStatus(Long orderId, OrderStatus status) {
	        PurchaseOrder order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
	        order.setStatus(status);
	        orderRepository.save(order);
	    }
}
