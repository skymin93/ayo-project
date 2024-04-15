package com.mysite.Ayoplanner.snslogin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.mysite.Ayoplanner.user.SiteUser;
import com.mysite.Ayoplanner.user.UserRole;

public class PrincipalDetails implements UserDetails, OAuth2User {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SiteUser siteUser;
    private Map<String, Object> attributes;

    public PrincipalDetails(SiteUser siteUser) {
        this.siteUser = siteUser;
    }

    public PrincipalDetails(SiteUser siteUser, Map<String, Object> attributes) {
        this.siteUser = siteUser;
        this.attributes = attributes;
    }

    @Override
    public List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if ("admin".equals(siteUser.getUsername())) {
            authorities
                .add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        } else {
            authorities
                .add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return siteUser.getPassword();
    }

    @Override
    public String getUsername() {
        return siteUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return siteUser.getUsername();
    }
}
