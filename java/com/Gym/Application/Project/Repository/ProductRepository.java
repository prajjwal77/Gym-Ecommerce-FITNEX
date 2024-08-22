package com.Gym.Application.Project.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Gym.Application.Project.Model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	  List<Product> findByCategory(String category);
	
}
