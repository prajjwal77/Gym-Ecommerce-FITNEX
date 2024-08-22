package com.Gym.Application.Project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Gym.Application.Project.Model.ContactUs;

@Repository
public interface ContactUsRepository  extends JpaRepository<ContactUs, Long>{

}
