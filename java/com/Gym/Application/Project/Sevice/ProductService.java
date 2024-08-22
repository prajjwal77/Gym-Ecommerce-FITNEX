package com.Gym.Application.Project.Sevice;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Gym.Application.Project.Model.Product;
import com.Gym.Application.Project.Repository.ProductRepository;

@Service
public class ProductService {

	   @Autowired
	    private ProductRepository productRepository;

	    public List<Product> findAll() {
	        return productRepository.findAll();
	    }

	    public List<Product> findByCategory(String category) {
	        return productRepository.findByCategory(category);
	    }
	    
	    public Product findById(Long id) {
	        Optional<Product> product = productRepository.findById(id);
	        return product.orElse(null); // or throw a custom exception
	    }
	    
}    
