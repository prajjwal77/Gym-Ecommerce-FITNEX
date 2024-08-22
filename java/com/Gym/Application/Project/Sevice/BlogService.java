package com.Gym.Application.Project.Sevice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Gym.Application.Project.Model.Blog;
import com.Gym.Application.Project.Repository.BlogRepository;

@Service
public class BlogService {
	
	@Autowired
	private BlogRepository blogRepository;
	
	public List<Blog> getAllBlogs(){
		return blogRepository.findAll();
	}

	public Blog findBlogById(Long id) {
		return blogRepository.findById(id).orElse(null);
	}
	
	public void saveBlog(Blog blog) {
		blogRepository.save(blog);
	}
}
