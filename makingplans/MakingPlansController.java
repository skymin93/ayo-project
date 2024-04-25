package com.mysite.Ayoplanner.makingplans;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/makingplans")
public class MakingPlansController {
	
	private final MakingPlansService makingPlansService;
	
	@GetMapping(value = "/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id) {
		CityInfo cityInfo = this.makingPlansService.getCityInfo(id);
		model.addAttribute("cityInfo", cityInfo);
		return "making_plans_detail";
	}
}

