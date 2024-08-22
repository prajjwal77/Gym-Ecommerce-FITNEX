package com.Gym.Application.Project.Sevice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Gym.Application.Project.Model.ContactUs;
import com.Gym.Application.Project.Repository.ContactUsRepository;

@Service
public class ContactUsService {

	@Autowired
	private ContactUsRepository contactUsRepository;
	
	 public void saveContactMessage(ContactUs contactUs) {
	        contactUsRepository.save(contactUs);
	    }
}
