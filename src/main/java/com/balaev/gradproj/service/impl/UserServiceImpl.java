package com.balaev.gradproj.service.impl;

import com.balaev.gradproj.domain.User;
import com.balaev.gradproj.repository.UserRepository;
import com.balaev.gradproj.service.api.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;


@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger LOG = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public User login(String login, String password) throws Exception {
        LOG.info("user login");
        User result = null;
        try {
            result = userRepository.userLogin(login, password);
        } catch (Exception e) {
            throw new Exception("Unknown exception", e);
        }
        return result;
    }

    @Override
    public boolean register(User user) {
        LOG.info("user registration");
        User findUser = null;
        try {
            findUser = findUserByLogin(user.getLogin());
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Error in finding user by login", e);
        }
        if (findUser == null) {
            userRepository.save(user);
            LOG.info("new user created");
            return true;
        } else {
            LOG.info("this user is registered yet");
            return false;
        }
    }

    @Override
    public User create(User user) {
        return userRepository.save(user);
    }
}
