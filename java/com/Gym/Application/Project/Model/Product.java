package com.Gym.Application.Project.Model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	   @Column(nullable = false)
	    private String category;

	    @Column(nullable = false)
	    private String title;

	    @Column(nullable = false)
	    private String price;

	    @Column(nullable = false)
	    private String img;
	    
	    private String img2; // Additional image
	    private String img3; // Additional image
	    private String description;
	    private String size;
	    private double discountPrice; // Discounted price
	    
	    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<CartItem> cartItems = new ArrayList<>();
	    
	    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = Review.class)
	    private List<Review> reviews = new ArrayList<>();
	    

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getPrice() {
			return price;
		}

		public void setPrice(String price) {
			this.price = price;
		}

		public String getImg() {
			return img;
		}

		public void setImg(String img) {
			this.img = img;
		}

		public String getImg2() {
			return img2;
		}

		public void setImg2(String img2) {
			this.img2 = img2;
		}

		public String getImg3() {
			return img3;
		}

		public void setImg3(String img3) {
			this.img3 = img3;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getSize() {
			return size;
		}

		public void setSize(String size) {
			this.size = size;
		}

		public double getDiscountPrice() {
			return discountPrice;
		}

		public void setDiscountPrice(double discountPrice) {
			this.discountPrice = discountPrice;
		}

		public List<Review> getReviews() {
			return reviews;
		}

		public void setReviews(List<Review> reviews) {
			this.reviews = reviews;
		}

		public List<CartItem> getCartItems() {
			return cartItems;
		}

		public void setCartItems(List<CartItem> cartItems) {
			this.cartItems = cartItems;
		}


}
