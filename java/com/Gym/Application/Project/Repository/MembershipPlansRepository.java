package com.Gym.Application.Project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Gym.Application.Project.Model.MembershipPlans;

@Repository
public interface MembershipPlansRepository extends JpaRepository<MembershipPlans, Long>{

}
