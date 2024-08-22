package com.Gym.Application.Project.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Gym.Application.Project.Model.CartItem;
import com.Gym.Application.Project.Model.Product;
import com.Gym.Application.Project.Model.User;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {

	List<CartItem> findByUser(User user);

	CartItem findByUserAndProduct(User user, Product product);
	
	void deleteByUserId(Long userId);
}
