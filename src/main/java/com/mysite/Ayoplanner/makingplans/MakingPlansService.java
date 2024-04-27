package com.mysite.Ayoplanner.makingplans;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mysite.Ayoplanner.DataNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MakingPlansService {
	
	private final CityInfoRepository cityInfoRepository;
	private final SavePlanRepository savePlanRepository;
	
	public List<CityInfo> getList() {
		return this.cityInfoRepository.findAll();
	}
	
	public CityInfo getCityInfo(Integer id) {
		Optional<CityInfo> cityInfo = this.cityInfoRepository.findById(id);
		if (cityInfo.isPresent()) {
			return cityInfo.get();
		} else {
			throw new DataNotFoundException("cityInfo not found");
		}
		
	}
	
	public void savePlanData(String savePlanData) {
        SavePlan savePlan = new SavePlan();
        savePlan.setSavePlanData(savePlanData);
        savePlanRepository.save(savePlan);
    }
}
