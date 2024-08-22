package com.Gym.Application.Project.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;


@Entity
public class PurchaseOrder {

	
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(name = "delivery_date")
	    private Date deliveryDate;

	    @Column(name = "order_date")
	    private Date orderDate;

	    @Column(name = "total_price", nullable = false)
	    private Double totalPrice;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "user_id", nullable = false)
	    private User user;

	    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<OrderItem> orderItems;
	    

	    @Enumerated(EnumType.STRING)
	    private OrderStatus status;


		public Long getId() {
			return id;
		}


		public void setId(Long id) {
			this.id = id;
		}


		public Date getDeliveryDate() {
			return deliveryDate;
		}


		public void setDeliveryDate(Date deliveryDate) {
			this.deliveryDate = deliveryDate;
		}


		public Date getOrderDate() {
			return orderDate;
		}


		public void setOrderDate(Date orderDate) {
			this.orderDate = orderDate;
		}


		public Double getTotalPrice() {
			return totalPrice;
		}


		public void setTotalPrice(Double totalPrice) {
			this.totalPrice = totalPrice;
		}


		public User getUser() {
			return user;
		}


		public void setUser(User user) {
			this.user = user;
		}


		public List<OrderItem> getOrderItems() {
			return orderItems;
		}


		public void setOrderItems(List<OrderItem> orderItems) {
			this.orderItems = orderItems;
		}


		public OrderStatus getStatus() {
			return status;
		}


		public void setStatus(OrderStatus status) {
			this.status = status;
		}

		

	    
}
