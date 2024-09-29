package com.patil.eventmanagement.security.model;

import com.patil.eventmanagement.entity.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {

    private String username = null;
    private String password = null;
    private List<GrantedAuthority> authorities;

    public UserDetailsImpl(Users users) {
        if (users == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        this.username = users.getEmail();
        this.password = users.getPassword();
        this.authorities = parseAuthorities(users.getRole().name());
    }

    private List<GrantedAuthority> parseAuthorities(String role) {
        return role == null ? List.of() :
                List.of(role.split(","))
                        .stream()
                        .map(String::trim)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
}
