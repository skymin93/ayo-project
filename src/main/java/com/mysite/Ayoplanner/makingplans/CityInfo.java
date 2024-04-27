package com.mysite.Ayoplanner.makingplans;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class CityInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 20)
	private String cityName;
	
	@Column(columnDefinition = "TEXT")
	private String cityInfo;
	
	@Column
	private String latitude;
	
	private String longitude;
	
	@OneToMany(mappedBy = "cityInfo", cascade = CascadeType.REMOVE)
	private List<SiteInfo> siteInfoList;
}

