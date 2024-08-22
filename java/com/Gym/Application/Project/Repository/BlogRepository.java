package com.Gym.Application.Project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Gym.Application.Project.Model.Blog;

@Repository
public interface BlogRepository extends JpaRepository<Blog,Long>{

}
