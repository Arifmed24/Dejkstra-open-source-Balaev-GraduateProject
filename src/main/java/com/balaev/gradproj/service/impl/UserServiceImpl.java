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

}
