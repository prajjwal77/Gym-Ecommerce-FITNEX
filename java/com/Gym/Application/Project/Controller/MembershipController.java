package com.Gym.Application.Project.Controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Gym.Application.Project.Model.MembershipPlans;
import com.Gym.Application.Project.Model.User;
import com.Gym.Application.Project.Repository.MembershipPlansRepository;
import com.Gym.Application.Project.Repository.UserRepository;

@Controller
public class MembershipController {
	  @Autowired
	    private MembershipPlansRepository membershipPlanRepository;

	    @Autowired
	    private UserRepository userRepository;

	    // Display plans
	    // Display basic plan
	    @GetMapping("/basic-plan")
	    public String showBasicPlan(Model model, Principal principal) {
	        return showPlanWithUser(1L, model, principal, "basic-plan");
	    }

	    // Display standard plan
	    @GetMapping("/standard-plan")
	    public String showStandardPlan(Model model, Principal principal) {
	        return showPlanWithUser(2L, model, principal, "standard-plan");
	    }

	    // Display premium plan
	    @GetMapping("/premium-plan")
	    public String showPremiumPlan(Model model, Principal principal) {
	        return showPlanWithUser(3L, model, principal, "premium-plan");
	    }

	    private String showPlanWithUser(Long planId, Model model, Principal principal, String viewName) {
	        // Fetch the user by their email (assuming principal.getName() returns the email)
	        User user = userRepository.findByEmail(principal.getName());
	        
	        if (user != null) {
	            model.addAttribute("userId", user.getId());

	            // Fetch the plan details
	            MembershipPlans plan = membershipPlanRepository.findById(planId).orElse(null);
	            if (plan != null) {
	                model.addAttribute("planName", plan.getName());
	                model.addAttribute("monthlyPrice", plan.getMonthlyPrice());
	                model.addAttribute("threeMonthPrice", plan.getThreeMonthPrice());
	                model.addAttribute("sixMonthPrice", plan.getSixMonthPrice());
	                model.addAttribute("yearlyPrice", plan.getYearlyPrice());
	            } else {
	                model.addAttribute("error", "Plan not found");
	                return "error-page"; // Redirect to an error page
	            }
	        } else {
	            model.addAttribute("error", "User not found");
	            return "error-page"; // Redirect to an error page
	        }

	        return viewName;
	    }

	    // Set new plans
	    @GetMapping("/set-plans")
	    public String setPlansPage(Model model) {
	        model.addAttribute("plans", membershipPlanRepository.findAll());
	        return "set-plans";
	    }

	    @PostMapping("/set-plans")
	    public String setPlans(
	        @RequestParam("name") String name,
	        @RequestParam("monthlyPrice") double monthlyPrice,
	        @RequestParam("threeMonthPrice") double threeMonthPrice,
	        @RequestParam("sixMonthPrice") double sixMonthPrice,
	        @RequestParam("yearlyPrice") double yearlyPrice,
	        Model model
	    ) {
	        MembershipPlans plan = new MembershipPlans();
	        plan.setName(name);
	        plan.setMonthlyPrice(monthlyPrice);
	        plan.setThreeMonthPrice(threeMonthPrice);
	        plan.setSixMonthPrice(sixMonthPrice);
	        plan.setYearlyPrice(yearlyPrice);
	        membershipPlanRepository.save(plan);

	        return "redirect:/set-plans";
	    }

	    // Delete plan
	    @PostMapping("/delete-plan")
	    public String deletePlan(@RequestParam("planId") Long planId, Model model) {
	        membershipPlanRepository.deleteById(planId);
	        return "redirect:/set-plans";
	    }

	    // Purchase plan
	    @PostMapping("/purchase")
	    public String purchasePlan(
	        @RequestParam("planId") Long planId,
	        @RequestParam("userId") Long userId,
	        @RequestParam("duration") String duration,
	        Model model
	    ) {
	        Optional<MembershipPlans> planOptional = membershipPlanRepository.findById(planId);
	        Optional<User> userOptional = userRepository.findById(userId);

	        if (planOptional.isPresent() && userOptional.isPresent()) {
	            MembershipPlans selectedPlan = planOptional.get();
	            User user = userOptional.get();

	            LocalDate startDate = LocalDate.now();
	            LocalDate endDate = calculateEndDate(startDate, duration);

	            user.setMembershipPlan(selectedPlan);
	            user.setMembershipStartDate(startDate);
	            user.setMembershipEndDate(endDate);

	            userRepository.save(user);

	            model.addAttribute("planName", selectedPlan.getName());
	            model.addAttribute("membershipStartDate", startDate);
	            model.addAttribute("membershipEndDate", endDate);
	            return "membership-payment-success";
	        }

	        model.addAttribute("error", "Plan or user not found");
	        return "error-page";
	    }

	    private LocalDate calculateEndDate(LocalDate startDate, String duration) {
	        switch (duration) {
	            case "monthly":
	                return startDate.plusMonths(1);
	            case "three-month":
	                return startDate.plusMonths(3);
	            case "six-month":
	                return startDate.plusMonths(6);
	            case "yearly":
	                return startDate.plusYears(1);
	            default:
	                throw new IllegalArgumentException("Invalid duration: " + duration);
	        }
	    }
}
