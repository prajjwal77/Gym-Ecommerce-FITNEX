package com.Gym.Application.Project.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "membership_plans")
public class MembershipPlans {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	    private String name;
	    private double monthlyPrice;
	    private double threeMonthPrice;
	    private double sixMonthPrice;
	    private double yearlyPrice;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public double getMonthlyPrice() {
			return monthlyPrice;
		}
		public void setMonthlyPrice(double monthlyPrice) {
			this.monthlyPrice = monthlyPrice;
		}
		public double getThreeMonthPrice() {
			return threeMonthPrice;
		}
		public void setThreeMonthPrice(double threeMonthPrice) {
			this.threeMonthPrice = threeMonthPrice;
		}
		public double getSixMonthPrice() {
			return sixMonthPrice;
		}
		public void setSixMonthPrice(double sixMonthPrice) {
			this.sixMonthPrice = sixMonthPrice;
		}
		public double getYearlyPrice() {
			return yearlyPrice;
		}
		public void setYearlyPrice(double yearlyPrice) {
			this.yearlyPrice = yearlyPrice;
		}
	    
	    
}
