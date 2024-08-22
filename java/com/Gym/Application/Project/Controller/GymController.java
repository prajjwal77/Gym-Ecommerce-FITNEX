package com.Gym.Application.Project.Controller;
import java.security.Principal;
import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Gym.Application.Project.Model.Blog;
import com.Gym.Application.Project.Model.ContactUs;
import com.Gym.Application.Project.Model.User;
import com.Gym.Application.Project.Sevice.BlogService;
import com.Gym.Application.Project.Sevice.ContactUsService;
import com.Gym.Application.Project.Sevice.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class GymController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private BlogService blogService;
	
	@Autowired
	private ContactUsService contactUsService;
	
	// private static String UPLOAD_DIR = "uploads/";
	//Home page
	@GetMapping("/")
	public String Home() {
		return"home";
	}
	//...........................................................................................................
	//showing register page
	@GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }
	//...........................................................................................................
	//registering new user
	 @PostMapping("/register")
	    public String registerUser(@ModelAttribute("user") User user) {
	     userService.saveUser(user);  
		 return "redirect:/login";
	    }
	 //...........................................................................................................
	 //login page
	 @GetMapping("/login")
	public String login() {
		 return "login";
	 }
	 //..........................................................................................................
	 @GetMapping("blogs")
	 public String showAllBlogs(Model model) {
		 List<Blog> blogs = blogService.getAllBlogs();
		 model.addAttribute("blog",blogs);
		 return "blogs";
	}
	 @PostMapping("/blogs/like/{id}")
	    @ResponseBody
	    public String likeBlog(@PathVariable Long id) {
	        Blog blog = blogService.findBlogById(id);
	        if (blog != null) {
	            blog.setLikes(blog.getLikes() + 1);
	            blogService.saveBlog(blog);
	            return "👍 " + blog.getLikes() + " Likes";
	        }
	        return "Blog not found";
	    }
	  @PostMapping("/blogs/comment/{id}")
	    @ResponseBody
	    public String commentBlog(@PathVariable Long id, @RequestParam String comment) {
	        Blog blog = blogService.findBlogById(id);
	        if (blog != null) {
	            blog.addComment(comment);
	            blogService.saveBlog(blog);
	            return "Comment added successfully";
	        }
	        return "Blog not found";
	    }
	  //.....................................................................................................
	  //Adding  New Blogs
	  @GetMapping("/blogs/add")
	    public String showAddBlogForm(Model model) {
	        model.addAttribute("blog", new Blog());
	        return "addBlog";
	    }
	    //.....................................................................................................
	    //saving new blog
	    @PostMapping("/blogs/add")
	    public String addBlog(@ModelAttribute Blog blog) {
	        blogService.saveBlog(blog);
	        return "redirect:/";
	    }
	    //.........................................................................................................
//	    @GetMapping("/membership")
//	    public String membership() {
//	        return "membership";
//	    }

	
//	    @GetMapping("/purchase")
//	    public String showMembershipPlans(Model model, Principal principal) {
//	        // Retrieve the logged-in user's email from Principal
//	        String userEmail = principal.getName();
//	        // Retrieve the user by email
//	        User user = userService.getUserByEmail(userEmail);
//	        // Add the user's ID to the model
//	        model.addAttribute("userId", user.getId());
//	        // Return the view name
//	        return "purchase";
//	    }

//	    @PostMapping("/purchase")
//	    public String purchaseMembership(
//	        @RequestParam("plan") String plan, 
//	        @RequestParam("userId") Long userId,
//	        @RequestParam(value = "cardName", required = false) String cardName,
//	        @RequestParam(value = "cardNumber", required = false) String cardNumber,
//	        @RequestParam(value = "expiryDate", required = false) String expiryDate,
//	        @RequestParam(value = "cvv", required = false) String cvv,
//	        @RequestParam(value = "paymentOption", required = true) String paymentOption,
//	        Model model
//	    ) {
//	        // Simulate payment processing based on payment option
//	        boolean paymentSuccessful = true; // Assume the payment is always successful for this example
//
//	        if (paymentSuccessful) {
//	            userService.updateUserMembership(userId, plan);
//	            return "redirect:/profile?userId=" + userId;
//	        } else {
//	            model.addAttribute("error", "Payment failed. Please try again.");
//	            return "purchase";
//	        }
//	    }
	    //.......................................................................................................
	    //profile 
	    @GetMapping("/profile")
	    public String profile(Model model, Principal principal) {
	        String userEmail = principal.getName(); // Assuming the principal holds the user's email
	        try {
	            User user = userService.getUserByEmail(userEmail);
	            model.addAttribute("user", user); // Add the entire user object to the model
	            return "profile";
	        } catch (UsernameNotFoundException e) {
	            // Handle user not found scenario
	            return "redirect:/login"; // Or some other error page
	        }
	    }
	    //..........................................................................................................
	    //profile edit page
	    @GetMapping("/profile/edit")
	    public String showEditProfileForm(Model model, Principal principal) {
	        String userEmail = principal.getName();
	        try {
	            User user = userService.getUserByEmail(userEmail);
	            model.addAttribute("user", user);
	            return "editProfile";
	        } catch (UsernameNotFoundException e) {
	            return "redirect:/login";
	        }
	    }
	    //............................................................................................................
	    //profile edit logic
	    @PostMapping("/profile/edit")
	    public String editProfile(@ModelAttribute User user, Principal principal) {
	        if (principal == null) {
	            return "redirect:/login";
	        }

	        String userEmail = principal.getName();
	        User existingUser = userService.getUserByEmail(userEmail);

	        // Log the received user data
	        System.out.println("Received user data: " + user);

	        // Update user information without changing the profile photo
	        existingUser.setFullName(user.getFullName());
	        existingUser.setAge(user.getAge());
	        existingUser.setWeight(user.getWeight());
	        existingUser.setHeight(user.getHeight());

	        // Log the updated user data
	        System.out.println("Updated user data: " + existingUser);

	        userService.saveUser(existingUser);

	        return "redirect:/profile";
	    }
	    //..........................................................................................................
	    //logout 
	    @GetMapping("/logout")
	    public String logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
	        if (authentication != null) {
	            new SecurityContextLogoutHandler().logout(request, response, authentication);
	        }
	        return "logout";
	    }
	    //..........................................................................................................
	    //about us...
	    @GetMapping("/about")
	    public String aboutUs(Model model) {
	        model.addAttribute("user", new User());
	        return "about";
	    }
	    //...........................................................................................................
	    // Quick Contact
	    @PostMapping("/contact")
	    public String quickContact(@RequestParam String email, @RequestParam String message, RedirectAttributes redirectAttributes) {
	        ContactUs contactMessage = new ContactUs();
	        contactMessage.setEmail(email);
	        contactMessage.setMessage(message);
	        contactUsService .saveContactMessage(contactMessage);
	        redirectAttributes.addFlashAttribute("message", "Your message has been sent successfully.");
	        return "redirect:/";
	    }
	    //..............................................................................................................
	    

}
