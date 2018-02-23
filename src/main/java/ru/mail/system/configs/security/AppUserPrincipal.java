package ru.mail.system.configs.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.mail.admin.model.Admin;

import java.util.Collection;

/**
 * @author Denis Monich
 * return UserPrincipal tu spring
 */
public class AppUserPrincipal implements UserDetails {

    private Admin admin;

    public AppUserPrincipal(Admin admin) {
        this.admin = admin;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return admin.getPassword();
    }

    @Override
    public String getUsername() {
        return admin.getLogin();
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
