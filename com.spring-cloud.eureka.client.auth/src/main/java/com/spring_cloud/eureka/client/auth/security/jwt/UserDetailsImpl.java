package com.spring_cloud.eureka.client.auth.security.jwt;


import com.spring_cloud.eureka.client.auth.model.UserRoleEnum;
import com.spring_cloud.eureka.client.auth.presentation.dto.UserDto;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class UserDetailsImpl implements UserDetails {

    private final Long id;

    private final String username;

    private final String password;

    private final UserRoleEnum role;

    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(UserDto user) {
        this.id = user.id();
        this.username = user.username();
        this.password = user.password();
        this.role = user.role();
        this.authorities = generateAuthorities(user.role());
    }

    private Collection<GrantedAuthority> generateAuthorities(UserRoleEnum role) {
        return List.of(new SimpleGrantedAuthority(role.getAuthority()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getUserId() { return this.id;}

    public UserRoleEnum getRole() { return this.role; }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() { return this.username;}

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
}
