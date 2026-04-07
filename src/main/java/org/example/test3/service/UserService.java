package org.example.test3.service;

import org.example.test3.dao.UserDao;
import org.example.test3.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean loginVerify(String username, String password) {
        boolean passLogin = false;
        passLogin = userDao.verifyUsername(username);
        if (passLogin) {
            passLogin = userDao.verifyPassword(username, password);
        }
        return passLogin;
    }

    public boolean userRegister(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return userDao.addUser(user);
    }
}
