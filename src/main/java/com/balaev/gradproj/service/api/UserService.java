package com.balaev.gradproj.service.api;

import com.balaev.gradproj.domain.User;

public interface UserService {
    User findUserByLogin(String login);

}
