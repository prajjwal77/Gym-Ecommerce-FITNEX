package com.Gym.Application.Project.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Gym.Application.Project.Model.PurchaseOrder;
import com.Gym.Application.Project.Model.User;

@Repository
public interface OrderRepository extends JpaRepository<PurchaseOrder, Long>{

	List<PurchaseOrder> findByUser(User user);
	List<PurchaseOrder> findByUserOrderByOrderDateDesc(User user);
}
