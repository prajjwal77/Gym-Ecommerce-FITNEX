package com.Gym.Application.Project.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Gym.Application.Project.Model.User;

@Repository
public interface UserRepository  extends JpaRepository<User, Long>{

	User findByEmail(String email);

}
