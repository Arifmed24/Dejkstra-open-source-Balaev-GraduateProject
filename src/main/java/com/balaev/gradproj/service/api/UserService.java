package com.balaev.gradproj.service.api;

import com.balaev.gradproj.domain.User;

public interface UserService {
    User findUserByLogin(String login);

    User login(String login, String password) throws Exception;

    boolean register(User user);

    User create(User user);
}
