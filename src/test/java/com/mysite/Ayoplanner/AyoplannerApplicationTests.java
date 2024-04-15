package com.mysite.Ayoplanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mysite.Ayoplanner.makingplans.CityInfo;
import com.mysite.Ayoplanner.makingplans.CityInfoRepository;
import com.mysite.Ayoplanner.makingplans.SiteInfo;
import com.mysite.Ayoplanner.makingplans.SiteInfoRepository;

@SpringBootTest
class AyoplannerApplicationTests {
	
	@Autowired
	private CityInfoRepository cityInfoRepository;
	
	@Autowired
	private SiteInfoRepository siteInfoRepository;

	@Test
	void textJpa() {
		Optional<CityInfo> oc = this.cityInfoRepository.findById(1);
		assertTrue(oc.isPresent());
		CityInfo c = oc.get();
		
		SiteInfo s1 = new SiteInfo();
		s1.setPlaceId("ChIJB0gcnCBw5kcRHoIAPcTEApc");
		s1.setCityInfo(c);
		s1.setPlaceCategory("명소");
		this.siteInfoRepository.save(s1);
		
		SiteInfo s2 = new SiteInfo();
		s2.setPlaceId("ChIJ442GNENu5kcRGYUrvgqHw88");
		s2.setCityInfo(c);
		s2.setPlaceCategory("명소");
		this.siteInfoRepository.save(s2);
		
		SiteInfo s3 = new SiteInfo();
		s3.setPlaceId("ChIJe2jeNttx5kcRi_mJsGHdkQc");
		s3.setCityInfo(c);
		s3.setPlaceCategory("명소");
		this.siteInfoRepository.save(s3);
		
		SiteInfo s4 = new SiteInfo();
		s4.setPlaceId("ChIJAQAAMCxu5kcRx--_4QnbGcI");
		s4.setCityInfo(c);
		s4.setPlaceCategory("명소");
		this.siteInfoRepository.save(s4);
		
		SiteInfo s5 = new SiteInfo();
		s5.setPlaceId("ChIJG5Qwtitu5kcR2CNEsYy9cdA");
		s5.setCityInfo(c);
		s5.setPlaceCategory("명소");
		this.siteInfoRepository.save(s5);
		
		SiteInfo s6 = new SiteInfo();
		s6.setPlaceId("ChIJHbrByclt5kcRn-ZGU8KjU_g");
		s6.setCityInfo(c);
		s6.setPlaceCategory("명소");
		this.siteInfoRepository.save(s6);
		
		SiteInfo s7 = new SiteInfo();
		s7.setPlaceId("ChIJoyC4CRxu5kcRRTPcWX5srLc");
		s7.setCityInfo(c);
		s7.setPlaceCategory("명소");
		this.siteInfoRepository.save(s7);
		
		SiteInfo s8 = new SiteInfo();
		s8.setPlaceId("ChIJa-wm0fBx5kcRTj1XkfsieqY");
		s8.setCityInfo(c);
		s8.setPlaceCategory("명소");
		this.siteInfoRepository.save(s8);
		
		SiteInfo s9 = new SiteInfo();
		s9.setPlaceId("ChIJmw6mpBhu5kcRzLhZUymiyhY");
		s9.setCityInfo(c);
		s9.setPlaceCategory("명소");
		this.siteInfoRepository.save(s9);
		
		SiteInfo s10 = new SiteInfo();
		s10.setPlaceId("ChIJ6XHvERZy5kcRcARsBv9AehU");
		s10.setCityInfo(c);
		s10.setPlaceCategory("명소");
		this.siteInfoRepository.save(s10);
		
		SiteInfo s11 = new SiteInfo();
		s11.setPlaceId("ChIJVUrgmz5u5kcRWPSN-T8a730");
		s11.setCityInfo(c);
		s11.setPlaceCategory("명소");
		this.siteInfoRepository.save(s11);
		
		SiteInfo s12 = new SiteInfo();
		s12.setPlaceId("ChIJTVkFpD9y5kcRTG0ELf3XFjw");
		s12.setCityInfo(c);
		s12.setPlaceCategory("명소");
		this.siteInfoRepository.save(s12);
		
		SiteInfo s13 = new SiteInfo();
		s13.setPlaceId("ChIJ0dzuSNBv5kcRa6BHUVdFm0k");
		s13.setCityInfo(c);
		s13.setPlaceCategory("명소");
		this.siteInfoRepository.save(s13);
		
		SiteInfo s14 = new SiteInfo();
		s14.setPlaceId("ChIJPUBtQTVu5kcRnPMYVq9Mthg");
		s14.setCityInfo(c);
		s14.setPlaceCategory("명소");
		this.siteInfoRepository.save(s14);
		
		SiteInfo s15 = new SiteInfo();
		s15.setPlaceId("ChIJ_7ZXHuFx5kcRs1LnTeCsOBM");
		s15.setCityInfo(c);
		s15.setPlaceCategory("명소");
		this.siteInfoRepository.save(s15);
		
		SiteInfo s16 = new SiteInfo();
		s16.setPlaceId("ChIJSUOPztFv5kcRnEbSPYG-9fM");
		s16.setCityInfo(c);
		s16.setPlaceCategory("명소");
		this.siteInfoRepository.save(s16);
		
		SiteInfo s17 = new SiteInfo();
		s17.setPlaceId("ChIJAYa7ntNx5kcRcmJxXPZ7m9k");
		s17.setCityInfo(c);
		s17.setPlaceCategory("명소");
		this.siteInfoRepository.save(s17);
		
		SiteInfo s18 = new SiteInfo();
		s18.setPlaceId("ChIJLW_xmDVu5kcRSKieA7n2tAY");
		s18.setCityInfo(c);
		s18.setPlaceCategory("명소");
		this.siteInfoRepository.save(s18);
		
		SiteInfo s19 = new SiteInfo();
		s19.setPlaceId("ChIJ3cEAXY5x5kcRbrQJ6q8puaQ");
		s19.setCityInfo(c);
		s19.setPlaceCategory("명소");
		this.siteInfoRepository.save(s19);
		
		SiteInfo s20 = new SiteInfo();
		s20.setPlaceId("ChIJcTWK-NFx5kcRI4buiJpXGtc");
		s20.setCityInfo(c);
		s20.setPlaceCategory("명소");
		this.siteInfoRepository.save(s20);
		
		SiteInfo s21 = new SiteInfo();
		s21.setPlaceId("ChIJPbTQ5hlu5kcRkvRS4Xy0Y2M");
		s21.setCityInfo(c);
		s21.setPlaceCategory("명소");
		this.siteInfoRepository.save(s21);
		
		SiteInfo s22 = new SiteInfo();
		s22.setPlaceId("ChIJ6SJcCP5x5kcRJt8m9_lR7_Q");
		s22.setCityInfo(c);
		s22.setPlaceCategory("명소");
		this.siteInfoRepository.save(s22);
		
		SiteInfo s23 = new SiteInfo();
		s23.setPlaceId("ChIJQbHKmTVu5kcRco5OGh_Zns4");
		s23.setCityInfo(c);
		s23.setPlaceCategory("명소");
		this.siteInfoRepository.save(s23);
		
		SiteInfo s24 = new SiteInfo();
		s24.setPlaceId("ChIJiQxv_05u5kcRESFIh6-QTvQ");
		s24.setCityInfo(c);
		s24.setPlaceCategory("명소");
		this.siteInfoRepository.save(s24);
		
		SiteInfo s25 = new SiteInfo();
		s25.setPlaceId("ChIJVeiKGZNt5kcRkPH9S37Lnzg");
		s25.setCityInfo(c);
		s25.setPlaceCategory("명소");
		this.siteInfoRepository.save(s25);
		
		SiteInfo s26 = new SiteInfo();
		s26.setPlaceId("ChIJRS81ac1v5kcRRUqQBmTTJJU");
		s26.setCityInfo(c);
		s26.setPlaceCategory("명소");
		this.siteInfoRepository.save(s26);
		
		SiteInfo s27 = new SiteInfo();
		s27.setPlaceId("ChIJiegK5OBx5kcR9t1Ycr1Bz1g");
		s27.setCityInfo(c);
		s27.setPlaceCategory("명소");
		this.siteInfoRepository.save(s27);
		
		SiteInfo s28 = new SiteInfo();
		s28.setPlaceId("ChIJPQ9b6R5u5kcRZKolq5EBcGc");
		s28.setCityInfo(c);
		s28.setPlaceCategory("명소");
		this.siteInfoRepository.save(s28);
		
		SiteInfo s29 = new SiteInfo();
		s29.setPlaceId("ChIJ8znTVS5u5kcREq8TmzOICFs");
		s29.setCityInfo(c);
		s29.setPlaceCategory("명소");
		this.siteInfoRepository.save(s29);
	}
}
