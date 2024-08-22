package com.Gym.Application.Project.Sevice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Gym.Application.Project.Model.User;
import com.Gym.Application.Project.Repository.UserRepository;
@Service
public class UserService {

	@Autowired
	private  UserRepository userRepository;
	
	@Autowired
	  private BCryptPasswordEncoder passwordEncoder;
	
	 public User getUserByEmail(String email) {
	        User user = userRepository.findByEmail(email);
	        if (user == null) {
	            throw new UsernameNotFoundException("User not found with email: " + email);
	        }
	        return user;
	    }
	
	 public User getUserById(Long userId) {
		 return userRepository.findById(userId).orElse(null);
		 
	 }
	 
	 public void saveUser(User user) {
	        user.setPassword(passwordEncoder.encode(user.getPassword()));
	        userRepository.save(user);
	    }
	 
	 
	 
	 public String saveProfilePhoto(MultipartFile profilePhoto) throws IOException {
	        if (profilePhoto.isEmpty()) {
	            throw new IOException("Failed to store empty file");
	        }

	        byte[] bytes = profilePhoto.getBytes();
	        Path path = Paths.get("static/photos" + profilePhoto.getOriginalFilename());
	        Files.write(path, bytes);

	        return path.toString();
	    }

}
