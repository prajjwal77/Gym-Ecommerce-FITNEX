package com.Gym.Application.Project.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class BlogController {

	@GetMapping("/showTop10GymWorkouts")
	public String showTop10GymWorkouts(Model model) {
		
		return "blog1";
	}
	
	@GetMapping("/nutritonTips")
	public String nutritonTips(Model model) {
		
		return "blog2";
	}
	
	@GetMapping("/yogaForBeginners")
	public String yogaForBeginners(Model model) {
		
		return "blog3";
	}
	
	@GetMapping("/cardioWorkout")
	public String effectiveCardioWorkout(Model model) {
		
		return "blog4";
	}
	
	@GetMapping("/strengthTraining")
	public String strengthTrainingbasics(Model model) {
		
		return "blog5";
	}
	
	@GetMapping("/hIITWorkoutsforBeginners")
	public String hIITWorkoutsforBeginners(Model model) {
		
		return "blog6";
	}
	
	@GetMapping("/stretchingTechniques")
	public String stretchingTechniques(Model model) {
		return "blog7";
	}
	
	@GetMapping("/workoutPlansforWomen")
	public String workoutPlansforWomen(Model model) {
		return "blog8";
	}
	
	@GetMapping("/postWorkoutNutrition")
	public String postWorkoutNutrition(Model model) {
		return "blog9";
	}
	@GetMapping("/homeGymEssentials")
	public String homeGymEssentials(Model model) {
		return "blog10";
	}
}
