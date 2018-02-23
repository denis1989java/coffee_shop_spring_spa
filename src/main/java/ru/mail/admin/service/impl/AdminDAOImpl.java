package ru.mail.admin.service.impl;

import org.springframework.stereotype.Service;
import ru.mail.admin.service.AdminDAO;
import ru.mail.admin.model.Admin;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Denis Monich
 * this class realise all logic to create valid users adn get valid/invalid user
 */
@Service
public class AdminDAOImpl implements AdminDAO {


    private List<Admin> admins;

    public AdminDAOImpl() {
    }

    /**
     * @param login email or login which user wrote to login form
     * @return valid user or null if user invalid
     */
    @Override
    public Admin loadUserByUsername(String login) {
        Admin returnUser = null;
        iniDataForTesting();
        for (Admin admin : admins) {
            if (admin.getLogin().equals(login)) {
                returnUser = new Admin(login, admin.getPassword());
                break;
            }
        }
        return returnUser;
    }

    /*
    creating default users
    */
    private void iniDataForTesting() {
        admins = new ArrayList<Admin>();
        admins.add(new Admin("admin", "admin"));

    }
}
