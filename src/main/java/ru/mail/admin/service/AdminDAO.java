package ru.mail.admin.service;

import ru.mail.admin.model.Admin;

/**
 * @author Denis Monich
 */
public interface AdminDAO {

    Admin loadUserByUsername(String username);

}
