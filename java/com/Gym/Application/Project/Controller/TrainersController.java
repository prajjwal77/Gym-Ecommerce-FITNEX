package com.Gym.Application.Project.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ch.qos.logback.core.model.Model;

@Controller

public class TrainersController {
	 @GetMapping("/trainer/prajjwal-yadav")
	    public String showPrajjwalYadavPage(Model model) {
	        // Add any dynamic data here if necessary
	        return "prajjwal-yadav";  // Return the corresponding HTML template
	    }

	    @GetMapping("/trainer/aditi-pandit")
	    public String showAditiPanditPage(Model model) {
	        return "aditi-pandit";
	    }
	    
	    @GetMapping("/trainer/jeet-selal")
	    public String showJeetSelelPage(Model model) {
	    	return "jeet-selal";
	    }
	    
	    @GetMapping("/trainer/abhishek-yadav")
	    public String showAbhishekYadavPage(Model model) {
	    	return "abhishek-yadav";
	    }
	    
	    @GetMapping("/trainer/sarah-johnson")
	    public String showSarahJohnsonPage(Model model) {
	    	return "sarah-johnson";
	    }
	    
	    @GetMapping("/trainer/michael-anderson")
	    public String showMichelAndersonPage(Model model) {
	    	return "michael-anderson";
	    }
	    
}
