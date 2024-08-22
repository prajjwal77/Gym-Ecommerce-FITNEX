package com.Gym.Application.Project.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Gym.Application.Project.Model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long>{

	List<Address> findByUserId(Long id);

}
