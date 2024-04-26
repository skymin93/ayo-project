package com.mysite.Ayoplanner.makingplans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class MakingPlansController {
	
    @Autowired
	private final MakingPlansService makingPlansService;
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/makingplans/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id) {
		CityInfo cityInfo = this.makingPlansService.getCityInfo(id);
		model.addAttribute("cityInfo", cityInfo);
		return "making_plans_detail";
	}
	
	@GetMapping("/makingplans/savePlan")
	public String savePlan() {
		return "mypage";
	}

    @PostMapping("/makingplans/savePlan")
    @ResponseBody
    public String savePlan(@RequestBody String savePlanData) {
    	makingPlansService.savePlanData(savePlanData);
        return "mypage";
    }
}
