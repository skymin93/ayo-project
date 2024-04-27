package com.mysite.Ayoplanner.makingplans;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CityInfoRepository extends JpaRepository<CityInfo, Integer> {
	Optional<CityInfo> findById(Integer Id);
	

}
