package com.Gym.Application.Project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Gym.Application.Project.Model.OrderItem;
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{

}
