package ru.mail.system.configs.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.mail.admin.service.AdminDAO;
import ru.mail.admin.model.Admin;

/**
 * @author Denis Monich
 * this class implements loading exist's user and transfer it for shaping of Principal object
 */
@Service(value = "appUserDetailsService")
public class AppUserDetailsServiceImpl implements UserDetailsService {

    private final AdminDAO userDao;

    @Autowired
    public AppUserDetailsServiceImpl(AdminDAO userDAO) {
        this.userDao = userDAO;
    }

    /**
     * @param username email or login which user wrote to login form
     * @return UserPrincipal object
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        Admin admin = userDao.loadUserByUsername(username.toLowerCase());
        if (admin == null) {
            return null;
        }
        return new AppUserPrincipal(admin);
    }
}
